(ns pg-types.read-column.array
  (:require
    [pg-types.read-column :refer :all]
    )
  (:import 
    [org.postgresql.jdbc4 Jdbc4Array]
    ))


(defmethod convert-column [:_ Jdbc4Array]
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

