(ns rmfu-ui.nav
  (:require [secretary.core :as secretary :include-macros true]
            [reagent.core :as reagent]
            [reagent.session :as session]))

(defn logout []
  "Logs the user out by deleting key form local storage and resetting the session"
  (.removeItem (.-localStorage js/window) "rmfu-feed-identity-token")
  (session/clear!)
  (secretary/dispatch! "/"))

(defn nav
  "Renders basic nav with an .active link"
  [active]
  (reagent/create-class
    {:component-will-mount (fn []
                             (if (session/get :profile)
                               (js/alert (session/get :profile))
                               (secretary/dispatch! "/")))
     :reagent-render (fn []
                       [:nav {:className "navbar navbar-light bg-faded"}
                        [:a.navbar-brand {:href "/#/feed"} "FEED"]
                        [:ul.nav.navbar-nav
                         (for [anchor ["about" "create" "profile"]
                               :let [title (clojure.string/capitalize anchor)
                                     class-names (if (= active anchor) "nav-item active" "nav-item")]]
                           ^{:key anchor} [:li {:className class-names}
                                           [:a.nav-link {:href (str "/#/" anchor)} title]])]
                        [:form.form-inline.navbar-form.pull-right
                         [:button.btn.btn-success-outline {:type "button" :on-click #(logout)} "logout"]]])}))
