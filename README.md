# ring-logger-timbre [![Circle CI](https://circleci.com/gh/nberger/ring-logger-timbre.svg?style=svg)](https://circleci.com/gh/nberger/ring-logger-timbre)

[Timbre](https://github.com/ptaoussanis/timbre) implementation for [ring-logger](https://github.com/nberger/ring-logger)

[![Clojars Project](http://clojars.org/ring-logger-timbre/latest-version.svg)](http://clojars.org/ring-logger-timbre)

## Usage

In your `project.clj`, add the following dependencies:

```clojure
    [ring-logger-timbre "0.7.5"]
```

Add the middleware to your stack, using the timbre implementation. It's similar to
using the default ring-logger, but requiring the timbre namespace:

```clojure
    (ns foo
      (:require [ring.adapter.jetty :as jetty]
                [ring.logger.timbre :as logger.timbre]))

    (defn my-ring-app [request]
      {:status 200
       :headers {"Content-Type" "text/html"}
       :body "Hello world!"})

    (jetty/run-jetty (logger.timbre/wrap-with-logger my-ring-app) {:port 8080})
```

## Contributing

Pull requests, issues and any feedback are all welcome!

## License

Copyright © 2015 Nicolás Berger

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
