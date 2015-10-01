(defproject ring-logger-timbre "0.7.4"
  :description "taoensso/timbre implementation for ring-logger"
  :url "http://github.com/nberger/ring-logger-timbre"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :deploy-repositories [["releases" :clojars]]
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [ring-logger "0.7.4"]
                 [com.taoensso/timbre "4.1.1"]]
  :profiles {:dev {:dependencies [[ring/ring-mock "0.2.0"]
                                  [ring/ring-codec "1.0.0"]]}})
