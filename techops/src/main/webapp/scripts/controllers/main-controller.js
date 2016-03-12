define(['../utils/constant'], function (constant) {
  /**
   * A module representing a login controller.
   * @exports controllers/login
   */
  var MainController = function ($scope, $rootScope, $location) {
    $rootScope.pageLoaded = true;
    $rootScope.logout = function () {
      window.location = constant.logout;
    }
    if(!$rootScope.loginDomainsDropdown || !$rootScope.loginDomainsDropdown.option) {
      $location.url('/non-authorized');
      return;
    }
    $scope.selectedDomain = $rootScope.loginDomainsDropdown;
    $scope.$watch('selectedDomain.option', function domainChangeCallBack() {
      $scope.$broadcast('selected-domain-changed', $scope.selectedDomain);
    }, true);
  };

  return {
    name: "MainController",
    fn: ["$scope", "$rootScope", "$location", MainController]
  }
});
