(ns spirit.process.normalise.pipeline.fill-empty
  (:require [hara.common.checks :refer [hash-map?]]
            [hara.function.args :refer [op]]
            [hara.event :refer [raise]]))

(defn process-fill-empty [sfill tdata nsv interim tsch spirit]
  (if-let [[k v] (first sfill)]
    (cond (not (get tdata k))
          (cond (fn? v)
                (recur (next sfill)
                       (assoc tdata k (op v (:ref-path interim) spirit))
                       nsv interim tsch spirit)

                (hash-map? v)
                (recur (next sfill)
                       (assoc tdata k (process-fill-empty v
                                                    (get tdata k)
                                                    (conj nsv k)
                                                    interim
                                                    (get tsch k)
                                                    spirit))
                       nsv interim tsch spirit)

                :else
                (recur (next sfill)
                       (assoc tdata k v) nsv interim tsch spirit))

          (and (hash-map? v)
               (-> tsch (get k) vector?)
               (-> tsch (get k) first :type (= :ref)))
          (recur (next sfill) tdata nsv interim tsch spirit)

          :else
          (let [subdata (get tdata k)]
            (cond (hash-map? subdata)
                  (recur (next sfill)
                         (assoc tdata k (process-fill-empty v
                                                      (get tdata k)
                                                      (conj nsv k)
                                                      interim
                                                      (get tsch k)
                                                      spirit))
                         nsv interim tsch spirit)
                  :else
                  (recur (next sfill) tdata nsv interim tsch spirit))))
    tdata))

(defn wrap-model-fill-empty
  "fills data by associating additional elements
  (normalise/normalise {:account/name \"Chris\" :account/age 9}
            {:schema (schema/schema examples/account-name-age-sex)
             :pipeline {:fill-empty {:account {:age 10}}}}
            *wrappers*)
  => {:account {:name \"Chris\", :age 9}}

  (normalise/normalise {:account/name \"Chris\"}
            {:schema (schema/schema examples/account-name-age-sex)
             :pipeline {:fill-empty {:account {:age (fn [_ spirit]
                                                   (:age spirit))}}}
             :age 10}
            *wrappers*)
  => {:account {:name \"Chris\", :age 10}}
  "
  {:added "0.3"}
  [f]
  (fn [tdata tsch nsv interim fns spirit]
    (let [sfill (:fill-empty interim)
          output (process-fill-empty sfill tdata nsv interim tsch spirit)]
      (f output tsch nsv (update-in interim [:ref-path]
                                    #(-> %
                                         (pop)
                                         (conj output)))
         fns spirit))))