(ns pg-types.all-test
  (:require 
    [midje.sweet :refer :all] 
    [pg-types.connection :refer :all]
    [pg-types.all :refer :all]
    [clojure.java.jdbc :as jdbc]
    [clj-time.core :as time-core]
    ))

(def db-spec (env-db-spec))

      

; better test this since we implement java.lang.string
(facts "character types" 
       (let [insert-map {:varchar_field  "Foo" :char_field "0123456789" :text_field "Bar"} ]
         (jdbc/with-db-transaction [tx db-spec]
           (jdbc/db-do-commands tx "CREATE TEMP TABLE test (varchar_field varchar, char_field char(10), text_field text)")
           (let [inserted-row (first (jdbc/insert! tx :test insert-map))]
             (fact inserted-row => insert-map)))))


