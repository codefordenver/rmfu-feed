(ns rmfu-ui.feed
  (:require [rmfu-ui.nav :refer [nav]]))

(defn feed []
  [:div
   [nav]
   [:div {:className "container jumbotron largemain"}
    [:div {:className "row"}
     [:div {:className "col-lg-12"}
      [:h1 {:className "displayinline"} "Feeds "] " "
      [:h4 {:className "displayinline greytext"} " {{Inspiring tagline goes here}}"]
      [:hr]
      [:h4 "Articles"]
      [:div {:className "articlelisting"}
       [:p {:className "articlelink"}
        [:a {:href "#"} "NFU Responds to crop insurance budget deal "
         [:span {:className "label label-default label-pill"} "14"]]]
       [:p {:className "user"} "Bill Stevenson "
        [:small "November 1, 2015"] " "
        [:a {:href "#"}
         [:span {:className "label label-primary"} "All"]]]]
      [:div {:className "articlelisting"}
       [:p {:className "articlelink"}
        [:a {:href "#"} "Emerging leaders tour Ring-A-Ding Farm "
         [:span {:className "label label-default label-pill"} "14"]]]
       [:p {:className "user"} "Bill Stevenson "
        [:small "November 1, 2015"] " "
        [:a {:href "#"}
         [:span {:className "label label-primary"} "Farming Technology"] " "
         [:span {:className "label label-primary"} "Fruit/Veg"]]]]
      [:div {:className "articlelisting"}
       [:p {:className "articlelink"}
        [:a {:href "#"} "Felis euismod semper eget lacinia "
         [:span {:className "label label-default label-pill"} "#replies"]]]
       [:p {:className "user"} "username "
        [:small "Month day, year"] " "
        [:a {:href "#"}
         [:span {:className "label label-primary"} "Label"]]]]
      [:div {:className "articlelisting"}
       [:p {:className "articlelink"}
        [:a {:href "#"} "Felis euismod semper eget lacinia "
         [:span {:className "label label-default label-pill"} "#replies"]]]
       [:p {:className "user"} "username "
        [:small "Month day, year"] " "
        [:a {:href "#"}
         [:span {:className "label label-primary"} "Label"]]]]
      [:div {:className "articlelisting"}
       [:p {:className "articlelink"}
        [:a {:href "#"} "Felis euismod semper eget lacinia "
         [:span {:className "label label-default label-pill"} "#replies"]]]
       [:p {:className "user"} "username "
        [:small "Month day, year"] " "
        [:a {:href "#"}
         [:span {:className "label label-primary"} "Label"]]]]
      [:div {:className "articlelisting"}
       [:p {:className "articlelink"}
        [:a {:href "#"} "Felis euismod semper eget lacinia "
         [:span {:className "label label-default label-pill"} "#replies"]]]
       [:p {:className "user"} "username "
        [:small "Month day, year"] " "
        [:a {:href "#"}
         [:span {:className "label label-primary"} "Label"]]]]
      [:h5 "Latest Comments "
       [:small
        [:small "what your neightbors are saying"]]]
      [:div {:className "table-responsive"}
       [:table {:className "table table-striped table-sm articletable"}
        [:thead
         [:tr
          [:th "User"]
          [:th "Article"]
          [:th "Label"]
          [:th "Date\n"]]]
        [:tbody
         [:tr
          [:td "Jen"]
          [:td "Farmer&#39;s Market Planning"]
          [:td
           [:span {:className "label label-default"} "Fruit/Veg"]]
          [:td "November 1, 2015"]]
         [:tr
          [:td "Katie"]
          [:td "Emerging Leaders Tour Rang-A-Ding Farm"]
          [:td
           [:span {:className "label label-default"} "Farming Technology"]
           [:span {:className "label label-default"} "Fruit/Veg"]]
          [:td "November 1, 2015"]]
         [:tr
          [:td "John"]
          [:td "NFU Responds to Farm Bill"]
          [:td
           [:span {:className "label label-default"} "All"]]
          [:td "November 1, 2015"]]
         [:tr {:className "table-success"}
          [:td "Success"]
          [:td "Article Name"]
          [:td
           [:span {:className "label label-default"} "Label"]]
          [:td "Month day, year"]]
         [:tr {:className "table-warning"}
          [:td "Warning"]
          [:td "Article Name"]
          [:td
           [:span {:className "label label-default"} "Label"]]
          [:td "Month day, year"]]
         [:tr {:className "table-danger"}
          [:td "Danger"]
          [:td "Article Name"]
          [:td
           [:span {:className "label label-default"} "Label"]]
          [:td "Month day, year"]]
         [:tr {:className "table-info"}
          [:td "Info"]
          [:td "Article Name"]
          [:td
           [:span {:className "label label-default"} "Label"]]
          [:td "Month day, year"]]]]]]]]])
