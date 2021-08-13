(defproject pg-types "3.0.0"
  :description "Postgresql Types with Clojure JDBC"
  :url "https://github.com/DrTom/clj-pg-types"
  :license {:name "Dual: EPL and LGPL"}
  :dependencies [
                 [clj-time "0.15.2"]
                 [clojure.java-time "0.3.2"]
                 [org.clojure/clojure "1.10.1"]
                 [org.clojure/data.json "1.0.0"]
                 [org.clojure/java.jdbc "0.7.11"]
                 [org.clojure/tools.logging "1.1.0"]
                 [org.postgresql/postgresql "42.2.12"]
                 ]

  :profiles {:dev {:dependencies [[logbug "5.0.0"]
                                  [com.taoensso/timbre "4.10.0"]]
                   :source-paths ["src" "src_dev"]
                   :resource-paths ["resources_dev"]
                   }})
