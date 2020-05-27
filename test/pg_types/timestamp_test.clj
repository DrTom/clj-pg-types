(ns pg-types.timestamp-test
  (:require
    [clojure.test :refer :all]
    [pg-types.connection :refer :all]
    [pg-types.all :refer :all]
    [clojure.java.jdbc :as jdbc]
    [clj-time.local]
    [java-time])

  (:import
    [java.sql Timestamp]
    [java.time.temporal ChronoUnit]

    ))


(def db-spec (env-db-spec))


(deftest foo
  (is (= 1 1)))

(deftest write-and-read-timestamps
  (jdbc/with-db-transaction [tx db-spec]
    (jdbc/db-do-commands tx "SET LOCAL TIME ZONE 'UTC'")
    (jdbc/db-do-commands tx "CREATE TEMP TABLE test (at timestamp WITH TIME ZONE )")
    (let [instant (java-time/instant)
          timestamp (Timestamp/from instant)]
      (jdbc/insert! tx :test {:at timestamp})
      (let [at (->> "select * from test"
                    (jdbc/query tx) first :at)]
        (is (= java.sql.Timestamp (type at)))
        (is (= timestamp at))))))


(deftest write-iso8601
  (testing "we can write iso8601 and retrieve java.sql.timestap
           WITH RESTRICTED PRECICION to milliseconds"
    (jdbc/with-db-transaction [tx db-spec]
      (jdbc/db-do-commands tx "SET LOCAL TIME ZONE 'UTC'")
      (jdbc/db-do-commands tx "CREATE TEMP TABLE test (at timestamp WITH TIME ZONE )")
      (let [instant (java-time/truncate-to (java-time/instant) :millis)
            iso8601 (str instant)
            timestamp (Timestamp/from instant)]
        (jdbc/insert! tx :test {:at iso8601})
        (let [at (->> "select * from test"
                      (jdbc/query tx) first :at)]
          (is (= java.sql.Timestamp (type at)))
          (is (= timestamp at)))))))
