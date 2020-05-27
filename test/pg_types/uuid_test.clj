(ns pg-types.uuid-test
  (:require
    [midje.sweet :refer :all]
    [pg-types.connection :refer :all]
    [pg-types.all :refer :all]
    [clojure.java.jdbc :as jdbc]
    [clj-time.core :as time-core]
    ))

(def db-spec (env-db-spec))


(facts "uuid"
       (jdbc/with-db-transaction [tx db-spec]
         (jdbc/db-do-commands tx "CREATE TEMP TABLE test (id uuid)")
         (let [str-uuid "65b85773-c935-439f-a3c0-6c538f84825d"
               uuid (java.util.UUID/fromString str-uuid) ]

           (facts "result of inserting a string value"
                  (let [result-value (:id (first (jdbc/insert! tx :test {:id str-uuid})))]
                    (fact "is the equivalent uuid type" result-value => uuid)))

           (facts "result of querying with a string value"
                  (let [res (first  (jdbc/query tx ["SELECT * FROM test WHERE id = ?" str-uuid]))]
                    (fact "yields the equivalent uuid type" res => {:id uuid})))
           (facts "result of querying with a uuid value"
                  (let [res (first  (jdbc/query tx ["SELECT * FROM test WHERE id = ?" uuid]))]
                    (fact "yields the equivalent uuid type" res => {:id uuid})))
           )
         (let [ruuid (java.util.UUID/randomUUID)]
           (facts "result of inserting a random uuid of type UUID"
                  (let [result-value (:id (first (jdbc/insert! tx :test {:id ruuid})))]
                    (fact "is the equivalent uuid type" result-value => ruuid))))))


