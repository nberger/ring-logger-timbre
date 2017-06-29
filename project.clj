(defproject ring-logger-timbre "0.7.6-SNAPSHOT"
  :description "taoensso/timbre implementation for ring-logger"
  :url "http://github.com/nberger/ring-logger-timbre"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :deploy-repositories [["releases" :clojars]]
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [ring-logger "0.7.5"]
                 [com.taoensso/timbre "4.1.1"]]
  :profiles {:1.6 {:dependencies [[org.clojure/clojure "1.6.0"]]}
             :1.7 {:dependencies [[org.clojure/clojure "1.7.0"]]}
             :1.8 {:dependencies [[org.clojure/clojure "1.8.0"]]}
             :1.9 {:dependencies [[org.clojure/clojure "1.9.0-master-SNAPSHOT"]]}
             :dev {:dependencies [[ring/ring-mock "0.2.0"]
                                  [ring/ring-codec "1.0.0"]]}}
  :repositories {"sonatype-oss-public" "https://oss.sonatype.org/content/groups/public/"}
  :aliases {"test-all" ["with-profile" "dev,test,1.6:dev,test,1.7:dev,test,1.8:dev,test,1.9" "test"]})
