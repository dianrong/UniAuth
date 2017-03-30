define(['../../utils/constant'], function(constant) {
    /**
     * A module representing a User controller.
     * @exports controllers/group.move
     */
    var Controller = function ($rootScope, $scope, GroupService, AlertService) {

        $scope.move = function () {
            if(!$rootScope.moveGroup.from.id){
                AlertService.addAutoDismissAlert(constant.messageType.warning, $rootScope.translate('groupMgr.tips.selectCurrentGroup'));
                return;
            }

            if(!$rootScope.moveGroup.to.id){
                AlertService.addAutoDismissAlert(constant.messageType.warning, $rootScope.translate('groupMgr.tips.selectTargetGroup'));
                return;
            }
            GroupService.move({
                "id": $rootScope.moveGroup.from.id,
                "targetGroupId": $rootScope.moveGroup.to.id
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
                $rootScope.moveGroup.from = {};
                $rootScope.moveGroup.to = {};
                //cancel the onMove status before move successfully
                $rootScope.onMove = false;
                //reset the tree component
                $rootScope.reset();
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
