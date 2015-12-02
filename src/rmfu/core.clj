(ns rmfu.core
  (:require
    [environ.core :refer [env]]
    [ring.adapter.jetty :as jetty]
    [ring.middleware.file :refer [wrap-file]]
    [ring.middleware.json :as json-middleware]
    [ring.middleware.params :as params-middleware]
    [ring.middleware.reload :refer [wrap-reload]]
    [compojure.core :refer [defroutes GET POST PUT]]
    [ring.util.response :refer [response redirect]]
    [compojure.route :as route]
    [ring.handler.dump :refer [handle-dump]]                ;; use handle-dump to inspect request
    [ring.middleware.cors :refer [wrap-cors]]
    [rmfu.persistance :as db]
    [rmfu.email :as email]
    [rmfu.auth :as auth]
    [buddy.auth :refer [authenticated?]]
    [buddy.auth.middleware :refer [wrap-authentication]]
    [buddy.auth.backends.token :refer [jws-backend]]
    [ring.util.http-response :refer :all]
    [compojure.api.sweet :refer :all]
    [cemerick.url :refer [url-encode]]))

(defn check-env
  "Checks if an environment variable exists"
  [env-var]
  (when (nil? (System/getenv env-var))
    (throw (Exception. (str env-var " is missing.")))))

(if (env :dev?)
  (do
    (println (format "_______ ENV DEV: %s   " (env :dev?)))
    (println (format "_______ CLIENT URL: %s" (env :client-url)))
    (check-env "RMFU_SECRET")
    (check-env "RMFU_FROM_EMAIL")
    (check-env "MANDRILL_API_KEY")))

(def secret (System/getenv "RMFU_SECRET"))

(defn sign-in
  "Signs a user in, only reply with 200 if :verified? true"
  [req]
  (let [body (get-in req [:body])
        email (:email body)]
    (if-let [claim (db/find-user-by-email email)]
      (if (:verified? claim)
        (if-let [token (auth/auth-user body)]
          (if-not (:blocked? claim)
            (ok token)
            (unauthorized "User has been blocked by RMFU staff"))
          (unauthorized "Invalid email or passsword"))
        (unauthorized "User not yet verified, please check your email"))
      (not-found "User not nound"))))

(defn sign-up [req]
  (let [body (get-in req [:body])
        add-user! (db/add-user! body)]
    (if add-user!
      (created (dissoc body :password :_id))
      (conflict (str (format "User already exist for %s" (:email body)))))))

(defn verify-email [req]
  (let [email (get-in req [:route-params :email])
        no-user-response (not-found (format "no user found for %s" email))]
    (if-let [user-exists (db/find-user-by-email email)]
      (do
        (db/update-verify-email! user-exists)
        (redirect "/#/email-verified"))
      no-user-response)))

(defn send-reset-password-email
  "handle reset-password request from user form,
  if user found we simply send an email"
  [req]
  (let [email (get-in req [:body :email])
        no-user-response (not-found (format "no user found for %s" email))]
    (if email
      (if-let [user-found (db/find-user-by-email email)]
        (do
          (let [token (rmfu.auth/sign-token email {:duration 1})
            encoded-token (url-encode token)]
          (email/send-reset-password-email user-found encoded-token)
          (accepted (str (format "User found for : %s" email) ", please check your email."))))
        no-user-response)
      no-user-response)))

(defn reset-password-redirect
  "handle reset password from email,
  redirects user to reset their password via the new password form"
  [req]
  (let [email (get-in req [:route-params :email])
        valid-token (rmfu.auth/unsign-token (get-in req [:params :token]))
        matches-email (= (:email valid-token) email)]
    (if matches-email
      (redirect (str (env :client-url) "/#/new-password?email=" email))
      (unauthorized "Invalid password reset token"))))

(defn reset-password-from-form! [req]
  (let [email (get-in req [:body :email])
        new-password (get-in req [:body :new-password])
        update-success? (db/update-password! email new-password)]
    (if update-success?
      (ok "Password updated!")
      (internal-server-error
        (format "Something went wrong with the password update or no user was found for %s" email)))))

(def auth-backend (jws-backend {:secret secret}))

