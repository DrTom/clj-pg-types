(ns pg-types.scratch
  (:require
    [clojure.java.jdbc :as jdbc]
    [pg-types.sql-parameter.timestamp]
    [pg-types.connection :refer :all]
    [clojure.tools.logging :as logging]
    [java-time]
    [logbug.debug]
    [taoensso.timbre.tools.logging]
    ))

(taoensso.timbre.tools.logging/use-timbre)
(logbug.debug/debug-ns 'pg-types.sql-parameter.timestamp)
(logbug.debug/debug-ns *ns*)

(defn create-test-table [tx]
  (jdbc/db-do-commands
    tx "CREATE TEMP TABLE test (at timestamp WITH TIME ZONE )"))

(defn first-at-in-test [tx]
  (->> "select * from test"
       (jdbc/query tx) first :at))

(defn run []
  (jdbc/with-db-transaction [tx (env-db-spec)]
    (create-test-table tx)
    (let [instant (java-time/instant)
          timestamp (-> instant java-time/instant->sql-timestamp)]
      (logging/debug 'instant instant)
      (logging/debug 'timestamp timestamp)
      (jdbc/insert! tx :test {:at instant})
      (let [at (first-at-in-test tx)]
        (logging/debug 'at at)))))


