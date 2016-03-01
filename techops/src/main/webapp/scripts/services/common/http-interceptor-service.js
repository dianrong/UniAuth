/**
 * Module representing a shirt.
 * @module controllers/login
 */
define(['../../utils/constant'], function (constant) {
  /**
   * A module representing a login controller.
   * @exports controllers/login
   */
  var Service = function ($q, $location) {
    var responseError = function (rejection) {
      if (rejection.status === 403) {
        $location.url('/403');
      }
      return $q.reject(rejection);
    };
    var response = function (response) {
      var data = null;
      if (response) {
        data = response.data;
        if (data && data.respCode && data.respCode == '_302') {
          if (data.result) {
            if (data.result.gotoUrl) {
              window.location = data.result.gotoUrl;
            } else {
              window.location = data.result;
            }
          }
        } else if (data && data.respCode && data.respCode == '_403') {
          $location.url('/403');
        }
      }
      return response;
    };
    var request = function(config) {
      config.headers['X-Requested-With'] = "XMLHttpRequest";
      return config;
    }
    return {
      responseError: responseError,
      response: response,
      request: request
    };
  };

  return {
    name: "HttpInterceptorService",
    svc: ["$q", "$location", Service]
  }


});
