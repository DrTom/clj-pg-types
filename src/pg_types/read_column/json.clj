(ns pg-types.read-column.json
  (:require
    [pg-types.read-column :refer :all]
    [clojure.data.json :as json]
    )
  (:import 
    [org.postgresql.util PGobject]
    ))


(defn- pg-object-to-json [pg-object]
  (json/read-str (.getValue pg-object) :key-fn keyword))


(defmethod convert-column [:json PGobject]
  [_ pg-object _ _]
  (pg-object-to-json pg-object))

(defmethod convert-column [:jsonb PGobject]
  [_ pg-object _ _]
  (pg-object-to-json pg-object))
