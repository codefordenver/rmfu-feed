(ns rmfu-ui.profile
  (:require [rmfu-ui.nav :refer [nav]]
            [reagent.core :as reagent]
            [ajax.core :refer [PUT GET]]
            [cljsjs.chosen]
            [cljsjs.jquery]
            [secretary.core :as secretary :include-macros true]))

(defn input-field-helper [state param props]
  [:input.form-control
   (merge props
          {:on-change #(swap! state assoc param (-> % .-target .-value))
           :value     (param @state)})])

(defn fetch-user-profile [state]
  (let [identity-token (.getItem (.-localStorage js/window) "rmfu-feed-identity-token")]
    (GET "/api/users"
         {:headers         {:identity identity-token}
          :error-handler   #(js/alert %)
          :response-format :json
          :keywords?       true
          :handler         (fn [res]
                             (reset! state res)
                             (-> (js/$ ".interest")
                                 (.val (apply array (:interests res)))
                                 (.trigger "chosen:updated")))})))

(defn profile []
  (let [app-state (reagent/atom {:first     "" :last ""
                         :zipcode   80203 :email ""
                         :interests []})]
    (reagent/create-class
      {:component-will-mount #(fetch-user-profile app-state)
       :component-did-mount  (fn []
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
       :reagent-render       (fn []
                               [:div
                                [nav "profile"]
                                [:div.container.jumbotron
                                 {:style {:max-width "700px"}}
                                 [:div.row
                                  [:div.col-lg-12
                                   [:h3.text-center "Tell Us About Yourself"]
                                   [:div.col-lg-6.col-md-6
                                    [input-field-helper app-state :first
                                     {:id "FirstName" :placeholder "First Name" :type "text"}]]
                                   [:div.col-lg-6.col-md-6
                                    [input-field-helper app-state :last
                                     {:id "LastName" :placeholder "Last Name" :type "text"}]]

                                   [:br]

                                   [:div.col-lg-12 {:style {:height "20px"}}]

                                   [:h4.text-center "Region of Interest"]

                                   [:div.col-lg-6.col-md-6
                                    [:div.input-group.select-group
                                     [:div.col-lg-10
                                      [input-field-helper app-state :zipcode
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
                                                       {:params          {:profile @app-state}
                                                        :format          :json
                                                        :error-handler   #(js/alert "Profile successfully saved.")
                                                        :response-format :json
                                                        :keywords?       true
                                                        :handler         #(js/alert %)}))}
                                     "Save and Continue to Custom Feed"]]]]]])})))
