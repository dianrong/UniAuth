define(['../../utils/constant'],function(constant) {
    /**
     * A module representing a User controller.
     * @exports controllers/User
     */
    var Controller = function ($rootScope, $scope, OrganizationService, AlertService) {

        $scope.removeUserFromOrganization = function () {

            if(!$scope.selectedNodes || $scope.selectedNodes.length == 0){
                AlertService.addAutoDismissAlert(constant.messageType.warning, $rootScope.translate('organizationMgr.tips.selectOrganizationUdel'));
                return;
            }
            var nodes = $scope.selectedNodes;
            var userIdOrganizationIdPairs = [];
            for(var i=0;i<nodes.length;i++) {
                var linkage = {};
                linkage.entry1 = nodes[i].id;
                linkage.entry2 = nodes[i].parentId;
                userIdOrganizationIdPairs.push(linkage);
            }
            var params = {};
            params.userIdGroupIdPairs = userIdOrganizationIdPairs;
            params.normalMember=false;

            OrganizationService.deleteUser(params, function (res) {
                if(res.info) {
                    for(var i=0; i<res.info.length;i++) {
                        AlertService.addAlert(constant.messageType.danger, res.info[i].msg);
                    }
                    return;
                }
                AlertService.addAutoDismissAlert(constant.messageType.info, $rootScope.translate('organizationMgr.tips.delOwnerSuccess'));
                $scope.selectedNodes.splice(0, $scope.selectedNodes.length);
                //reset the tree component
                $rootScope.reset();
            }, function () {
                $scope.addedOrganization = {};
                AlertService.addAutoDismissAlert(constant.messageType.danger, $rootScope.translate('organizationMgr.tips.delOwnerFailure'));
            });
        };

    };

    return {
        name: "OrganizationDeleteOwnerController",
        fn: ["$rootScope","$scope", "OrganizationService", "AlertService", Controller]
    };

});
