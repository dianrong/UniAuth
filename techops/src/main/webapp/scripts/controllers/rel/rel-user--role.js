define(['../../utils/constant'], function (constant, AlertService) {
    /**
     * A module representing a User controller.
     * @exports controllers/User
     */
    var Controller = function ($scope, $rootScope, UserService, AlertService) {

        $scope.user = UserService.userShared;
        $scope.refreshUsers = function(email) {
            var params = {email: email, status:0, pageNumber:0, pageSize: 16};
            return UserService.getUsers(params).$promise.then(function(response) {
                if(response.data && response.data.data) {
                    $scope.users = response.data.data;
                } else {
                    $scope.users = [];
                }
            });
        }

        $scope.userRolesMsg = constant.loadEmpty;
        $scope.getUserRolesWithCheckedInfo = function () {
            $scope.userRolesMsg = constant.loading;
            var params = {};
            params.domainId = $rootScope.loginDomainsDropdown.option.id;
            if(!$scope.user.selected) {
                $scope.userRolesMsg = constant.loadEmpty;
                return;
            }
            params.id = $scope.user.selected.id;

            UserService.queryRolesWithCheckedInfo(params, function (res) {
                var result = res.data;
                if(res.info) {
                    $scope.userRolesMsg = constant.loadError;
                    return;
                }
                if(!result) {
                    $scope.userRolesMsg = constant.loadEmpty;
                    $scope.roles = [];
                    return;
                }

                $scope.userRolesMsg = '';
                $scope.roles = result;
            }, function () {
                $scope.roles = [];
                $scope.userRolesMsg = constant.loadError;
            });
        };
        if($scope.user.selected) {
            $scope.getUserRolesWithCheckedInfo();
        }
        $scope.replaceRolesToUser = function() {
            var params = {};
            var checkedRoleIds = [];
            var roles = $scope.roles;
            for(var i=0; i<roles.length;i++) {
                if(roles[i].checked) {
                    checkedRoleIds.push(roles[i].id);
                }
            }
            params.roleIds = checkedRoleIds;
            params.id = $scope.user.selected.id;
            UserService.replaceRolesToUser(params, function (res) {
                if(res.info) {
                    AlertService.addAlert(constant.messageType.danger, res.info);
                    return;
                }
                AlertService.addAutoDismissAlert(constant.messageType.info, '替换用户对应的角色成功.');
                $scope.getUserRolesWithCheckedInfo();
            }, function () {
                $scope.roles = [];
                AlertService.addAutoDismissAlert(constant.messageType.danger, '替换用户对应的角色失败, 请联系系统管理员.');
            });
        }
        $scope.selectAllRolesForUser = function() {
            for(var i=0;i<$scope.roles.length;i++) {
                $scope.roles[i].checked = true;
            }
        }
        var watch = $scope.$watch('user.selected', $scope.getUserRolesWithCheckedInfo);

        $scope.$on('selected-domain-changed', function(){
            $scope.roles = [];
            $scope.refreshUsers();
            $scope.getUserRolesWithCheckedInfo();
        });
        //watch();
    };

    return {
        name: "RelUserRoleController",
        fn: ["$scope", "$rootScope", "UserService", "AlertService", Controller]
    };

});
