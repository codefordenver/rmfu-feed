(ns rmfu-ui.alert
  (:require [reagent.core :as reagent]
            [timothypratley.reanimated.core :as anim]))

(defn alert
  "takes state atom with a :title, :message, :display keys
  and hides on a optional timeout"
  [type state & opts]
  (reagent/create-class
    {:reagent-render
     (fn []
       [anim/pop-when (get-in @state [:alert :display])
        [:div.usa-alert {:className (cond
                                      (= type :success) "usa-alert-success"
                                      (= type :info) "usa-alert-info"
                                      (= type :warning) "usa-alert-warning"
                                      (= type :error) "usa-alert-error")}
         [:div.usa-alert-body
          [:h3.usa-alert-heading (get-in @state [:alert :title])]
          [:p.usa-alert-text (get-in @state [:alert :message])]]
         [anim/timeout #(swap! state update-in [:alert :display] not)
          (or (first opts) 2500)]]])}))
