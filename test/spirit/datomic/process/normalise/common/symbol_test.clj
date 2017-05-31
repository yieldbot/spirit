(ns spirit.datomic.process.normalise.common.symbol-test
  (:use hara.test)
  (:require [spirit.datomic.process.normalise.base :as normalise]
            [spirit.datomic.process.normalise.common.symbol :refer :all]
            [spirit.common.schema :as schema]
            [data.examples :as examples]
            ))

^{:refer spirit.datomic.process.normalise.common.symbol/wrap-single-symbol :added "0.3"}
(fact "wraps normalise to work with symbols for queries as well as :ref attributes of datoms"

  (normalise/normalise {:account {:type 'hello}}
                       {:schema (schema/schema {:account/type [{:type :keyword
                                                                :keyword {:ns :account.type}}]})}
                       {:normalise-single [wrap-single-symbol]})
  => {:account {:type '?hello}})