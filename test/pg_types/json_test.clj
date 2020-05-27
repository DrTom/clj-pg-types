(ns pg-types.json-test
  (:require
    [midje.sweet :refer :all]
    [pg-types.connection :refer :all]
    [pg-types.all :refer :all] [clojure.java.jdbc :as jdbc]
    ))

(def db-spec (env-db-spec))

(facts "json"
       (facts "maps"
              (let  [json-map-test-data {:x 42 :a [4 3 2]}]
                (jdbc/with-db-transaction [tx db-spec]
                  (jdbc/db-do-commands tx "CREATE TEMP TABLE test (id text, json_field json, jsonb_field jsonb)")
                  (facts "result of inserting json and jsonb"
                         (let [ result (first (jdbc/insert! tx :test {:id "map"
                                                                      :jsonb_field json-map-test-data
                                                                      :json_field json-map-test-data}))]
                           (fact "is equivalent to the original data" result => {:id "map"
                                                                                 :jsonb_field json-map-test-data
                                                                                 :json_field json-map-test-data})))
                  (facts "result of querying json and jsonb"
                         (let [row  (first (jdbc/query tx "SELECT * FROM test WHERE id = 'map'"))]
                           (fact row => truthy)
                           (fact "json is equal to original data" (:json_field row) => json-map-test-data)
                           (fact "jsonb is equal to original data" (:jsonb_field row) => json-map-test-data)
                           )))))

       (facts "array"
              (let  [json-array-test-data [1 2 "X" true false nil]]
                (jdbc/with-db-transaction [tx db-spec]
                  (jdbc/db-do-commands tx "CREATE TEMP TABLE test (id text, json_field json, jsonb_field jsonb)")
                  (facts "result of inserting json and jsonb"
                         (let [ result (first (jdbc/insert! tx :test {:id "array"
                                                                      :jsonb_field json-array-test-data
                                                                      :json_field json-array-test-data}))]
                           (fact "is equivalent to the original data" result => {:id "array"
                                                                                 :jsonb_field json-array-test-data
                                                                                 :json_field json-array-test-data})))
                  (facts "result of querying json and jsonb"
                         (let [row  (first (jdbc/query tx "SELECT * FROM test WHERE id = 'array'"))]
                           (fact row => truthy)
                           (fact "json is equal to original data" (:json_field row) => json-array-test-data)
                           (fact "jsonb is equal to original data" (:jsonb_field row) => json-array-test-data)
                           )))))

       (facts "lazy seq"
              (let  [json-array-test-data (map identity [1 2 "X" true false nil]) ]
                (jdbc/with-db-transaction [tx db-spec]
                  (jdbc/db-do-commands tx "CREATE TEMP TABLE test (id text, json_field json, jsonb_field jsonb)")
                  (facts "result of inserting json and jsonb"
                         (let [ result (first (jdbc/insert! tx :test {:id "array"
                                                                      :jsonb_field json-array-test-data
                                                                      :json_field json-array-test-data}))]
                           (fact "is equivalent to the original data" result => {:id "array"
                                                                                 :jsonb_field json-array-test-data
                                                                                 :json_field json-array-test-data})))
                  (facts "result of querying json and jsonb"
                         (let [row  (first (jdbc/query tx "SELECT * FROM test WHERE id = 'array'"))]
                           (fact row => truthy)
                           (fact "json is equal to original data" (:json_field row) => json-array-test-data)
                           (fact "jsonb is equal to original data" (:jsonb_field row) => json-array-test-data)
                           )))))

       )




