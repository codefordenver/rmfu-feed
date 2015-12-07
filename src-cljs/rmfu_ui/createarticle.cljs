(ns rmfu-ui.createarticle
  (:require [rmfu-ui.nav :refer [nav]]
            [rmfu-ui.utils :refer [get-identity-token]]
            [rmfu.validation :as validate]
            [rmfu-ui.alert :refer [alert]]
            [reagent.core :as reagent]
            [rmfu-ui.utils :as utils]
            [ajax.core :refer [POST]]))

(def ^{:private true} initial-article-state
  "The initial state for a new article"
  {:title   nil
   :content nil})

(def ^{:private true} new-article-state
  "The state of the create article page."
  (reagent/atom initial-article-state))

(def app-state
  (reagent/atom {:alert {:display false
                         :message nil
                         :title   nil}}))

(defn alert-update-fn [title]
  (swap! app-state assoc-in [:alert :title] title)
  (swap! app-state update-in [:alert :display] not))

(defn get-event-value
  [e]
  (-> e .-target .-value))

(defn createarticle
  "The page for creating a new article."
  []
  [:div
   [:div.container.jumbotron.large-main
    [:div.row
     [:div.col-lg-12
      [alert :error app-state 2500]
      [:h1.text-center "Create Article"]
      [:h4 "Title of Article"]
      [:input.form-control
       {:type        "text",
        :placeholder "Milk contains up to 10% of the following hormones upon bottling.",
        :value       (:title @new-article-state)
        :on-change   #(swap! new-article-state assoc :title (get-event-value %))}]
      [:br]

      [:h4 "Description of Article (optional):"]
      [:textarea.form-control
       {:rows      "14"
        :value     (:content @new-article-state)
        :on-change #(swap! new-article-state assoc :content (get-event-value %))}]

      [:p.text-center
       [:button.btn
        {:name     "type"
         :type     "button"
         :value    "create_article"
         :on-click (fn []
                     (try
                       (validate/article @new-article-state)
                       (POST "/api/articles"
                             {:headers       {:identity (get-identity-token)}
                              :format        :json
                              :params        @new-article-state
                              :error-handler #(alert-update-fn %)
                              :handler       (fn [res-body]
                                               (reset! new-article-state initial-article-state)
                                               (utils/navigate-to res-body))})
                       (catch js/Object _ (alert-update-fn "Article title cannot be empty"))))}
        "Create the Article"]]]]]])
