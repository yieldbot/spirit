(ns spirit.data.exchange
  (:require [spirit.protocol.iexchange :as exchange]
            [spirit.data.exchange.base :as base]
            [hara.component :as component])
  (:refer-clojure :exclude [queue]))

(defn list-queues
  "returns current list of queues
 
   (list-queues (queue/create {:routing routes}))
   => (contains {\"q1\" map?
                 \"q2\" map?})"
  {:added "0.5"}
  ([mq]
   (exchange/-list-queues mq)))

(defn add-queue
  "adds a queue to the mq
 
   (-> (queue/create {:routing routes})
       (add-queue \"q3\")
       (list-queues))
   => (contains {\"q1\" map?
                 \"q2\" map?
                 \"q3\" map?})"
  {:added "0.5"}
  ([mq name]
   (add-queue mq name {}))
  ([mq name opts]
   (exchange/-add-queue mq name opts)))

(defn delete-queue
  "deletes a queue from the mq
 
   (-> (queue/create {:routing routes})
       (delete-queue \"q1\")
       (list-queues))
   => (contains {\"q2\" map?})"
  {:added "0.5"}
  ([mq name]
   (exchange/-delete-queue mq name)))

(defn list-exchanges
  "returns current list of exchanges
 
   (list-exchanges (queue/create {:routing routes}))
   => (contains {\"ex1\" map?
                 \"ex2\" map?})"
  {:added "0.5"}
  ([mq]
   (exchange/-list-exchanges mq)))

(defn add-exchange
  "adds an exchange to the mq
 
   (-> (queue/create {:routing routes})
       (add-exchange \"ex3\")
       (list-exchanges))
   => (contains {\"ex1\" map?
                 \"ex2\" map?
                 \"ex3\" map?})"
  {:added "0.5"}
  ([mq name]
   (add-exchange mq name {}))
  ([mq name opts]
   (exchange/-add-exchange mq name opts)))

(defn delete-exchange
  "removes an exchange from the mq
 
   (-> (queue/create {:routing routes})
       (delete-exchange \"ex1\")
       (list-exchanges))
   => (contains {\"ex2\" map?})"
  {:added "0.5"}
  ([mq name]
   (exchange/-delete-exchange mq name)))

(defn list-bindings
  "returns current list of exchanges
 
   (list-bindings (queue/create {:routing routes}))
   => (contains-in {\"ex1\" {:exchanges {\"ex2\" [map?]}
                           :queues {\"q1\" [map?]}}
                    \"ex2\" {:queues {\"q2\" [map?]}}})"
  {:added "0.5"}
  ([mq]
   (exchange/-list-bindings mq)))

(defn bind-exchange
  "binds a queue to the exchange
 
   (-> (queue/create {:routing routes})
       (add-exchange \"ex3\")
       (bind-exchange \"ex1\" \"ex3\")
       (list-bindings))
   => (contains-in {\"ex1\" {:exchanges {\"ex2\" [map?]
                                       \"ex3\" [map?]}
                          :queues {\"q1\" [map?]}}
                    \"ex2\" {:queues {\"q2\" [map?]}}})"
  {:added "0.5"}
  ([mq source dest]
   (bind-exchange mq source dest {}))
  ([mq source dest opts]
   (exchange/-bind-exchange mq source dest opts)))

(defn bind-queue
  "binds an exchange to the exchange
 
   (-> (queue/create {:routing routes})
       (add-queue \"q3\")
       (bind-queue \"ex1\" \"q3\")
       (list-bindings))
   => (contains-in {\"ex1\" {:exchanges {\"ex2\" [map?]}
                           :queues {\"q1\" [map?]
                                   \"q3\" [map?]}}
                    \"ex2\" {:queues {\"q2\" [map?]}}})"
  {:added "0.5"}
  ([mq source dest]
   (bind-queue mq source dest {}))
  ([mq source dest opts]
   (exchange/-bind-queue mq source dest opts)))

(defn list-consumers
  "lists all the consumers for the mq
 
   (-> (queue/create {:routing routes :consumers consumers})
       (list-consumers))
   => (contains-in {\"q1\" {:hello map?,
                          :world map?},
                    \"q2\" {:foo map?}})"
  {:added "0.5"}
  ([mq]
   (exchange/-list-consumers mq)))

(defn add-consumer
  "adds a consumers to the mq
 
   (-> (queue/create {:routing routes :consumers consumers})
       (add-consumer \"q2\" {:id :bar :sync true :function prn})
       (list-consumers))
   => (contains-in {\"q1\" {:hello map?,
                          :world map?},
                    \"q2\" {:foo map?
                         :bar map?}})"
  {:added "0.5"}
  ([mq name handler]
   (exchange/-add-consumer mq name handler)))

(defn delete-consumer
  "deletes the consumer from the queue
   
   (-> (queue/create {:routing routes :consumers consumers})
       (delete-consumer \"q1\" :hello)
       (list-consumers))
   => (contains-in {\"q1\" {:world map?},
                    \"q2\" {:foo map?}})"
  {:added "0.5"}
  ([mq name id]
   (exchange/-delete-consumer mq name id)))

(defn publish
  "publishes a message to an exchange
 
   (def p (promise))
   
   (-> (queue/create {:routing routes
                      :consumers {\"q1\" {:hello {:function #(deliver p %)}}}})
       (publish \"ex1\" \"hello there\"))
   
   @p => \"hello there\""
  {:added "0.5"}
  ([mq exchange message]
   (publish mq exchange message {}))
  ([mq exchange message opts]
   (exchange/-publish mq exchange message opts)))
 
(defn create
  [m]
  (exchange/create m))
 
(defn exchange
  ([]
   (exchange {:type :mock}))
  ([m]
   (-> (exchange/create m)
       (component/start))))
