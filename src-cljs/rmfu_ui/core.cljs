(ns ^:figwheel-always rmfu-ui.core
  (:require
    [reagent.core :as reagent :refer [atom]]
    [ajax.core :refer [POST]]))

(enable-console-print!)

;; define your app data so that it doesn't get over-written on reload

(defonce show-loading (atom {:show false}))

(defn post-profile [profile]
  ;(swap! show-loading assoc :show true)
  (let []
    (println "posting->" (:username profile) ":" (:password profile))
    (POST "http://localhost:3000/login"
          {:params  {:username (:username profile)
                     ;; replace this with a string parser
                     :password (:password profile)}
           :format  :json
           ;; :response-format :json
           ;:keywords? true
           :handler (fn [res]
                      (do
                        (swap! show-loading assoc :show false)
                        (println "res:" res))
                      )})))

(defn app []
  "This is our <root/> component"
  (let [profile (atom {:username ""
                       :password ""})

        iconometry ["☴" "☳" "☱" "☲" " × " "~" "☀" "☁" "☄"]

        stop #(reset! profile {:username "reset"
                               :password "reset"})

        enter #(let [username (:username @profile)
                     password (:password @profile)]
                (if-not (or (empty? username) (empty? password))
                  (post-profile profile)))

        detect-key #(case (.-which %)
                     13 (enter)
                     27 (stop)
                     nil)]

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
       [:div.form-group {:style {:padding "1em"}}
        [:label "username:"]
        [:input {:type        "text"
                 ;:value       (:username @profile)
                 :on-blur     enter
                 :className   "form-control"
                 :placeholder "username"
                 :on-change   #(swap! profile assoc :username (-> % .-target .-value))
                 ;:on-key-down detect-key
                 }]
        [:label "password:"]
        [:input {:className   "form-control"
                 ;:value       (:password @profile)
                 :type        "password"
                 :placeholder "*********"
                 :on-change   #(swap! profile assoc :password (-> % .-target .-value))
                 ;:on-key-down detect-key
                 }]
        [:div.checkbox
         [:label
          [:input {:type "checkbox"}] "remember me ?"]]
        [:button.btn.btn-default {:type     "submit"
                                  :on-click (fn [e]
                                              (post-profile @profile))} "sign-in"]
        [:button.btn.btn-default.pull-right {:type     "submit"
                                             :on-click (fn [x] (println "clicked"))} "sign-up"]]
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

