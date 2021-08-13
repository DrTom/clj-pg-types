(ns pg-types.enum-test
  (:require
    [pg-types.all :refer :all]
    [pg-types.connection :refer :all]
    [pg-types.read-column :refer :all]
    [pg-types.sql-parameter :refer :all]

    [clojure.java.jdbc :as jdbc]
    [clojure.test :refer :all]
    )
  (:import
    [org.postgresql.util PGobject]
    ))


; These tests use a very similar structure as the first example
; in <https://www.postgresql.org/docs/9.1/static/datatype-enum.html>.

(defn kw->str [k]
  (if (keyword? k) (subs (str k) 1) (str k) ))

; If you leave this out you will get back a string.
(defmethod convert-column [:mood java.lang.String]
  [_ s _ _]
  (keyword s))

(defmethod convert-parameter [:mood clojure.lang.Keyword]
  [type-name-kw value _ _]
  (doto (PGobject.)
    (.setType (kw->str type-name-kw))
    (.setValue (kw->str value))))

(deftest enum-kw
  (testing "inserting a custom enum mood type as a keyword and retuning it as a keyword"
    (jdbc/with-db-transaction [tx (env-db-spec)]
      (jdbc/db-set-rollback-only! tx)
      (jdbc/db-do-commands tx "DROP TYPE IF EXISTS mood;")
      (jdbc/db-do-commands tx "CREATE TYPE mood AS ENUM ('sad', 'ok', 'happy');")
      (jdbc/db-do-commands tx "CREATE TEMP TABLE person (name text, current_mood mood);")
      (jdbc/insert! tx :person {:name "Moe" :current_mood :happy} )
      (let [row (->> ["SELECT * FROM person"]
                     (jdbc/query tx )
                     first)]
        (testing "returns the proper current mood"
          (is (= (:current_mood row) :happy)))))))


; This will let you write strings, too.
(defmethod convert-parameter [:mood java.lang.String]
  [type-name-kw value _ _]
  (doto (PGobject.)
    (.setType (kw->str type-name-kw))
    (.setValue value)))

(deftest enum-string
  (testing "inserting a custom enum mood type as a string and retuning it as a keyword"
    (jdbc/with-db-transaction [tx (env-db-spec)]
      (jdbc/db-set-rollback-only! tx)
      (jdbc/db-do-commands tx "DROP TYPE IF EXISTS mood;")
      (jdbc/db-do-commands tx "CREATE TYPE mood AS ENUM ('sad', 'ok', 'happy');")
      (jdbc/db-do-commands tx "CREATE TEMP TABLE person (name text, current_mood mood);")
      (jdbc/insert! tx :person {:name "Moe" :current_mood "happy"} )
      (let [row (->> ["SELECT * FROM person"]
                     (jdbc/query tx )
                     first)]
        (testing "returns the proper current mood"
          (is (= (:current_mood row) :happy)))))))


