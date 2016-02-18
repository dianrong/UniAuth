define(['angular', 'ngResource', 'ngRoute', 'ngCookies', 'ngTranslate', 'ngTranslateLoad', 'ngSanitize', 'dialogs',
    'controllers/main-controller', 'utils/constant', 'utils/utils','angular.ui.bootstrap',
    'ngLocalStorage', 'angularFileUpload'],
  function(angular, ngResource, ngRoute, ngCookies, ngTranslate, ngTranslateLoad, ngSanitize, dialogs,
           mainController, constant, utils) {
    var appName = "ops";
    var app = angular.module(appName, ['ngResource', 'ngRoute', 'pascalprecht.translate', 'ngSanitize',
        'ngCookies', 'ui.bootstrap', 'LocalStorageModule', 'dialogs.main', 'angularFileUpload']);
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

    app.config(['$routeProvider', 'localStorageServiceProvider', '$httpProvider', '$translateProvider',
        function($routeProvider, localStorageServiceProvider, $httpProvider, $translateProvider) {

        // wait for interceptor $httpProvider.interceptors.push('httpInterceptorSvc');
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

        $translateProvider.useSanitizeValueStrategy('sanitize');
        $translateProvider.translations('en-US',{
            DIALOGS_ERROR: "Error",
            DIALOGS_ERROR_MSG: "An unknown error has occurred.",
            DIALOGS_CLOSE: "Close",
            DIALOGS_PLEASE_WAIT: "Please Wait",
            DIALOGS_PLEASE_WAIT_ELIPS: "Please Wait...",
            DIALOGS_PLEASE_WAIT_MSG: "Waiting on operation to complete.",
            DIALOGS_PERCENT_COMPLETE: "% Complete",
            DIALOGS_NOTIFICATION: "Notification",
            DIALOGS_NOTIFICATION_MSG: "Unknown application notification.",
            DIALOGS_CONFIRMATION: "Confirmation",
            DIALOGS_CONFIRMATION_MSG: "Confirmation required.",
            DIALOGS_OK: "OK",
            DIALOGS_YES: "Yes",
            DIALOGS_NO: "No"
        });

        $translateProvider.translations('zh-CN',{
            DIALOGS_ERROR: "错误",
            DIALOGS_ERROR_MSG: "出现未知错误。",
            DIALOGS_CLOSE: "关闭",
            DIALOGS_PLEASE_WAIT: "请稍候",
            DIALOGS_PLEASE_WAIT_ELIPS: "请稍候...",
            DIALOGS_PLEASE_WAIT_MSG: "请等待操作完成。",
            DIALOGS_PERCENT_COMPLETE: "% 已完成",
            DIALOGS_NOTIFICATION: "通知",
            DIALOGS_NOTIFICATION_MSG: "未知应用程序的通知。",
            DIALOGS_CONFIRMATION: "确认",
            DIALOGS_CONFIRMATION_MSG: "确认要求。",
            DIALOGS_OK: "确定",
            DIALOGS_YES: "确认",
            DIALOGS_NO: "取消"
        });

        $translateProvider.preferredLanguage('zh-CN');
    }]);
    return app;
});