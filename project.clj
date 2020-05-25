(defproject pg-types "2.4.0"
  :description "Postgresql Types with Clojure JDBC"
  :url "https://github.com/DrTom/clj-pg-types"
  :license {:name "Dual: EPL and LGPL"}
  :dependencies [
                 [clj-time "0.15.2"]
                 [org.clojure/clojure "1.10.1"]
                 [org.clojure/data.json "1.0.0"]
                 [org.clojure/java.jdbc "0.7.11"]
                 [org.clojure/tools.logging "1.1.0"]
                 [org.postgresql/postgresql "42.2.12"]
                 [clojure.java-time "0.3.2"]
                 ]

  :profiles {:dev {:dependencies [
                                  [logbug "4.2.2"]
                                  [midje "1.9.9"]
                                  [org.slf4j/slf4j-api "1.7.30"]
                                  [org.slf4j/slf4j-log4j12 "1.7.30"]
                                  ]
                   :plugins [[lein-midje "3.1.1"]]

                   :resource-paths ["resources_dev"]

                   }})
