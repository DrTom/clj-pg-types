(ns pg-types.read-column.array
  (:require
    [pg-types.read-column :refer :all]
    )
  (:import 
    [java.sql Array]
    ))


(defmethod convert-column [:_ Array]
  [_ array rsmeta idx]
  (let [scalar-type-name-kw (-> (.getColumnTypeName rsmeta idx)
                                (subs 1)
                                keyword)]
    (->> (.getArray array)
         (map (fn [val] 
                (pg-types.read-column/convert-column 
                  scalar-type-name-kw val nil nil)))
         (into [])
         )))

