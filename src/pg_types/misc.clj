(ns pg-types.misc)


(defn dispatch-type-name-kw [type-name]
  (if (= "_" (-> type-name name (subs 0 1)))
    :_
    (keyword type-name)))


(defn statement-type-name-kw [stmt ix]
  (-> (.getParameterMetaData stmt)
      (.getParameterTypeName ix)
      dispatch-type-name-kw))

