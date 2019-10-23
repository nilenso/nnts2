(ns nnts2.routes.invitation
  (:require [compojure.api.sweet :refer [context POST GET PATCH defroutes]]
            [nnts2.handler.invitation :as handler]
            [nnts2.spec.invitation :as spec]))

(defroutes routes
  (context "/orgs/:org-id/invites" []
    :coercion :spec
    :path-params [org-id :- ::spec/org-id]
    (GET "/" []
      #(handler/get-invites-for-org % org-id))

    (POST "/" []
      :body [body ::spec/invitation]
      #(handler/create % body org-id))

    (PATCH "/:invite-id" []
      :path-params [invite-id :- ::spec/invite-id]
      :body [body ::spec/update-invitation]
      #(handler/update % body org-id invite-id)))

  (context "/invites/:invite-id" []
    :coercion :spec
    :path-params [invite-id :- ::spec/invite-id]
    (PATCH "/:status" []
      :path-params [status :- ::spec/accept-or-reject]
      #(handler/respond-to-invite % invite-id status))))


;; create invite by admin member for an org
;; accept invite by a new user (not about org) (udpating an existing invite)
;; see all invites for an org by admin user
