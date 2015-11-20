(ns rmfu-ui.admin
  (:require [rmfu-ui.nav :refer [nav]]))

(defn admin []
  [:div
   [nav "admin"]
   [:div.container.jumbotron.largemain
    [:div.row
     [:div.col-lg-12
      [:h1.text-center "admin"]]]]])
