define(['../../utils/constant'], function (constant) {
    /**
     * A module representing a User controller.
     * @exports controllers/User
     */
    var Controller = function ($scope, GroupService) {
        var params = {};
        params.onlyShowGroup = false;
        params.userGroupType = 0;
        $scope.getTree = GroupService.syncTree;
        $scope.getTree(params);
    };

    return {
        name: "GroupAddController",
        fn: ["$scope", "GroupService", Controller]
    };

});
