(ns rmfu.persistance
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [monger.conversion :refer [from-db-object]]
            [buddy.hashers :as hasher]
            [rmfu.model :as model])
  (:import org.bson.types.ObjectId))


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
  (let [valid? (hasher/check provided saved)]
    (println (format "valid password: %s" valid?))
    valid?))

(defn find-user-by-email [email]
  (mc/find-one-as-map db "users" {:email email}))

(defn find-user [user]
  (let [{:keys [username password]} user
        lookup (find-user-by-email username)]
    (when-not (nil? lookup)
      (if (valid-password? password (:password lookup))
        user))))

(defn add-user! [user]
  "Adds a user to DB that is by default unverified, we expect verification via email"
  (let [{:keys [email password]} user
        coll "users"
        oid (ObjectId.)
        user-doc (merge user {:_id oid})
        hash-password #(hasher/encrypt %)]
    (if (nil? (find-user-by-email email))
      (mc/insert-and-return db coll (merge user-doc {:password  (hash-password password)
                                                     :verified? false}))
      (format "User already exist with %s" email))))

(println (add-user! (model/->User "david" "me@me.com" "123")))



