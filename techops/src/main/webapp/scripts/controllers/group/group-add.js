define(['../../utils/constant'], function (constant) {
    /**
     * A module representing a User controller.
     * @exports controllers/User
     */
    var Controller = function ($scope, GroupService) {
        $scope.getTree = function() {
            var params = {};
            params.onlyShowGroup = false;
            params.userGroupType = 0;
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
                GroupService.tree.data = result;
            }, function () {
                GroupService.tree.data = {};
                $scope.treeLoading = constant.loadError;
            });
        }
        $scope.getTree();
    };

    return {
        name: "GroupAddController",
        fn: ["$scope", "GroupService", Controller]
    };

});
