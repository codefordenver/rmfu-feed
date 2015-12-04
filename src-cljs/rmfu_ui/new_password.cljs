(ns rmfu-ui.new-password
  (:require [rmfu-ui.alert :refer [alert]]
            [ajax.core :refer [PUT]]
            [rmfu-ui.welcome :refer [welcome-component-wrapper]]
            [reagent.core :as reagent]
            [secretary.core :as secretary]
            [reagent.validation :as validation]
            [ajax.core :refer [POST]]
            [reagent.session :as session]))


(defn new-password-component []
  (let [app-state (reagent/atom {:password ""
                                 :alert    {:type    :success
                                            :display false
                                            :message nil
                                            :title   nil}})
        update-alert-fn (fn [type title msg]
                          (swap! app-state assoc-in [:alert] {:type    type
                                                              :display true
                                                              :message msg
                                                              :title   title}))]
    (fn []
      [welcome-component-wrapper
       [:div.form-group {:style {:padding "1em"}}
        [alert app-state 3000]
        [:p.text-center.bg-primary "New Password"]
        [:label "password:"]
        [:input {:type        "password"
                 :className   "form-control"
                 :value       (:password @app-state)
                 :placeholder "8 or more characters"
                 :on-change   (fn [e] (swap! app-state update-in [:password] #(-> e .-target .-value)))}]
        [:br]
        [:button.btn.btn-default
         {:type     "button"
          :on-click (fn [_]
                      (if (or (empty? (:password @app-state))
                               (not (validation/has-value? (:password @app-state))))
                        (update-alert-fn :error nil "Invalid Password")
                        (PUT "/reset-password-from-form"
                             {:params        {:token        (session/get :token)
                                              :new-password (:password @app-state)}
                              :format        :json
                              :error-handler #(update-alert-fn :error (:status-text %) (:response %))
                              :handler       (fn [res]
                                               (session/remove! :token)
                                               (update-alert-fn :success res "redirecting to home page in 5 seconds...")
                                               (.replaceState js/history #js {} "welcome" "/")
                                               (.setTimeout js/window #(secretary/dispatch! "/") 5000))})))} "reset"]]])))
