(ns pg-types.timestamp-test
  (:require
    [midje.sweet :refer :all]
    [pg-types.connection :refer :all]
    [pg-types.all :refer :all]
    [clojure.java.jdbc :as jdbc]
    [clj-time.local]
    [java-time]
    ))

(def db-spec (env-db-spec))

;(facts "write and read timestamps"
;       (jdbc/with-db-transaction [tx db-spec]
;         (jdbc/db-do-commands tx "SET LOCAL TIME ZONE 'UTC'")
;         (jdbc/db-do-commands tx "CREATE TEMP TABLE test (id text, at timestamp WITHOUT TIME ZONE, at_wtz timestamp WITH TIME ZONE )")
;         (let [now  (java-time/instant)]
;           (facts "about TIMESTAMP WITHOUT TIMEZONE"
;                  (facts "result of a to string coerced / iso8601 compatible inserted value"
;                         (let [now-str (str now)
;                               at (:at (first (jdbc/insert! tx :test {:id "iso8601" :at now-str})))]
;                           (fact "is of type DateTime" (type at) => org.joda.time.DateTime)
;                           (fact "is equal to the original object" at => now)))
;                  (facts "result of a DateTime inserted value into timestamp WITHOUT TIME ZONE"
;                         (let [at (:at (first (jdbc/insert! tx :test {:id "date-time" :at now})))]
;                           (fact "is of type DateTime" (type at) => java.sql.Date)
;                           (fact "is equal to the original object" at => now))))
;
;           (facts "about TIMESTAMP WITH TIMEZONE"
;                  (facts "result of a to string coerced / iso8601 compatible inserted value"
;                         (let [now-str (str now)
;                               at (:at_wtz (first (jdbc/insert! tx :test {:id "iso8601" :at_wtz now-str})))]
;                           (fact "is of type DateTime" (type at) => org.joda.time.DateTime)
;                           (fact "is equal to the original object" at => now)))
;                  (facts "result of a DateTime inserted value into timestamp WITH TIME ZONE"
;                         (let [at (:at_wtz (first (jdbc/insert! tx :test {:id "date-time-wtz" :at_wtz now})))]
;                           (fact "is of type DateTime" (type at) => org.joda.time.DateTime)
;                           Not that
;                           (fact "is equal to the original object" at => now)))
;                  )
;
;           ))
;       )
;
;(str (time-core/now))
;(str (clj-time.local/local-now))

(deftest foo
  (is (= 1 1)))

(deftest write-and-read-timestamps
  (jdbc/with-db-transaction [tx db-spec]
    (jdbc/db-do-commands tx "SET LOCAL TIME ZONE 'UTC'")
    (jdbc/db-do-commands tx "CREATE TEMP TABLE test (id text, at timestamp WITH TIME ZONE )")
    (let [inst (java-time/instant)
          at (:at (first (jdbc/insert! tx :test {:id "instant" :at instant})))]
      (is (= inst at)))))


