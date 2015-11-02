(ns rmfu.core
  (use [clojure.java.shell :only [sh]])
  (:require
    [environ.core :refer [env]]
    [ring.adapter.jetty :as jetty]
    [ring.middleware.file :refer [wrap-file]]
    [ring.middleware.json :as middleware]
    [ring.middleware.params]
    [ring.middleware.reload :refer [wrap-reload]]
    [compojure.core :refer [defroutes GET POST PUT]]
    [ring.util.response :refer [response redirect]]
    [compojure.route :refer [not-found]]
    [ring.handler.dump :refer [handle-dump]]                ;; use handle-dump to inspect request
    [ring.middleware.cors :refer [wrap-cors]]
    [rmfu.persistance :as db]
    [rmfu.email :as email]))

;; TODO: use transit intead of plain JSON

;; enviorment variables defined in project.clj
(if (env :dev?)
  (do
    (println (format "_______ ENV DEV: %s    _______" (env :dev?)))
    (println (format "_______ CLIENT URL: %s _______" (env :client-url)))))

(defn greet [req]
  (let [name (get-in req [:route-params :name])]
    {:status  200
     :headers {}
     :body    (str "yo, " name)}))

(defn sign-in
  "Sings a user in, only reply with 200 if :verified? true"
  [req]
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
  (let [email (get-in req [:route-params :email])]
    (when-let [user-exists (db/find-user-by-email email)]
      (db/update-verify-email! user-exists))
    (redirect "/#/email-verified")))

(defn send-reset-password-email
  "handle reset-password request from user form,
  if user found we simply send an email"
  [req]
  (let [email (get-in req [:query-params "email"])
        no-user-response {:status  404
                          :headers {}
                          :body    (str (format "no user found for %s" email))}]
    (if email
      (if-let [user-found (db/find-user-by-email email)]
        (do
          (email/send-reset-password-email user-found)
          {:status  201
           :headers {}
           :body    (str (format "User found for : %s" email) ", please check your email.")})
        no-user-response)
      no-user-response)))

(defn reset-password-redirect
  "handle reset password now from email,
  redirects user to reset their password via the new password form"
  [req]
  (let [email (get-in req [:route-params :email])]
    (redirect (str (env :client-url) "/#/new-password?email=" email))))

(defn reset-password-from-form! [req]
  (let [email (get-in req [:body :email])
        new-password (get-in req [:body :new-password])
        update-success? (db/update-password! email new-password)]
    (if (= (type update-success?) com.mongodb.WriteResult)
      {:status  200
       :headers {}
       :body    (str "Password updated!")}
      {:status  500
       :headers {}
       :body    (str "Something went wrong with the password update.")})))

(defroutes app-routes
           (GET "/yo/:name" [] greet)
           (POST "/signin" [] sign-in)
           (POST "/signup" [] sign-up)
           (GET "/send-reset-password-email" [] send-reset-password-email)
           (GET "/reset-password-redirect/:email" [] reset-password-redirect)
           (PUT "/reset-password-from-form" [] reset-password-from-form!)
           (GET "/verify-email/:email" [] verify-email)
           (wrap-file "/" "resources/public")               ;; server static files from this directory
           (not-found "Resource not found"))

(def app
  (-> app-routes
      (wrap-cors :access-control-allow-origin [#".+"]
                 :access-control-allow-methods [:get :put :post :delete])
      (middleware/wrap-json-body {:keywords? true})
      (ring.middleware.params/wrap-params)))

(defn -main [port]
  (jetty/run-jetty app {:port (Integer. port)}))

(defn -dev [port]
  (jetty/run-jetty (wrap-reload #'app) {:port (Integer. port)}))