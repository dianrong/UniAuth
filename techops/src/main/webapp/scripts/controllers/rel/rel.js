define(['../../utils/constant'], function (constant) {
    /**
     * A module representing a User controller.
     * @exports controllers/User
     */
    var Controller = function ($scope, PermService, RoleService, UserService, GroupService, TagService) {
        $scope.$on('selected-domain-changed', function(){
            PermService.permShared.selected = undefined;
            RoleService.roleShared.selected = undefined;
            RoleService.roleUserGrpShared.selected = undefined;
            UserService.userShared.selected = undefined;
            GroupService.grpShared.selected = undefined;
            TagService.tagShared.selected = undefined;
        });
    };

    return {
        name: "RelController",
        fn: ["$scope", "PermService", "RoleService", "UserService", "GroupService", "TagService", Controller]
    };

});
