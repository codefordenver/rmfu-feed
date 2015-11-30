(ns rmfu-ui.utils)

(defn get-identity-token []
  (.getItem (.-localStorage js/window) "rmfu-feed-identity-token"))
