define(['../../utils/constant'], function (constant) {
    /**
     * A module representing a User controller.
     * @exports controllers/User
     */
    var Controller = function ($scope, $rootScope, PermService, AlertService) {

        $scope.perm = PermService.permShared;
        $scope.refreshPerms = function(value) {
            var params = {value: value, status:0, pageNumber:0, pageSize: 16};
            params.domainId = $rootScope.loginDomainsDropdown.option.id;
            return PermService.getPerms(params).$promise.then(function(response) {
                if(response.data && response.data.data) {
                    $scope.perms = response.data.data;
                } else {
                    $scope.perms = [];
                }
            });
        };
        $scope.permRolesMsg = constant.loadEmpty;
        $scope.getAllRolesWithCheckedInfoInDomain = function () {
            $scope.permRolesMsg = constant.loading;
            var params = {};
            params.domainId = $rootScope.loginDomainsDropdown.option.id;
            if(!$scope.perm.selected) {
                $scope.roles = undefined;
                $scope.permRolesMsg = constant.loadEmpty;
                return;
            }
            params.id = $scope.perm.selected.id;

            PermService.queryRolesWithCheckedInfo(params, function (res) {
                var result = res.data;
                if(res.info) {
                    $scope.permRolesMsg = constant.loadError;
                    return;
                }
                if(!result) {
                    $scope.permRolesMsg = constant.loadEmpty;
                    $scope.roles = [];
                    return;
                }

                $scope.permRolesMsg = '';
                $scope.roles = result;
            }, function () {
                $scope.roles = [];
                $scope.permRolesMsg = constant.loadError;
            });
        };
        if($scope.perm.selected) {
            $scope.getAllRolesWithCheckedInfoInDomain();
        }
        $scope.replaceRolesToPerm = function() {
            var params = {};
            var checkedRoleIds = [];
            var roles = $scope.roles;
            for(var i=0; i<roles.length;i++) {
                if(roles[i].checked) {
                    checkedRoleIds.push(roles[i].id);
                }
            }
            params.roleIds = checkedRoleIds;
            params.id = $scope.perm.selected.id;
            PermService.replacePermsToRole(params, function (res) {
                if(res.info) {
                    for(var i=0; i<res.info.length;i++) {
                        AlertService.addAlert(constant.messageType.danger, res.info[i].msg);
                    }
                    return;
                }
                AlertService.addAutoDismissAlert(constant.messageType.info, $rootScope.translate('relMgr.tips.replaceRoleSuccess'));
                $scope.getAllRolesWithCheckedInfoInDomain();
            }, function () {
                $scope.roles = [];
                AlertService.addAutoDismissAlert(constant.messageType.danger, $rootScope.translate('relMgr.tips.replaceRoleFailure'));
            });
        };

        $scope.selectAllRolesForPerm = function(tag) {
        	tag = tag !== false
            for(var i=0;i<$scope.roles.length;i++) {
                $scope.roles[i].checked = tag;
            }
        };
        var watch = $scope.$watch('perm.selected', $scope.getAllRolesWithCheckedInfoInDomain);

        $scope.$on('selected-domain-changed', function(){
            $scope.roles = [];
            $scope.refreshPerms();
            $scope.getAllRolesWithCheckedInfoInDomain();
        });
        //watch();
        $scope.predicate = '';
        $scope.comparator = false;
        
        $scope.$on('selected-language-changed', $scope.getAllRolesWithCheckedInfoInDomain);
    };

    return {
        name: "RelPermRoleController",
        fn: ["$scope", "$rootScope", "PermService", "AlertService", Controller]
    };

});
