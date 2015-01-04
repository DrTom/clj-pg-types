(ns pg-types.all.arrays-test
  (:require 
    [midje.sweet :refer :all] 
    [pg-types.connection :refer :all]
    [pg-types.all :refer :all]
    [clojure.java.jdbc :as jdbc]
    [clj-time.core :as time-core]
    [clojure.tools.logging :as logging]
    ))

(def db-spec (env-db-spec))

(fact "arrays" 
      (jdbc/with-db-transaction [tx db-spec]
        (jdbc/db-do-commands tx "CREATE TEMP TABLE test (iarray integer[], tarray text[])")
        (facts "result of inserting " 
               (let [ result (first (jdbc/insert! tx :test {:iarray  [1 2 3]
                                                            :tarray ["Foo" "Bar" "Baz"]}))]
                 (fact "vector of integers is equal to input" (:iarray result) => [1 2 3])
                 (fact "vector of strings is equal to input" (:tarray result) => ["Foo" "Bar" "Baz"])
                 ))))

(fact "lazy seq" 
      (jdbc/with-db-transaction [tx db-spec]
        (jdbc/db-do-commands tx "CREATE TEMP TABLE test (iarray integer[])")
        (facts "result of inserting " 
               (let [result (first (jdbc/insert! tx :test {:iarray  (map identity [1 2 3])}))]
                 (fact "equal to input" (:iarray result) => [1 2 3])
                 ))))


(fact "array of timestamp converted" 
      (jdbc/with-db-transaction [tx db-spec]
        (jdbc/db-do-commands tx "CREATE TEMP TABLE test (tarray timestamp[])")
        (let [now (time-core/now)
              now-iso8601-str (str now) ]
          (logging/warn now-iso8601-str)
          (facts "result of inserting " 
                 (let [result (first (jdbc/insert! tx :test {:tarray [now-iso8601-str]}))]
                   (fact "equal to input" (:tarray result) => [now])
                   )))))

