(ns rmfu-ui.feed
  (:require [rmfu-ui.alert :refer [alert]]
            [reagent.core :as reagent]
            [ajax.core :refer [GET]]
            [rmfu-ui.utils :refer [get-identity-token]]))

(def ^{:private true} loaded-articles (reagent/atom []))

(defn- get-articles []
  (GET "api/articles"
       {:headers         {:identity (get-identity-token)}
        :error-handler   #(js/alert %)
        :response-format :json
        :keywords?       true
        :handler         (fn [res]
                           (reset! loaded-articles res))}))

(defn article-nutshell
  "Component for article list item."
  [article]
  [:div.articlelisting
   [:p.articlelink
    [:a
     {:href (str "#/articles/" (:_id article))}
     (:title article)]]
   [:p.user
    (:author-email article)
    [:small (:created article)]]])

(defn feed []
  (reagent/create-class
    {:component-will-mount
       #(get-articles)
     :reagent-render
       (fn []
          [:div
           [:div.container.jumbotron.large-main
            [:div.row
             [:div.col-lg-12
              [:h1.displayinline "Feed"]
              " "
              [:h4.displayinline.greytext.pull-right [:a {:href "/#/customfeed"} "Customize"]]
              [:hr]
              [:h4 "Articles"]
              (map article-nutshell @loaded-articles)
              [:h5
               "Latest Comments "
               [:small [:small "what your neightbors are saying"]]]
              [:div.table-responsive
               [:table.table.table-striped.table-sm.articletable
                [:thead
                 [:tr
                  [:th "User"]
                  [:th "Article"]
                  [:th "Label"]
                  [:th "Date\n                 "]]]
                [:tbody
                 [:tr
                  [:td "Jen"]
                  [:td "Farmer's Market Planning"]
                  [:td [:span.label.label-default "Fruit/Veg"]]
                  [:td "November 1, 2015"]]
                 [:tr
                  [:td "Katie"]
                  [:td "Emerging Leaders Tour Rang-A-Ding Farm"]
                  [:td
                   [:span.label.label-default "Farming Technology"]
                   " "
                   [:span.label.label-default "Fruit/Veg"]]
                  [:td "November 1, 2015"]]
                 [:tr
                  [:td "John"]
                  [:td "NFU Responds to Farm Bill"]
                  [:td [:span.label.label-default "All"]]
                  [:td "November 1, 2015"]]
                 [:tr.table-success
                  [:td "Success"]
                  [:td "Article Name"]
                  [:td [:span.label.label-default "Label"]]
                  [:td "Month day, year"]]
                 [:tr.table-warning
                  [:td "Warning"]
                  [:td "Article Name"]
                  [:td [:span.label.label-default "Label"]]
                  [:td "Month day, year"]]
                 [:tr.table-danger
                  [:td "Danger"]
                  [:td "Article Name"]
                  [:td [:span.label.label-default "Label"]]
                  [:td "Month day, year"]]
                 [:tr.table-info
                  [:td "Info"]
                  [:td "Article Name"]
                  [:td [:span.label.label-default "Label"]]
                  [:td "Month day, year"]]]]]]]]])}))
