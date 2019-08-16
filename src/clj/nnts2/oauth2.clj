(ns nnts2.oauth2
  (:require
    [nnts2.config :refer [oauth2-spec]]))

(defn authorization-params
  [{:keys [authorize-uri access-token-uri client-id client-secret scopes launch-uri redirect-uri]}]
  {:google
   {:authorize-uri    authorize-uri
    :access-token-uri access-token-uri
    :client-id        client-id
    :client-secret    client-secret
    :scopes           scopes
    :launch-uri       launch-uri
    :redirect-uri     redirect-uri
    :landing-uri      launch-uri}})
