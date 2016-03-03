define(['../../utils/constant'],function(constant) {
    /**
     * A module representing a User controller.
     * @exports controllers/User
     */
    var Controller = function ($rootScope, $scope, GroupService) {
        var paramsCtlLevel = {};
        paramsCtlLevel.onlyShowGroup = true;
        //paramsCtlLevel.userGroupType = 0;
        $scope.getTree = GroupService.syncTree;
        $scope.getTree(paramsCtlLevel);

        $scope.addGroup = function () {

            var params = $scope.grp;
            if(!$rootScope.shareGroup.selected || !$rootScope.shareGroup.selected.id){
                $scope.groupMessage = '请先选择一个父组, 再添加组.';
                return;
            }
            params.targetGroupId=$rootScope.shareGroup.selected.id;

            GroupService.add(params, function (res) {
                $scope.groupMessage = '';
                var result = res.data;
                if(res.info) {
                    $scope.groupMessage = res.info;
                    return;
                }
                if(!result) {
                    $scope.groupMessage = constant.loadEmpty;
                    return;
                }
                $scope.addedGroup = result.data;
                $scope.getTree(paramsCtlLevel);
            }, function () {
                $scope.addedGroup = {};
                $scope.groupMessage = constant.loadError;
            });
        };

    };

    return {
        name: "GroupAddController",
        fn: ["$rootScope","$scope", "GroupService", Controller]
    };

});
