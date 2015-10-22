(defproject rmfu "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.122"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [reagent "0.5.0"]
                 [ring "1.4.0"]
                 [compojure "1.4.0"]
                 [cljs-ajax "0.5.0"]
                 [ring-cors "0.1.7"]
                 [ring/ring-json "0.4.0"]]

  :plugins [[lein-cljsbuild "1.1.0"]
            [lein-figwheel "0.4.1"]]

  :min-lein-version "2.0.0"                                 ;; better support in heroku
  :uberjar-name "rmfu.jar"
  ;; :main rmfu.core
  ;; the dev flag for this dev profile is automatic
  ;; when running with $ lein run
  :profiles {:dev
             {:main rmfu.core/dev-main}}

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"]

  :source-paths ["src" "src-cljs"]

  :cljsbuild {
              :builds [{:id           "dev"
                        :source-paths ["src-cljs"]

                        :figwheel     {:on-jsload "rmfu-ui.core/on-js-reload"}

                        :compiler     {:main                 rmfu-ui.core
                                       :asset-path           "js/compiled/out"
                                       :output-to            "resources/public/js/compiled/rmfu_ui.js"
                                       :output-dir           "resources/public/js/compiled/out"
                                       :source-map-timestamp true}}
                       {:id           "min"
                        :source-paths ["src-cljs"]
                        :compiler     {:output-to     "resources/public/js/compiled/rmfu_ui.js"
                                       :main          rmfu-ui.core
                                       :optimizations :advanced
                                       :pretty-print  false}}]})
