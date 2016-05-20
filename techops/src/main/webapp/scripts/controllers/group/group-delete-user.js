define(['../../utils/constant'],function(constant) {
    /**
     * A module representing a User controller.
     * @exports controllers/User
     */
    var Controller = function ($rootScope, $scope, GroupService, AlertService) {
        var paramsCtlLevel = {};
        paramsCtlLevel.onlyShowGroup = false;
        paramsCtlLevel.userGroupType = 0;
        $scope.getTree = GroupService.syncTree;
        $scope.getTree(paramsCtlLevel);

        $scope.removeUserFromGroup = function () {

            if(!$scope.selectedNodes || $scope.selectedNodes.length == 0){
                AlertService.addAutoDismissAlert(constant.messageType.warning, "请先选择您要删除的用户.");
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
                AlertService.addAutoDismissAlert(constant.messageType.info, "用户删除成功");
                $scope.getTree(paramsCtlLevel);
                $scope.selectedNodes.splice(0, $scope.selectedNodes.length);
            }, function () {
                AlertService.addAutoDismissAlert(constant.messageType.danger, "用户删除失败, 请联系系统管理员.");
            });
        };

    };

    return {
        name: "GroupDeleteUserController",
        fn: ["$rootScope","$scope", "GroupService", "AlertService", Controller]
    };

});
