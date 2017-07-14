(ns spirit.pipeline.base.keyword-test
  (:use hara.test)
  (:require [spirit.pipeline :as pipeline]
            [spirit.pipeline.base.keyword :refer :all]
            [spirit.schema :as schema]
            [data.examples :as examples]))

^{:refer spirit.pipeline.base.keyword/wrap-single-keyword :added "0.3"}
(fact "removes the keyword namespace if there is one"

  (pipeline/normalise {:account {:type :account.type/vip}}
                       {:schema (schema/schema {:account/type [{:type :keyword
                                                                :keyword {:ns :account.type}}]})}
                       {:normalise-single [wrap-single-keyword]})
  => {:account {:type :vip}})
