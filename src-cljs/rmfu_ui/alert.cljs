(ns rmfu-ui.alert
  (:require [reagent.core :as reagent]
            [timothypratley.reanimated.core :as anim]))

(defn alert
      "takes state atom and updates via update-fn on a timeout"
      [type state update-fn & duration]
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
               [anim/timeout #(update-fn) (or duration 2500)]]])}))
