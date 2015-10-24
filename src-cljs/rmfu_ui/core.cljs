(ns ^:figwheel-always rmfu-ui.core
  (:require
    [reagent.core :as reagent :refer [atom]]
    [ajax.core :refer [POST]]))

(enable-console-print!)

;; define your app data so that it doesn't get over-written on reload

(defonce form-state (atom {:show-loading false
                           :signing-up false}))

;; -------------------------
;; HTTP Request

(defn post-sign-in [profile]
  (let [{:keys [email password]} profile]
    (swap! form-state assoc :show-loading (not (:show-loading @form-state)))
    (println "posting->" email ":" password)
    (POST "http://localhost:3000/signin"
          ;; TODO: validate these fields
          {:params  {:email email
                     :password password}
           :format  :json
           ;; :response-format :json
           ;; :keywords? true
           :handler (fn [res]
                      (do
                        (swap! form-state assoc :show-loading (not (:show-loading @form-state)))
                        (println "res:" res))
                      )})))

(defn post-sign-up [profile]
  (let [{:keys [username password email]} profile]
    (swap! form-state assoc :show-loading (not (:show-loading @form-state)))
    (POST "http://localhost:3000/signup"
          ;; TODO: validate these fields
          {:params  {:username username
                     :password password
                     :email email}
           :format  :json
           ;; :response-format :json
           ;; :keywords? true
           :handler (fn [res]
                      (do
                        (swap! form-state assoc :show-loading (not (:show-loading @form-state)))
                        (println "res:" res))
                      )})))

;; -------------------------
;; Utility functions

(defn sign-in [profile]
  (let [{:keys [email password]} @profile]
    (swap! form-state assoc :signing-up false)
    (if (not (or (empty? email) (empty? password)))
      (post-sign-in @profile))))

(defn sign-up [profile]
  (let [{:keys [email username password]} @profile]
    (if (not (or (empty? email) (empty? username) (empty? password)))
      (post-sign-up @profile))))

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
             :on-key-down detect-key
             }])
  )

(defn passsword-input-field [profile]
  (let [detect-key #(case (.-which %)
                     13 (sign-in @profile)
                     27 (swap! profile assoc :password "")
                     nil)]
    [:input {:type        "password"
             :className   "form-control"
             :value        (:password @profile)
             ;:on-blur     #(sign-in @profile)
             :placeholder "*********"
             :on-change   #(swap! profile assoc :password (-> % .-target .-value))
             :on-key-down  detect-key
             }]))

(defn username-input-field [profile show?]
  (when (true? show?)
    [:div
     [:p.text-center.bg-primary "Create Account"]
     [:label "username:"]
     [:input {:type        "text"
              :className   "form-control"
              :value        (@profile :username)
              ;:on-blur     #(sign-in @profile)
              :placeholder "username"
              :on-change   #(swap! profile assoc :username (-> % .-target .-value))
              }]]))

;; -------------------------
;; <Root/>

;; (add-watch form-state :logger #(-> %4 clj->js js/console.log))

(defn app [form-state]

  "This is our <root/> component"

  (let [profile (atom {:username ""
                       :email ""
                       :password ""})

        show-loading? (:show-loading @form-state)

        signing-up? (:signing-up @form-state)

        iconometry ["☴" "☳" "☱" "☲" " × " "~" "☀" "☁" "☄"]]

    (add-watch profile :logger #(-> %4 clj->js js/console.log))
    [:div.container.jumbotron
     [:div.row
      [:div.col-lg-12
       [:p.text-center "welcome to"]
       [:h1.text-center
        [:a {:href "/"
             :style {:color           "dimgray"
                     :font-size       "1.15em"}} "FEED"]]
       [:hr]
       [:h4.text-center [:small "☴"]]
       [:h4.text-center
        [:small [:em "by "]]
        [:a {:href "http://www.rmfu.org/" :target "blank"}
         [:small [:strong "Rocky Mountain Farmers Union"]]]]
       [:p.text-center "and " [:a {:href "http://www.codefordenver.org/" :target "blank"}
                               [:strong "Code For Denver"]]]
       [:div.form-group {:style {:padding "1em"}}

        [username-input-field profile (:signing-up @form-state)]
        ((fn [state] (when (false? state) [:p.text-center.bg-primary "Sign in"])) signing-up?)
        [:label "email:"]
        [email-input-field profile]
        [:label "password:"]
        [passsword-input-field profile]

        [:br]

        ((fn [state]
           (if (true? state)
             [:div.checkbox
              [:label
               [:input {:type "checkbox"}] "remember me ?"]])) signing-up?)

        [:button.btn.btn-default {:type "button"
                                  :on-click (fn [e]
                                              (sign-in profile)
                                              (.preventDefault e))} "sign-in"]

        [:button.btn.btn-default.pull-right {:type "button"
                                             :on-click (fn [e]
                                                         (swap! form-state assoc :signing-up true)
                                                         (sign-up profile)
                                                         (.preventDefault e))
                                             } "sign-up"]]

       ((fn [state] (if (true? state)
                      [:p.ball-loader.text-center {:style {:left   180
                                                           :bottom 69}}]
                      "")) show-loading?)]]]))

(reagent/render-component [app form-state]
                          (. js/document (getElementById "app")))


(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
  )

