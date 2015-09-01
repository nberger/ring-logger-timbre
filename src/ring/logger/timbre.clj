(ns ring.logger.timbre
  (:require [taoensso.timbre :as log]
            [ring.logger.protocols :refer [Logger]]
            [ring.logger :as logger]))

(defrecord TimbreLogger []
  Logger

  (add-extra-middleware [_ handler] handler)
  (log [_ level throwable message] (log/log level throwable message)))

(defn make-timbre-logger []
  (TimbreLogger.))

(defn wrap-with-logger
  "Returns a Ring middleware handler which uses taoensso/timbre as logger.

  Supported options are the same as of ring.logger/wrap-with-logger, except of
  :logger-impl which is fixed to a TimbreLogger instance"
  ([handler options]
   (logger/wrap-with-logger
     handler
     (merge options {:logger (make-timbre-logger)})))
  ([handler] (wrap-with-logger handler {})))
