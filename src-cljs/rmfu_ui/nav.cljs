(ns rmfu-ui.nav
  (:require [secretary.core :as secretary :include-macros true]))

(defn logout []
  "Logs the user out by deleting key form local storage"
  (.removeItem (.-localStorage js/window) "rmfu-feed-identity-token")
  (secretary/dispatch! "/"))

(defn nav
  "Renders basic nav with an .active link"
  [active]
  [:nav {:className "navbar navbar-light bg-faded"}
   [:a.navbar-brand {:href "/#/customfeed"} "FEED"]
   [:ul.nav.navbar-nav
    (for [anchor ["about" "create" "profile"]
          :let [title (clojure.string/capitalize anchor)
                class-names (if (= active anchor) "nav-item active" "nav-item")]]
      ^{:key anchor} [:li {:className class-names}
                      [:a.nav-link {:href (str "/#/" anchor)} title]])]
   [:form.form-inline.navbar-form.pull-right
    [:button.btn.btn-success-outline {:type "button" :on-click #(logout)} "logout"]]])
