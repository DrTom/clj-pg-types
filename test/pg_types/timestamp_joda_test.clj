(ns pg-types.timestamp-joda-test
  (:require
    [pg-types.all]
    [pg-types.connection :refer :all]
    [pg-types.read_column.timestamp.clj-time]
    [clj-time.core :as clj-time]
    [clojure.java.jdbc :as jdbc]
    [clojure.test :refer :all]))


(defn create-test-table [tx]
  (jdbc/db-do-commands
    tx "CREATE TEMP TABLE test (at timestamp WITH TIME ZONE )"))

(defn first-at-in-test [tx]
  (->> "select * from test"
       (jdbc/query tx) first :at))

(deftest writing-and-reading-joda-timestamps
  (testing "writing a joda-date-time and then reading the value yields equivalent values "
    (jdbc/with-db-transaction [tx (env-db-spec)]
      (create-test-table tx)
      (let [joda-date-time (clj-time/now)]
        (jdbc/insert! tx :test {:at joda-date-time})
        (let [at (first-at-in-test tx)]
          (is (= joda-date-time at)))))))

