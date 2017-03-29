define(['../../utils/constant'], function(constant) {
    /**
     * A module representing a User controller.
     * @exports controllers/User
     */
    var Controller = function ($rootScope, $scope, GroupService, AlertService) {
        $scope.confirm = function () {

            if(!$rootScope.targetGroup || !$rootScope.targetGroup.id || !$rootScope.targetGroup.code){
                AlertService.addAutoDismissAlert(constant.messageType.warning, $rootScope.translate('groupMgr.tips.selectGroupUdel'));
                return;
            }
            GroupService.del({
                "id": $rootScope.targetGroup.id,
                "code": $rootScope.targetGroup.code,
                "status": 1,
                "targetGroupId": $rootScope.targetGroup.id
            }, function (res) {
                if(res.info) {
                    for(var i=0; i<res.info.length;i++) {
                        AlertService.addAlert(constant.messageType.danger, res.info[i].msg);
                    }
                    return;
                }
                AlertService.addAutoDismissAlert(constant.messageType.info, $rootScope.translate('groupMgr.tips.groupDelSuccess'));
                //reset the tree component
                $rootScope.reset();
            }, function () {
                AlertService.addAutoDismissAlert(constant.messageType.danger, $rootScope.translate('groupMgr.tips.groupDelFailure'));
            });
        };
    };

    return {
        name: "GroupDeleteController",
        fn: ["$rootScope", "$scope", "GroupService", "AlertService", Controller]
    };

});
