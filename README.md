# Postgresql Types with Clojure JDBC

This library provides conversion between postgresql types and
structures and those used in Clojure on top of [clojure.java.jdbc][].

## Design and Architecture

This library is focused on Postgresql type conversion. It provides automatic
conversion in the simple case. This library is unobtrusive. It does not force
an *all or nothing* approach. The design is such that the various conversion can
be enabled at a fine granularity. This library provides a low level API to the
end of defining custom type conversions selectively on the Postgresql type and
the type of the providing respectively receiving entity.


## Release and Dependency Information

*  `pg-types "2.4.0"`


## Java, Clojure, and  Postgresql Drivers Dependencies

This version has been tested against Java 11 and Java 8. It is tested against
Clojure version `1.10` but older versions should work too. The tested
Postgresql driver version is `42.2.12`.


## Timestamps and `clj-time` vs `clojure.java-time`

Timestamps are converted to/from the Joda-Time library via the now deprecated
[clj-time](https://github.com/clj-time/clj-time). The next major release will
remove this behavior.

If you don't want Joda-Time types with the `2.x` release simply do not require
`pg-types.all` but only those conversions you desire. The following loads all
available conversions **excluding** Joda-Time.

```clojure
(ns your-ns
  (:require
    [pg-types.read-column.array]
    [pg-types.read-column.json]
    [pg-types.read-column]
    [pg-types.sql-parameter.array]
    [pg-types.sql-parameter.json]
    [pg-types.sql-parameter.uuid]
    [pg-types.sql-parameter]))
```

The type of a timestamp will then be `java.sql.Timestamp` which can be
converted to to more accessible types for example via
[Clojure.Java-Time](https://github.com/dm3/clojure.java-time):


```clojure
(java-time/local-date my-timestamp)
(java-time/local-time my-timestamp)
```


## Usage

### Basic Usage

Requiring the `pg-types.all` namespace enables automatic conversion for all
implemented pg types, providers and receivers. See the following for concrete
examples:

```clojure
(ns pg-types.examples
  (:require
    [clojure.java.jdbc :as jdbc]
    [pg-types.all]
    [pg-types.connection :as connection]
    ))

; defines connection parameter based on the env vars PGUSER, etc ...
(def conn (connection/env-db-spec))

; converting a map to ad from json (and also jsonb as of postgresql 9.4)
(def a-map-to-and-from-json
  (jdbc/query conn
              ["SELECT ?::json as json_map"
               {:x 42 :a [ 1 2 "Foo" true nil]}]))

; arrays work to for json
(def an-array-to-and-from-json
  (jdbc/query conn
              ["SELECT ?::json as json_array"
               [ 1 2 "Foo" true nil]]))

; converting a lazy seq to a pg array and then to persistent vector
(def lazy-seq-to-array-to-persistent-vector
  (jdbc/query conn
              ["SELECT ?::int[] as vector"
               (map identity [ 1 2 ])]))


; uuid conversion from string, returns a java.util.UUID
; note: uuid conversion also works for query parameters in the where clause
(def string-to-uuid
  (jdbc/query conn
              ["SELECT ?::uuid as id"
               "de305d54-75b4-431b-adb2-eb6b9e546013"]))

; timestamp conversion from a iso8601 string, returns a org.joda.time.DateTime as used with clj-time
; note: this would also accept DateTime and of course java.sql.Timestamp as input
(def string-to-timestamp-to-date-time
  (jdbc/query conn
              ["SELECT ?::timestamp as some_date_time"
               "2015-01-04T09:28:40.214Z"]))
```

### Enabling Conversions Selectively

Requiring `pg-types.sql-parameter.timestamp` will enable the conversions from
`String` (in the way described in the previous section) and `DateTime` to the
Postgresql `timestamp` type. Other parameter import conversions are defined in
their corresponding namespace under [`pg-types.sql-parameter`][].

Conversely requiring `pg-types/read-column/timestamp` will enable the
conversion from Postgresql `timestamp` to `DateTime`. Other export
conversions are defined in their corresponding namespace under
[`pg-types.read-column`][].


#### Implementing Conversions for Custom Enumerated Types

Custom types such as "Enumerated Types"
<https://www.postgresql.org/docs/9.1/static/datatype-enum.html> require
custom conversions.

See [`./test/pg_types/enum_test.clj`](./test/pg_types/enum_test.clj)
for an annotated example.


### Advanced Usage and Internals

#### Background

[clojure.java.jdbc][] provides the protocol [`ISQLParameter`][] for converting
data from Clojure to the database system. Clojure Protocols are single dispatch
within the java type hierarchy and in the case of `ISQLParameter` on the input
type, e.g. `IPersistentMap`. However, what is generally required for importing
a value is dispatch on the Postgresql data type and the input type.  This is
not possible with Clojure protocols.

#### Import via the `convert-parameter` Multimethod

This library hooks into `ISQLParameter` by (re-) implementing it for
`java.lang.Object` and routing the import via the `convert-parameter`
multimethod. The default implementation passes the value on. It has thus the
same effect as the original protocol implementation. The implementing methods
receive all parameters of `ISQLParameter`'s `set-parameter` and additionally the
Postgresql type-name as a keyword. The multimethod dispatch is on the type-name
keyword and the input type. The implementation is in the namespace
[`pg-types.sql-parameter.clj`][].



#### Export via the `convert-column` Multimethod

The `convert-column` Multimethod follows the same principle as
`convert-parameter` by hooking into the `IResultSetReadColumn` protocol.
The implementation is located in [`pg-types.read-column.clj`][].

#### Caveats

As mentioned previously, this library is designed to be unobtrusive. It is
unlikely, that it will conflict or influence other libraries. The sole
exception is if a library also (re-) implements `ISQLParameter` or
`IResultSetReadColumn` for `java.lang.Object`.

It is far more likely that other code implements either of the two protocols
with specific types which will disable the provided multimethods accidentally
or by purpose. We mentioned the restrictions of protocols and requirements to
dispatch on multiple properties for implementing `ISQLParameter`. It stands
therefore to reason not to provide any implementation for `ISQLParameter` but
define methods for the multimethod `convert-parameter`.



##### Arrays and the type-name Keyword Dispatch Value

The type-name of an array of some type is the type-name prefixed with an
underscore `_`, eg `_text` for a column `text[]`. This will not be dispatched
as `:_text` but simply as `:_`. All array types are therefore captured e.g.
with

```clojure
(defmethod convert-column [:_ Array]
  [_ array rsmeta idx]
  ;...
```


##### Converting values within Arrays

The values within an array will not be subject to whatever is
implemented either for `IResultSetReadColumn` or `convert-column`. This
is how [clojure.java.jdbc][] works together with the Postgresql driver.
The default implementation of `convert-column` for arrays applies a
little trick to achieve this by re injecting the scalar to
`convert-value`, see [`./src/pg_types/read_column/array.clj`][].
Apparently, the value `rsmeta` and `idx` are not reliable for the
consumer in this case and therefore `nil` is set for both.

  [`./src/pg_types/read_column/array.clj`]: ./src/pg_types/read_column/array.clj




## License

Copyright © 2020 Thomas Schank


 Postgresql Types for Clojure JDBC may be used under the terms of either the

 * GNU Lesser General Public License (LGPL) v3
   https://www.gnu.org/licenses/lgpl

or the

 * Eclipse Public License (EPL)
   http://www.eclipse.org/org/documents/epl-v10.php


  [`ISQLParameter`]: http://clojure.github.io/java.jdbc/#clojure.java.jdbc/ISQLParameter
  [`pg-types.read-column.clj`]: ./src/pg_types/read_column.clj
  [`pg-types.read-column`]: ./src/pg_types/read_column/
  [`pg-types.sql-parameter.clj`]: ./src/pg_types/sql_parameter.clj
  [`pg-types.sql-parameter`]: ./src/pg_types/sql_parameter/
  [clojure.java.jdbc]: https://github.com/clojure/java.jdbc
