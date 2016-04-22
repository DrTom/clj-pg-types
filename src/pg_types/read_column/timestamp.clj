(ns pg-types.read-column.timestamp
  (:require
    [pg-types.read-column :refer :all]
    [clj-time.coerce :as time-coerce]
    ))

(defmethod convert-column [:timestamp java.sql.Timestamp]
  [_ val _ _]
  (time-coerce/from-sql-time val))

(defmethod convert-column [:timestamptz java.sql.Timestamp]
  [_ val _ _]
  (time-coerce/from-sql-time val))
