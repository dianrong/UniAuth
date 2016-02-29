/**
 * Module representing a shirt.
 * @module controllers/login
 */
define(['../../utils/constant'], function (constant) {
  /**
   * A module representing a login controller.
   * @exports controllers/login
   */
  var Service = function ($resource) {
    /* 
      Get     /domain/:domainId
      Put     /domain/:domainId/user/:userIds
      delete  /domain/:domainId/user/:userId
      Get     /domain/:domainId/menu
      Get     /domain/:domainId/url
     */
    var svc = $resource(constant.apiBase + '/:catalog', null, {
      /*
         /domain/:domainId
      */
      logout: {
        method: 'GET',
        params: {
          catalog: 'logout/cas'
        },
        //isArray: false,
        timeout: constant.reqTimeout,
        interceptor: {
          response: function (response) {
            var result = response.resource;
            result.$status = response.status;
            return result;
          }
        }
      }
    });
    return svc;
  };

  return {
    name: "LogoutService",
    svc: ["$resource", Service]
  }


});
