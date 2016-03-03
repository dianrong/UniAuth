define(['../utils/constant'], function (constant) {
  /**
   * A module representing a login controller.
   * @exports controllers/login
   */
  var MainController = function ($scope, $rootScope) {
    $rootScope.pageLoaded = true;
    $rootScope.logout = function () {
      window.location = constant.logout;
    }
  };

  return {
    name: "MainController",
    fn: ["$scope", "$rootScope", MainController]
  }
});
