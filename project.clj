(defproject pg-types "1.0.0-beta.6"
  :description "Postgresql Types with Clojure JDBC"
  :url "https://github.com/DrTom/clj-pg-types"
  :license {:name "Dual: EPL and LGPL"}
  :dependencies [
                 [clj-logging-config "1.9.12"]
                 [clj-time "0.9.0"]
                 [org.clojure/clojure "1.6.0"]
                 [org.clojure/data.json "0.2.5"]
                 [org.clojure/java.jdbc "0.3.6"]
                 [org.clojure/tools.logging "0.3.1"]
                 [org.postgresql/postgresql "9.3-1102-jdbc4"]
                 ]

  :profiles {:dev {:dependencies [
                                  [midje "1.6.3"]
                                  [org.slf4j/slf4j-api "1.7.9"]
                                  [org.slf4j/slf4j-log4j12 "1.7.9"]
                                  ]
                   :plugins [[lein-midje "3.1.1"]]

                   :resource-paths ["resources_dev"]

                   }}

  :plugins [[org.apache.maven.wagon/wagon-ssh-external "2.6"]]

  :deploy-repositories [ ["tmp" "scp://maven@schank.ch/tmp/maven-repo/"]]

  )
