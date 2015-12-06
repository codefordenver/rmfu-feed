(ns rmfu.auth
  (:require [buddy.hashers :as hasher]
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

(defn sign-token [doc & opts]
  (let [claim {:exp (time/plus (time/now) (time/days (or (:duration opts) 3)))}]
    (jws/sign (merge claim doc) secret)))

(defn auth-user [user claim]
  (let [{:keys [password]} user]
    (when-not (nil? claim)
      (if (valid-password? password (:password claim))
        (sign-token (dissoc claim :password :_id))
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
