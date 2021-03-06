(ns adi.process.normalise.base-test
  (:use midje.sweet)
  (:require [adi.process.normalise.base :refer :all]
            [adi.schema :as schema]
            [adi.test.examples :as examples]))

^{:refer adi.process.normalise.base/submaps :added "0.3"}
(fact "creates a submap based upon a lookup subkey"
  (submaps {:allow  {:account :check}
            :ignore {:account :check}} #{:allow :ignore} :account)
  => {:allow :check, :ignore :check})

^{:refer adi.process.normalise.base/normalise :added "0.3"}
(fact "base normalise function for testing purposes"

  (normalise {:account/name "Chris"
              :account/age 10}
             {:schema (schema/schema examples/account-name-age-sex)})
  => {:account {:age 10, :name "Chris"}}

  (normalise {:link/value "hello"
              :link {:next/value "world"
                     :next/next {:value "!"}}}
             {:schema (schema/schema examples/link-value-next)})
  => {:link {:next {:next {:value "!"}
                    :value "world"}
             :value "hello"}})
