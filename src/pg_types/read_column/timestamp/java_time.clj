(ns pg-types.read-column.timestamp.java-time
  (:require
    [pg-types.read-column :refer :all]))

(defmethod convert-column [:timestamp java.sql.Timestamp]
  [_ val _ _]
  (.toInstant val))

(defmethod convert-column [:timestamptz java.sql.Timestamp]
  [_ val _ _]
  (.toInstant val))
