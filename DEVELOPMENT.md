# Development Mode:

## REPL driven development:

1. `lein repl` then
 
```
(ns user)
(reset)
```

## Other Resources
- [Quick intro to clojure](http://kimh.github.io/clojure-by-example/#about&sref=https://delicious.com/search/clojure,book/mine)
- How to setup cursive with intellij
https://vimeo.com/103812557

- use `(use 'clojure.repl)`
to be able to use `(doc foo)` in the repl

## Clojure DEV Notes

defn- makes a non-public definition 
~ private defn function

### mongodb / monger
```
  ;; prefer **mc/find-maps** over **mc/find** -> lower_level API

  ;; use from-db-object fn to convert mongodb obj to a map
  ;;(from-db-object (mc/find db coll {:first_name "jose"}) true)

  ;; (mc/insert db coll {:first_name "jose" :last_name "maria"})
  ;; (print (mc/find-maps db coll))
```
