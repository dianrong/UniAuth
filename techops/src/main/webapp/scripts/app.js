define(['angular', 'ngResource', 'angular.ui.router', 'ngCookies', 'ngTranslate', 'ngTranslateLoad', 'ngSanitize', 'dialogs', 'ngTreeController',
    'controllers/main-controller', 'utils/constant', 'utils/utils','angular.ui.bootstrap',
    'ngLocalStorage'],
  function(angular, ngResource, ngUiRouter, ngCookies, ngTranslate, ngTranslateLoad, ngSanitize, dialogs,
           ngTreeController, mainController, constant, utils) {
    var appName = "ops";
    var app = angular.module(appName, ['ngResource', 'ui.router', 'pascalprecht.translate', 'ngSanitize',
        'ngCookies', 'ui.bootstrap', 'LocalStorageModule', 'dialogs.main', 'treeControl']);
    app.controller(mainController.name, mainController.fn);
    app.bootstrap = function() {

        (function () {

            fetchPermission().then(bootstrapApplication);

            function fetchPermission() {
                var initInjector = angular.injector(["ng"]);
                var $http = initInjector.get("$http");
                return $http.get(constant.apiBase + "/user/current").then(function (res) {
                    app.constant("permission", {
                        userInfo : res.data.data
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

    app.run(['$cookies', '$location', '$rootScope', '$state', '$stateParams', 'permission', '$http',
        function ($cookies, $location, $rootScope, $state, $stateParams, permission, $http) {
      // It's very handy to add references to $state and $stateParams to the $rootScope
      // so that you can access them from any scope within your applications.For example,
      // <li ng-class="{ active: $state.includes('contacts.list') }"> will set the <li>
      // to active whenever 'contacts.list' or one of its decendents is active.
      $rootScope.$state = $state;
      $rootScope.$stateParams = $stateParams;

      $rootScope.userInfo = permission.userInfo;
      $rootScope.groupSelected = {};
      utils.generatorDropdown($rootScope, 'loginDomainsDropdown', permission.userInfo.switchableDomains, permission.userInfo.switchableDomains[0]);
      $rootScope.pageTitle = '权限运维系统 点融网-Dianrong';

    }]);

    app.config(['localStorageServiceProvider', '$httpProvider', '$translateProvider', '$stateProvider', '$urlRouterProvider', '$urlMatcherFactoryProvider',
        function(localStorageServiceProvider, $httpProvider, $translateProvider, $stateProvider, $urlRouterProvider, $urlMatcherFactoryProvider) {

        $httpProvider.interceptors.push('HttpInterceptorService');
        $urlRouterProvider.otherwise('/');
        $stateProvider.
        state('user', {
            url: "/",
            controller: "UserController",
            templateUrl: "views/user/user.html"
        }).
        state('group', {
            abstract: true,
            url: "/group",
            controller: "GroupController",
            templateUrl: "views/group/group.html"
        }).
        state('group.add', {
            url: '',
            controller: "GroupAddController",
            templateUrl: 'views/group/group-add.html'
        }).
        state('group.modify', {
            url: '/modify',
            templateUrl: 'views/group/group-modify.html'
        })

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