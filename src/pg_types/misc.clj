(ns pg-types.misc
  (:require
    [clojure.tools.logging :as logging]
    ))


(defn dispatch-type-name-kw [type-name]
  ;(logging/debug 'dispatch-type-name-kw type-name)
  (if (or (= "_" (-> type-name name (subs 0 1)))
          (= "][" (-> type-name name reverse (clojure.string/join) (subs 0 2))))
    :_
    (keyword type-name)))


(defn statement-type-name-kw [stmt ix]
  (-> (.getParameterMetaData stmt)
      (.getParameterTypeName ix)
      dispatch-type-name-kw))

