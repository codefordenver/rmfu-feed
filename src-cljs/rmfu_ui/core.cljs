(ns ^:figwheel-always rmfu-ui.core
  (:require
    [reagent.core :as reagent :refer [atom]]
    [ajax.core :refer [POST]]))

(enable-console-print!)

;; define your app data so that it doesn't get over-written on reload

(defonce show-loading (atom {:show false}))

;; -------------------------
;; HTTP Request

(defn signin [profile]
  (let [{:keys [username password]} profile]
    (swap! show-loading assoc :show true)
    (println "posting->" username ":" password)
    (POST "http://localhost:3000/login"
          {:params  {:username username
                     ;; replace this with a string parser
                     :password password}
           :format  :json
           ;; :response-format :json
           ;:keywords? true
           :handler (fn [res]
                      (do
                        (swap! show-loading assoc :show false)
                        (println "res:" res))
                      )})))

;; -------------------------
;; <Components/>

(defn username-and-password-form [profile]
  (let [
        stop #(reset! profile {:username "reset" :password "reset"})

        sigin (fn [profile]
                (signin profile))

        enter (let [username (:username profile)
                    password (:password profile)]
                (if-not (or (empty? username) (empty? password))
                  (signin profile)))

        detect-key #(case (.-which %)
                     13 (enter)
                     27 (stop)
                     )]
    [:div.form-group {:style {:padding "1em"}}
     [:label "username:"]
     [:input {:type        "text"
              :className   "form-control"
              ;:value       (:username @profile)
              ;:on-blur     enter
              :placeholder "username"
              :on-change   #(swap! profile assoc :username (-> % .-target .-value))
              ;:on-key-down detect-key
              }]
     [:label "password:"]
     [:input {:type        "password"
              :className   "form-control"
              ;:value       (:password @profile)
              ;:on-blur     enter
              :placeholder "*********"
              :on-change   #(swap! profile assoc :password (-> % .-target .-value))
              ;:on-key-down detect-key
              }]
     [:div.checkbox
      [:label
       [:input {:type "checkbox"}] "remember me ?"]]
     [:button.btn.btn-default {:on-click #(sigin @profile)} "sign-in"]
     [:button.btn.btn-default.pull-right {:type     "submit"
                                          :on-click (fn [_] (println "clicked"))} "sign-up"]])
  )

;; -------------------------
;; <Root/>

(defn app []
  "This is our <root/> component"
  (let [profile (atom {:username ""
                       :password ""})
        iconometry ["☴" "☳" "☱" "☲" " × " "~" "☀" "☁" "☄"]]

    (add-watch profile :logger #(-> %4 clj->js js/console.log))

    [:div.container.jumbotron
     [:div.row
      [:div.col-lg-12
       [:p.text-center "welcome to"]
       [:h1.text-center {:style {:color           "dimgray"
                                 :font-size       "5em"
                                 :font-decoration "underline"}} "FEED"]
       [:hr]
       [:h4.text-center [:small "☴"]]
       [:h4.text-center
        [:small [:em "by "]]
        [:a {:href "http://www.rmfu.org/" :target "blank"}
         [:small [:strong "Rocky Mountain Farmers Union"]]]]
       [:p.text-center "and " [:a {:href "http://www.codefordenver.org/" :target "blank"}
                               [:strong "Code For Denver"]]]
       [username-and-password-form profile]
       ((fn [state] (if (true? (:show state))
                      [:p.ball-loader.text-center {:style {:left   180
                                                           :bottom 69}}]
                      "")) @show-loading)]]]))

(reagent/render-component [app]
                          (. js/document (getElementById "app")))


(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
  )

