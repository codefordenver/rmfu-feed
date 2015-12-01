(ns ^:figwheel-always rmfu-ui.core
  (:require
    [ajax.core :refer [POST GET PUT]]
    [goog.events :as events]
    [goog.history.EventType :as EventType]
    [reagent.core :as reagent :refer [atom]]
    [reagent.session :as session]
    [reagent.validation :as validation]
    [rmfu-ui.createarticle :refer [createarticle]]
    [rmfu-ui.article :refer [article]]
    [rmfu-ui.profile :refer [profile]]
    [rmfu-ui.customfeed :refer [customfeed]]
    [rmfu-ui.feed :refer [feed]]
    [rmfu-ui.admin :refer [admin]]
    [rmfu-ui.nav :refer [nav]]
    [secretary.core :as secretary :include-macros true]
    [rmfu-ui.welcome :refer [welcome-component-wrapper]])
  (:import goog.History))

(enable-console-print!)

;; define your app data so that it doesn't get over-written on reload

(defonce form-state (atom {:show-loading false
                           :signing-up   false}))

;; -------------------------
;; HTTP Request

(defn post-sign-in [profile]
  (let [{:keys [email password]} profile]
    (POST "/signin"
          {:params        {:email    email
                           :password password}
           :format        :json
           :error-handler #(js/alert %)
           :handler       (fn [res]
                            (do
                            (if (session/get :rmbr-token)
                                (.setItem js/localStorage "rmfu-feed-identity-token" res))
                            (secretary/dispatch! "/feed")))})))

(defn post-sign-up [profile]
  (let [{:keys [username password email]} profile]
    (POST "/signup"
          {:params        {:username username
                           :password password
                           :email    email}
           :format        :json
           :error-handler #(js/alert %)
           :handler       (fn [_]
                            (do
                              (js/alert "User created, please check your email to verify your account")
                              (secretary/dispatch! "/"))
                            )})))

(defn request-password-reset [profile]
  (POST "/send-reset-password-email"
        {:params        {:email (:email profile)}
         :error-handler #(js/alert %)
         :handler       (fn [res]
                          (do
                            (println "res:" res)
                            (js/alert res))
                          )}))

(defn update-password [profile]
  (PUT "/reset-password-from-form"
       {:params        {:email        (:email profile)
                        :new-password (:password profile)}
        :format        :json
        :error-handler #(js/alert %)
        :handler       (fn [res]
                         (do
                           (println "res:" res)
                           (.replaceState js/history #js {} "welcome" "/")
                           (js/alert res))
                         )}))

;; -------------------------
;; Utility functions

;; (add-watch form-state :logger #(-> %4 clj->js js/console.log))

(defn- is-valid-signup-data
  "Checks if the email, username, and password are valid for signing up a new user"
  [{:keys [email username password]}]
  (and (not-any? empty? [email username password])
       (validation/min-length? password 8)
       (validation/has-value? username)
       (validation/is-email? email)
       (validation/has-value? password)))

(defn sign-in [profile]
  (let [{:keys [email password]} profile]
    (if-not (or (empty? email) (empty? password))
      (if (validation/is-email? email)
        (post-sign-in profile)
        (js/alert "Invalid Email")))))

(defn sign-up [profile]
  (if (is-valid-signup-data profile)
    (post-sign-up profile)
    (js/alert "Invalid Credentials")))

(defn reset-password-email [profile]
  (if-not (empty? (:email profile))
    (if (validation/is-email? (:email profile))
      (request-password-reset profile)
      (js/alert "Invalid Email"))))

;; -------------------------
;; <Components/>

(defn email-input-field [profile]
  (let [detect-key #(case (.-which %)
                     13 (sign-in @profile)
                     27 (swap! profile assoc :email "")
                     nil)]
    [:input {:type        "email"
             :className   "form-control"
             :value       (:email @profile)
             :placeholder "me@mail.net"
             :on-change   #(swap! profile assoc :email (-> % .-target .-value))
             :on-key-down detect-key}])
  )

(defn passsword-input-field [profile]
  (let [detect-key #(case (.-which %)
                     13 (sign-in @profile)
                     27 (swap! profile assoc :password "")
                     nil)]
    [:input {:type        "password"
             :className   "form-control"
             :value       (:password @profile)
             :placeholder "8 or more characters"
             :on-change   #(swap! profile assoc :password (-> % .-target .-value))
             :on-key-down detect-key}]))

