define(['../../utils/constant', '../../utils/utils'], function (constant, utils) {
    var Controller = function ($scope, $rootScope, UserService, RoleService, AlertService) {
    	// init transfer
    	$scope.uniauthTransfer={
    				srcItems: [],
    				targetItems: [],
    				displayKey: 'account',
    				srcTitle : $rootScope.translate('relMgr.relUser.notRelUser'),
    				targetTitle : $rootScope.translate('relMgr.relUser.relUser'),
    				showInputFilter : true,
    				filter_input_placeholder : $rootScope.translate('relMgr.relUser.filter.input.placeholder'),
    				filter_input_refresh_fun : queryNotSelectedUser,
    				filter_input_refresh_delay :500
    	};
    	$scope.role = RoleService.roleUserGrpShared;
        $scope.refreshRoles = function(name) {
            var params = {name: name, status:0, pageNumber:0, pageSize: 16};
            params.domainId = $rootScope.loginDomainsDropdown.option.id;
            return RoleService.getRoles(params).$promise.then(function(response) {
                if(response.data && response.data.data) {
                    $scope.roles = response.data.data;
                } else {
                    $scope.roles = [];
                }
            });
        };
        
        $scope.saveRolesToUser = function() {
            if(!$scope.role.selected || !$scope.role.selected.id) {
                AlertService.addAutoDismissAlert(constant.messageType.warning, $rootScope.translate('relMgr.tips.pleaseChooseRole'));
                return;
            }
            var params = {};
            params.id = $scope.role.selected.id;
            params.grpIds = [];
            params.userIds = selectedUserIds;
            params.needProcessGoupIds = false;
            RoleService.replaceGroupsAndUsersToRole(params, function (res) {
                if(res.info) {
                    for(var i=0; i<res.info.length;i++) {
                        AlertService.addAlert(constant.messageType.danger, res.info[i].msg);
                    }
                    return;
                }
                AlertService.addAutoDismissAlert(constant.messageType.info, $rootScope.translate('relMgr.tips.replaceUserSuccess'));
            }, function () {
                $scope.roles = [];
                AlertService.addAutoDismissAlert(constant.messageType.danger, $rootScope.translate('relMgr.tips.replaceUserFailure'));
            });
        };
        
        // cache
        var selectedUserIds = [];
        function queryNotSelectedUser(account){
        	if (!$scope.role.selected || !$scope.role.selected.id) return;
        	account = account || '';
        	var param = {
        			excludeUserIds : selectedUserIds,
        			account :  account,
        			status : constant.statusEnable,
        			pageSize : constant.pageSize,
        			pageNumber : 0
        	};
        	UserService.getUsers(param).$promise.then(function(response) {
                if(response.data && response.data.data) {
                	$scope.uniauthTransfer.srcItems = response.data.data;
                } else {
                	$scope.uniauthTransfer.srcItems = [];
                }
            });
        }
        
        function queryRoleUser(callback){
        	if (!$scope.role.selected || !$scope.role.selected.id) return;
        	var param = {
        			id : $scope.role.selected.id
        	};
        	RoleService.queryRoleUser(param).$promise.then(function(response) {
                if(response.data) {
                	$scope.uniauthTransfer.targetItems = response.data;
                } else {
                	$scope.uniauthTransfer.targetItems = [];
                }
                if(callback && typeof(callback) === "function") {
                    callback();
                }
            });
        }
        
        function roleSelectInvoke(){
        	// query selected user
        	queryRoleUser(function(){
        	    refreshCache();
        	    // query not selected user
        	    queryNotSelectedUser($scope.uniauthTransfer.filter_input_predicate);        	    
        	});
        }
        
        function refreshCache(){
        	selectedUserIds = [];
        	if(!$scope.uniauthTransfer.targetItems || $scope.uniauthTransfer.targetItems.length === 0) return;
        	for (var i = 0; i < $scope.uniauthTransfer.targetItems.length; i++){
        		selectedUserIds.push($scope.uniauthTransfer.targetItems[i].id);
        	}
        }
        
        $scope.$watch('role.selected', roleSelectInvoke);
        $scope.$watch('uniauthTransfer.targetItems', refreshCache, true);
        $scope.$on('selected-domain-changed', function(){
        	$scope.refreshRoles();
        });
        $scope.$on('selected-language-changed', function(){
        	$scope.refreshRoles();
        	$scope.uniauthTransfer.srcTitle = $rootScope.translate('relMgr.relUser.notRelUser');
        	$scope.uniauthTransfer.targetTitle = $rootScope.translate('relMgr.relUser.relUser');
        	$scope.uniauthTransfer.filter_input_placeholder = $rootScope.translate('relMgr.relUser.filter.input.placeholder');
        });
    };
    return {
        name: "RelRoleUserController",
        fn: ["$scope", "$rootScope", "UserService", "RoleService" , "AlertService", Controller]
    };
});
