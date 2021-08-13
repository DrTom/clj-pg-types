(ns pg-types.timestamp-java-time-test
  (:require
    [pg-types.all]
    [pg-types.connection :refer :all]
    [pg-types.read_column.timestamp.java-time]
    [clj-time.core :as clj-time]
    [clojure.java.jdbc :as jdbc]
    [clojure.test :refer :all]
    [java-time]))

(defn create-test-table [tx]
  (jdbc/db-do-commands
    tx "CREATE TEMP TABLE test (at timestamp WITH TIME ZONE )"))

(defn first-at-in-test [tx]
  (->> "select * from test"
       (jdbc/query tx) first :at))

(deftest writing-and-reading-java-instance-timestamps
  (testing "writing a instance and then reading the value yields equivalent values "
    (jdbc/with-db-transaction [tx (env-db-spec)]
      (create-test-table tx)
      (let [instant (java-time/instant)]
        (jdbc/insert! tx :test {:at instant})
        (let [at (first-at-in-test tx)]
          (is (= instant at)))))))
