define(['../../utils/constant', '../../utils/utils'], function (constant, utils) {
    /**
     * A module representing a User controller.
     * @exports controllers/User
     */
    var Controller = function ($rootScope, $scope, $location, GroupService, dialogs) {
        $scope.getTree = function(userStatus) {
            var params = {};
            if(!userStatus) {
                params.onlyShowGroup = true;
            } else {
                params.onlyShowGroup = false;
                params.status = userStatus;
            }
            GroupService.getTree(params, function (res) {
                var result = res.data;
                if(res.info) {
                    $scope.treeLoading = constant.loadError;
                    return;
                }
                if(!result) {
                    $scope.treeLoading = constant.loadEmpty;
                    return;
                }

                $scope.treeLoading = '';
                $scope.tree = result.data;

            }, function () {
                $scope.tree = {};
                $scope.treeLoading = constant.loadError;
            });
        }
        $scope.tree = GroupService.tree;
        //$rootScope.groupSelected.name = "被选中的测试组.";
    };

    return {
        name: "GroupController",
        fn: ["$rootScope", "$scope", "$location", "GroupService", "dialogs", Controller]
    };

});
