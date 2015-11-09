(ns rmfu-ui.nav
  (:require [secretary.core :as secretary :include-macros true]))

(defn nav
  "Renders basic nav with an .active link"
  [active]
  [:nav {:className "navbar navbar-light bg-faded"}
   [:a.navbar-brand "FEED"]
   [:ul.nav.navbar-nav
    (for [anchor ["about" "create" "profile"]
          :let [title (clojure.string/capitalize anchor)
                class-names (if (= active anchor) "nav-item active" "nav-item")]]
      ^{:key anchor} [:li {:className class-names}
                      [:a.nav-link {:href (str "/#/" anchor)} title]])]
   [:form.form-inline.navbar-form.pull-right
    [:button.btn.btn-success-outline {:type "button"} "logout"]]])