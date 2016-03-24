define(['../../utils/constant'],function(constant) {
    /**
     * A module representing a User controller.
     * @exports controllers/User
     */
    var Controller = function ($rootScope, $scope, GroupService, UserService, AlertService) {
        var paramsCtlLevel = {};
        paramsCtlLevel.onlyShowGroup = false;
        paramsCtlLevel.userGroupType = 0;
        $scope.getTree = GroupService.syncTree;
        $scope.getTree(paramsCtlLevel);

        $scope.user = {};
        $scope.refreshUsers = function(email) {
            var params = {email: email, status: 0, pageNumber: 0, pageSize: 16};
            return UserService.getUsers(params).$promise.then(function(response) {
                if(response.data && response.data.data) {
                    $scope.users = response.data.data;
                } else {
                    $scope.users = [];
                }
            });
        }

        $scope.addUserToGroup = function () {

            if(!$rootScope.shareGroup.selected || !$rootScope.shareGroup.selected.id){
                AlertService.addAutoDismissAlert(constant.messageType.warning, '请先选择一个父组, 再添加用户.');
                return;
            }
            if(!$scope.user.selected || !$scope.user.selected.id) {
                AlertService.addAutoDismissAlert(constant.messageType.warning, '请先选择一个用户, 再添加.');
                return;
            }

            var params = {};
            params.groupId=$rootScope.shareGroup.selected.id;
            params.normalMember=true;
            params.userIds = [];
            params.userIds.push($scope.user.selected.id);

            GroupService.addUser(params, function (res) {
                if(res.info) {
                    for(var i=0; i<res.info.length;i++) {
                        AlertService.addAlert(constant.messageType.danger, res.info[i].msg);
                    }
                    return;
                }
                AlertService.addAutoDismissAlert(constant.messageType.info, "用户添加成功.");
                $scope.getTree(paramsCtlLevel);
            }, function () {
                $scope.addedGroup = {};
                AlertService.addAutoDismissAlert(constant.messageType.danger, '添加用户失败, 请联系系统管理员.');
            });
        };

    };

    return {
        name: "GroupAddUserController",
        fn: ["$rootScope","$scope", "GroupService", "UserService", "AlertService", Controller]
    };

});
