(ns adi.normalise.common.underscore
  (:require [hara.common.checks :refer [hash-map?]]
            [ribol.core :refer [raise]]))

(defn rep-key
  ([tsch]
     (rep-key tsch []))
  ([tsch lvl]
     (if-let [[k v] (first tsch)]
       (cond (hash-map? v)
             (or (rep-key (get tsch k))
                 (recur (rest tsch) lvl))

             (vector? v)
             (if (or (-> v first :required)
                     (-> v first :representative))
               (conj lvl k)
               (recur (rest tsch) lvl)))
       (raise [:adi :normalise :needs-require-key]
              (str "REP_KEY: Needs a :required or :representative key for " lvl)))))

(defn wrap-branch-underscore [f]
  (fn [subdata subsch nsv interim fns env]
    (cond (not (= subdata '_))
          (f subdata subsch nsv interim fns env)

          (= (:type env) "query")
          (assoc-in {} (rep-key subsch) '#{_})

          :else
          (raise [:adi :normalise :query-only
                  {:nsv nsv :key-path (:key-path interim)}]
                 (str "WRAP_BRANCH_UNDERSCORE: '_' only allowed on queries")))))