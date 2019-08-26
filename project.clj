(defproject nnts2 "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/clojurescript "1.10.520"
                  :exclusions [com.google.javascript/closure-compiler-unshaded
                               org.clojure/google-closure-library]]
                 [thheller/shadow-cljs "2.8.45"]
                 [ragtime "0.8.0"]
                 [cljs-ajax "0.8.0"]
                 [org.clojure/test.check "0.10.0"]
                 [reagent "0.8.1"]
                 [re-frame "0.10.8"]
                 [secretary "1.2.3"]
                 [compojure "1.6.1"]
                 [ring-oauth2 "0.1.4"]
                 [ring/ring-defaults "0.3.2"]
                 [yogthos/config "1.1.5"]
                 [ring "1.7.1"]
                 [aero "1.1.3"]
                 [clj-http "3.10.0"]
                 [ring/ring-json "0.5.0"]
                 [nilenso/honeysql-postgres "0.2.6"]
                 [ring-logger "1.0.1"]
                 [honeysql "0.9.5"]
                 [cheshire "5.9.0"]
                 [ring/ring-mock "0.4.0"]
                 [kibu/pushy "0.3.8"]
                 [org.postgresql/postgresql "42.2.6"]]
  :plugins []
  :main nnts2.core
  :min-lein-version "2.5.3"
  :source-paths ["src/clj" "src/cljs"]
  :test-paths ["test/clj" "test/cljs"]
  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target" "test/js"]

  :aliases {"dev"  ["with-profile" "dev" "run" "-m" "shadow.cljs.devtools.cli" "watch" "app"]
            "prod" ["with-profile" "prod" "run" "-m" "shadow.cljs.devtools.cli" "release" "app"]}
  :profiles
  {:dev
            {:dependencies [[binaryage/devtools "0.9.10"]]}
   :prod    {}
   :uberjar {:source-paths ["env/prod/clj"]
             :omit-source  true
             :main         nnts2.core
             :aot          [nnts2.core]
             :uberjar-name "nnts2.jar"
             :prep-tasks   ["compile" ["prod"]]}})
