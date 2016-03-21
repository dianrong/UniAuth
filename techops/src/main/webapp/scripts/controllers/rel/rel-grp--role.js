define(['../../utils/constant'], function (constant) {

    var Controller = function ($scope, $rootScope, GroupService, AlertService) {

        $scope.grp = GroupService.grpShared;
        $scope.refreshGrps = function(name) {
            var params = {name: name, status:0, pageNumber:0, pageSize: 16};
            return GroupService.queryGroup(params).$promise.then(function(response) {
                if(response.data && response.data.data) {
                    $scope.grps = response.data.data;
                } else {
                    $scope.grps = [];
                }
            });
        }

        $scope.grpRolesMsg = constant.loadEmpty;
        $scope.getGrpRolesWithCheckedInfo = function () {
            $scope.grpRolesMsg = constant.loading;
            var params = {};
            params.domainId = $rootScope.loginDomainsDropdown.option.id;
            if(!$scope.grp.selected) {
                $scope.grpRolesMsg = constant.loadEmpty;
                return;
            }
            params.id = $scope.grp.selected.id;

            GroupService.queryRolesWithCheckedInfo(params, function (res) {
                var result = res.data;
                if(res.info) {
                    $scope.grpRolesMsg = constant.loadError;
                    return;
                }
                if(!result) {
                    $scope.grpRolesMsg = constant.loadEmpty;
                    $scope.roles = [];
                    return;
                }

                $scope.grpRolesMsg = '';
                $scope.roles = result;
            }, function () {
                $scope.roles = [];
                $scope.grpRolesMsg = constant.loadError;
            });
        };
        if($scope.grp.selected) {
            $scope.getGrpRolesWithCheckedInfo();
        }
        $scope.replaceRolesToGrp = function() {
            var params = {};
            var checkedRoleIds = [];
            var roles = $scope.roles;
            for(var i=0; i<roles.length;i++) {
                if(roles[i].checked) {
                    checkedRoleIds.push(roles[i].id);
                }
            }
            params.roleIds = checkedRoleIds;
            params.id = $scope.grp.selected.id;
            GroupService.replaceRolesToGrp(params, function (res) {
                if(res.info) {
                    AlertService.addAlert(constant.messageType.info, res.info);
                    return;
                }
                AlertService.addAutoDismissAlert(constant.messageType.info, '成功替换group对应的角色.');
                $scope.getGrpRolesWithCheckedInfo();
            }, function () {
                $scope.roles = [];
                AlertService.addAutoDismissAlert(constant.messageType.danger, '替换group对应的角色失败, 请联系系统管理员');
            });
        }
        $scope.selectAllRolesForGrp = function() {
            for(var i=0;i<$scope.roles.length;i++) {
                $scope.roles[i].checked = true;
            }
        }
        var watch = $scope.$watch('grp.selected', $scope.getGrpRolesWithCheckedInfo);

        $scope.$on('selected-domain-changed', function(){
            $scope.grps = [];
            $scope.refreshGrps();
            $scope.getGrpRolesWithCheckedInfo();
        });
        //watch();
    };

    return {
        name: "RelGrpRoleController",
        fn: ["$scope", "$rootScope", "GroupService", "AlertService", Controller]
    };

});
