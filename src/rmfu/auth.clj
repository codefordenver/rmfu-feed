(ns rmfu.auth
  (:require [buddy.hashers :as hasher]
            [rmfu.persistance :as db]
            [buddy.sign.jws :as jws]
            [buddy.auth :refer [authenticated?]]
            [ring.util.http-response :refer [unauthorized]]
            [clj-time.core :as time]
            [slingshot.slingshot :refer [try+]]))

;; read secret
(def secret (System/getenv "RMFU_SECRET"))

(defn valid-password?
  "checks user provided againts stored"
  [provided saved]
  (hasher/check provided saved))

(defn sign-token [doc]
  (let [claim {:exp (time/plus (time/now) (time/hours 1))}]
    (jws/sign (merge claim doc) secret)))

(defn auth-user [user]
  (let [{:keys [email password]} user
        lookup (db/find-user-by-email email)]
    (when-not (nil? lookup)
      (if (valid-password? password (:password lookup))
        (sign-token (dissoc lookup :password :_id :articles))
        nil))))

(defn unsign-token [doc]
  (try+
    (jws/unsign doc secret)
    (catch [:type :validation] e
      (println "Error: " (:message e)))))

(defn auth-mw [handler]
  "tiny authentication middleware, checks for identity in :headers"
  (fn [request]
    (if (get-in request [:headers "identity"])
      (handler request)
      (unauthorized {:error "Invalid Token"}))))
