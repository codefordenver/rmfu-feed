(ns rmfu-ui.alert
  (:require [timothypratley.reanimated.core :as anim]))

(defn alert
  "takes state atom with a :title, :message, :display keys
  and hides on a optional timeout"
  ([state duration]
   (fn []
     [anim/pop-when (get-in @state [:alert :display])
      (let [type (get-in @state [:alert :type])]
        [:div.usa-alert {:className (cond
                                      (= type :success) "usa-alert-success"
                                      (= type :info) "usa-alert-info"
                                      (= type :warning) "usa-alert-warning"
                                      (= type :error) "usa-alert-error")}
         [:div.usa-alert-body
          [:h3.usa-alert-heading (get-in @state [:alert :title])]
          [:p.usa-alert-text (get-in @state [:alert :message])]]
         [anim/timeout #(swap! state update-in [:alert :display] not)
          duration]])]))
  ([type state duration]
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
        duration]]])))
