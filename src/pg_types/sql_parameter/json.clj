(ns pg-types.sql-parameter.json
  (:require
    [pg-types.sql-parameter :refer :all]
    [clojure.data.json :as json]
    )
  (:import
    [org.postgresql.util PGobject]
    ))

(defn create-pg-object [type-name-kw value]
  (doto (PGobject.)
    (.setType (if (keyword? type-name-kw)
                (subs (str type-name-kw) 1)
                (str type-name-kw)))
    (.setValue (json/write-str value :key-fn #(subs (str %) 1)))))

(defmethod convert-parameter [:json clojure.lang.IPersistentMap]
  [type-name-kw value _ _]
  (create-pg-object type-name-kw value))

(defmethod convert-parameter [:jsonb clojure.lang.IPersistentMap]
  [type-name-kw value _ _]
  (create-pg-object type-name-kw value))

(defmethod convert-parameter [:json clojure.lang.IPersistentVector]
  [type-name-kw value _ _]
  (create-pg-object type-name-kw value))

(defmethod convert-parameter [:jsonb clojure.lang.IPersistentVector]
  [type-name-kw value _ _]
  (create-pg-object type-name-kw value))

(defmethod convert-parameter [:json clojure.lang.ISeq]
  [type-name-kw value _ _]
  (create-pg-object type-name-kw (into [] value)))

(defmethod convert-parameter [:jsonb clojure.lang.ISeq]
  [type-name-kw value _ _]
  (create-pg-object type-name-kw (into [] value)))


