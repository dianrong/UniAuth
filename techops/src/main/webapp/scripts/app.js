define(['angular', 'ngResource', 'ngRoute', 'ngCookies', 'controllers/main-controller', 'utils/constant', 'utils/utils','angular.ui.bootstrap', 'ngLocalStorage'],
  function(angular, ngResource, ngRoute, ngCookies, mainController, constant, utils) {
    var appName = "ops";
    var app = angular.module(appName, ['ngResource', 'ngRoute', 'ngCookies', 'ui.bootstrap', 'LocalStorageModule']);
    app.controller(mainController.name, mainController.fn);
    app.bootstrap = function() {

        (function () {

            fetchPermission().then(bootstrapApplication);

            function fetchPermission() {
                var initInjector = angular.injector(["ng"]);
                var $http = initInjector.get("$http");
                return $http.get(constant.apiBase + "/user/techops/domain").then(function (res) {
                    var permissionMapping = {},
                        userPermissions = [];
                    app.constant("permission", {
                        userPermissions: userPermissions,
                        permissionMapping: permissionMapping,
                        loginDomainsDropdown:res.data.data
                    });

                }, function (errorResponse) {
                    // Handle error case
                });
            }

            function bootstrapApplication() {
                angular.element(document).ready(function () {
                    angular.bootstrap(document, [appName]);
                });
            }
        }());
    };

    app.run(['$cookies', '$location', '$rootScope', 'permission', '$http', function ($cookies, $location, $rootScope, permission, $http) {
      $rootScope.permissionMapping = permission.permissionMapping;
      $rootScope.permissions = permission.userPermissions;
      utils.generatorDropdown($rootScope, 'loginDomainsDropdown', permission.loginDomainsDropdown, permission.loginDomainsDropdown[0]);
      $rootScope.userName = "shenglong.qian@dianrong.com";
      //$http.get(Constant.apiBase + '/common/currentuser').then(function (res) {
      //  if (res.data.respCode === '_200') {
      //    $rootScope.userName = res.data.result.name;
      //  }
      //  }, function (err) {
      //        console.log(err);
      //  });
      $rootScope.$on('$routeChangeSuccess', function (event, routeData) {
          $rootScope.pageTitle = (routeData.helpAlias? routeData.helpAlias : '首页') + ' BDChannel 点融网-Dianrong';
          if (!$rootScope.getCookieUsername()) {
              //$location.url('/login');
          }
      })
    }]);

    app.config(['$routeProvider',function($routeProvider) {
        $routeProvider.
        when('/', {
            templateUrl: 'views/user/user.html',
            controller: 'UserController',
            helpAlias: '用户管理'
        }).
        when('/test-user', {
          templateUrl: 'views/user/user.html',
          controller: 'UserController',
          helpAlias: '用户管理'
        }).
        when('/notfound', {
          templateUrl: 'views/common/notfound.html',
          helpAlias: '啥也木有'
        }).
        otherwise({
          redirectTo: 'notfound'
        });
    }]);
    return app;
});