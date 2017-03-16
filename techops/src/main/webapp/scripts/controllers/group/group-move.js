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
            if(!$scope.moveGroup.from){
                AlertService.addAutoDismissAlert(constant.messageType.warning, "请选择当前组");
                return;
            }

            if(!$scope.moveGroup.to){
                AlertService.addAutoDismissAlert(constant.messageType.warning, "请选择目标组");
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
                AlertService.addAutoDismissAlert(constant.messageType.info, "移动成功");
                //clear movegroup
                // $scope.moveGroup = {};
                //sync the tree
                $scope.getTree(paramsCtlLevel);
            }, function () {
                AlertService.addAutoDismissAlert(constant.messageType.danger, "移动失败");
            });
        };

    };

    return {
        name: "GroupMoveController",
        fn: ["$rootScope", "$scope", "GroupService", "AlertService", Controller]
    };

});
