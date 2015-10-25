(ns rmfu.core
  (use [clojure.java.shell :only [sh]])
  (:require
    [ring.adapter.jetty :as jetty]
    [ring.middleware.file :refer [wrap-file]]
    [ring.middleware.json :as middleware]
    [ring.middleware.reload :refer [wrap-reload]]
    [compojure.core :refer [defroutes GET POST PUT]]
    [ring.util.response :refer [response redirect]]
    [compojure.route :refer [not-found]]
    [ring.handler.dump :refer [handle-dump]]                ;; use handle-dump to inspect request
    [ring.middleware.cors :refer [wrap-cors]]
    [rmfu.persistance :as db]))

;; TODO: use transit intead of plain JSON
;; TODO: add confirmation page for verified email

(defn greet [req]
  (let [name (get-in req [:route-params :name])]
    {:status  200
     :headers {}
     :body    (str "yo, " name)}))

(defn sign-in [req]
  "Sings a user in, only reply with 200 if :verified? true"
  (let [body (get-in req [:body])
        email (body :email)]
    (if (db/auth-user body)
      (if (:verified? (db/find-user-by-email email))
        {:status  200
         :headers {}
         :body    (str "verified and valid password")}
        ;; TODO: :else ask user to go verify
        {:status  201
         :headers {}
         :body    (str "user not verified")}
        )
      {:status  401
       :headers {}
       :body    (str "Not Found")})))

(defn sign-up [req]
  (let [body (get-in req [:body])
        add-user! (db/add-user! body)]
    (println "attempting to post with" body)
    (if-not (or (empty? add-user!) (nil? add-user!))
      {:status  201
       :headers {}
       :body    (str body)}
      {:status  409
       :headers {}
       :body    (str add-user!)})))

(defn verify-email [req]
  (let [email (get-in req [:route-params :email])
        user-exists (db/find-user-by-email email)]
    (do (if-not (nil? user-exists)
          (db/update-verify-email! user-exists))
        (redirect "http://localhost:3449/#/email-verified"))))

(defroutes app-routes
           (GET "/yo/:name" [] greet)
           (POST "/signin" [] sign-in)
           (POST "/signup" [] sign-up)
           ;(PUT "/reset-password" [] reset-password)
           (GET "/verify-email/:email" [] verify-email)
           ;;(wrap-file "/" "resources/report")               ;; server static files from this directory
           (not-found "Resource not found"))

(def app
  (-> app-routes
      (wrap-cors :access-control-allow-origin [#".+"]
                 :access-control-allow-methods [:get :put :post :delete])
      (middleware/wrap-json-body {:keywords? true})))

(defn -main [port]
  (jetty/run-jetty app {:port (Integer. port)}))

(defn -dev-main [port]
  (jetty/run-jetty (wrap-reload #'app) {:port (Integer. port)}))