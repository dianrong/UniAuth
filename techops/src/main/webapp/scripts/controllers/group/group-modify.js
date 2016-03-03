define(function() {
    /**
     * A module representing a User controller.
     * @exports controllers/User
     */
    var Controller = function ($scope, GroupService) {
        var params = {};
        params.onlyShowGroup = true;
        $scope.getTree = GroupService.syncTree;
        $scope.getTree(params);
    };

    return {
        name: "GroupController",
        fn: ["$scope", "GroupService", Controller]
    };

});
