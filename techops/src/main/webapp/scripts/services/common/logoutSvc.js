/**
 * Module representing a shirt.
 * @module controllers/login
 */
define(['utils/Constant'], function (Constant) {
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
    //var svc = $resource(Constant.apiBase + '/domain/:domainId/:catalog/:catalogId', null, {
    var svc = $resource(Constant.apiBase + '/:catalog', null, {
      /*
         /domain/:domainId
      */
      logout: {
        method: 'GET',
        params: {
          catalog: 'logout'
        },
        //isArray: false,
        timeout: Constant.reqTimeout,
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
    name: "LogoutSvc",
    svc: ["$resource", Service]
  }


});
