(ns rmfu.email
  (:require [clj-mandrill.core :as mandrill]
            [environ.core :refer [env]]
            [slingshot.slingshot :refer [try+]]))

(defonce rmfu-from-email (System/getenv "RMFU_FROM_EMAIL"))

;; assumes MANDRILL_API_KEY env var has been defined

(defn send-confirmation-email [profile]
  (let [{:keys [email username]} profile]
    (mandrill/send-template "RMFU Feed Email Registration"
                            {:subject "Verify your email" :from_email rmfu-from-email :from_name "RMFU"
                             :to      [{:email email :name username}]})))

(defn send-reset-password-email [profile]
  (let [{:keys [email username]} profile]
    (mandrill/send-template "RMFU Feed Reset Password"
                            {:subject "Reset your password " :from_email rmfu-from-email :from_name "RMFU"
                             :to      [{:email email :name username}]})))

