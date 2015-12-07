(ns rmfu-ui.profile
  (:require [reagent.core :as reagent]
            [ajax.core :refer [PUT GET]]
            [cljsjs.chosen]
            [cljsjs.jquery]
            [secretary.core :as secretary]
            [rmfu-ui.alert :refer [alert]]))

(def app-state (reagent/atom {:first     "" :last ""
                              :zipcode   80203 :email ""
                              :interests []}))

(defn input-field-helper [param props]
  [:input.form-control
   (merge props
          {:on-change #(swap! app-state assoc param (-> % .-target .-value))
           :value     (param @app-state)})])

(defn identity-token []
  (.getItem (.-localStorage js/window) "rmfu-feed-identity-token"))

(defn fetch-user-profile []
  (GET "/api/user"
       {:headers         {:identity (identity-token)}
        :error-handler   #(secretary/dispatch! "/")
        :response-format :json
        :keywords?       true
        :handler         (fn [res]
                           (reset! app-state res)
                           (-> (js/$ ".interest")
                               (.val (apply array (:interests res)))
                               (.trigger "chosen:updated")))}))

(defn profile []
  (let [alert-state (reagent/atom {:display false
                                   :message nil
                                   :title   nil})
        update-alert-fn (fn [title]
                            (swap! alert-state assoc-in [:alert] {:display true
                                                                  :title   title}))]
       (reagent/create-class
         {:component-will-mount
          #(fetch-user-profile)
          :component-did-mount
          (fn []
              (-> (js/$ ".interest")
                  (.chosen #js {:width "250px"})
                  (.on "change"
                       (fn [_ params]
                           (let [selected (aget params "selected")
                                 deselected (aget params "deselected")]
                                (cond
                                  selected
                                  (swap! app-state update-in [:interests] (fnil conj []) selected)
                                  deselected
                                  (swap! app-state update-in [:interests] #(remove #{deselected} %))))))))
          :reagent-render
          (fn []
              [:div.container.jumbotron
               {:style {:max-width "700px"}}
               [:div.row
                [:div.col-lg-12
                 [:h3.text-center "Tell Us About Yourself"]
                 [alert :success alert-state 2500]
                 [:div.col-lg-6.col-md-6
                  [input-field-helper :first
                   {:id "FirstName" :placeholder "First Name" :type "text"}]]
                 [:div.col-lg-6.col-md-6
                  [input-field-helper :last
                   {:id "LastName" :placeholder "Last Name" :type "text"}]]

                 [:br]

                 [:div.col-lg-12 {:style {:height "20px"}}]

                 [:h4.text-center "Region of Interest"]

                 [:div.col-lg-6.col-md-6
                  [:div.input-group.select-group
                   [:div.col-lg-10
                    [input-field-helper :zipcode
                     {:placeholder "Enter Zip Code" :type "number"}]]]]

                 [:br]
                 [:br]

                 [:h4 "Topics of Interest"]

                 [:select.interest.chosen-container.chosen-container-multi
                  {:data-placeholder "Choose an interest..."
                   :tab-index        "3"
                   :multiple         true}
                  [:option {:value "fruit/vegetable"} "Fruit / Vegetable"]
                  [:option {:value "ranch/meat"} "Ranch / Meat"]
                  [:option {:value "commodity"} "commodity"]
                  [:option {:value "small-grain"} "Small Grain"]
                  [:option {:value "water/soil"} "Water Soil"]
                  [:option {:value "conservation"} "Conservation"]
                  [:option {:value "farm-technology"} "Farm Technology"]
                  [:option {:value "energy"} "Energy"]]

                 [:p.text-center
                  [:button.btn.btn-primary.active
                   {:name     "type" :type "button" :value "continue"
                    :on-click (fn [_]
                                  (PUT "/update-user-profile"
                                       {:params        {:profile @app-state}
                                        :format        :json
                                        :handler       #(update-alert-fn "Profile successfully saved.")
                                        :error-handler #(js/alert %)}))}
                   "Save and Continue to Custom Feed"]]]]])})))
