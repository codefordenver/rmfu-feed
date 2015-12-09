(ns rmfu-ui.feed
  (:require [rmfu-ui.nav :refer [nav]]
            [rmfu-ui.alert :refer [alert]]
            [reagent.core :as reagent]
            [ajax.core :refer [GET]]
            [rmfu-ui.utils :refer [get-identity-token]]))

(def ^{:private true} article-state (reagent/atom {:offset     0
                                               :prevOffset 0
                                               :articles   []}))

(def page-size 12)

(defn- get-articles [offset page-size]
  (GET (str "api/articles?offset=" offset "&page-size=" page-size)
       {:headers         {:identity (get-identity-token)}
        :error-handler   #(js/alert %)
        :response-format :json
        :keywords?       true
        :handler         (fn [res]
                           (swap! article-state assoc-in [:articles] res))}))

(defn article-nutshell
  "Component for article list item."
  [article id]
  ^{:key id}
  [:div.articlelisting
   [:p.articlelink
    [:a
     {:href (str "#/articles/#!" (:_id article))}
     (:title article)]]
   [:p.user
    (:author article) " / "
    [:small (:created article)]]])

(defn feed []
  (reagent/create-class
    {:component-will-mount
     #(get-articles 0 page-size)
     :component-did-update
     (fn []
       (let [{:keys [offset prevOffset]} @article-state]
         (when-not (= offset prevOffset)
           (do
             (get-articles (:offset @article-state) page-size)
             (swap! article-state assoc :prevOffset offset)))))
     :reagent-render
     (fn []
       (let [disable-next (< (count (:articles @article-state)) page-size)]
         [:div
          [:div.container.jumbotron.large-main
           [:div.row
            [:div.col-lg-12
             [:h1.displayinline "Feed"]
             " "
             [:h4.displayinline.greytext.pull-right [:a {:href "/#/customfeed"} "Customize"]]
             [:hr]
             [:h4 "Articles"]
             (for [article (:articles @article-state)
                   :let [id (:_id article)]]
               (article-nutshell article id))
             [:h5.text-center
              [:button {:on-click (fn []
                                    (swap! article-state update :offset #(max (- % page-size) 0)))} "<<"]
              [:button {:disabled disable-next
                        :on-click (fn []
                                    (swap! article-state update :offset #(+ % page-size)))} ">>"]]]]]]))}))
