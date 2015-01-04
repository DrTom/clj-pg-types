(ns pg-types.sql-parameter.uuid
  (:require 
    [pg-types.sql-parameter :refer :all]
    ))

(defmethod convert-parameter [:uuid java.lang.String]
  [_ value _ _]
  (java.util.UUID/fromString value))

