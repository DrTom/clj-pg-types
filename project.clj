(defproject pg-types "2.1.0"
  :description "Postgresql Types with Clojure JDBC"
  :url "https://github.com/DrTom/clj-pg-types"
  :license {:name "Dual: EPL and LGPL"}
  :dependencies [
                 [clj-time "0.11.0"]
                 [org.clojure/clojure "1.6.0"]
                 [org.clojure/data.json "0.2.6"]
                 [org.clojure/java.jdbc "0.4.2"]
                 [org.clojure/tools.logging "0.3.1"]
                 [org.postgresql/postgresql "9.4-1206-jdbc41"]
                 ]

  :profiles {:dev {:dependencies [
                                  [midje "1.8.3"]
                                  [org.slf4j/slf4j-api "1.7.13"]
                                  [org.slf4j/slf4j-log4j12 "1.7.13"]
                                  ]
                   :plugins [[lein-midje "3.1.1"]]

                   :resource-paths ["resources_dev"]

                   }}
  )
