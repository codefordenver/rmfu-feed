(ns rmfu-ui.utils
  (:require [ajax.core :refer [POST]]))

(defn get-identity-token []
  (.getItem (.-localStorage js/window) "rmfu-feed-identity-token"))

(defn index-of                                              ;; no cljs support for .indexOx
  "return the index of the supplied item, or nil"           ;; therefore this helper
  [v item]
  (let [len (count v)]
    (loop [i 0]
      (cond
        (<= len i) nil,
        (= item (get v i)) i,
        :else
        (recur (inc i))))))

(defn navigate-to
  "Updates the html route via the history.pushState api"
  [url]
  (.assign js/location (str js/window.location.protocol
                         "//"
                         js/window.location.host
                         "#"
                         url)))

(defn request-password-reset [email & opts]
  (POST "/send-reset-password-email"
        {:params        {:email email}
         :format        :json
         :error-handler #(js/alert %)
         :handler       (fn [res]
                          (js/alert res)
                          (when-let [callback (first opts)]
                            (callback res)))}))

(defn resend-verify-email [email & opts]
  (POST "/resend-verify-email"
        {:params        {:email email}
         :format        :json
         :error-handler #(js/alert %)
         :handler       (fn [res]
                          (js/alert res)
                          (when-let [callback (first opts)]
                            (callback res)))}))
