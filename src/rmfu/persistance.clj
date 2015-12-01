(ns rmfu.persistance
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [monger.conversion :refer [from-db-object]]
            [monger.operators :refer :all]
            [monger.result :refer [acknowledged?]]
            [buddy.hashers :as hasher]
            [rmfu.email :as email]
            [environ.core :refer [env]]
            [rmfu.validation :as validate]
            [slingshot.slingshot :refer [try+]])
  (:import org.bson.types.ObjectId))

(defonce db-config {:name (or (System/getenv "RMFU_DB") "rmfu")})

(def conn (atom nil))

(let [uri (System/getenv "MONGO_DB_URL")]
  (if (env :production?)
    (reset! conn (mg/connect-via-uri uri))
    (reset! conn (mg/connect))))

(defonce db (mg/get-db (if (env :production?)
                         (:conn @conn) @conn) (:name db-config)))

(defn hash-password
  "MD5-encrypt and return password"
  [p]
  (hasher/encrypt p))

(defn find-user-by-email [email]
  (mc/find-one-as-map db "users" {:email email}))

(defn find-user-by-username [username]
  (mc/find-one-as-map db "users" {:username username}))

(defn update-verify-email!
  "update :verified? field in User doc, but only if :verified? is false"
  [user]
  (let [coll "users"
        oid (:_id user)]
    (when-not (:verified? user)
      (try+
        (mc/update-by-id db coll oid {$set {:verified? true}})
        (catch [:type :validation] e
          (println "Error: " (:message e))))
      nil)))

(defn update-password! [email new-password]
  (let [user (find-user-by-email email)
        coll "users"
        oid (:_id user)]
    (acknowledged?
      (mc/update-by-id db coll oid {$set {:password (hash-password new-password)}}))))

(defn update-user-profile!
  "Updates user doc with profile map, returns true if db operation is successful"
  [user profile]
  (let [coll "users"
        oid (:_id user)]
    (acknowledged?
      (mc/update-by-id db coll oid {$set profile}))))

(defn add-user!
  "Adds a user to DB that is by default unverified, we expect verification via email"
  [user]
  (let [{:keys [email password]} user
        coll "users"
        oid (ObjectId.)
        user-doc (merge user {:_id oid})
        claim (find-user-by-email email)]
    (if-not (and (:email claim) email)
      (do
        (email/send-confirmation-email user)
        (acknowledged?
          (mc/insert db coll
                     (merge user-doc
                            {:password  (hash-password password)
                             :verified? false
                             :first     ""
                             :last      ""
                             :zipcode   0
                             :blocked?  false}))))
      nil)))

(defn init-admin-account!
  "create admin account if not already present in database,
  send the reset-password email to set admin password"
  []
  (let [admin (find-user-by-username "admin")
        profile {:username  "admin"
                 :email     (System/getenv "RMFU_FROM_EMAIL")
                 :verified? true
                 :is-admin? true
                 :first     "admin"
                 :last      "admin"
                 :zipcode   80216
                 :blocked?  false}]
    (if-not admin
      (do (mc/insert-and-return db "users" profile)
          (email/send-reset-password-email profile)))))

(init-admin-account!)

(defn find-all-users
  "Returns all users, expect admin"
  []
  (remove #(contains? % :is-admin?) (mc/find-maps db "users")))

(defn block-user
  "Blocks / Unblocks user by email"
  [email state]
  (let [claim (find-user-by-email email)
        coll "users"
        oid (:_id claim)]
    (acknowledged?
      (mc/update-by-id db coll oid {$set {:blocked? state}}))))

(defn add-article!
  "Adds a new article to the database.
  Warning: This method is not idempotent currently."
  [article author-email]
  (let [{:keys [title content]} article
        is-article-valid? (validate/article article)
        article-oid (ObjectId.)
        article-doc {:author-email author-email
                     :_id          article-oid
                     :title        title
                     :content      content
                     :created      (java.util.Date.)}]
    (when is-article-valid?
      (acknowledged? (mc/insert db "articles" article-doc)))))
