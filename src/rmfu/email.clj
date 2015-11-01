(ns rmfu.email
  (:require [clj-mandrill.core :as mandrill]))

;(defonce mandrill-api-key (System/getenv "MANDRILL_API_KEY"))
(defonce rmfu-from-email (System/getenv "RMFU_FROM_EMAIL"))

(println "ping! the madrill api ........ " (mandrill/ping)) ;; verify creds with mandrill

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

