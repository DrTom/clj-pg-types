(ns pg-types.char-test
  (:require
    [pg-types.connection :refer :all]
    [pg-types.all]
    [clojure.java.jdbc :as jdbc]
    [clojure.test :refer :all]
    ))

(def db-spec (env-db-spec))

(deftest char-types
  (testing "character types"
    (let [insert-map {:varchar_field  "Foo"
                      :char_field "0123456789"
                      :text_field "Bar"}]
      (jdbc/with-db-transaction [tx db-spec]
        (jdbc/db-do-commands
          tx "CREATE TEMP TABLE test
             (varchar_field varchar, char_field char(10), text_field text)")
        (let [inserted-row (first (jdbc/insert! tx :test insert-map))]
          (is (= inserted-row insert-map)))))))


