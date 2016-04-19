define(['angular', 'ngResource', 'angular.ui.router', 'ngCookies', 'ngTranslate', 'ngTranslateLoad', 'ngSanitize', 'dialogs', 'ngTreeController',
    'controllers/main-controller', 'utils/constant', 'utils/utils', 'angularFileUpload', 'ngAnimate', 'ui-select','angular.ui.bootstrap',
    'ngLocalStorage'],
  function(angular, ngResource, ngUiRouter, ngCookies, ngTranslate, ngTranslateLoad, ngSanitize, dialogs,
           ngTreeController, mainController, constant, utils) {
    var appName = "ops";
    var app = angular.module(appName, ['ngResource', 'ui.router', 'pascalprecht.translate', 'ngSanitize',
        'ngCookies', 'ui.bootstrap', 'LocalStorageModule', 'dialogs.main', 'treeControl', 'ngAnimate', 'ui.select', 'angularFileUpload']);
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
                    console.log('app init failed....');
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
      $rootScope.shareGroup = {};
      utils.generatorDropdown($rootScope, 'loginDomainsDropdown', permission.userInfo.switchableDomains, permission.userInfo.switchableDomains[0]);
      $http.get(constant.apiBase + "/cfg/download/TECHOPS_TITLE").then(function (res) {
        if(res.data && res.data.data && res.data.data.value) {
          $rootScope.pageTitle = res.data.data.value;
        } else {
          $rootScope.pageTitle = '权限运维系统';
        }
      }, function (errorResponse) {
          $rootScope.pageTitle = '权限运维系统';
      });
    }]);

    app.config(['localStorageServiceProvider', '$httpProvider', '$translateProvider', '$stateProvider', '$urlRouterProvider', '$rootScopeProvider',
        function(localStorageServiceProvider, $httpProvider, $translateProvider, $stateProvider, $urlRouterProvider, $rootScopeProvider) {
        //$rootScopeProvider.digestTtl(100);
        $httpProvider.defaults.useXDomain = true;
        $httpProvider.defaults.withCredentials = true;
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
            controller: "GroupModifyController",
            templateUrl: 'views/group/group-modify.html'
        }).
        state('group.delete', {
            url: '/delete',
            controller: "GroupDeleteController",
            templateUrl: 'views/group/group-delete.html'
        }).
        state('group.add-user', {
            url: '/add-user',
            controller: "GroupAddUserController",
            templateUrl: 'views/group/group-add-user.html'
        }).
        state('group.delete-user', {
            url: '/delete-user',
            controller: "GroupDeleteUserController",
            templateUrl: 'views/group/group-delete-user.html'
        }).
        state('group.add-owner', {
            url: '/add-owner',
            controller: "GroupAddOwnerController",
            templateUrl: 'views/group/group-add-owner.html'
        }).
        state('group.delete-owner', {
            url: '/delete-owner',
            controller: "GroupDeleteOwnerController",
            templateUrl: 'views/group/group-delete-owner.html'
        }).
        state('role', {
            url: "/role",
            controller: "RoleController",
            templateUrl: "views/role/role.html"
        }).
        state('perm', {
            url: "/perm",
            controller: "PermController",
            templateUrl: "views/perm/perm.html"
        }).
        state('tag', {
            abstract: true,
            url: "/tag",
            controller: "TagController",
            templateUrl: "views/tag/tag.html"
        }).
        state('tag.tag-info', {
            url: '',
            controller: "TagInfoController",
            templateUrl: "views/tag/tag-info.html"
        }).
        state('tag.tag-type', {
            url: '',
            controller: "TagTypeController",
            templateUrl: "views/tag/tag-type.html"
        }).
        state('rel', {
            abstract: true,
            controller: "RelController",
            url: "/rel",
            templateUrl: "views/rel/rel.html"
        }).
        state('rel.role--perm', {
            url: '',
            controller: "RelRolePermController",
            templateUrl: "views/rel/rel-role--perm.html"
        }).
        state('rel.perm--role', {
            url: '/rel-perm--role',
            controller: "RelPermRoleController",
            templateUrl: "views/rel/rel-perm--role.html"
        }).
        state('rel.user--role', {
            url: '/rel-user--role',
            controller: "RelUserRoleController",
            templateUrl: "views/rel/rel-user--role.html"
        }).
        state('rel.grp--role', {
            url: '/rel-grp--role',
            controller: "RelGrpRoleController",
            templateUrl: "views/rel/rel-grp--role.html"
        }).
        state('rel.role--user-grp', {
            url: '/rel-role--user-grp',
            controller: "RelRoleUserGroupController",
            templateUrl: "views/rel/rel-role--user-grp.html"
        }).
        state('rel.tag--user-grp', {
            url: '/rel-tag--user-grp',
            controller: "RelTagUserGroupController",
            templateUrl: "views/rel/rel-tag--user-grp.html"
        }).
        state('rel.user--tag', {
            url: '/rel-user--tag',
            controller: "RelUserTagController",
            templateUrl: "views/rel/rel-user--tag.html"
        }).
        state('domain', {
            url: "/domain",
            controller: "DomainController",
            templateUrl: "views/domain/domain.html"
        }).
        state('audit', {
            url: "/audit",
            controller: "AuditController",
            templateUrl: "views/audit/audit.html"
        }).
        state('sys-cfg', {
            url: "/sys-cfg",
            controller: "CfgController",
            templateUrl: "views/cfg/cfg.html"
        }).
        state('non-authorized', {
            url: '/non-authorized',
            templateUrl: 'views/common/non-authorized.html'
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