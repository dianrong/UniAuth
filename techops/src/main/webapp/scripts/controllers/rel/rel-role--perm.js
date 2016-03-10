define(['../../utils/constant'], function (constant) {
    /**
     * A module representing a User controller.
     * @exports controllers/User
     */
    var Controller = function ($scope, $rootScope, $location,PermService) {
        $scope.getAllPermsInDomain = function () {
            var params = {}
            params.pageNumber = 0;
            params.pageSize = constant.maxPageSize;
            if(!$rootScope.loginDomainsDropdown || !$rootScope.loginDomainsDropdown.option || !$rootScope.loginDomainsDropdown.option.id) {
                $location.url('/non-authorized');
            }
            params.domainId = $rootScope.loginDomainsDropdown.option.id;

            PermService.getPerms(params, function (res) {
                var result = res.data;
                if(res.info) {
                    $scope.rolePermsLoading = constant.loadError;
                    return;
                }
                if(!result) {
                    $scope.rolePermsLoading = constant.loadEmpty;
                    return;
                }

                $scope.rolePermsLoading = '';
                $scope.perms = result.data;
            }, function () {
                $scope.perms = [];
                $scope.rolePermsLoading = constant.loadError;
            });
        };
        $scope.getAllPermsInDomain();
    };

    return {
        name: "RelRolePermController",
        fn: ["$scope", "$rootScope", "$location", "PermService", Controller]
    };

});
