(ns pg-types.sql-parameter
  (:require
    [clojure.java.jdbc :as jdbc]
    [pg-types.misc :as misc]
    [clojure.tools.logging :as logging]
    )
  (:import
    [java.sql PreparedStatement ParameterMetaData]
    [org.postgresql.util PGobject]
    ))

;(ns-unmap *ns* 'convert-parameter)

(defn- dispatch-convert-parameter [type-name-kw value _ _]
  [type-name-kw (type value)])

(defmulti convert-parameter dispatch-convert-parameter)

(defmethod convert-parameter :default
  [type-name-kw value stmt ix]
  value)

(defn dispatch-to-convert-parameter [val ^PreparedStatement stmt ^long ix]
  (let [type-name-kw (misc/statement-type-name-kw stmt ix)
        _ (logging/debug "calling convert-parameter with: " [type-name-kw val stmt ix] " type: " (type val))
        result (convert-parameter type-name-kw val stmt ix)]
    (logging/debug "converted-parameter: " result)
    result))

(extend-protocol jdbc/ISQLParameter
  java.lang.Object
  (set-parameter [val ^PreparedStatement stmt ^long ix]
    (.setObject stmt ix
                (dispatch-to-convert-parameter val stmt ix))))

