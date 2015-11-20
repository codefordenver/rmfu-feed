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
  (let [profile (session/get :profile)]
    (reagent/create-class
      {:component-will-mount (fn []
                               (if profile
                                 (println profile)
                                 (secretary/dispatch! "/")))
       :reagent-render       (fn []
                               [:nav {:className "navbar navbar-light bg-faded"}
                                [:a.navbar-brand {:href "/#/feed"} "FEED"]
                                [:ul.nav.navbar-nav
                                 (for [anchor ["about" "create" "profile"]
                                       :let [title (clojure.string/capitalize anchor)
                                             class-names (if (= active anchor) "nav-item active" "nav-item")]]
                                   ^{:key anchor} [:li {:className class-names}
                                                   [:a.nav-link {:href (str "/#/" anchor)} title]])
                                 (when (true? (:is-admin? profile))
                                   [:li {:className "nav-item"}
                                    [:a.nav-link {:href "/#/admin"} "Admin"]])]
                                [:form.form-inline.navbar-form.pull-right
                                 [:button.btn.btn-success-outline {:type "button" :on-click #(logout)} "logout"]]])})))
