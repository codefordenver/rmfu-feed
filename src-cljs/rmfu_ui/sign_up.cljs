(ns rmfu-ui.sign-up
  (:require [rmfu-ui.alert :refer [alert]]
            [rmfu-ui.welcome :refer [welcome-component-wrapper]]
            [reagent.core :as reagent]
            [secretary.core :as secretary]
            [ajax.core :refer [POST]]
            [reagent.validation :as validation]))

(defn- is-valid-signup-data
  "Checks if the email, username, and password are valid for signing up a new user"
  [{:keys [email username password]}]
  (and (not-any? empty? [email username password])
       (validation/min-length? password 8)
       (validation/has-value? username)
       (validation/is-email? email)
       (validation/has-value? password)))

(defn post-sign-up [{:keys [username password email]}]
  (POST "/signup"
        {:params        {:username username
                         :password password
                         :email    email}
         :format        :json
         :error-handler #(js/alert %)
         :handler       #(do
                          (js/alert "User created, please check your email to verify your account")
                          (secretary/dispatch! "/"))}))

(defn sign-up-handler [profile]
  (if (is-valid-signup-data profile)
    (post-sign-up profile)
    (js/alert "Invalid Credentials")))

(defn username-input-field [profile]
  [:input {:type        "text"
           :className   "form-control"
           :value       (@profile :username)
           :placeholder "username"
           :on-change   #(swap! profile assoc :username (-> % .-target .-value))}])
(defn email-input-field [profile]
  [:input {:type        "email"
           :className   "form-control"
           :value       (:email @profile)
           :placeholder "me@mail.net"
           :on-change   #(swap! profile assoc :email (-> % .-target .-value))}])

(defn passsword-input-field [profile]
  [:input {:type        "password"
           :className   "form-control"
           :value       (:password @profile)
           :placeholder "8 or more characters"
           :on-change   #(swap! profile assoc :password (-> % .-target .-value))}])

(defn sign-up []
  (let [profile-state (reagent/atom {:username ""
                                     :email    ""
                                     :password ""})]
    [:div
     [welcome-component-wrapper
      [:div.form-group {:style {:padding "1em"}}
       [:p.text-center.greytext "Create Your Account"]
       [:label "username:"]
       [username-input-field profile-state]
       [:label "email:"]
       [email-input-field profile-state]
       [:label "password:"]
       [passsword-input-field profile-state]
       [:br]
       [:button.btn.pull-right.btn-primary.active.pull-right
        {:type     "button"
         :on-click (fn []
                     (sign-up-handler @profile-state))
         } "Sign Me Up!"]

         [:br][:br][:hr]
         [:p.text-center.greytext "Do you already have an account?"]
         [:div.text-center
           [:button.btn.btn-primary
            {:type     "button"
             :on-click #(secretary/dispatch! "/")
             } "Sign In To Your Account Here"]]

         ]]]))
