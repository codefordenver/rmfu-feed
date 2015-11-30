(ns rmfu-ui.article
  (:require [rmfu-ui.nav :refer [nav]]
            [rmfu-ui.utils :refer [get-identity-token]]
            [reagent.core :as reagent :refer [atom]]
            [reagent.session :as session]
            [ajax.core :refer [PUT GET]]
            [secretary.core :as secretary :include-macros true]))

(defn article []
  (let [article-state (atom {:title "" :text "" :date ""})
        fetch-article (fn [article-id] (GET (str "api/articles/" article-id)
                                            {:headers         {:identity (get-identity-token)}
                                             :error-handler   #(js/alert %)
                                             :response-format :json
                                             :keywords?       true
                                             :handler         (fn [res]
                                                                (reset! article-state res))}))]
    (reagent/create-class
      {:component-will-mount #(fetch-article (session/get :article-id))
       :reagent-render  (fn []
                          [:div
                           [nav]
                           [:div.container.jumbotron.large-main
                            [:div.row
                             [:div.col-lg-12
                              [:h1.text-center (:title @article-state)]
                              [:div.pre-scrollable.borderbox
                                [:p (:content @article-state)]]
                              [:p.greytext.displayinline (:author-email @article-state)] [:p.date.displayinline.pull-right (:created @article-state)]
                              [:br]
                              [:h4.greyheading "Category"]]
                            [:h4.greyheading.text-center "Comments"]
                            [:div
                             [:div.pre-scrollable.borderboxblack
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
