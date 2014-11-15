(defproject weather-data "0.1.0-SNAPSHOT"
  :description "A small clojure project where played around with some weather data."
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"] [org.clojure/data.csv "0.1.2"] [clj-time "0.8.0"]]
  :profiles {:dev {:plugins [[com.jakemccrary/lein-test-refresh "0.5.2"]]}})
