define(['angular', 'ngResource', 'ngRoute', 'ngCookies', 'controllers/MainController', 'utils/Constant', 'angular.ui.bootstrap', 'ngLocalStorage'],
  function(angular, ngResource, ngRoute, ngCookies, mainController, Constant) {
    var appName = "ops";
    var app = angular.module(appName, ['ngResource', 'ngRoute', 'ngCookies', 'ui.bootstrap', 'LocalStorageModule']);
    app.controller(mainController.name, mainController.fn);
    app.bootstrap = function() {
        (function () {
            fetchPermission().then(bootstrapApplication);
            function fetchPermission() {
            }
            function bootstrapApplication() {
                angular.element(document).ready(function () {
                    angular.bootstrap(document, [appName]);
                });
            }
        }());
    };

    app.run(['$cookies', '$location', '$rootScope', 'Permission', '$http', function ($cookies, $location, $rootScope, Permission, $http) {
      $rootScope.permissionMapping = Permission.permissionMapping;
      $rootScope.permissions = Permission.userPermissions;
      $http.get(Constant.techOpsBase + '/common/currentuser').then(function (res) {
        if (res.data.respCode === '_200') {
          $rootScope.userName = res.data.result.name;
        }
        }, function (err) {
              console.log(err);
        });
      $rootScope.$on('$routeChangeSuccess', function (event, routeData) {
          $rootScope.pageTitle = (routeData.helpAlias? routeData.helpAlias : '首页') + ' BDChannel 点融网-Dianrong';
          if (!$rootScope.getCookieUsername()) {
              //$location.url('/login');
          }
      })
    }]);

    app.config(['$routeProvider',function($routeProvider) {
        $routeProvider.
        when('/user', {
          templateUrl: 'views/user/user.html',
          controller: 'UserController',
          helpAlias: '用户管理'
        }).
        when('/group', {
          templateUrl: 'views/group/group.html',
          controller: 'GroupController',
          helpAlias: '组管理'
        }).
        otherwise({
          redirectTo: 'views/common/notfound.html'
        });
    }]);
    return app;
});