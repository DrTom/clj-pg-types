(ns pg-types.sql-parameter.timestamp
  (:require
    [pg-types.sql-parameter :refer :all]
    [clj-time.coerce :as time-coerce]
    [clj-time.core :as time-core]
    [clj-time.format :as time-format]
    ))

(defmethod convert-parameter [:timestamp java.lang.String]
  [_ value _ _]
  (time-coerce/to-sql-time
    (time-format/parse (time-format/formatters :date-time) value)))

(defmethod convert-parameter [:timestamptz java.lang.String]
  [_ value _ _]
  (time-coerce/to-sql-time
    (time-format/parse (time-format/formatters :date-time) value)))

(defmethod convert-parameter [:timestamp org.joda.time.DateTime]
  [_ value _ _]
  (time-coerce/to-sql-time value))

(defmethod convert-parameter [:timestamptz org.joda.time.DateTime]
  [_ value _ _]
  (time-coerce/to-sql-time value))

