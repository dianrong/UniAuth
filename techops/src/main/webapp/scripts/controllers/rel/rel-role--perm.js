define(['../../utils/constant'], function (constant) {
    /**
     * A module representing a User controller.
     * @exports controllers/User
     */
    var Controller = function ($scope, $rootScope, RoleService, AlertService) {

        $scope.role = RoleService.roleShared;
        $scope.refreshRoles = function(name) {
            if(!$rootScope.loginDomainsDropdown || !$rootScope.loginDomainsDropdown.option || !$rootScope.loginDomainsDropdown.option.id) {
                $scope.rolePermsMsg = constant.loadEmpty;
                return;
            }
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
        $scope.rolePermsMsg = constant.loadEmpty;
        $scope.getAllPermsWithCheckedInfoInDomain = function () {
            if(!$rootScope.loginDomainsDropdown || !$rootScope.loginDomainsDropdown.option || !$rootScope.loginDomainsDropdown.option.id) {
                $scope.rolePermsMsg = constant.loadEmpty;
                return;
            }
            $scope.rolePermsMsg = constant.loading;
            var params = {};
            params.domainId = $rootScope.loginDomainsDropdown.option.id;
            if(!$scope.role.selected) {
                $scope.perms = undefined;
                $scope.rolePermsMsg = constant.loadEmpty;
                return;
            }
            params.id = $scope.role.selected.id;

            RoleService.queryPermsWithCheckedInfo(params, function (res) {
                var result = res.data;
                if(res.info) {
                    $scope.rolePermsMsg = constant.loadError;
                    return;
                }
                if(!result) {
                    $scope.rolePermsMsg = constant.loadEmpty;
                    $scope.perms = [];
                    return;
                }

                $scope.rolePermsMsg = '';
                $scope.perms = result;
            }, function () {
                $scope.perms = [];
                $scope.rolePermsMsg = constant.loadError;
            });
        };
        if($scope.role.selected) {
            $scope.getAllPermsWithCheckedInfoInDomain();
        }
        $scope.replacePermsToRole = function() {
            var params = {};
            var checkedPermIds = [];
            var perms = $scope.perms;
            for(var i=0; i<perms.length;i++) {
                if(perms[i].checked) {
                    checkedPermIds.push(perms[i].id);
                }
            }
            params.permIds = checkedPermIds;
            params.id = $scope.role.selected.id;
            RoleService.replacePermsToRole(params, function (res) {
                if(res.info) {
                    for(var i=0; i<res.info.length;i++) {
                        AlertService.addAlert(constant.messageType.danger, res.info[i].msg);
                    }
                    return;
                }
                AlertService.addAutoDismissAlert(constant.messageType.info, $rootScope.translate('relMgr.tips.replacePermSuccess'));
                $scope.getAllPermsWithCheckedInfoInDomain();
            }, function () {
                $scope.perms = [];
                $scope.rolePermsMsg = constant.submitFail;
            });
        }
        $scope.selectAllPermsForRole = function(tag) {
        	tag = tag !== false
            for(var i=0;i<$scope.perms.length;i++) {
                $scope.perms[i].checked = tag; 
            }
        }
        var watch = $scope.$watch('role.selected', $scope.getAllPermsWithCheckedInfoInDomain);

        $scope.$on('selected-domain-changed', function(){
            $scope.perms = [];
            $scope.refreshRoles();
            $scope.getAllPermsWithCheckedInfoInDomain();
        });
        //watch();

        $scope.predicate = '';
        $scope.comparator = false;
        
        $scope.$on('selected-language-changed', $scope.getAllPermsWithCheckedInfoInDomain);
    };

    return {
        name: "RelRolePermController",
        fn: ["$scope", "$rootScope", "RoleService", "AlertService", Controller]
    };

});
