(ns rmfu.persistance
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [monger.conversion :refer [from-db-object]]
            [buddy.hashers :as hasher]
            [rmfu.model :as model]
            [clj-mandrill.core :as mandrill])
  (:import org.bson.types.ObjectId))

;; TODO: move all auth related fns to separete namespace

;(defonce mandrill-api-key (System/getenv "MANDRILL_API_KEY"))

(println (mandrill/ping))

;(println (mandrill/send-template "RMFU Feed Email Registration"
;                                 {:subject "Verify your email" :from_email "" :from_name "Code For Denver"
;                                  :to [{:email "" :verify "Bob"}]}))

(defonce db-config {:name "rmfu"})

(defonce conn (mg/connect))

(defonce db (mg/get-db conn (:name db-config)))

(defn valid-password? [provided saved]
  (let [valid? (hasher/check provided saved)]
    (println (format "valid password: %s" valid?))
    valid?))

(defn find-user-by-email [email]
  (mc/find-one-as-map db "users" {:email email}))

(defn auth-user [user]
  (let [{:keys [email password]} user
        lookup (find-user-by-email email)]
    (when-not (nil? lookup)
      (if (valid-password? password (:password lookup))
        user))))

(defn update-verify-email [user]
  (let [coll "users"
        oid (:_id user)]
    (try
      (mc/update-by-id db coll oid (merge user {:verified? true}))
      (catch Exception e (str "caught exception: " (.getMessage e))))))

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



