(ns rmfu-ui.feed
  (:require [rmfu-ui.nav :refer [nav]]))

(defn feed []
  [:div
   [nav]
   [:div.container.jumbotron.largemain
    [:div.row
     [:div.col-lg-12
      [:h1.displayinline "Feeds "]
      " "
      [:h4.displayinline.greytext " {{Inspiring tagline goes here}}"]
      [:hr]
      [:h4 "Articles"]
      [:div.articlelisting
       [:p.articlelink
        [:a
         {:href "#"}
         "NFU Responds to crop insurance budget deal "
         [:span.label.label-default.label-pill "14"]]]
       [:p.user
        "Bill Stevenson "
        [:small "November 1, 2015"]
        " "
        [:a {:href "#"} [:span.label.label-primary "All"]]]]
      [:div.articlelisting
       [:p.articlelink
        [:a
         {:href "#"}
         "Emerging leaders tour Ring-A-Ding Farm "
         [:span.label.label-default.label-pill "14"]]]
       [:p.user
        "Bill Stevenson "
        [:small "November 1, 2015"]
        " "
        [:a
         {:href "#"}
         [:span.label.label-primary "Farming Technology"]
         " "
         [:span.label.label-primary "Fruit/Veg"]]]]
      [:div.articlelisting
       [:p.articlelink
        [:a
         {:href "#"}
         "Felis euismod semper eget lacinia "
         [:span.label.label-default.label-pill "#replies"]]]
       [:p.user
        "username "
        [:small "Month day, year"]
        " "
        [:a {:href "#"} [:span.label.label-primary "Label"]]]]
      [:div.articlelisting
       [:p.articlelink
        [:a
         {:href "#"}
         "Felis euismod semper eget lacinia "
         [:span.label.label-default.label-pill "#replies"]]]
       [:p.user
        "username "
        [:small "Month day, year"]
        " "
        [:a {:href "#"} [:span.label.label-primary "Label"]]]]
      [:div.articlelisting
       [:p.articlelink
        [:a
         {:href "#"}
         "Felis euismod semper eget lacinia "
         [:span.label.label-default.label-pill "#replies"]]]
       [:p.user
        "username "
        [:small "Month day, year"]
        " "
        [:a {:href "#"} [:span.label.label-primary "Label"]]]]
      [:div.articlelisting
       [:p.articlelink
        [:a
         {:href "#"}
         "Felis euismod semper eget lacinia "
         [:span.label.label-default.label-pill "#replies"]]]
       [:p.user
        "username "
        [:small "Month day, year"]
        " "
        [:a {:href "#"} [:span.label.label-primary "Label"]]]]
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
          [:td "Month day, year"]]]]]]]]

 ])
