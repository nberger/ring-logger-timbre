(ns ring.logger.timbre
  (:require [taoensso.timbre :as log]
            [ring.logger.protocols :refer [Logger]]
            [ring.logger :as logger]))

(defrecord TimbreLogger []
  Logger

  (add-extra-middleware [_ handler] handler)

  (error [_ x] (log/error x))
  (error-with-ex [_ ex x] (log/error ex x))
  (info [_ x] (log/info x))
  (warn [_ x] (log/warn x))
  (debug [_ x] (log/debug x))
  (trace [_ x] (log/trace x)))

(defn make-timbre-logger []
  (TimbreLogger.))

(defn wrap-with-logger
  "Returns a Ring middleware handler which uses taoensso/timbre as logger.

  Supported options are the same as of ring.logger/wrap-with-logger, except of
  :logger-impl which is fixed to a TimbreLogger instance"
  ([handler options]
   (logger/wrap-with-logger
     handler
     (merge options {:logger-impl (make-timbre-logger)})))
  ([handler] (wrap-with-logger handler {})))
