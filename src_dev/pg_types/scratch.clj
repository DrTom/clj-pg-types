(ns pg-types.scratch
  (:require
    [clojure.java.jdbc :as jdbc]
    [pg-types.connection :refer :all]
    ))


;(jdbc/query (env-db-spec) ["SELECT ?::text[] AS tarray" "{'Foo','Bar','Baz'}"])

;(jdbc/query (env-db-spec) ["SELECT ?::integer[] AS tarray" "{1,2,3}"])


;(java.time.LocalTime/now)

;(java.sql.Time/valueOf (local-time))

;(java.util.Date/getTime)

(java.sql.Time/getTime)

