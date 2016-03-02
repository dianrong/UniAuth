define(['../../utils/constant', '../../utils/utils'], function (constant, utils) {
    /**
     * A module representing a User controller.
     * @exports controllers/User
     */
    var Controller = function ($scope, GroupService) {
        $scope.tree = GroupService.tree;
    };

    return {
        name: "GroupController",
        fn: ["$scope", "GroupService", Controller]
    };

});
