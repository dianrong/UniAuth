define(['../../utils/constant'],function(constant) {
    /**
     * A module representing a User controller.
     * @exports controllers/User
     */
    var Controller = function ($rootScope, $scope, GroupService, UserService) {
        var paramsCtlLevel = {};
        paramsCtlLevel.onlyShowGroup = false;
        paramsCtlLevel.userGroupType = 0;
        $scope.getTree = GroupService.syncTree;
        $scope.getTree(paramsCtlLevel);

        $scope.removeUserFromGroup = function () {

            if(!$scope.selectedNodes || $scope.selectedNodes.length == 0){
                $scope.groupMessage = '请先选择您要删除的用户.';
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
                $scope.groupMessage = '';
                if(res.info) {
                    $scope.groupMessage = res.info;
                    return;
                }
                $scope.getTree(paramsCtlLevel);
                $scope.selectedNodes = [];
            }, function () {
                $scope.addedGroup = {};
                $scope.groupMessage = constant.loadError;
            });
        };

    };

    return {
        name: "GroupDeleteUserController",
        fn: ["$rootScope","$scope", "GroupService", "UserService", Controller]
    };

});
