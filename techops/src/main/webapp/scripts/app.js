define(['angular', 'ngResource', 'angular.ui.router', 'ngCookies', 'ngTranslate', 'ngTranslateLoad','ngSanitize', 'dialogs', 'ngTreeController',
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

            fetchPermission().then(fetchLanguages).then(bootstrapApplication);

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

	    function fetchLanguages() {
                var initInjector = angular.injector(["ng"]);
                var $http = initInjector.get("$http");
                return $http.get(constant.apiBase + "/i18n/query").then(function (res) {
                    app.constant("languages", {
                        langs : res.data.data
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

	app.controller('Ctrl', ['$translate', '$scope','$http','$rootScope', function ($translate, $scope,$http,$rootScope) {
	 
	  $scope.changeLanguage = function (langKey,it) {
		  $http.get(constant.apiBase + "/i18n/changeLanguage?lang="+langKey).then(function (res) {
			  		$translate.use(langKey);
					$rootScope.translateConstant();
					$scope.languagesDropdown.selectOption(it)
		        }, function (errorResponse) {
		        	console.log('change language error');
		        	//$translate.use(langKey);
		        });
	  };
	}]);

     app.run(['$cookies', '$location', '$rootScope', '$state', '$stateParams', 'permission', '$http','languages','$translate',
        function ($cookies, $location, $rootScope, $state, $stateParams, permission, $http,languages,$translate) {
      // It's very handy to add references to $state and $stateParams to the $rootScope
      // so that you can access them from any scope within your applications.For example,
      // <li ng-class="{ active: $state.includes('contacts.list') }"> will set the <li>
      // to active whenever 'contacts.list' or one of its decendents is active.
      $rootScope.$state = $state;
      $rootScope.$stateParams = $stateParams;

      $rootScope.userInfo = permission.userInfo;
      $rootScope.shareGroup = {};
      $rootScope.translate=function(msg){
    	  return $translate.instant(msg);
      };
      $rootScope.translateOptions = function(options){
    	  for( i in options){
    		  if(options[i].code){
    			  options[i].name= $rootScope.translate(options[i].code)
    		  }
    	  }
      };
      $rootScope.translateConstant = function(){
    	//对constant进行翻译
    	  constant.loadEmpty=$rootScope.translate(constant.loadEmpty_code);
    	  constant.loading=$rootScope.translate(constant.loading_code);
    	  constant.createError=$rootScope.translate(constant.createError_code);
    	  constant.submitFail=$rootScope.translate(constant.submitFail_code);
    	  constant.loadError=$rootScope.translate(constant.loadError_code);
    	  $rootScope.translateOptions(constant.commonStatus);
    	  $rootScope.translateOptions(constant.auditOrderBy);
    	  $rootScope.translateOptions(constant.success);
    	  $rootScope.translateOptions(constant.ascDesc);
      };
      utils.generatorDropdown($rootScope, 'loginDomainsDropdown', permission.userInfo.switchableDomains, permission.userInfo.switchableDomains[0]);
      var currentLang ;
      angular.forEach(languages.langs.supportLanguages,function(item){
    	  if(item.code== languages.langs.current){
    		  currentLang = item;
    	  }
      });
      utils.generatorDropdown($rootScope, 'languagesDropdown', languages.langs.supportLanguages, currentLang || languages.langs.supportLanguages[0]);
      $http.get(constant.apiBase + "/cfg/download/TECHOPS_TITLE").then(function (res) {
        if(res.data && res.data.data && res.data.data.value) {
          $rootScope.pageTitle = res.data.data.value;
        } else {
          $rootScope.pageTitle = $translate.instant('header.title');
        }
        $rootScope.translateConstant();
      }, function (errorResponse) {
          $rootScope.pageTitle = $translate.instant('header.title');
          $rootScope.translateConstant();
      });
      
    }]);
    
    app.config(['localStorageServiceProvider', '$httpProvider', '$translateProvider', '$stateProvider', '$urlRouterProvider', '$rootScopeProvider','languages',
        function(localStorageServiceProvider, $httpProvider, $translateProvider, $stateProvider, $urlRouterProvider, $rootScopeProvider,languages) {
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
        state('rel.grp--tag', {
            url: '/rel-grp--tag',
            controller: "RelGroupTagController",
            templateUrl: "views/rel/rel-grp--tag.html"
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
        
        
        $translateProvider.useUrlLoader('servicei18n');
        
        $translateProvider.preferredLanguage(languages.langs.current);
        
        //$translateProvider.fallbackLanguage('zh');

    }]);
    return app;
});
