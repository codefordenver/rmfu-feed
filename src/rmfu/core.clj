(ns rmfu.core
  (use [clojure.java.shell :only [sh]])
  (:require
    [ring.adapter.jetty :as jetty]
    [ring.middleware.file :refer [wrap-file]]
    [ring.middleware.json :as middleware]
    [ring.middleware.reload :refer [wrap-reload]]
    [compojure.core :refer [defroutes GET POST]]
    [ring.util.response :refer [response]]
    [compojure.route :refer [not-found]]
    [ring.handler.dump :refer [handle-dump]]                ;; use handle-dump to inspect request
    [ring.middleware.cors :refer [wrap-cors]]
    [rmfu.persistance :as db]))

(defn greet [req]
  (let [name (get-in req [:route-params :name])]
    {:status  200
     :headers {}
     :body    (str "hello, " name)}))

(defn login [req]
  (let [body (get-in req [:body])]
    (if-let [user (db/find-user body)]
      {:status  200
       :headers {}
       :body    (str user)}
      {:status  401
       :headers {}
       :body    (str "Not Found")})))

(defroutes app-routes
           (GET "/yo/:name" [] greet)
           (POST "/login" [] login)
           ;;(wrap-file "/" "resources/report")               ;; server static files from this directory
           (not-found "Resource not found"))

(def app
  (-> app-routes
      (wrap-cors :access-control-allow-origin [#".+"]
                 :access-control-allow-methods [:get :put :post :delete])
      (middleware/wrap-json-body {:keywords? true})))

(defn -main [port]
  (jetty/run-jetty app {:port (Integer. port)}))

(defn dev-main [port]
  (jetty/run-jetty (wrap-reload #'app) {:port (Integer. port)}))