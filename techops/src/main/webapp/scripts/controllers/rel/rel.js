define(['../../utils/constant'], function (constant) {
    /**
     * A module representing a User controller.
     * @exports controllers/User
     */
    var Controller = function ($scope, PermService, RoleService) {
        $scope.$on('selected-domain-changed', function(){
            PermService.permShared.selected = undefined;
            RoleService.roleShared.selected = undefined;
        });
    };

    return {
        name: "RelController",
        fn: ["$scope", "PermService", "RoleService", Controller]
    };

});
