(ns pg-types.sql-parameter.array
  (:require
    [pg-types.sql-parameter :refer :all]
    [clojure.data.json :as json]
    ))

(defn create-jdbc4-array [type-name-kw value stmt ix]
  (let [scalar-type ( -> (.getParameterMetaData stmt) 
                         (.getParameterTypeName ix)
                         (subs 1))
        connection (.getConnection stmt) ]
    (.createArrayOf  connection scalar-type (to-array value))))

(defmethod convert-parameter [:_ clojure.lang.IPersistentVector]
  [type-name-kw value stmt ix]
  (create-jdbc4-array type-name-kw value stmt ix))

(defmethod convert-parameter [:_ clojure.lang.ISeq]
  [type-name-kw value stmt ix]
  (create-jdbc4-array type-name-kw (into [] value) stmt ix))




