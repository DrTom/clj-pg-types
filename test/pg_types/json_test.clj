(ns pg-types.json-test
  (:require
    [pg-types.connection :refer :all]
    [pg-types.all]
    [clojure.java.jdbc :as jdbc]
    [clojure.test :refer :all]))

(def db-spec (env-db-spec))

(deftest json

  (testing "maps"
    (let  [json-map-test-data {:x 42 :a [4 3 2]}]
      (jdbc/with-db-transaction [tx db-spec]
        (jdbc/db-do-commands tx "CREATE TEMP TABLE test (id text, json_field json, jsonb_field jsonb)")
        (testing "result of inserting json and jsonb"
          (let [ result (first (jdbc/insert! tx :test {:id "map"
                                                       :jsonb_field json-map-test-data
                                                       :json_field json-map-test-data}))]
            (testing "is equivalent to the original data"
              (is (= result {:id "map"
                             :jsonb_field json-map-test-data
                             :json_field json-map-test-data})))))
        (testing "result of querying json and jsonb"
          (let [row  (first (jdbc/query tx "SELECT * FROM test WHERE id = 'map'"))]
            (testing (is row))
            (testing "json is equal to original data"
              (is (= (:json_field row) json-map-test-data)))
            (testing "jsonb is equal to original data"
              (is (= (:jsonb_field row) json-map-test-data)))
            )))))

  (testing "array"
    (let  [json-array-test-data [1 2 "X" true false nil]]
      (jdbc/with-db-transaction [tx db-spec]
        (jdbc/db-do-commands tx "CREATE TEMP TABLE test (id text, json_field json, jsonb_field jsonb)")
        (testing "result of inserting json and jsonb"
          (let [ result (first (jdbc/insert! tx :test {:id "array"
                                                       :jsonb_field json-array-test-data
                                                       :json_field json-array-test-data}))]
            (testing "is equivalent to the original data"
              (is (= result
                     {:id "array"
                      :jsonb_field json-array-test-data
                      :json_field json-array-test-data})))))
        (testing "result of querying json and jsonb"
          (let [row  (first (jdbc/query tx "SELECT * FROM test WHERE id = 'array'"))]
            (testing (is row))
            (testing "json is equal to original data"
              (is (= (:json_field row) json-array-test-data)))
            (testing "jsonb is equal to original data"
              (is (= (:jsonb_field row) json-array-test-data)))
            )))))

  (testing "lazy seq"
    (let  [json-array-test-data (map identity [1 2 "X" true false nil]) ]
      (jdbc/with-db-transaction [tx db-spec]
        (jdbc/db-do-commands tx "CREATE TEMP TABLE test (id text, json_field json, jsonb_field jsonb)")
        (testing "result of inserting json and jsonb"
          (let [ result (first (jdbc/insert! tx :test {:id "array"
                                                       :jsonb_field json-array-test-data
                                                       :json_field json-array-test-data}))]
            (testing "is equivalent to the original data" (is (= result {:id "array"
                                                                         :jsonb_field json-array-test-data
                                                                         :json_field json-array-test-data})))))
        (testing "result of querying json and jsonb"
          (let [row  (first (jdbc/query tx "SELECT * FROM test WHERE id = 'array'"))]
            (testing (is row))
            (testing "json is equal to original data" (is (= (:json_field row) json-array-test-data)))
            (testing "jsonb is equal to original data" (is (= (:jsonb_field row)  json-array-test-data)))
            ))))))





