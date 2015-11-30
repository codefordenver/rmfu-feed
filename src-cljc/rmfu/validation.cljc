(ns rmfu.validation
  (:require [schema.core :as s :include-macros true]))

(def Article
  "Defines a schema for a Article data type"
  {:title   s/Str
   :content s/Str})

(defn article [a]
  (s/validate Article a))