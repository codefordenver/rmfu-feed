(ns rmfu-ui.feed
  (:require [rmfu-ui.nav :refer [nav]]))

(defn feed []
  [:div
   [nav]
   [:div {:class "container jumbotron largemain"}
        [:div {:class "row"}
         [:div {:class "col-lg-12"}
          [:h1 {:class "displayinline"} "Feeds "] " "
          [:h4 {:class "displayinline greytext"} " {{Inspiring tagline goes here}}"]
          [:hr]
          [:h4 "Articles"]
          [:div {:class "articlelisting"}
           [:p {:class "articlelink"}
            [:a {:href "#"} "NFU Responds to crop insurance budget deal "
             [:span {:class "label label-default label-pill"} "14"]]]
           [:p {:class "user"} "Bill Stevenson "
            [:small "November 1, 2015"] " "
            [:a {:href "#"}
             [:span {:class "label label-primary"} "All"]]] ]
          [:div {:class "articlelisting"}
           [:p {:class "articlelink"}
            [:a {:href "#"} "Emerging leaders tour Ring-A-Ding Farm "
             [:span {:class "label label-default label-pill"} "14"]]]
           [:p {:class "user"} "Bill Stevenson "
            [:small "November 1, 2015"] " "
            [:a {:href "#"}
             [:span {:class "label label-primary"} "Farming Technology"] " "
             [:span {:class "label label-primary"} "Fruit/Veg"]]] ]
          [:div {:class "articlelisting"}
           [:p {:class "articlelink"}
            [:a {:href "#"} "Felis euismod semper eget lacinia "
             [:span {:class "label label-default label-pill"} "#replies"]]]
           [:p {:class "user"} "username "
            [:small "Month day, year"] " "
            [:a {:href "#"}
             [:span {:class "label label-primary"} "Label"]]] ]
          [:div {:class "articlelisting"}
           [:p {:class "articlelink"}
            [:a {:href "#"} "Felis euismod semper eget lacinia "
             [:span {:class "label label-default label-pill"} "#replies"]]]
           [:p {:class "user"} "username "
            [:small "Month day, year"] " "
            [:a {:href "#"}
             [:span {:class "label label-primary"} "Label"]]] ]
          [:div {:class "articlelisting"}
           [:p {:class "articlelink"}
            [:a {:href "#"} "Felis euismod semper eget lacinia "
             [:span {:class "label label-default label-pill"} "#replies"]]]
           [:p {:class "user"} "username "
            [:small "Month day, year"] " "
            [:a {:href "#"}
             [:span {:class "label label-primary"} "Label"]]] ]
          [:div {:class "articlelisting"}
           [:p {:class "articlelink"}
            [:a {:href "#"} "Felis euismod semper eget lacinia "
             [:span {:class "label label-default label-pill"} "#replies"]]]
           [:p {:class "user"} "username "
            [:small "Month day, year"] " "
            [:a {:href "#"}
             [:span {:class "label label-primary"} "Label"]]] ]
          [:h5 "Latest Comments "
           [:small
            [:small "what your neightbors are saying"]]]
          [:div {:class "table-responsive"}
           [:table {:class "table table-striped table-sm articletable"}
            [:thead
             [:tr
              [:th "User"]
              [:th "Article"]
              [:th "Label"]
              [:th "Date\n                 "]] ]
            [:tbody
             [:tr
              [:td "Jen"]
              [:td "Farmer&#39;s Market Planning"]
              [:td
               [:span {:class "label label-default"} "Fruit/Veg"]]
              [:td "November 1, 2015"] ]
             [:tr
              [:td "Katie"]
              [:td "Emerging Leaders Tour Rang-A-Ding Farm"]
              [:td
               [:span {:class "label label-default"} "Farming Technology"] " "
               [:span {:class "label label-default"} "Fruit/Veg"]]
              [:td "November 1, 2015"] ]
             [:tr
              [:td "John"]
              [:td "NFU Responds to Farm Bill"]
              [:td
               [:span {:class "label label-default"} "All"]]
              [:td "November 1, 2015"] ]
             [:tr {:class "table-success"}
              [:td "Success"]
              [:td "Article Name"]
              [:td
               [:span {:class "label label-default"} "Label"]]
              [:td "Month day, year"] ]
             [:tr {:class "table-warning"}
              [:td "Warning"]
              [:td "Article Name"]
              [:td
               [:span {:class "label label-default"} "Label"]]
              [:td "Month day, year"] ]
             [:tr {:class "table-danger"}
              [:td "Danger"]
              [:td "Article Name"]
              [:td
               [:span {:class "label label-default"} "Label"]]
              [:td "Month day, year"] ]
             [:tr {:class "table-info"}
              [:td "Info"]
              [:td "Article Name"]
              [:td
               [:span {:class "label label-default"} "Label"]]
              [:td "Month day, year"] ] ] ] ] ] ] ]

 ])
