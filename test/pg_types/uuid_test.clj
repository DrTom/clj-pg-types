(ns pg-types.uuid-test
  (:require
    [pg-types.all]
    [pg-types.connection :refer :all]
    [clojure.java.jdbc :as jdbc]
    [clojure.test :refer :all]))

(def db-spec (env-db-spec))


(deftest uuid
  (jdbc/with-db-transaction [tx db-spec]
    (jdbc/db-do-commands tx "CREATE TEMP TABLE test (id uuid)")
    (let [str-uuid "65b85773-c935-439f-a3c0-6c538f84825d"
          uuid (java.util.UUID/fromString str-uuid) ]

      (testing "result of inserting a string value"
        (let [result-value (:id (first (jdbc/insert! tx :test {:id str-uuid})))]
          (testing "is the equivalent uuid type" (is (= result-value uuid)))))

      (testing "result of querying with a string value"
        (let [res (first  (jdbc/query tx ["SELECT * FROM test WHERE id = ?" str-uuid]))]
          (testing "yields the equivalent uuid type" (is (= res {:id uuid})))))

      (testing "result of querying with a uuid value"
        (let [res (first  (jdbc/query tx ["SELECT * FROM test WHERE id = ?" uuid]))]
          (testing "yields the equivalent uuid type" (is (=  res {:id uuid}))))))

    (let [ruuid (java.util.UUID/randomUUID)]
      (testing "result of inserting a random uuid of type UUID"
        (let [result-value (:id (first (jdbc/insert! tx :test {:id ruuid})))]
          (testing "is the equivalent uuid type" (is (= result-value ruuid))))))))


