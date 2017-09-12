define(['../../utils/constant'],function(constant) {

    var Controller = function ($rootScope, $scope, OrganizationService, UserService, AlertService) {

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

        $scope.addUserToOrganization = function () {
            if(!$rootScope.targetOrganization || !$rootScope.targetOrganization.id){
                AlertService.addAutoDismissAlert(constant.messageType.warning, $rootScope.translate('organizationMgr.tips.chooseParentOrganization'));
                return;
            }
            if(!$scope.user.selected || !$scope.user.selected.id) {
                AlertService.addAutoDismissAlert(constant.messageType.warning,$rootScope.translate('organizationMgr.tips.chooseOwner') );
                return;
            }

            var params = {
                groupId:$rootScope.targetOrganization.id,
                normalMember:false,
                userIds:[]
            };
            params.userIds.push($scope.user.selected.id);

            OrganizationService.addUser(params, function (res) {
                if(res.info) {
                    for(var i=0; i<res.info.length;i++) {
                        AlertService.addAlert(constant.messageType.danger, res.info[i].msg);
                    }
                    return;
                }
                AlertService.addAutoDismissAlert(constant.messageType.info, $rootScope.translate('organizationMgr.tips.addOwnerSuccessed'));
                //reset the tree component
                $rootScope.reset();
            }, function () {
                $scope.addedOrganization = {};
                AlertService.addAutoDismissAlert(constant.messageType.danger, $rootScope.translate('organizationMgr.tips.addOwnerFailure'));
            });
        };

    };

    return {
        name: "OrganizationAddOwnerController",
        fn: ["$rootScope","$scope", "OrganizationService", "UserService", "AlertService", Controller]
    };

});
