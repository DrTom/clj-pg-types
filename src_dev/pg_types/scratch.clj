(ns pg-types.scratch
  (:require
    [clojure.java.jdbc :as jdbc]
    [pg-types.sql-parameter.timestamp]
    [pg-types.connection :refer :all]
    [java-time]
    [clj-logging-config.log4j :as logging-config]
    [clojure.tools.logging :as logging]
    ))

;(.setLevel (Logger/getLogger (str *ns*)) Level/ALL)
;(logging/debug "works")

;(jdbc/query (env-db-spec) ["SELECT ?::text[] AS tarray" "{'Foo','Bar','Baz'}"])

;(jdbc/query (env-db-spec) ["SELECT ?::integer[] AS tarray" "{1,2,3}"])


;(java.time.LocalTime/now)

;(java.sql.Time/valueOf (local-time))

;(java.util.Date/getTime)

;(java.sql.Time/getTime)

(logging-config/set-logger! :level :debug)
