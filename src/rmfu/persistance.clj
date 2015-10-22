(ns rmfu.persistance
  (:import org.bson.types.ObjectId)
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [monger.conversion :refer [from-db-object]]
            [buddy.hashers :as hasher]
            [rmfu.model :as model]))

(defonce db-config {:name "rmfu"})

(defonce conn (mg/connect))

(defonce db (mg/get-db conn (:name db-config)))

(let []
  (println "___Mongodb-Init___")

  ;; prefer **mc/find-maps** over **mc/find** -> lower_level API

  ;; use from-db-object fn to convert mongodb obj to a map
  ;;(from-db-object (mc/find db coll {:first_name "jose"}) true)

  ;; (mc/insert db coll {:first_name "jose" :last_name "maria"})
  ;; (print (mc/find-maps db coll))
  )

(defn valid-password? [provided saved]
  (do (println (str provided saved))
      (hasher/check provided saved)))

(defn find-user-by-email [email]
  (mc/find-one-as-map db "users" email))

(defn find-user [user]
  (let [{:keys [username password]} user
        lookup (find-user-by-email username)]
    (when-not (nil? lookup)
      (if (valid-password? password (:password lookup))
        user))))

(defn add-user! [user]
  (let [{:keys [email password]} user
        coll "users"
        oid (ObjectId.)
        hash-password #(hasher/encrypt %)]
    (if (nil? (find-user-by-email email))
      (mc/insert-and-return db coll (merge user {:_id oid :password (hash-password password)}))
      (throw (ex-info "user already exist with" {:email email})))))

;; (add-user! (model/->User "jose" "maria" "123@soy.com" "123"))



