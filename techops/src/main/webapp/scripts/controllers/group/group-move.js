define(['../../utils/constant'], function(constant) {
    /**
     * A module representing a User controller.
     * @exports controllers/group.move
     */
    var Controller = function ($rootScope, $scope, GroupService, AlertService) {
        $rootScope.moveFLag = true;
        $scope.moveGroup = $rootScope.shareGroup;

        var paramsCtlLevel = {};
        paramsCtlLevel.onlyShowGroup = true;
        //paramsCtlLevel.userGroupType = 1;
        $scope.getTree = GroupService.syncTree;
        $scope.getTree(paramsCtlLevel);

        $scope.move = function () {
            if(!$scope.moveGroup.from.id){
                AlertService.addAutoDismissAlert(constant.messageType.warning, $rootScope.translate('groupMgr.tips.selectCurrentGroup'));
                return;
            }

            if(!$scope.moveGroup.to.id){
                AlertService.addAutoDismissAlert(constant.messageType.warning, $rootScope.translate('groupMgr.tips.selectTargetGroup'));
                return;
            }
            GroupService.move({
                "id": $scope.moveGroup.from.id,
                "targetGroupId": $scope.moveGroup.to.id
            }, function (res) {
                var result = res.data;
                if(res.info) {
                    for(var i=0; i<res.info.length;i++) {
                        AlertService.addAlert(constant.messageType.danger, res.info[i].msg);
                    }
                    return;
                }
                AlertService.addAutoDismissAlert(constant.messageType.info, $rootScope.translate('groupMgr.tips.groupMoveSuccess'));
                //reset
                $scope.moveGroup.from = {};
                $scope.moveGroup.to = {};
                $rootScope.moveFLag = true;
                //sync the tree
                $scope.getTree(paramsCtlLevel);
            }, function () {
                AlertService.addAutoDismissAlert(constant.messageType.danger, $rootScope.translate('groupMgr.tips.groupMoveFailure'));
            });
        };

    };

    return {
        name: "GroupMoveController",
        fn: ["$rootScope", "$scope", "GroupService", "AlertService", Controller]
    };

});
