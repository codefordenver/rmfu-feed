(ns rmfu-ui.sign-in
  (:require [rmfu-ui.alert :refer [alert]]
            [rmfu-ui.welcome :refer [welcome-component-wrapper]]
            [rmfu-ui.alert :refer [alert]]
            [reagent.core :as reagent]
            [secretary.core :as secretary]
            [ajax.core :refer [POST]]
            [reagent.session :as session]
            [reagent.validation :as validation]
            [cljsjs.fixed-data-table]))

(defn identity-token []
  (.getItem (.-localStorage js/window) "rmfu-feed-identity-token"))

(def app-state (reagent/atom
                 {:profile {:email    ""
                            :password ""}
                  :alert   {:display false
                            :message nil
                            :title   nil}}))

(defn alert-update-display-fn [res]
  (do
    (swap! app-state assoc-in [:alert :title] (:status-text res))
    (swap! app-state assoc-in [:alert :message] (:response res))
    (swap! app-state update-in [:alert :display] not)))

(defn post-sign-in [{:keys [email password]}]
  (POST "/signin"
        {:params        {:email    email
                         :password password}
         :format        :json
         :error-handler (fn [res]
                          (alert-update-display-fn res))
         :handler       (fn [res]
                          (do
                            (.setItem js/localStorage "rmfu-feed-identity-token" res)
                            (session/put! :profile res)
                            (secretary/dispatch! "/feed")))}))

(defn sign-in-handler []
  (let [{:keys [email password]} (:profile @app-state)]
    (if-not (or (empty? email) (empty? password))
      (if (validation/is-email? email)
        (post-sign-in (:profile @app-state))
        (do
          (swap! app-state assoc-in [:alert :title] "Invalid Email")
          (swap! app-state update-in [:alert :display] not))))))

(defn email-input-field []
  (let [detect-key #(case (.-which %)
                     13 (sign-in-handler)
                     27 (swap! app-state assoc-in [:profile :email] "")
                     nil)]
    [:input {:type        "email"
             :className   "form-control"
             :value       (get-in @app-state [:profile :email])
             :placeholder "me@mail.net"
             :on-change   #(swap! app-state assoc-in [:profile :email] (-> % .-target .-value))
             :on-key-down detect-key}]))

(defn passsword-input-field []
  (let [detect-key #(case (.-which %)
                     13 (sign-in-handler)
                     27 (swap! app-state assoc-in [:profile :password] "")
                     nil)]
    [:input {:type        "password"
             :className   "form-control"
             :value       (get-in @app-state [:profile :password])
             :placeholder "8 or more characters"
             :on-change   #(swap! app-state assoc-in [:profile :password] (-> % .-target .-value))
             :on-key-down detect-key}]))

(defn sign-in []
  (let []
    (reagent/create-class
      {:component-will-mount
       (fn []
         (when (identity-token)
           (secretary/dispatch! "/feed")))
       :reagent-render
       (fn []
         [:div
          [welcome-component-wrapper
           [:div.form-group {:style {:padding "1em"}}
            [alert :warning app-state 5000]
            [:p.text-center.greytext "Sign In to Your Account"]
            [:label "email:"]
            [email-input-field]
            [:label "password:"]
            [passsword-input-field]
            [:br]
            [:div.checkbox
             [:label
              [:input {:type "checkbox"}] "remember me?"]
             [:p.pull-right
              [:button.btn.btn-link
               {:type     "button"
                :style {:padding "0"}
                :on-click #(secretary/dispatch! "/reset-password")} "I forgot my password."]]]
            [:br]
            [:button.btn.btn-primary.active.pull-right
             {:type     "button"
              :on-click #(sign-in-handler)} "Sign In!"]

            [:br][:br][:hr]
            [:p.text-center.greytext "Don't have an account?"]
            [:div.text-center
            [:button.btn.btn-primary
             {:type     "button"
              :on-click #(secretary/dispatch! "/sign-up")} "Sign Up Here"]]]]])})))
