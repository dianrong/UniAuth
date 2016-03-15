define(['../utils/constant'], function (constant) {
  /**
   * A module representing a login controller.
   * @exports controllers/login
   */
  var MainController = function ($scope, $rootScope, $location, $state) {
    $rootScope.pageLoaded = true;
    $rootScope.logout = function () {
      window.location = constant.logout;
    }
    function mustSelectALoginDomain() {
      if(!$rootScope.loginDomainsDropdown || !$rootScope.loginDomainsDropdown.option) {
        $state.go('non-authorized');
      }
    }
    mustSelectALoginDomain();
    $scope.$state = $state;
    $scope.$watch('$state.current.templateUrl', mustSelectALoginDomain);

    $scope.selectedDomain = $rootScope.loginDomainsDropdown;
    $scope.$watch('selectedDomain.option', function domainChangeCallBack() {
      $scope.$broadcast('selected-domain-changed', $scope.selectedDomain);
    }, true);
  };

  return {
    name: "MainController",
    fn: ["$scope", "$rootScope", "$location", "$state", MainController]
  }
});
