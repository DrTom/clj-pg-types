(ns pg-types.examples
  (:require 
    [clojure.java.jdbc :as jdbc]
    [pg-types.all]
    [pg-types.connection :as connection]
    ))

; defines connection parameter based on the env vars PGUSER, etc ...
(def conn (connection/env-db-spec))

; converting a map to ad from json (and also jsonb as of postgresql 9.4)
(def a-map-to-and-from-json 
  (jdbc/query conn 
              ["SELECT ?::json as json_map" 
               {:x 42 :a [ 1 2 "Foo" true nil]}]))

; arrays work to for json
(def an-array-to-and-from-json 
  (jdbc/query conn 
              ["SELECT ?::json as json_array" 
               [ 1 2 "Foo" true nil]]))

; converting a lazy seq to a pg array and then to persistent vector
(def lazy-seq-to-array-to-persistent-vector
  (jdbc/query conn 
              ["SELECT ?::int[] as vector" 
               (map identity [ 1 2 ])]))


; uuid conversion from string, returns a java.util.UUID
; note: uuid conversion also works for query parameters in the where clause
(def string-to-uuid
  (jdbc/query conn 
              ["SELECT ?::uuid as id" 
               "de305d54-75b4-431b-adb2-eb6b9e546013"]))

; timestamp conversion from a iso8601 string, returns a org.joda.time.DateTime as used with clj-time
; note: this would also accept DateTime and of course java.sql.Timestamp as input
(def string-to-timestamp-to-date-time
  (jdbc/query conn 
              ["SELECT ?::timestamp as some_date_time" 
               "2015-01-04T09:28:40.214Z"]))

; we can even convert types in arrays in both direction
(def seq-of-strings-to-vector
  (jdbc/query conn 
              ["SELECT ?::timestamp[] as some_timestamps" 
               ["2015-01-04T09:28:40.214Z"]]))

