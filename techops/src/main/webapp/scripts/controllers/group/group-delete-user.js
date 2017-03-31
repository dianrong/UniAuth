define(['../../utils/constant'],function(constant) {
    /**
     * A module representing a User controller.
     * @exports controllers/User
     */
    var Controller = function ($rootScope, $scope, GroupService, AlertService) {
        $scope.removeUserFromGroup = function () {
            if(!$scope.selectedNodes || $scope.selectedNodes.length == 0){
                AlertService.addAutoDismissAlert(constant.messageType.warning, $rootScope.translate('groupMgr.tips.selectUserUdel'));
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
            params.normalMember=true;
            GroupService.deleteUser(params, function (res) {
                if(res.info) {
                    for(var i=0; i<res.info.length;i++) {
                        AlertService.addAlert(constant.messageType.danger, res.info[i].msg);
                    }
                    return;
                }
                AlertService.addAutoDismissAlert(constant.messageType.info, $rootScope.translate('groupMgr.tips.delUserSuccess'));
                //reset the tree component
                $rootScope.reset();
            }, function () {
                AlertService.addAutoDismissAlert(constant.messageType.danger, $rootScope.translate('groupMgr.tips.delUserFailure'));
            });
        };

    };

    return {
        name: "GroupDeleteUserController",
        fn: ["$rootScope","$scope", "GroupService", "AlertService", Controller]
    };

});
