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
        $location.url('/non-authorized');
      }
      return $q.reject(rejection);
    };

    var response = function (response) {
      if (response) {
        var data = response.data;
        if (data && data.info) {
          var infoArray = data.info;
          for (var i = 0; i < infoArray.length; i++) {
            var info = infoArray[i];
            if(angular.equals(constant.errorCode.LOGIN_REDIRECT_URL, info.name)) {
              window.location = info.msg;
            }
          }
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
