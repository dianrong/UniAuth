/**
 * Module representing a shirt.
 * @module controllers/User
 */
define(['../../utils/constant', '../../utils/utils'], function (constant, utils) {
    /**
     * A module representing a User controller.
     * @exports controllers/User
     */
    var Controller = function ($rootScope, $scope, $location, UserService, CommonService, localStorageService) {
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
                debugger;
                var result = res.data;
                if (result === undefined) {
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

        //$scope.storeUserDetail = function (detail) {
        //    localStorageService.set('userDetail', detail);
        //};

        /*$scope.getUsersByPage = function () {
         UserService.getUsers({
         page: $scope.pagination.curPage,
         pageSize: $scope.pagination.pageSize
         }, function (resp) {
         $scope.users = resp.result.data;
         $scope.pagination.curPage = resp.result.page;
         $scope.pagination.totalCount = resp.result.totalCount;
         });

         };*/

        // dropdown list
        //$scope.disableDropdownList = constant.disableDropdownList;
        //
        //$scope.roleTypeDropdownList = constant.roleTypesArr;

        //$scope.getGroups = function () {
        //    GroupSvc.getGroups({
        //        page: 1,
        //        pageSize: Constant.hackMaxPageSize // HACK
        //    }, function (res) {
        //        if (!res || !res.result || !res.result.data) {
        //            $scope.groupList = [];
        //        } else {
        //            $scope.groupList = res.result.data;
        //        }
        //        $scope.groupList.unshift({
        //            name: '请选择',
        //            value: null
        //        });
        //    });
        //};
        //$scope.getGroups();

        //$scope.addNewUser = function () {
        //    if (typeof $scope.newUser === 'undefined') {
        //        return false;
        //    }
        //
        //    for (var prop in $scope.newUser) {
        //        if (typeof $scope.newUser[prop] === 'undefined' || $scope.newUser[prop] === null) {
        //            return false;
        //        }
        //    }
        //    $scope.addNewUserErrorMsg = '';
        //    UserService.addUser($scope.newUser, function (res) {
        //        var result = constant.transformResponse(res);
        //        if (result === undefined) {
        //            $scope.addNewUserErrorMsg = res.errMsg ? res.errMsg : '添加用户失败，请稍后再试';
        //            return;
        //        }
        //        //$scope.addNewUserSuccess = true;
        //        $location.path('/user/' + res.result);
        //    }, function (errorResp) {
        //        $scope.addNewUserErrorMsg = errorResp.errMsg ? errorResp.errMsg : '添加用户失败，请稍后再试';
        //    });
        //};
    };

    return {
        name: "UserController",
        fn: ["$rootScope", "$scope", "$location", "UserService", "CommonService", "localStorageService", Controller]
    };


});
