(ns rmfu-ui.article
  (:require [rmfu-ui.nav :refer [nav]]
            [rmfu-ui.utils :refer [get-identity-token]]
            [reagent.core :as reagent]
            [reagent.session :as session]
            [ajax.core :refer [PUT GET]]))

(defn article []
  (let [article-state (reagent/atom {:title ""
                                     :text  ""
                                     :date  ""})
        fetch-article (fn [article-id]
                        (GET (str "api/articles/" article-id)
                             {:headers         {:identity (get-identity-token)}
                              :error-handler   #(js/alert %)
                              :response-format :json
                              :keywords?       true
                              :handler         #(reset! article-state %)}))]
    (reagent/create-class
      {:component-will-mount #(fetch-article (session/get :article-id))
       :component-did-mount  #(js/disqusReset (session/get :article-id)
                                              (-> js/document .-location .-href))

       :reagent-render       (fn []
                               [:div.container.jumbotron.large-main
                                [:div.row
                                 [:div.col-lg-12
                                  [:h1.text-center (:title @article-state)]
                                  [:div.pre-scrollable.borderbox
                                   [:p (:content @article-state)]]
                                  [:p.greytext.displayinline (:author-email @article-state)] [:p.date.displayinline.pull-right (:created @article-state)]
                                  [:br]
                                  [:h4.greyheading "Category"]]
                                 [:h4.greyheading.text-center "Comments"]
                                 [:div
                                  [:div.pre-scrollable.borderboxblack.whitebackground
                                   [:div#disqus_thread]]]]])})))
