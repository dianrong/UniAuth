/**
 * Module representing a shirt.
 * @module controllers/login
 */
define(['../../utils/constant'], function (constant) {
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
    var svc = $resource(constant.apiBase + '/common/:catalog', null, {
      getRole: {
        method: 'GET',
        params: {
          catalog: 'role'
        },
        //isArray: true,
        timeout: constant.reqTimeout
      },
      getProvince: {
        method: 'GET',
        params: {
          area: 'provinces'
        },
        isArray: false,
        timeout: constant.reqTimeout
      },
      getCity: {
        method: 'GET',
        params: {
          area: '@area',
          place: 'cities'
        },
        isArray: false,
        timeout: constant.reqTimeout
      },
    });
    return svc;
  };

  return {
    name: 'CommonService',
    svc: ['$resource', Service]
  };


});
