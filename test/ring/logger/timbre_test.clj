(ns ring.logger.timbre-test
  (:require [clojure.test :refer :all]
            [clojure.string :as s]
            [taoensso.timbre :as timbre]
            [clansi.core :as clansi]
            [ring.logger.timbre :as logger.timbre]
            [ring.util.codec :as codec]
            [ring.mock.request :as mock]))

(defn atom-appender
  [a]
  {:enabled?   true
   :async?     false
   :min-level  nil
   :rate-limit nil
   :output-fn  :inherit
   :fn
   (fn [{:keys [level ?ns-str output-fn] :as data}]
     (swap! a conj [?ns-str level nil (output-fn data)]))})

(defn make-timbre-test-config [entries]
  (merge timbre/example-config
         {:level :trace
          :appenders {:atom (atom-appender entries)}}))

(defmacro with-timbre-test-config [entries & body]
  `(timbre/with-config (make-timbre-test-config ~entries)
     ~@body))

(deftest basic-ok-request-logging
  (let [entries (atom [])]
    (with-timbre-test-config entries
      (let [handler (-> (fn [req]
                          {:status 200
                           :body "ok"
                           :headers {:a "header in the response"}})
                        (logger.timbre/wrap-with-logger))]
        (handler (mock/request :get "/doc/10"))
        (is (= [:info :debug :trace :info] (map second @entries)))
        (is (re-find #"Starting.*get /doc/10 for localhost"
                     (-> @entries first (nth 3))))
        (is (re-find #":headers \{:a \"header in the response\"\}"
                     (-> @entries (nth 2) (nth 3))))
        (is (re-find #"Finished.*get /doc/10 for localhost in \(\d+ ms\) Status:.*200"
                     (-> @entries last (nth 3))))))))

(deftest basic-ok-body-logging
  (let [entries (atom [])]
    (with-timbre-test-config entries
      (let [handler (-> (fn [req]
                          {:status 200
                           :body "ok"
                           :headers {:a "header in the response"}})
                        (logger.timbre/wrap-with-body-logger))
            params {:foo :bar :zoo 123}]
        (handler (-> (mock/request :post "/doc/10")
                     (mock/body params)))
        (is (= [:debug] (map second @entries)))
        (is (.endsWith (-> @entries first (nth 3))
                       (str "-- Raw request body: '" (codec/form-encode params) "'")))))))

(deftest basic-error-request-logging
  (let [entries (atom [])]
    (with-timbre-test-config entries
      (let [handler (-> (fn [req]
                          {:status 500
                           :body "Oh noes!"
                           :headers {:a "header in the response"}})
                        (logger.timbre/wrap-with-logger))]
        (handler (mock/request :get "/doc/10"))
        (is (= [:info :debug :trace :error] (map second @entries)))
        (is (re-find #"Starting.*get /doc/10 for localhost"
                     (-> @entries first (nth 3))))
        (is (re-find #":headers \{:a \"header in the response\"\}"
                     (-> @entries (nth 2) (nth 3))))
        (is (re-find #"Finished.*get /doc/10 for localhost in \(\d+ ms\) Status:.*500"
                     (-> @entries last (nth 3))))))))

(deftest basic-error-with-exception-request-logging
  (let [entries (atom [])]
    (with-timbre-test-config entries
      (let [handler (-> (fn [req]
                          (throw (Exception. "I'm a handler that throws!")))
                        (logger.timbre/wrap-with-logger))]
        (is (thrown-with-msg? Exception #"handler that throws"
                              (handler (mock/request :get "/doc/10"))))
        (is (= [:info :debug :error] (map second @entries)))
        (is (re-find #"Starting.*get /doc/10 for localhost"
                     (-> @entries first (nth 3))))
        (is (re-find #"Uncaught exception .*I'm a handler that throws\!.*processing request.*for localhost in \(\d+ ms\)"
                     (-> @entries (nth 2) (nth 3))))
        (is (not (re-find #"Finished" (-> @entries last (nth 3)))))))))

(deftest basic-ok-request-logging-with-default-appender
  (timbre/with-level :trace
    (clansi/without-ansi ;; avoid ansi colors for easier re-find
      (let [entries (atom [])
            handler (-> (fn [req]
                          {:status 200
                           :body "ok"
                           :headers {:a "header in the response"}})
                        (logger.timbre/wrap-with-logger))
            out (with-out-str (handler (mock/request :get "/doc/10")))
            lines (s/split-lines out)]
        (is (not-any? #(re-find #"nil.*Starting" %) lines)
            "There should not be a nil before the Starting message (PR #6 fixed that behavior)")
        (is (= 4 (count lines)))
        (is (= ["INFO" "DEBUG" "TRACE" "INFO"] (->> lines
                                                    (map #(s/split % #" "))
                                                    (map #(nth % 3)))))
        (is (re-find #"Starting.*get /doc/10 for localhost"
                     (first lines)))
        (is (re-find #":headers \{:a \"header in the response\"\}"
                     (nth lines 2)))
        (is (re-find #"Finished.*get /doc/10 for localhost in \(\d+ ms\) Status:.*200"
                     (last lines)))))))
