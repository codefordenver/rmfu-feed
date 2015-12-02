(defproject rmfu "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.122"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [reagent "0.5.1"]
                 [reagent-utils "0.1.5"]
                 [ring "1.4.0"]
                 [metosin/ring-http-response "0.6.5"]
                 [metosin/compojure-api "0.23.1"]
                 [compojure "1.4.0"]
                 [cljs-ajax "0.5.0"]
                 [ring-cors "0.1.7"]
                 [ring/ring-json "0.4.0"]
                 [com.novemberain/monger "3.0.0-rc2"]
                 [buddy "0.7.2"]
                 [clj-mandrill "0.1.0"]
                 [secretary "1.2.3"]
                 [environ "1.0.1"]
                 [slingshot "0.12.2"]
                 [cljsjs/chosen "1.4.2-1"]
                 [cljsjs/jquery "2.1.4-0"]
                 [cljsjs/fixed-data-table "0.4.6-0"]
                 [timothypratley/reanimated "0.1.4"]
                 [metosin/schema-tools "0.7.0"]
                 [prismatic/schema "1.0.3"]
                 [com.cemerick/url "0.1.1"]]

  :plugins [[lein-cljsbuild "1.1.0"]
            [lein-figwheel "0.4.1"]
            [lein-environ "1.0.1"]]

  :min-lein-version "2.5.0"                                 ;; better support in heroku
  :uberjar-name "rmfu.jar"
  :main rmfu.core

  ;; the dev flag for this dev profile is automatic
  ;; when running with $ lein run
  :profiles {:dev     {:main            rmfu.core/-dev
                       :env             {:dev?          true
                                         :host-name     "https://rmfu-feed.herokuapp.com/" ;; include the dash at the end
                                         :client-url    "http://localhost:3449"
                                         :api-end-point "http://localhost:3000"}

                       :closure-defines {"API_END_POINT" "http://localhost:3000"}

                       :cljsbuild       {
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
                                                                  :pretty-print  false}}]}}

             :repl    {:main rmfu.core}

             :uberjar {:env         {:production? true}
                       :aot         :all
                       :omit-source true
                       :cljsbuild   {:jar    true
                                     :builds {:app
                                              {:source-paths ["src-cljs"]
                                               :compiler     {:output-to     "resources/public/js/compiled/rmfu_ui.js"
                                                              :main          rmfu-ui.core
                                                              :optimizations :advanced
                                                              :pretty-print  false}}}}}}

  :hooks [leiningen.cljsbuild]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"]

  :source-paths ["src" "src-cljs" "src-cljc"])
