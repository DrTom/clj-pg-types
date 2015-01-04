(ns pg-types.scratch

  (:require 
    [clojure.java.jdbc :as jdbc]
    [pg-types.connection :refer :all]
    )

  )


;(jdbc/query (env-db-spec) ["SELECT ?::text[] AS tarray" "{'Foo','Bar','Baz'}"])

;(jdbc/query (env-db-spec) ["SELECT ?::integer[] AS tarray" "{1,2,3}"])





