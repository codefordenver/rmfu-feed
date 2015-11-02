(ns rmfu.auth
  (:require [buddy.hashers :as hasher]
            [rmfu.persistance :as db]))

(defn valid-password?
  "checks user provided againts stored"
  [provided saved]
  (hasher/check provided saved))

(defn auth-user [user]
  (let [{:keys [email password]} user
        lookup (db/find-user-by-email email)]
    (when-not (nil? lookup)
      (valid-password? password (:password lookup)))))