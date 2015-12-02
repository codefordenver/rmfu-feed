(ns rmfu-ui.admin
  (:require [rmfu-ui.alert :refer [alert]]
            [reagent.core :as reagent]
            [secretary.core :as secretary]
            [ajax.core :refer [PUT GET]]
            [cljsjs.fixed-data-table]))

(declare cell-and-checkbox)

(def Table  (reagent/adapt-react-class js/FixedDataTable.Table))
(def Column (reagent/adapt-react-class js/FixedDataTable.Column))

(defn index-of                                              ;; no cljs support for .indexOx
  "return the index of the supplied item, or nil"           ;; therefore this helper
  [v item]
  (let [len (count v)]
    (loop [i 0]
      (cond
        (<= len i) nil,
        (= item (get v i)) i,
        :else
        (recur (inc i))))))

(def app-state (reagent/atom {:users []
                              :alert {:display false
                                      :message nil
                                      :title   nil}}))
(defn alert-update-display-fn []
  (swap! app-state update-in [:alert :display] not))

(defn identity-token []
  (.getItem (.-localStorage js/window) "rmfu-feed-identity-token"))

(defn block-user [email state]
  (PUT "/api/block-user"
       {:params        {:email email :blocked? state}
        :format        :json
        :headers       {:identity (identity-token)}
        :handler       (fn [res]
                         (swap! app-state assoc-in [:alert :message] (str "User with email: " email " successfully " res))
                         (alert-update-display-fn))
        :error-handler #(js/alert %)}))

(defn getter [k row] (get row k))

(defn blocked-getter [k row]
  (with-meta [cell-and-checkbox row] {:key k}))

(defn cell-and-checkbox [row]
  (let [users (sort-by first (:users @app-state))
        table (mapv #(into [] (vals %)) users)
        index (index-of table row)
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

(defn admin []
  (reagent/create-class
    {:component-will-mount
                     (fn []
                       (GET "/api/users"
                            {:headers         {:identity (identity-token)}
                             :error-handler   #(secretary/dispatch! "/")
                             :response-format :json
                             :keywords?       true
                             :handler         #(swap! app-state assoc-in [:users] %)}))
     :reagent-render (fn []
                       (let [users (sort-by first (:users @app-state))
                             table (mapv #(into [] (vals %)) users)]
                         [:div.table
                          [:div.container.jumbotron.large-main
                           [:div.row
                            [:div.col-lg-12
                             [:h1.text-center "admin tools"]
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
                                       :width 100 :cellRenderer reagent/as-element}]]]
                            [alert :info app-state]]]]))}))
