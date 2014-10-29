(defproject im.chit/adi "0.3.1-SNAPSHOT"
  :description "adi (a datomic interface)"
  :url "https://www.github.com/zcaudate/adi"
  :license {:name "The MIT License"
            :url "http://http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [im.chit/hara.common    "2.1.7"]
                 [im.chit/hara.string    "2.1.7"]
                 [im.chit/hara.data      "2.1.7"]
                 [im.chit/hara.function  "2.1.7"]
                 [im.chit/hara.component "2.1.7"]
                 [im.chit/ribol "0.4.0"]
                 [inflections "0.9.9"]]

  :documentation {:files {"docs/index"
                       {:input "test/midje_doc/adi_guide.clj"
                        :title "adi"
                        :sub-title "a datomic interface"
                        :author "Chris Zheng"
                        :email  "z@caudate.me"}}}

  :profiles {:dev {:plugins [[lein-midje "3.1.1"]
                             [lein-midje-doc "0.0.24"]]
                   :dependencies [;;[com.datomic/datomic-free "0.8.3971"]
                                  [com.datomic/datomic-free "0.9.5052" :exclusions [joda-time]]
                                  [midje "1.6.3"]
                                  [clj-time "0.6.0"]
                                  [me.raynes/fs "1.4.5"]
                                  [cheshire "5.2.0"]]}})
