(ns pg-types.connection)

(defn env-db-spec []
  (conj {:dbtype "postgresql"
         :user (or (System/getenv "PGUSER")
                   (java.lang.System/getProperty "user.name"))
         :dbname (or (System/getenv "PGDATABASE")
                     (java.lang.System/getProperty "user.name"))}
        (when-let [password (System/getenv "PGPASSWORD")]
          {:password password})
        (when-let [port (System/getenv "PGPORT")]
          {:port port})))

