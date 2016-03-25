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
                AlertService.addAutoDismissAlert(constant.messageType.warning, '请先选择一个组, 再删除组.');
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
                AlertService.addAutoDismissAlert(constant.messageType.info, "组删除成功.");
                $scope.getTree(paramsCtlLevel);
            }, function () {
                AlertService.addAutoDismissAlert(constant.messageType.danger, "组删除失败, 请联系系统管理员.");
            });
        };
    };

    return {
        name: "GroupDeleteController",
        fn: ["$rootScope", "$scope", "GroupService", "AlertService", Controller]
    };

});
