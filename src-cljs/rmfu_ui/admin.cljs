(ns rmfu-ui.admin
  (:require [rmfu-ui.alert :refer [alert]]
            [reagent.core :as reagent]
            [secretary.core :as secretary]
            [ajax.core :refer [PUT GET DELETE]]
            [cljsjs.jquery]
            [cljsjs.fixed-data-table]
            [rmfu-ui.utils :refer [index-of get-identity-token]]))

(declare cell-and-checkbox)

(def Table (reagent/adapt-react-class js/FixedDataTable.Table))
(def Column (reagent/adapt-react-class js/FixedDataTable.Column))

(def app-state (reagent/atom {:users    []
                              :alert    {:display false
                                         :message nil
                                         :title   nil}
                              :articles []}))

(defn alert-update-display-fn [msg]
  (swap! app-state assoc-in [:alert] {:display true
                                      :message msg
                                      :title   nil}))

(defn block-user [email state]
  (PUT "/api/block-user"
       {:params        {:email email :blocked? state}
        :format        :json
        :headers       {:identity (get-identity-token)}
        :handler       (fn [res]
                         (alert-update-display-fn (str "User with email: " email " successfully " res)))
        :error-handler #(js/alert %)}))

(defn fetch-all-users []
  (GET "/api/users"
       {:headers         {:identity (get-identity-token)}
        :error-handler   #(secretary/dispatch! "/")
        :response-format :json
        :keywords?       true
        :handler         #(swap! app-state assoc-in [:users] %)}))

(defn fetch-all-articles []
  (GET "/api/articles"
       {:headers         {:identity (get-identity-token)}
        :error-handler   #(secretary/dispatch! "/")
        :response-format :json
        :keywords?       true
        :handler         #(swap! app-state assoc-in [:articles] %)}))

(defn delete-article [id]
  (DELETE (str "/api/articles/" id)
          {:headers       {:identity (get-identity-token)}
           :error-handler #(alert-update-display-fn %)
           :handler       #(alert-update-display-fn %)}))

(defn getter [k row] (get row k))

(defn blocked-getter [k row]
  (with-meta [cell-and-checkbox row] {:key k}))

(defn cell-and-checkbox [row]
  (let [users (sort-by first (:users @app-state))
        users-table (mapv #(into [] (vals %)) users)
        index (index-of users-table row)
        user (get (:users @app-state) index)]
    [:div.checkbox
     [:label
      [:input {:type      "checkbox"
               :checked   (:blocked? user)
               :on-change (fn [e]
                            (let [val (-> e .-target .-checked)
                                  email (get row 0)]
                              (swap! app-state update-in [:users] #(assoc-in % [index :blocked?] val))
                              (block-user email val)))}]]]))

(defn block-users-table [table]
  [:div.col-lg-12
   [:h2.text-center "block users"]
   [:h5.text-center [:small "use the checkbox to (un)block a user"]]
   [Table {:width        600
           :height       400
           :rowHeight    50
           :rowGetter    #(get table %)
           :rowsCount    (count table)
           :headerHeight 50}
    [Column {:label "last" :dataKey 6 :cellDataGetter getter :width 100 :align "center"}]
    [Column {:label "first" :dataKey 5 :cellDataGetter getter :width 100 :align "center"}]
    [Column {:label "email" :dataKey 0 :cellDataGetter getter :width 100 :align "center"}]
    [Column {:label "username" :dataKey 2 :cellDataGetter getter :width 100 :align "center"}]
    [Column {:label "blocked?" :dataKey 1 :align "center" :cellDataGetter blocked-getter
             :width 100 :cellRenderer reagent/as-element}]]])

(defn remove-article-table []
  [:div.col-lg-12
   [:h2.text-center "moderate articles"]
   [:h5.text-center [:small "use the [x] buttons to remove an article"]]
   [:table {:id           "moderate-articles-table"
            :className    "table table-striped table-bordered"
            :cell-spacing "0"
            :width        "20px"}
    [:thead>tr
     [:th "article-id"]
     [:th "article-title"]
     [:th "author-email"]
     [:th "delete"]]
    [:tbody
     (for [article (:articles @app-state)
           :let [id (:_id article)]]
       ^{:key id} [:tr
                   [:td id]
                   [:td.text-overflow (:title article)]
                   [:td (:author article)]
                   [:td
                    [:button.btn.btn-default
                     {:on-click (fn [_]
                                  (delete-article id)
                                  (fetch-all-articles))}
                     "×"]]])]]])

(defn admin []
  (let []
    (reagent/create-class
      {:component-will-mount
                            (fn []
                              (fetch-all-users)
                              (fetch-all-articles))
       :component-did-mount (fn []
                              (.ready
                                (js/$ js/document)
                                (fn []
                                  (.DataTable (js/$ "#moderate-articles-table")))))
       :reagent-render      (fn []
                              (let [users (sort-by first (:users @app-state))
                                    users-table (mapv #(into [] (vals %)) users)]
                                [:div.table
                                 [:div.container.jumbotron.large-main
                                  [:div.row
                                   [:h1.text-center "admin tools"]
                                   [:br]
                                   [block-users-table users-table]
                                   [:br]
                                   [alert :info app-state 2500]
                                   [:br]
                                   [remove-article-table]]]]))})))
