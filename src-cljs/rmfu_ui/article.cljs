(ns rmfu-ui.article
  (:require [rmfu-ui.utils :refer [get-identity-token navigate-to]]
            [reagent.core :as reagent]
            [reagent.session :as session]
            [ajax.core :refer [DELETE GET]]
            [secretary.core :as secretary]))

(defn- delete-article-by-id
  "Deletes the article"
  [article-id]
  (DELETE (str "api/articles/" article-id)
          {:headers         {:identity (get-identity-token)}
           :error-handler   #(js/alert %)
           :handler         #(navigate-to "/")}))

(defn article []
  (let [article-state (reagent/atom {:title ""
                                     :text  ""
                                     :date  ""})
        fetch-article (fn [article-id]
                        (GET (str "api/articles/" article-id)
                             {:headers         {:identity (get-identity-token)}
                              :error-handler   (fn [res]
                                                 (session/put! :error-msg (get-in res [:response :error]))
                                                 (secretary/dispatch! "/oops"))
                              :response-format :json
                              :keywords?       true
                              :handler         #(reset! article-state %)}))]
    (reagent/create-class
      {:component-will-mount #(fetch-article (session/get :article-id))
       :component-did-mount  #(js/disqusReset (session/get :article-id)
                                              (-> js/document .-location .-href)
                                              (:title @article-state))

       :reagent-render       (fn []
                               [:div.container.jumbotron.large-main
                                [:div.row
                                 [:div.col-lg-12
                                  (let [profile (session/get :profile)]
                                    (when (and profile
                                               (or (:is-admin? profile)
                                                   (= (:author @article-state) (:username profile))))
                                    [:div.pull-right
                                     [:button.btn.btn-danger
                                      {:on-click #(when (js/confirm "Are you sure you want to delete this article?")
                                                    (delete-article-by-id (session/get :article-id)))}
                                       "Delete"]]))
                                  [:h1.text-center (:title @article-state)]
                                  [:div.borderbox
                                   [:p.article-content (:content @article-state)]]
                                  [:p.greytext.displayinline (:author @article-state)] [:p.date.displayinline.pull-right (:created @article-state)]
                                  [:br]
                                  [:h4.greyheading "Category"]]
                                 [:h4.greyheading.text-center "Comments"]
                                 [:div
                                  [:div.borderboxblack.whitebackground
                                   [:div#disqus_thread]]]]])})))
