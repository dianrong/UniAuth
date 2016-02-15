/**
 * Module representing a shirt.
 * @module controllers/login
 */
define(['utils/Constant'], function (Constant) {
  'use strict';
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
    var svc = $resource(Constant.apiBase + '/common/:catalog', null, {
      getRole: {
        method: 'GET',
        params: {
          catalog: 'role'
        },
        //isArray: true,
        timeout: Constant.reqTimeout
      },
      getProvince: {
        method: 'GET',
        params: {
          area: 'provinces'
        },
        isArray: false,
        timeout: Constant.reqTimeout
      },
      getCity: {
        method: 'GET',
        params: {
          area: '@area',
          place: 'cities'
        },
        isArray: false,
        timeout: Constant.reqTimeout
      },
    });
    return svc;
  };

  return {
    name: 'CommonSvc',
    svc: ['$resource', Service]
  };


});
