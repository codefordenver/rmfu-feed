(ns rmfu-ui.utils)

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