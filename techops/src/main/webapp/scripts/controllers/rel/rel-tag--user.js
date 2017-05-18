define(['../../utils/constant', '../../utils/utils'], function (constant, utils) {
    var Controller = function ($scope, $rootScope, UserService, TagService, GroupService, AlertService) {
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

    	$scope.tag = TagService.tagShared;
        $scope.refreshTags = function(code) {
            var params = {fuzzyCode: code, status:0, pageNumber:0, pageSize: 16};
            params.domainId = $rootScope.loginDomainsDropdown.option.id;
            return TagService.getTags(params).$promise.then(function(response) {
                if(response.data && response.data.data) {
                    $scope.tags = response.data.data;
                } else {
                    $scope.tags = [];
                }
            });
        };

        $scope.saveTagsToUser = function() {
        	if(!$scope.tag.selected || !$scope.tag.selected.id) {
                AlertService.addAutoDismissAlert(constant.messageType.warning, $rootScope.translate('relMgr.tips.plaseChooseTag'));
                return;
            }
        	 var params = {};
             params.id = $scope.tag.selected.id;
             params.grpIds = [];
             params.userIds = selectedUserIds;
             params.needProcessGoupIds = false;
            TagService.replaceGroupsAndUsersToTag(params, function (res) {
                if(res.info) {
                    for(var i=0; i<res.info.length;i++) {
                        AlertService.addAlert(constant.messageType.danger, res.info[i].msg);
                    }
                    return;
                }
                AlertService.addAutoDismissAlert(constant.messageType.info, $rootScope.translate('relMgr.tips.replaceTagUserSuccess'));
            }, function () {
                $scope.tags = [];
                AlertService.addAutoDismissAlert(constant.messageType.danger, $rootScope.translate('relMgr.tips.replaceTagUserFailure'));
            });
        };
        
        // cache
        var selectedUserIds = [];
        function queryNotSelectedUser(account){
        	if (!$scope.tag.selected || !$scope.tag.selected.id) return;
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
        
        function queryTagUser(callback){
        	if (!$scope.tag.selected || !$scope.tag.selected.id) return;
        	var param = {
        			id : $scope.tag.selected.id
        	};
        	TagService.queryTagUser(param).$promise.then(function(response) {
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
        
        function tagSelectInvoke(){
        	// query selected user
        	queryTagUser(function(){
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
        
        $scope.$watch('tag.selected', tagSelectInvoke);
        $scope.$watch('uniauthTransfer.targetItems', refreshCache, true);
        $scope.$on('selected-domain-changed', function(){
        	$scope.refreshTags();
        });
        $scope.$on('selected-language-changed', function(){
        	$scope.refreshTags();
        	$scope.uniauthTransfer.srcTitle = $rootScope.translate('relMgr.relUser.notRelUser');
        	$scope.uniauthTransfer.targetTitle = $rootScope.translate('relMgr.relUser.relUser');
        	$scope.uniauthTransfer.filter_input_placeholder = $rootScope.translate('relMgr.relUser.filter.input.placeholder');
        });
    };

    return {
        name: "RelTagUserController",
        fn: ["$scope", "$rootScope", "UserService", "TagService", "GroupService", "AlertService", Controller]
    };

});
