define(['../../utils/constant'], function (constant) {

    var Controller = function ($scope, $rootScope, GroupService) {

        $scope.grp = GroupService.grpShared;
        $scope.refreshGrps = function(name) {
            var params = {name: name, pageNumber:0, pageSize: 16};
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
                    $scope.grpRolesMsg = constant.submitFail;
                    return;
                }
                $scope.grpRolesMsg = '';
                $scope.getGrpRolesWithCheckedInfo();
            }, function () {
                $scope.roles = [];
                $scope.grpRolesMsg = constant.submitFail;
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
        fn: ["$scope", "$rootScope", "GroupService", Controller]
    };

});
