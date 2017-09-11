define(['../../utils/constant'],function(constant) {
    /**
     * A module representing a User controller.
     * @exports controllers/User
     */
    var Controller = function ($rootScope, $scope, OrganizationService, AlertService) {
        $scope.removeUserFromOrganization = function () {
            if(!$scope.selectedNodes || $scope.selectedNodes.length == 0){
                AlertService.addAutoDismissAlert(constant.messageType.warning, $rootScope.translate('organizationMgr.tips.selectUserUdel'));
                return;
            }
            var nodes = $scope.selectedNodes;
            var userIdOrganizationIdPairs = [];
            for(var i=0;i<nodes.length;i++) {
                var linkage = {};
                linkage.entry1 = nodes[i].id;
                linkage.entry2 = nodes[i].parent.id;
                userIdOrganizationIdPairs.push(linkage);
            }
            var params = {};
            params.userIdOrganizationIdPairs = userIdOrganizationIdPairs;
            params.normalMember=true;
            OrganizationService.deleteUser(params, function (res) {
                if(res.info) {
                    for(var i=0; i<res.info.length;i++) {
                        AlertService.addAlert(constant.messageType.danger, res.info[i].msg);
                    }
                    return;
                }
                AlertService.addAutoDismissAlert(constant.messageType.info, $rootScope.translate('organizationMgr.tips.delUserSuccess'));
                //reset the tree component
                $rootScope.reset();
            }, function () {
                AlertService.addAutoDismissAlert(constant.messageType.danger, $rootScope.translate('organizationMgr.tips.delUserFailure'));
            });
        };

    };

    return {
        name: "OrganizationDeleteUserController",
        fn: ["$rootScope","$scope", "OrganizationService", "AlertService", Controller]
    };

});
