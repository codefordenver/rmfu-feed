(ns rmfu-ui.nav
  (:require [secretary.core :as secretary :include-macros true]
            [reagent.core :as reagent]
            [ajax.core :refer [GET]]
            [reagent.session :as session]))

(defn logout
  "Logs the user out by deleting key from local storage and resetting the session"
  []
  (do (.removeItem (.-localStorage js/window) "rmfu-feed-identity-token")
      (session/clear!)
      (secretary/dispatch! "/")))

(defn identity-token []
  (.getItem (.-localStorage js/window) "rmfu-feed-identity-token"))

(defn nav
  "Renders basic nav with an .active link"
  [active]
  (fn []
    (reagent/create-class
      {:component-will-mount
       (fn []
         (if (identity-token)
           (GET "/api/user"
                {:headers         {:identity (identity-token)}
                 :error-handler   #(secretary/dispatch! "/")
                 :response-format :json
                 :keywords?       true
                 :handler         (fn [res]
                                    (session/put! :profile res))})
           (do
             (session/clear!)
             (secretary/dispatch! "/"))))
       :reagent-render
       (fn []
         [:nav {:className "navbar navbar-light bg-faded"}
          (if-let [profile (session/get :profile)]
            [:div
             [:a.navbar-brand.feed-title {:href "/#/feed"} "FEED"]
             [:ul.nav.navbar-nav
              (for [anchor ["about" "create" "profile"]
                    :let [title (clojure.string/capitalize anchor)
                          class-names (if (= active anchor) "nav-item active" "nav-item")]]
                ^{:key anchor} [:li {:className class-names}
                                [:a.nav-link {:href (str "/#/" anchor)} title]])
              (when (:is-admin? profile)
                [:li {:className "nav-item"}
                 [:a.nav-link.active {:href "/#/admin"} "Admin"]])]
             [:form.form-inline.navbar-form.pull-right
              [:button.btn.btn-success-outline {:type "button" :on-click #(logout)} "logout"]]]
            [:a.navbar-brand.feed-title {:href "/"} "FEED"])])})))
