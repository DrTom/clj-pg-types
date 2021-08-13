(ns pg-types.arrays-test
  (:require
    [pg-types.connection :refer :all]
    [pg-types.all]
    [clojure.java.jdbc :as jdbc]
    [clojure.test :refer :all]
    [java-time]))

(def db-spec (env-db-spec))

(deftest arrays
  (jdbc/with-db-transaction [tx db-spec]
    (jdbc/db-do-commands tx "CREATE TEMP TABLE test
                            (iarray integer[], tarray text[])")
    (testing "result of inserting "
      (let [result (->> {:iarray  [1 2 3]
                         :tarray ["Foo" "Bar" "Baz"]}
                        (jdbc/insert! tx :test ) first)]
        (testing "vector of integers is equal to input"
          (is (= (:iarray result) [1 2 3])))
        (testing "vector of strings is equal to input"
          (is (= (:tarray result) ["Foo" "Bar" "Baz"])))))))

(deftest lazy-seqs
  (jdbc/with-db-transaction [tx db-spec]
    (jdbc/db-do-commands tx "CREATE TEMP TABLE test (iarray integer[])")
    (testing "result of inserting "
      (let [result (->> {:iarray  (map identity [1 2 3])}
                        (jdbc/insert! tx :test) first)]
        (testing "equal to input"
          (is (= (:iarray result) [1 2 3])))))))


(deftest array-of-timestamps
  (jdbc/with-db-transaction [tx db-spec]
    (jdbc/db-do-commands tx "CREATE TEMP TABLE test
                            (tarray timestamp WITH TIME ZONE[])")
    (let [timestamp (-> (java-time/instant)
                        java-time/instant->sql-timestamp)]
      (testing "result of inserting and array of timestamps"
        (let [result (->> {:tarray [timestamp]}
                          (jdbc/insert! tx :test) first)]
          (testing "is equal to input"
                  (is (= (:tarray result) [timestamp]))))))))

