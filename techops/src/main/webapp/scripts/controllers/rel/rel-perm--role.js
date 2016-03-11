define(['../../utils/constant'], function (constant) {
    /**
     * A module representing a User controller.
     * @exports controllers/User
     */
    var Controller = function ($scope, $rootScope, $location, PermService) {

        $scope.perm = PermService.permShared;
        $scope.refreshPerms = function(value) {
            var params = {value: value, pageNumber:0, pageSize: 16};
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
            $scope.rolePermsMsg = constant.loading;
            var params = {};
            params.domainId = $rootScope.loginDomainsDropdown.option.id;
            if(!$scope.role.selected) {
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
                var result = res.data;
                if(res.info) {
                    $scope.rolePermsMsg = constant.submitFail;
                    return;
                }
                $scope.rolePermsMsg = '';
                $scope.getAllPermsWithCheckedInfoInDomain();
            }, function () {
                $scope.perms = [];
                $scope.rolePermsMsg = constant.submitFail;
            });
        }
        var watch = $scope.$watch('role.selected', $scope.getAllPermsWithCheckedInfoInDomain);
        $scope.$on('selected-domain-changed', function(){
            $scope.role.selected = undefined;
            $scope.perms = [];
            $scope.refreshRoles();
            $scope.getAllPermsWithCheckedInfoInDomain();
        });
        //watch();
    };

    return {
        name: "RelPermRoleController",
        fn: ["$scope", "$rootScope", "$location", "RoleService", Controller]
    };

});
