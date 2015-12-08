(ns rmfu-ui.welcome
  (:require [secretary.core :as secretary :include-macros true]))

(defn welcome-component-wrapper
  "Wraps element in a welcome componet inside a .jumbotron"
  [element]
  [:div.container.jumbotron
   [:div.row
    [:div.col-lg-12
     [:p.text-center "welcome to"]
     [:h1.text-center
      [:a {:on-click #(secretary/dispatch! "/")
           :style    {:color     "dimgray"
                      :font-size "1.15em"}} "FEED"]]
     [:img.welcome-logo {:href "/#/feed" :src "/images/rmfu-logo.png"}]
     [:hr]
     [:h4.text-center [:small "☀"]]
     [:h4.text-center
      [:small [:em "by "]]
      [:a {:href "http://www.rmfu.org/" :target "blank"}
       [:small [:strong "Rocky Mountain Farmers Union"]]]]
     [:p.text-center "and " [:a {:href "http://www.codefordenver.org/" :target "blank"}
                             [:strong "Code For Denver"]]] element]]])
