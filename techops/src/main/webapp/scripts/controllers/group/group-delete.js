define(['../../utils/constant'], function(constant) {
    /**
     * A module representing a User controller.
     * @exports controllers/User
     */
    var Controller = function ($rootScope, $scope, GroupService, AlertService) {
        var paramsCtlLevel = {};
        paramsCtlLevel.onlyShowGroup = true;
        $scope.getTree = GroupService.syncTree;
        $scope.getTree(paramsCtlLevel);

        $scope.confirm = function () {

            if(!$rootScope.shareGroup.selected || !$rootScope.shareGroup.selected.id || !$rootScope.shareGroup.selected.code){
                AlertService.addAutoDismissAlert(constant.messageType.warning, $rootScope.translate('groupMgr.tips.selectGroupUdel'));
                return;
            }
            GroupService.modify({
                "id": $rootScope.shareGroup.selected.id,
                "code": $rootScope.shareGroup.selected.code,
                "status": 1,
                "targetGroupId": $rootScope.shareGroup.selected.id
            }, function (res) {
                if(res.info) {
                    for(var i=0; i<res.info.length;i++) {
                        AlertService.addAlert(constant.messageType.danger, res.info[i].msg);
                    }
                    return;
                }
                $rootScope.shareGroup.selected = {};
                AlertService.addAutoDismissAlert(constant.messageType.info, $rootScope.translate('groupMgr.tips.groupDelSuccess'));
                $scope.getTree(paramsCtlLevel);
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