(defn username-input-field [profile]
  [:input {:type        "text"
           :className   "form-control"
           :value       (@profile :username)
           :placeholder "username"
           :on-change   #(swap! profile assoc :username (-> % .-target .-value))}])

(defn sign-in-component []
  (let [profile (atom {:username ""
                       :email    ""
                       :password ""})]
    (reagent/create-class
      {:component-will-mount (fn []
                               (if-let [identity-token (.getItem (.-localStorage js/window) "rmfu-feed-identity-token")]
                                 (GET "/api/user"
                                      {:headers         {:identity identity-token}
                                       :error-handler   #(js/alert %)
                                       :response-format :json
                                       :keywords?       true
                                       :handler         (fn [res]
                                                          (session/put! :profile res)
                                                          (secretary/dispatch! "/feed"))})))
       :reagent-render       (fn []
                               [welcome-component-wrapper
                                [:div.form-group {:style {:padding "1em"}}
                                 [:p.text-center.bg-primary "Sign in"]
                                 [:label "email:"]
                                 [email-input-field profile]
                                 [:label "password:"]
                                 [passsword-input-field profile]
                                 [:br]
                                 [:div.checkbox
                                  [:label
                                   [:input {:type "checkbox"
                                            :on-click (fn [e] (session/put! :rmbr-token (.-checked (.-target e))))}] "remember me?"]
                                  [:p.pull-right
                                   [:button.btn.btn-sm
                                    {:type     "button"
                                     :on-click #(secretary/dispatch! "/reset-password")} "forgot password?"]]]
                                 [:br]
                                 [:button.btn.btn-primary.active
                                  {:type     "button"
                                   :on-click #(sign-in @profile)} "sign-in"]

                                 [:button.btn.btn-default.pull-right
                                  {:type     "button"
                                   :on-click #(secretary/dispatch! "/sign-up")} "sign-up"]]])})))

(defn sign-up-component []
  (let [profile (atom {:username ""
                       :email    ""
                       :password ""})]
    [welcome-component-wrapper
     [:div.form-group {:style {:padding "1em"}}
      [:p.text-center.bg-primary "Create Account"]
      [:label "username:"]
      [username-input-field profile]
      [:label "email:"]
      [email-input-field profile]
      [:label "password:"]
      [passsword-input-field profile]
      [:br]
      [:button.btn.btn-default {:type     "button"
                                :on-click #(secretary/dispatch! "/")
                                } "sign-in"]

      [:button.btn.pull-right.btn-primary.active {:type     "button"
                                                  :on-click #(sign-up @profile)
                                                  } "sign-up"]]]))

(defn reset-password-component []
  (let [profile (atom {:username ""
                       :email    ""
                       :password ""})]
    [welcome-component-wrapper
     [:div.form-group {:style {:padding "1em"}}
      [:p.text-center.bg-primary "Reset Your Password"]
      [:label "email:"]
      [email-input-field profile]
      [:br]
      [:p [:button.btn.btn-default {:type     "button"
                                    :on-click (fn [e]
                                                (reset-password-email @profile)
                                                (.preventDefault e))
                                    } "reset"]]]]))

(defn new-password-component []
  (let [profile (atom {:username ""
                       :email    ""
                       :password ""})]
    (do
      (swap! profile assoc :email (session/get :email))
      (welcome-component-wrapper
        [:div.form-group {:style {:padding "1em"}}
         [:p.text-center.bg-primary "New Password"]
         [:label "password:"]
         [passsword-input-field profile]
         [:br]
         [:button.btn.btn-default {:type     "button"
                                   :on-click (fn [e]
                                               (if (or (empty? (:password @profile)) (not (validation/has-value? (:password @profile))))
                                                 (js/alert "Invalid Password")
                                                 (update-password @profile))
                                               (.preventDefault e))
                                   } "reset"]]))))

(defn show-loading-component []
  [:p.ball-loader.text-center {:style {:left   180
                                       :bottom 69}}])
(defn email-verified-component []
  [welcome-component-wrapper
   [:div
    [:h3.text-center
     [:u "Your email has been verified"]
     [:br]
     [:p [:a {:on-click #(secretary/dispatch! "/")} "you may now proceed to login"]]]]])

;; -------------------------
;; Routes

(defn current-page []
  [:div [(session/get :current-page)]])

(defn four-o-four [msg]
  [:div
   [nav]
   [:div.container.jumbotron.big-text [:u (or msg 404)]]])

(secretary/defroute "/" []
                    (session/put! :current-page #'sign-in-component))

(secretary/defroute "/sign-up" []
                    (session/put! :current-page #'sign-up-component))

(secretary/defroute "/email-verified" []
                    (session/put! :current-page #'email-verified-component))

(secretary/defroute "/reset-password" []
                    (session/put! :current-page #'reset-password-component))

(secretary/defroute "/new-password" [query-params]
                    (session/put! :email (:email query-params))
                    (session/put! :current-page #'new-password-component))

(secretary/defroute "/profile" []
                    (session/put! :current-page #'profile))

(secretary/defroute "/create" []
                    (session/put! :current-page #'createarticle))

(secretary/defroute "/article" []
                    (session/put! :current-page #'article))

(secretary/defroute "/customfeed" []
                    (session/put! :current-page #'customfeed))

(secretary/defroute "/feed" []
                    (session/put! :current-page #'feed))

(secretary/defroute "/admin" []
                    (session/put! :current-page #'admin))

(secretary/defroute "*" []
                    (session/put! :current-page #'four-o-four))

;; -------------------------
;; History
;; must be called after routes have been defined

(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
      EventType/NAVIGATE
      (fn [event]
        (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

;; -------------------------
;; Bootstrap app

(defn init! []
  (secretary/set-config! :prefix "#")
  (hook-browser-navigation!)
  (reagent/render-component [current-page]
                            (.getElementById js/document "app")))

(init!)

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
  )
