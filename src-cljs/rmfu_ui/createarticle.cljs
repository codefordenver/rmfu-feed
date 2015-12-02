(ns rmfu-ui.createarticle
  (:require [rmfu-ui.nav :refer [nav]]
            [rmfu-ui.utils :refer [get-identity-token]]
            [rmfu.validation :as validate]
            [rmfu-ui.alert :refer [alert]]
            [reagent.core :as reagent]
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
      [alert :error app-state]
      [:h1.text-center "Create Article"]
      [:h4 "Title of Article"]
      [:input.form-control
       {:type        "text",
        :placeholder "Milk contains up to 10% of the following hormones upon bottling.",
        :value       (:title @new-article-state)
        :on-change   #(swap! new-article-state assoc :title (get-event-value %))}]
      [:br]
      ; [:h4
      ;  "Link to Article (optional):"]
      ; [:input.form-control
      ;  {:type "text", :placeholder "http://www.milksafe.com/hormones.html", :value ""}]

      [:h4 "Description of Article (optional):"]
      [:textarea.form-control
       {:rows      "14"
        :value     (:content @new-article-state)
        :on-change #(swap! new-article-state assoc :content (get-event-value %))}]

      ; Keeping this stuff here for now
      ; Not sure if we are going to include this funcitonality or not

      ; [:h4 "Relevant to these Regions"]
      ; [:div.input-group
      ;  [:input.form-control
      ;   {:type "text", :placeholder "Enter Zip Code", :value ""}]
      ;  [:span.input-group-addon.select-group
      ;   [:select.c-select.select-item
      ;    [:option
      ;     "+1 mile"]
      ;    [:option
      ;     "+10 miles"]
      ;    [:option
      ;     "+50 miles"]]]
      ;  [:div.input-group-btn
      ;   [:button.btn.btn-default
      ;    {:name "type", :type "button", :value "add"}
      ;    "Add"]]]
      ; [:br]
      ; [:h4 "Relevant to these Topics"]
      ; [:div.input-group
      ;   [:input.form-control
      ;     {:type "topics", :placeholder "Enter Keywords or select from suggestions below", :value ""}]
      ;   [:span.input-group-btn
      ;     [:button.btn.btn-default
      ;       {:type "button", :value "add"}
      ;       "Add"]]]
      ; [:ul.list-inline
      ;   [:li
      ;     [:a
      ;       {:href "#"}
      ;       "Dairy"]]
      ;   [:li
      ;     [:a
      ;       {:href "#"}
      ;       "Organic"]]
      ;   [:li
      ;     [:a
      ;       {:href "#"}
      ;       "GMO"]]
      ;   [:li
      ;     [:a
      ;       {:href "#"}
      ;       "Fertilizer"]]
      ;   [:li
      ;     [:a
      ;       {:href "#"}
      ;       "Equipment"]]
      ;   [:li
      ;     [:a
      ;       {:href "#"}
      ;       "Poultry"]]
      ;   [:li
      ;     [:a
      ;       {:href "#"}
      ;       "Sustainable"]]
      ;   [:li
      ;     [:a
      ;       {:href "#"}
      ;       "Green"]]
      ;   [:li
      ;     [:a
      ;       {:href "#"}
      ;       "Pests"]]
      ;   [:li
      ;     [:a
      ;       {:href "#"}
      ;       "Tobacco"]]
      ;   [:li
      ;     [:a
      ;       {:href "#"}
      ;       "Fuel"]]
      ;   [:li
      ;     [:a
      ;       {:href "#"}
      ;       "Seeds"]]
      ;   [:li
      ;     [:a
      ;       {:href "#"}
      ;       "Wool"]]
      ;   [:li
      ;     [:a
      ;       {:href "#"}
      ;       "Solar"]]
      ;   [:li
      ;     [:a
      ;       {:href "#"}
      ;       "Wind"]]
      ;   [:li
      ;     [:a
      ;       {:href "#"}
      ;       "Legislation"]]
      ;   [:li
      ;     [:a
      ;       {:href "#"}
      ;       "Vegetables"]]
      ;   [:li
      ;     [:a
      ;       {:href "#"}
      ;       "Fruit"]]
      ;   [:li
      ;     [:a
      ;       {:href "#"}
      ;       "Polls"]]]
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
                              :handler       (fn [res]
                                               (js/alert (str "success. Created at: " res))
                                               (reset! new-article-state initial-article-state))})
                       (catch js/Object _ (alert-update-fn "Article title cannot be empty"))))}
        "Create the Article"]]]]]])
