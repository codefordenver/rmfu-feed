(ns rmfu.auth
  (:require [buddy.hashers :as hasher]))

(defn valid-password? [provided saved]
  "checks user provided againts stored"
  (hasher/check provided saved))