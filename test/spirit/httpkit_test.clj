(ns spirit.httpkit-test
  (:require [spirit.http.server :as server]
            [spirit.http.client :as client]
            [spirit.httpkit.server]
            [spirit.httpkit.client]
            [hara.component :as component]))

(comment
  (ns-unalias 'spirit.httpkit-test 'client)
  
  (def server     (-> (server/create {:type :httpkit
                                      :host "localhost"
                                      :port 7889
                                      ;; :websocket   {:path     "/ws"
                                      ;;               :spec     {:on/me :<spec/me>}}
                                      :applications
                                      {:default {:path     "api"
                                                 :format   :edn
                                                 :handlers {:on/me (fn [req] (prn req) :on/me)}
                                                 :routes   {:on/me "me"}
                                                 ;;:spec     {:on/me :<spec/me>}
                                                 }}})
                      (component/start)))
  
  (component/stop server)
  
  (def client     (-> (client/create {:type   :httpkit
                                      :host   "localhost"
                                      :port   7889
                                      :format :edn
                                      :path   "api"
                                      :routes {:on/me "me"}})
                      (component/start)))
  
  (client/request client {:id :on/me} {:callback prn})
  
  (component/stop client))

(comment

  (def server    (http/server    {:type :httpkit
                                  :host "test.spirit"
                                  :port 7889
                                  :websocket   {:path     "/ws"
                                                :spec     {:on/me :<spec/me>}}}))

  (def ws-client (http/ws-client {:type :jetty
                                  :host "test.spirit"
                                  :port 7889
                                  :websocket {:path     "/ws"
                                              :spec     {:on/me :<spec/me>}}})))

