(defproject pg-types "2.3.0"
  :description "Postgresql Types with Clojure JDBC"
  :url "https://github.com/DrTom/clj-pg-types"
  :license {:name "Dual: EPL and LGPL"}
  :dependencies [
                 [clj-time "0.11.0"]
                 [org.clojure/clojure "1.6.0"]
                 [org.clojure/data.json "0.2.6"]
                 [org.clojure/java.jdbc "0.4.2"]
                 [org.clojure/tools.logging "0.3.1"]
                 [org.postgresql/postgresql "9.4-1201-jdbc41"]
                 ]

  :profiles {:dev {:dependencies [
                                  [logbug "4.0.0"]
                                  [midje "1.7.0"]
                                  [org.slf4j/slf4j-api "1.7.12"]
                                  [org.slf4j/slf4j-log4j12 "1.7.12"]
                                  ]
                   :plugins [[lein-midje "3.1.1"]]

                   :resource-paths ["resources_dev"]

                   }}
  )
