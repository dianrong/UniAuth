define(['../../utils/constant'], function(constant) {
    /**
     * A module representing a User controller.
     * @exports controllers/User
     */
    var Controller = function ($rootScope, $scope, GroupService) {
        var paramsCtlLevel = {};
        paramsCtlLevel.onlyShowGroup = true;
        $scope.getTree = GroupService.syncTree;
        $scope.getTree(paramsCtlLevel);

        $scope.confirm = function () {

            if(!$rootScope.shareGroup.selected || !$rootScope.shareGroup.selected.id || !$rootScope.shareGroup.selected.code){
                $scope.groupMessage = '请先选择一个组, 再删除组.';
                return;
            }
            $scope.groupMessage = constant.submiting;
            GroupService.modify({
                "id": $rootScope.shareGroup.selected.id,
                "code": $rootScope.shareGroup.selected.code,
                "status": 1
            }, function (res) {
                $scope.groupMessage = '删除成功';
                if(res.info) {
                    $scope.groupMessage = res.info;
                    return;
                }
                $rootScope.shareGroup.selected = {};
                $scope.getTree(paramsCtlLevel);
            }, function () {
                $scope.groupMessage = constant.loadError;
            });
        };
    };

    return {
        name: "GroupDeleteController",
        fn: ["$rootScope", "$scope", "GroupService", Controller]
    };

});
