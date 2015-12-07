(ns ^:figwheel-always rmfu-ui.core
  (:require
    [ajax.core :refer [POST GET]]
    [goog.events :as events]
    [goog.history.EventType :as EventType]
    [reagent.core :as reagent]
    [reagent.session :as session]
    [reagent.validation :as validation]
    [rmfu-ui.createarticle :refer [createarticle]]
    [rmfu-ui.article :refer [article]]
    [rmfu-ui.profile :refer [profile]]
    [rmfu-ui.nav :refer [nav]]
    [rmfu-ui.sign-in :refer [sign-in]]
    [rmfu-ui.sign-up :refer [sign-up]]
    [rmfu-ui.new-password :refer [new-password-component]]
    [rmfu-ui.customfeed :refer [customfeed]]
    [rmfu-ui.feed :refer [feed]]
    [rmfu-ui.admin :refer [admin]]
    [rmfu-ui.nav :refer [nav]]
    [rmfu-ui.about :refer [about]]
    [secretary.core :as secretary :include-macros true]
    [rmfu-ui.welcome :refer [welcome-component-wrapper]])
  (:import goog.History))

(enable-console-print!)

;; -------------------------
;; HTTP Request


(defn request-password-reset [profile]
  (POST "/send-reset-password-email"
        {:params        {:email (:email profile)}
         :format        :json
         :error-handler #(js/alert %)
         :handler       (fn [res]
                          (do
                            (println "res:" res)
                            (js/alert res))
                          )}))

;; -------------------------
;; Utility functions

;; (add-watch form-state :logger #(-> %4 clj->js js/console.log))

(defn reset-password-email [profile]
  (if-not (empty? (:email profile))
    (if (validation/is-email? (:email profile))
      (request-password-reset profile)
      (js/alert "Invalid Email"))))

;; -------------------------
;; <Components/>

(defn email-input-field [profile]
  [:input {:type        "email"
           :className   "form-control"
           :value       (:email @profile)
           :placeholder "me@mail.net"
           :on-change   #(swap! profile assoc :email (-> % .-target .-value))}])

(defn reset-password-component []
  (let [profile (reagent/atom {:username ""
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
  [:div
   [nav]
   [(session/get :current-page)]])

(defn four-o-four [msg]
  [:div.container.jumbotron.big-text [:u (or msg 404)]])

(secretary/defroute "/" []
                    (session/put! :current-page #'sign-in))

(secretary/defroute "/sign-in" []
                    (session/put! :current-page #'sign-in))

(secretary/defroute "/sign-up" []
                    (session/put! :current-page #'sign-up))

(secretary/defroute "/profile" []
                    (session/put! :current-page #'profile))

(secretary/defroute "/create" []
                    (session/put! :current-page #'createarticle))

(secretary/defroute "/articles/#!:id" [id]
                    (session/put! :article-id id)
                    (session/put! :current-page #'article))

(secretary/defroute "/customfeed" []
                    (session/put! :current-page #'customfeed))

(secretary/defroute "/feed" []
                    (session/put! :current-page #'feed))

(secretary/defroute "/admin" []
                    (session/put! :current-page #'admin))

(secretary/defroute "/about" []
                    (session/put! :current-page #'about))

(secretary/defroute "/email-verified" []
                    (session/put! :current-page #'email-verified-component))

(secretary/defroute "/reset-password" []
                    (session/put! :current-page #'reset-password-component))

(secretary/defroute "/new-password" [query-params]
                    (session/put! :token (:token query-params))
                    (session/put! :current-page #'new-password-component))

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
