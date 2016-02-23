define(['../../utils/constant', '../../utils/utils'], function (constant, utils) {
    /**
     * A module representing a User controller.
     * @exports controllers/User
     */
    var Controller = function ($rootScope, $scope, $location, GroupService, dialogs) {
        $rootScope.groupSelected.name = "被选中的测试组.";
    };

    return {
        name: "GroupController",
        fn: ["$rootScope", "$scope", "$location", "GroupService", "dialogs", Controller]
    };

});
