(ns rmfu.persistance
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [monger.conversion :refer [from-db-object]]
            [monger.operators :refer :all]
            [buddy.hashers :as hasher]
            [rmfu.model :as model]
            [rmfu.email :as email]
            [rmfu.auth :as auth]
            [environ.core :refer [env]])
  (:import org.bson.types.ObjectId))

;; TODO: move all auth related fns to auth namespace

(defonce db-config {:name "rmfu"})

(def conn (atom nil))

(let [uri (System/getenv "MONGO_DB_URL")]
    (if (env :production?)
           (reset! conn (mg/connect-via-uri uri))
           (reset! conn (mg/connect))))

(defonce db (mg/get-db (if (env :production?)
(:conn @conn) @conn) (:name db-config)))

(defn find-user-by-email [email]
  (mc/find-one-as-map db "users" {:email email}))

(defn auth-user [user]
  (let [{:keys [email password]} user
        lookup (find-user-by-email email)]
    (when-not (nil? lookup)
      (auth/valid-password? password (:password lookup)))))

(defn update-verify-email!
  "update :verified? field in doc, but only if :verified? is false"
  [user]
  (let [coll "users"
        oid (:_id user)]
    (when-not (:verified? user)
      (try
        (mc/update-by-id db coll oid {$set {:verified? true}})
        (catch Exception e (str "caught exception: " (.getMessage e)))))))

(defn update-password! [email new-password]
  (let [user (find-user-by-email email)
        coll "users"
        oid (:_id user)
        hash-password #(hasher/encrypt %)]
    (try
      (mc/update-by-id db coll oid {$set {:password (hash-password new-password)}})
      (catch Exception e (str "caught exception: " (.getMessage e))))))

(defn add-user!
  "Adds a user to DB that is by default unverified, we expect verification via email"
  [user]
  (let [{:keys [email password]} user
        coll "users"
        oid (ObjectId.)
        user-doc (merge user {:_id oid})
        hash-password #(hasher/encrypt %)]
    (if (nil? (find-user-by-email email))
      (do
        (email/send-confirmation-email user)
        (mc/insert-and-return db coll (merge user-doc {:password  (hash-password password)
                                                       :verified? false})))
      (format "User already exist with %s" email))))