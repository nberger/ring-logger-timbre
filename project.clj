(defproject ring-logger-timbre "0.7.6-SNAPSHOT"
  :description "taoensso/timbre implementation for ring-logger"
  :url "http://github.com/nberger/ring-logger-timbre"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :deploy-repositories [["releases" :clojars]]
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [ring-logger "0.7.7"]
                 [com.taoensso/timbre "4.10.0"]]
  :profiles {:dev {:dependencies [[ring/ring-mock "0.3.0"]
                                  [ring/ring-codec "1.0.1"]]}})
