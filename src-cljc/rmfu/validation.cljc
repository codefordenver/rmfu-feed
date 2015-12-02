(ns rmfu.validation
  (:require #?(:clj  [schema.core :as s]
               :cljs [schema.core :as s :include-macros true])))

(def Article
  "Defines a schema for a Article data type"
  {:title   (s/both s/Str (s/pred (comp not clojure.string/blank?)))
   :content s/Any})

(defn article [a]
  (s/validate Article a))