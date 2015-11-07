(ns rmfu.auth
  (:require [buddy.hashers :as hasher]
            [rmfu.persistance :as db]
            [buddy.core.keys :as keys]
            [buddy.sign.jws :as jws]
            [clj-time.core :as time]))

;; read secret
(def secret (System/getenv "RMFU_SECRET"))

(defn valid-password?
  "checks user provided againts stored"
  [provided saved]
  (hasher/check provided saved))

(defn sign-token [doc]
  (let [claim {:exp (time/plus (time/now) (time/seconds 20))}]
    (jws/sign (merge claim doc) secret)))

(defn auth-user [user]
  (let [{:keys [email password]} user
        lookup (db/find-user-by-email email)]
    (when-not (nil? lookup)
      (if (valid-password? password (:password lookup))
        (sign-token (dissoc user :password))
        nil))))

(defn unsing-token [doc]
  (jws/unsign doc secret))