define(['../../utils/constant'],function(constant) {
    /**
     * A module representing a User controller.
     * @exports controllers/User
     */
    var Controller = function ($rootScope, $scope, GroupService, AlertService) {

        $scope.removeUserFromGroup = function () {

            if(!$scope.selectedNodes || $scope.selectedNodes.length == 0){
                AlertService.addAutoDismissAlert(constant.messageType.warning, $rootScope.translate('groupMgr.tips.selectGroupUdel'));
                return;
            }
            var nodes = $scope.selectedNodes;
            var userIdGroupIdPairs = [];
            for(var i=0;i<nodes.length;i++) {
                var linkage = {};
                linkage.entry1 = nodes[i].id;
                linkage.entry2 = nodes[i].parent.id;
                userIdGroupIdPairs.push(linkage);
            }
            var params = {};
            params.userIdGroupIdPairs = userIdGroupIdPairs;
            params.normalMember=false;

            GroupService.deleteUser(params, function (res) {
                if(res.info) {
                    for(var i=0; i<res.info.length;i++) {
                        AlertService.addAlert(constant.messageType.danger, res.info[i].msg);
                    }
                    return;
                }
                AlertService.addAutoDismissAlert(constant.messageType.info, $rootScope.translate('groupMgr.tips.delOwnerSuccess'));
                $scope.selectedNodes.splice(0, $scope.selectedNodes.length);
                //reset the tree component
                $rootScope.reset();
            }, function () {
                $scope.addedGroup = {};
                AlertService.addAutoDismissAlert(constant.messageType.danger, $rootScope.translate('groupMgr.tips.delOwnerFailure'));
            });
        };

    };

    return {
        name: "GroupDeleteOwnerController",
        fn: ["$rootScope","$scope", "GroupService", "AlertService", Controller]
    };

});
