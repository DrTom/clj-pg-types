(ns pg-types.timestamp-test
  (:require
    [pg-types.all :refer :all]
    [pg-types.connection :refer :all]
    [clj-time.local]
    [clojure.java.jdbc :as jdbc]
    [clojure.test :refer :all]
    [java-time])
  (:import [java.sql Timestamp]))


(defn create-test-table [tx]
  (jdbc/db-do-commands
    tx "CREATE TEMP TABLE test (at timestamp WITH TIME ZONE )"))

(defn first-at-in-test [tx]
  (->> "select * from test"
       (jdbc/query tx) first :at))

(deftest writing-and-reading-timestamps

  (testing "writing rand reading a timestamp yields equivalent values "
    (jdbc/with-db-transaction [tx (env-db-spec)]
      (create-test-table tx)
      (let [timestamp (-> (java-time/instant)
                          java-time/instant->sql-timestamp)]
        (jdbc/insert! tx :test {:at timestamp})
        (let [at (first-at-in-test tx)]
          (is (= Timestamp (type at)))
          (is (= timestamp at))))))

  (testing "we can write iso8601 and retrieve java.sql.timestamp
           WITH RESTRICTED PRECICION to milliseconds"
    (jdbc/with-db-transaction [tx (env-db-spec)]
      (jdbc/db-do-commands tx "CREATE TEMP TABLE test (at timestamp WITH TIME ZONE )")
      (let [instant (-> (java-time/instant)
                        (java-time/truncate-to :millis))
            iso8601 (str instant)
            timestamp (Timestamp/from instant)]
        (jdbc/insert! tx :test {:at iso8601})
        (let [at (first-at-in-test tx)]
          (is (= Timestamp (type at)))
          (is (= timestamp at))))))

  (testing "we can still write joda retrieve java.sql.timestamp"
    (jdbc/with-db-transaction [tx (env-db-spec)]
      (jdbc/db-do-commands tx "CREATE TEMP TABLE test (at timestamp WITH TIME ZONE )")
      (let [joda-date-time (clj-time.core/now)
            timestamp (clj-time.coerce/to-sql-time (clj-time.core/now))]
        (jdbc/insert! tx :test {:at joda-date-time})
        (let [at (first-at-in-test tx)]
          (is (= Timestamp (type at)))
          (is (= timestamp at)))))))
