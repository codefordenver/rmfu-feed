(ns rmfu-ui.admin
  (:require [rmfu-ui.nav :refer [nav]]
            [reagent.core :as reagent]
            [secretary.core :as secretary]
            [ajax.core :refer [PUT GET]]
            [cljsjs.fixed-data-table]))

(declare cell-and-checkbox)

(def Table (reagent/adapt-react-class js/FixedDataTable.Table))
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

(def app-state (reagent/atom {:users []}))

(def identity-token (.getItem (.-localStorage js/window) "rmfu-feed-identity-token"))

(defn block-user [email state]
  (PUT "/api/block-user"
       {:params        {:email email :blocked? state}
        :format        :json
        :headers       {:identity identity-token}
        :handler       #(js/alert (str "User with email: " email " successfully " %))
        :error-handler #(js/alert %)}))

(defn getter [k row] (get row k))

(defn blocked-getter [k row]
  (with-meta [cell-and-checkbox row] {:key k}))

(defn cell-and-checkbox [row]
  (let [users (:users @app-state)
        table (mapv #(into [] (vals %)) users)
        index (index-of table row)
        user (get (:users @app-state) index)]
    [:div.checkbox
     [:label
      [:input {:type      "checkbox"
               :checked   (:blocked? user)
               :on-change (fn [e]
                            (let [val (-> e .-target .-checked)
                                  email (get row 5)]
                              (swap! app-state update-in [:users] #(assoc-in % [index :blocked?] val))
                              (block-user email val)))}]]]))

(defn admin []
  (reagent/create-class
    {:component-will-mount
                     (fn []
                       (GET "/api/users"
                            {:headers         {:identity identity-token}
                             :error-handler   #(secretary/dispatch! "/")
                             :response-format :json
                             :keywords?       true
                             :handler         #(swap! app-state assoc-in [:users] %)}))
     :reagent-render (fn []
                       (let [users (:users @app-state)
                             table (mapv #(into [] (vals %)) users)]
                         [:div.table
                          [nav]
                          [:div.container.jumbotron.largemain
                           [:div.row
                            [:div.col-lg-12
                             [:h1.text-center "admin tools"]
                             [Table {:width        600
                                     :height       400
                                     :rowHeight    50
                                     :rowGetter    #(get table %)
                                     :rowsCount    (count table)
                                     :headerHeight 50}
                              [Column {:label "last" :dataKey 2 :cellDataGetter getter :width 100 :align "center"}]
                              [Column {:label "first" :dataKey 3 :cellDataGetter getter :width 100 :align "center"}]
                              [Column {:label "email" :dataKey 5 :cellDataGetter getter :width 100 :align "center"}]
                              [Column {:label "username" :dataKey 6 :cellDataGetter getter :width 100 :align "center"}]
                              [Column {:label "blocked?" :dataKey 0 :align "center" :cellDataGetter blocked-getter
                                       :width 100 :cellRenderer reagent/as-element}]]]]]]))}))
