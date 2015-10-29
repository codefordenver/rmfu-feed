(ns rmfu.auth
  (:require [buddy.hashers :as hasher]))

(defn valid-password?
  "checks user provided againts stored"
  [provided saved]
  (hasher/check provided saved))