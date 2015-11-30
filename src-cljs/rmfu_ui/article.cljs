(ns rmfu-ui.article
  (:require [rmfu-ui.nav :refer [nav]]
            [reagent.core :as reagent :refer [atom]]
            [reagent.session :as session]
            [ajax.core :refer [PUT GET]]
            [secretary.core :as secretary :include-macros true]))

(defn article []
  (let [app-state (atom {:title "" :text "" :date ""})
        fetch-article (fn [article-id] (GET (str "/article/" article-id)
                                            {
                                             :error-handler (fn [] (print "blah"));#(secretary/dispatch! "/")
                                             :response-format :json
                                             :keywords? true
                                             :handler       (fn [res]
                                                              (reset! app-state res)
                                                              )}))
        ]
  (reagent/create-class
    {
     :component-will-mount #(fetch-article (session/get :article-id))
     :reagent-render (fn []
  [:div
   [nav]
   [:div.container.jumbotron.large-main
    [:div.row
     [:div.col-lg-12
      [:h1.text-center (:title @app-state)]
        [:div.pre-scrollable.borderbox
        [:div.dategroup
          [:h4.articleheading (:subtitle @app-state)]
          [:p.date (:date @app-state)]]
          [:p (:article-text @app-state)]]
          [:br]
        [:h4.greyheading "Category"]
        [:p.greytext.displayinline "username"] [:p.date.displayinline.pull-right "October 1, 2015"]]
      [:h4.greyheading.text-center "Comments"]
      [:div
       [:div.pre-scrollable.borderboxblack.height150
        [:div.col-lg-3.date
         "Oct 1, 2015"]
        [:div.col-lg-9.bullet
         "Joe - Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididul Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."]
        [:div.col-lg-3.date
         "Oct 1, 2015"]
        [:div.col-lg-9.bullet
         "Bob - Consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et!"]
        [:div.col-lg-3.date
         "Oct 1, 2015"]
        [:div.col-lg-9.bullet
         "Bill - Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."]
        [:div.col-lg-3.date
         "Oct 1, 2015"]
        [:div.col-lg-9.bullet
         "Roscoe - Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."]
        [:div.col-lg-3.date
         "Oct 1, 2015"]
        [:div.col-lg-9.bullet
         "Tiberius - Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur."]]]]]])
     })))
