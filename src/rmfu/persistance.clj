(ns rmfu.persistance
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [monger.conversion :refer [from-db-object]]
            [monger.operators :refer :all]
            [buddy.hashers :as hasher]
            ;; [rmfu.model :as model]
            [rmfu.email :as email]
            [environ.core :refer [env]]
            [slingshot.slingshot :refer [try+]])
  (:import org.bson.types.ObjectId))

(defonce db-config {:name (or (System/getenv "RMFU_DB") "rmfu")})

(defonce secret (System/getenv "RMFU_SECRET"))

(def conn (atom nil))

(let [uri (System/getenv "MONGO_DB_URL")]
  (if (env :production?)
    (reset! conn (mg/connect-via-uri uri))
    (reset! conn (mg/connect))))

(defonce db (mg/get-db (if (env :production?)
                         (:conn @conn) @conn) (:name db-config)))

(defn find-user-by-email [email]
  (mc/find-one-as-map db "users" {:email email}))

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
        oid (:_id user)
        hash-password #(hasher/encrypt %)]
    (try+
      (mc/update-by-id db coll oid {$set {:password (hash-password new-password)}})
      (catch [:type :validation] e
        (println "Error: " (:message e))))))

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