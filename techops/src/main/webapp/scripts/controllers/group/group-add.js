define(['../../utils/constant'],function(constant) {
    /**
     * A module representing a User controller.
     * @exports controllers/User
     */
    var Controller = function ($rootScope, $scope, GroupService, AlertService) {
        var paramsCtlLevel = {};
        paramsCtlLevel.onlyShowGroup = true;
        //paramsCtlLevel.userGroupType = 0;
        $scope.getTree = GroupService.syncTree;
        $scope.getTree(paramsCtlLevel);

        $scope.addGroup = function () {

            var params = $scope.grp;
            if(!$rootScope.shareGroup.selected || !$rootScope.shareGroup.selected.id){
                AlertService.addAutoDismissAlert(constant.messageType.info, '请先选择一个父组, 再添加组.');
                return;
            }
            params.targetGroupId=$rootScope.shareGroup.selected.id;

            GroupService.add(params, function (res) {
                var result = res.data;
                if(res.info) {
                    for(var i=0; i<res.info.length;i++) {
                        AlertService.addAlert(constant.messageType.danger, res.info[i].msg);
                    }
                    return;
                }
                $scope.addedGroup = result.data;
                AlertService.addAutoDismissAlert(constant.messageType.info, "组添加成功.");
                $scope.getTree(paramsCtlLevel);
            }, function () {
                $scope.addedGroup = {};
                AlertService.addAutoDismissAlert(constant.messageType.danger, "组添加失败.");
            });
        };

    };

    return {
        name: "GroupAddController",
        fn: ["$rootScope","$scope", "GroupService", "AlertService", Controller]
    };

});
