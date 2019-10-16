(ns com.ig.swagger.search.util.ring-middleware
  (:use ring.util.response
        [selmer.parser :as parser]))

(defn remove-context
  "Removes the deployed servlet context from a URI when running as a
   deployed web application"
  [handler]
  (fn [request]
    (let [uri (:uri request)]
    (if-let [context (:servlet-context-path request)]
        (if (.startsWith uri context)
          (handler (assoc request :uri
                                  (.substring uri (.length context))))
          (let [response (handler request)]
            (if (.equals uri "/search")
              (content-type response "text/html;charset=utf-8")
              (handler request)
              )))
        (let [response (handler request)]
          (if (.equals uri "/search")
            (content-type response "text/html;charset=utf-8")
            (handler request)
            ))))))

(defn wrap-with-additional-keys-in-req
  "Adds to all requests the key value pairs"
  [handler & additional-keys-in-req]
  (fn [req]
    (handler (apply assoc req additional-keys-in-req))))

