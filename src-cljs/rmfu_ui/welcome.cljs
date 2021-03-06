(ns rmfu-ui.welcome
  (:require [secretary.core :as secretary :include-macros true]))

(defn welcome-component-wrapper
  "Wraps element in a welcome componet inside a .jumbotron"
  [element]
  [:div.container.jumbotron
   [:div.row
    [:div.col-lg-12
     [:h1.welcomeheading.text-center
      [:a {:on-click #(secretary/dispatch! "/")} "Feed"]]
      [:p.text-center "Growing conversation and community with"]
     [:img.welcome-logo {:href "/#/feed" :src "/images/rmfu-logo.png"}]
     [:hr]
                             element]]])
