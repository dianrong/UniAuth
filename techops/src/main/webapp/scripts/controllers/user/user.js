define(['../../utils/constant', '../../utils/utils'], function (constant, utils) {
    /**
     * A module representing a User controller.
     * @exports controllers/User
     */
    var Controller = function ($rootScope, $scope, $location, UserService, dialogs, CommonService) {
        $scope.pagination = {
            pageSize: constant.pageSize,
            curPage: 1,
            totalCount: 0
        };

        //Event listeners
        $scope.queryUser = function () {

            var params = $scope.userQuery;

            if (!params) {
                params = {};
            }
            params.pageNumber = $scope.pagination.curPage - 1;
            params.pageSize = $scope.pagination.pageSize;

            $scope.users = [];
            $scope.usersLoading = constant.loading;

            UserService.getUsers(params, function (res) {
                var result = res.data;
                if (result === undefined || result.info) {
                    $scope.usersLoading = constant.loadError;
                    return;
                }
                if (!result || !result.data || !result.data.length) {
                    $scope.usersLoading = constant.loadEmpty;
                    return;
                }

                $scope.usersLoading = '';
                $scope.users = result.data;

                $scope.pagination.curPage = result.currentPage + 1;
                $scope.pagination.totalCount = result.totalCount;
                $scope.pagination.pageSize = result.pageSize;

            }, function () {
                $scope.users = [];
                $scope.usersLoading = constant.loadError;
            });
        };

        $scope.queryUser();

        $scope.launch = function(which, param) {
            switch(which) {
                case 'status':
                    var dlg = dialogs.create('views/user/dialogs/enable-disable.html','EnableDisableController', param, {size:'lg'});
                    dlg.result.then(function (btn) {
                        $scope.queryUser();
                    }, function (btn) {
                        // nothing happen
                    });
                    break;
            }
        };

    };

    return {
        name: "UserController",
        fn: ["$rootScope", "$scope", "$location", "UserService", "dialogs", "CommonService", Controller]
    };

});
