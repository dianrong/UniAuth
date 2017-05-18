define(['../../utils/constant'],function(constant) {
    /**
     * A module representing a User controller.
     * @exports controllers/User
     */
    var Controller = function ($rootScope, $scope, GroupService, UserService, AlertService) {

        $scope.user = {};
        $scope.refreshUsers = function(account) {
            var params = {account: account, status: 0, pageNumber:0, pageSize: 16};
            return UserService.getUsers(params).$promise.then(function(response) {
                if(response.data && response.data.data) {
                    $scope.users = response.data.data;
                } else {
                    $scope.users = [];
                }
            });
        };

        $scope.addUserToGroup = function () {

            if(!$rootScope.targetGroup || !$rootScope.targetGroup.id){
                AlertService.addAutoDismissAlert(constant.messageType.warning, $rootScope.translate('groupMgr.tips.chooseParentGroup'));
                return;
            }
            if(!$scope.user.selected || !$scope.user.selected.id) {
                AlertService.addAutoDismissAlert(constant.messageType.warning,$rootScope.translate('groupMgr.tips.chooseOwner') );
                return;
            }

            var params = {
                groupId:$rootScope.targetGroup.id,
                normalMember:false,
                userIds:[]
            };
            params.userIds.push($scope.user.selected.id);

            GroupService.addUser(params, function (res) {
                if(res.info) {
                    for(var i=0; i<res.info.length;i++) {
                        AlertService.addAlert(constant.messageType.danger, res.info[i].msg);
                    }
                    return;
                }
                AlertService.addAutoDismissAlert(constant.messageType.info, $rootScope.translate('groupMgr.tips.addOwnerSuccessed'));
                //reset the tree component
                $rootScope.reset();
            }, function () {
                $scope.addedGroup = {};
                AlertService.addAutoDismissAlert(constant.messageType.danger, $rootScope.translate('groupMgr.tips.addOwnerFailure'));
            });
        };

    };

    return {
        name: "GroupAddOwnerController",
        fn: ["$rootScope","$scope", "GroupService", "UserService", "AlertService", Controller]
    };

});
