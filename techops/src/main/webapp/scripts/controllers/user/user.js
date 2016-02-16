/**
 * Module representing a shirt.
 * @module controllers/User
 */
define(['../../utils/constant'], function (Constant) {
    /**
     * A module representing a User controller.
     * @exports controllers/User
     */
    var Controller = function ($scope, $location, UserService, CommonService, localStorageService) {
        $scope.pagination = {
            pageSize: Constant.pageSize,
            curPage: 1,
            totalCount: 0
        };

        //Event listeners
        $scope.queryUser = function () {
            var params = $scope.userQuery;

            var count = 0,
                total = 0;
            for (var prop in params) {
                total++;
                if (params[prop] === null || typeof params[prop] === 'undefined') {
                    count++;
                }
            }

            if (count === total) {
                //return false;
            }
            if (!params) {
                params = {};
            }
            params.page = $scope.pagination.curPage;
            params.pageSize = $scope.pagination.pageSize;

            $scope.users = [];
            $scope.usersLoading = Constant.loading;

            UserService.getUsers(params, function (res) {

                var result = Constant.transformResponse(res);
                if (result === undefined) {
                    $scope.usersLoading = Constant.loadError;
                    return;
                }
                if (!result || !result.data || !result.data.length) {
                    $scope.usersLoading = Constant.loadEmpty;
                    return;
                }

                $scope.usersLoading = '';
                $scope.users = result.data;

                $scope.pagination.curPage = res.result.page;
                $scope.pagination.totalCount = res.result.totalCount;
                $scope.pagination.pageSize = res.result.pageSize;

            }, function () {
                $scope.users = [];
                $scope.usersLoading = Constant.loadError;
            });
        };

        $scope.queryUser();

        $scope.storeUserDetail = function (detail) {
            localStorageService.set('userDetail', detail);
        };

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
        $scope.disableDropdownList = Constant.disableDropdownList;

        $scope.roleTypeDropdownList = Constant.roleTypesArr;

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

        $scope.addNewUser = function () {
            if (typeof $scope.newUser === 'undefined') {
                return false;
            }

            for (var prop in $scope.newUser) {
                if (typeof $scope.newUser[prop] === 'undefined' || $scope.newUser[prop] === null) {
                    return false;
                }
            }
            $scope.addNewUserErrorMsg = '';
            UserService.addUser($scope.newUser, function (res) {
                var result = Constant.transformResponse(res);
                if (result === undefined) {
                    $scope.addNewUserErrorMsg = res.errMsg ? res.errMsg : '添加用户失败，请稍后再试';
                    return;
                }
                //$scope.addNewUserSuccess = true;
                $location.path('/user/' + res.result);
            }, function (errorResp) {
                $scope.addNewUserErrorMsg = errorResp.errMsg ? errorResp.errMsg : '添加用户失败，请稍后再试';
            });
        };
    };

    return {
        name: "UserController",
        fn: ["$scope", "$location", "UserService", "CommonService", "localStorageService", Controller]
    };


});