(defn update-user-profile [req]
  (let [profile (get-in req [:body :profile])
        claim (db/find-user-by-email (:email profile))
        db-op (db/update-user-profile! claim profile)]
    (if claim
      (if db-op (ok "user updated!")
                (internal-server-error "could not update user profile"))
      (unauthorized "not authorized to update user profile"))))

(defroutes* api-routes
            (context* "/api" []

                      (wrap-authentication
                        (GET* "/user" {:as request}
                              :middlewares [rmfu.auth/auth-mw]
                              :header-params [identity :- String]
                              (let [identity (get-in request [:headers "identity"])
                                    unsigned-token (auth/unsign-token identity)]
                                (if unsigned-token
                                  (ok (dissoc (db/find-user-by-username (:username unsigned-token)) :password :_id))
                                  (unauthorized {:error "not auth"}))))
                        auth-backend)

                      (wrap-authentication
                        (POST* "/articles" {:as request}
                               :middlewares [rmfu.auth/auth-mw]
                               :header-params [identity :- String]
                               (let [identity (get-in request [:headers "identity"])
                                     unsigned-token (auth/unsign-token identity)]
                                 (if unsigned-token
                                   (let [user (db/find-user-by-username (:username unsigned-token))
                                         persisted-article (db/add-article! (:body request) (:email user))]
                                     (created (str "/articles/" (:_id persisted-article))))
                                   (unauthorized {:error "not auth"}))))
                        auth-backend)

                      (wrap-authentication
                        (GET* "/articles/:id" {:as request}
                              :path-params [id :- String]
                              :middlewares [rmfu.auth/auth-mw]
                              :header-params [identity :- String]
                              (let [identity (get-in request [:headers "identity"])
                                    unsigned-token (auth/unsign-token identity)]
                                (if unsigned-token
                                  (if-let [article (db/find-article-by-id id)]
                                    (ok article)
                                    (not-found (str "No article found with id: " id)))
                                  (unauthorized {:error "not auth"}))))
                        auth-backend)

                      ;; ADMIN ONLY ROUTES
                      ;; -----------------

                      (wrap-authentication
                        (GET* "/users" {:as request}
                              :middlewares [rmfu.auth/auth-mw]
                              :header-params [identity :- String]
                              (let [identity (get-in request [:headers "identity"])
                                    unsigned-token (auth/unsign-token identity)]
                                (if (and unsigned-token (:is-admin? unsigned-token))
                                  (ok (map #(dissoc % :_id :password :interests) (db/find-all-users)))
                                  (unauthorized {:error "not authorized to retrieve a list of all users"}))))
                        auth-backend)

                      (wrap-authentication
                        (PUT* "/block-user" {:as request}
                              :middlewares [rmfu.auth/auth-mw]
                              :header-params [identity :- String]
                              (let [identity (get-in request [:headers "identity"])
                                    unsigned-token (auth/unsign-token identity)
                                    email (get-in request [:body :email])
                                    state (get-in request [:body :blocked?])]
                                (if (and unsigned-token (:is-admin? unsigned-token) (db/block-user email state))
                                  (ok (if state "blocked" "unblocked"))
                                  (unauthorized {:error "not authorized to block users"}))))
                        auth-backend)))


(defroutes* public-routes
            (POST* "/signin" [] sign-in)
            (POST "/signup" [] sign-up)
            (POST "/send-reset-password-email" [] send-reset-password-email)
            (GET "/reset-password-redirect/:email" [] reset-password-redirect)
            (PUT "/reset-password-from-form" [] reset-password-from-form!)
            (PUT "/update-user-profile" [] update-user-profile)
            (GET "/verify-email/:email" [] verify-email)
            (route/resources "/assets")                     ;; serve /assets for chosen lib jar
            (route/not-found (not-found "Resource not found")))

(defroutes app-routes
           api-routes
           public-routes)

(defapi app
        (->
          app-routes
          (wrap-cors :access-control-allow-origin [#".+"]
                     :access-control-allow-methods [:get :put :post :delete])
          params-middleware/wrap-params
          (json-middleware/wrap-json-body {:keywords? true})
          (json-middleware/wrap-json-response)
          (wrap-file "resources/public")))                  ;; server static files from this directory

(defn -main [port]
  (jetty/run-jetty app {:port (Integer. port)}))

(defn -dev [port]
  (jetty/run-jetty (wrap-reload #'app) {:port (Integer. port)}))
