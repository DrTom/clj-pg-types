(ns pg-types.examples-time
  (:require
    [pg-types.connection :as connection]
    [clojure.java.jdbc :as jdbc]
    [java-time]
    ))

; defines connection parameter based on the env vars PGUSER, etc ...
(def conn (connection/env-db-spec))

; timestamp conversion from a iso8601 string, returns a org.joda.time.DateTime as used with clj-time
; note: this would also accept DateTime and of course java.sql.Timestamp as input
(def string-to-timestamp-to-date-time
  (->  (jdbc/query conn
              ["SELECT ?::timestamp"
               "2015-01-04T09:28:40.214Z"])
      first
      :timestamp
      type
      ))


(->  (jdbc/query conn
              ["SELECT now()"])
      first
      :now
      )


(def ^:dynamic *ts*
  (jdbc/with-db-transaction [tx (connection/env-db-spec)]
    ;(jdbc/db-do-commands tx "SET LOCAL TIME ZONE 'Europe/Berlin'")
    ;(jdbc/db-do-commands tx "SET LOCAL TIME ZONE 'UTC'")
    (jdbc/db-do-commands tx "CREATE TEMP TABLE test (id text, at_wotz timestamp WITHOUT TIME ZONE, at_wtz timestamp WITH TIME ZONE )")
    (jdbc/db-do-commands tx "INSERT INTO test (at_wotz, at_wtz) VALUES (now(), now())")
    ;(jdbc/query tx "SELECT now()")
    (-> (jdbc/query tx "SELECT * FROM test")
        first
        :at_wtz
        )))

;(.toLocalDate *ts*)
;(.toInstant *ts*)

(java-time/local-date *ts*)
(java-time/local-time *ts*)

