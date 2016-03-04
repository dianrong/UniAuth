define(function() {
    /**
     * A module representing a User controller.
     * @exports controllers/User
     */
    var Controller = function ($rootScope, $scope, GroupService) {
        $scope.grp = $rootScope.shareGroup;

        var paramsCtlLevel = {};
        paramsCtlLevel.onlyShowGroup = true;
        //paramsCtlLevel.userGroupType = 1;
        $scope.getTree = GroupService.syncTree;
        $scope.getTree(paramsCtlLevel);

        $scope.modifyGroup = function () {

            if(!$rootScope.shareGroup.selected || !$rootScope.shareGroup.selected.id){
                $scope.groupMessage = '请先选择一个组, 再修改组.';
                return;
            }

            GroupService.modify({
                    "id": $scope.grp.selected.id,
                    "name": $scope.grp.selected.label,
                    "code": $scope.grp.selected.code,
                    "description": $scope.grp.selected.description
                }, function (res) {
                $scope.groupMessage = '';
                var result = res.data;
                if(res.info) {
                    $scope.groupMessage = res.info;
                    return;
                }
                $scope.modifiedGroup = result.data;
                //sync the selected group
                GroupService.getGrpDetails({
                    id: $rootScope.shareGroup.selected.id
                }, function (result) {
                    $scope.selected = result.data;
                    $rootScope.shareGroup.selected = $scope.selected;
                }, function (err) {e
                    console.log(err);
                });
                //sync the tree
                $scope.getTree(paramsCtlLevel);
            }, function () {
                $scope.modifidGroup = {};
                $scope.groupMessage = constant.loadError;
            });
            $scope.getTree(paramsCtlLevel);
        };
    };

    return {
        name: "GroupModifyController",
        fn: ["$rootScope", "$scope", "GroupService", Controller]
    };

});
