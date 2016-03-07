define(['../../utils/constant', '../../utils/utils'], function (constant, utils) {

    var Controller = function ($rootScope, $scope, $location, RoleService, dialogs) {

        // always cache the query role input
        if($rootScope.roleQuery) {
            $scope.roleQuery = $rootScope.roleQuery;
        }
        if($scope.roleQuery) {
            $rootScope.roleQuery = $scope.roleQuery;
        } else {
            $scope.roleQuery = {};
            $rootScope.roleQuery = $scope.roleQuery;
        }
        $scope.pagination = {
            pageSize: constant.pageSize,
            curPage: 1,
            totalCount: 0
        };

        $scope.queryRole = function () {

            var params = $scope.roleQuery;
            if (!params) {
                params = {};
            }
            params.pageNumber = $scope.pagination.curPage - 1;
            params.pageSize = $scope.pagination.pageSize;

            $scope.roles = [];
            $scope.rolesLoading = constant.loading;
            if(!$rootScope.loginDomainsDropdown || !$rootScope.loginDomainsDropdown.option || !$rootScope.loginDomainsDropdown.option.id) {
                $location.url('/non-authorized');
            }
            params.domainId = $rootScope.loginDomainsDropdown.option.id;
            if($rootScope.roleCodesDropdown && $rootScope.roleCodesDropdown.option && $rootScope.roleCodesDropdown.option.id) {
                params.roleCodeId = $rootScope.roleCodesDropdown.option.id;
            } else {
                params.roleCodeId = undefined;
            }
            RoleService.getRoles(params, function (res) {
                var result = res.data;
                if(res.info) {
                    $scope.rolesLoading = constant.loadError;
                    return;
                }
                if(!result) {
                    $scope.rolesLoading = constant.loadEmpty;
                    return;
                }

                $scope.rolesLoading = '';
                $scope.roles = result.data;

                $scope.pagination.curPage = result.currentPage + 1;
                $scope.pagination.totalCount = result.totalCount;
                $scope.pagination.pageSize = result.pageSize;

            }, function () {
                $scope.roles = [];
                $scope.rolesLoading = constant.loadError;
            });
        };

        $scope.queryRole();

        $scope.launch = function(which, param) {
            switch(which) {
                case 'status':
                    var dlg = dialogs.create('views/common/dialogs/enable-disable.html','EnableDisableController',
                        {
                            "header":param.status?'角色-启用':'角色-禁用',
                            "msg":"您确定要" + (param.status?'启用':'禁用') + "角色: " + param.name + "吗?"
                        }, {size:'md'}
                    );
                    dlg.result.then(function (yes) {
                        RoleService.enableDisableUser(
                            {
                                'id':param.id,
                                'status':param.status?0:1
                            }
                            , function(res) {
                                // status change successed
                                $scope.queryUser();
                            }, function(err) {
                                console.log(err);
                            }
                        );
                    }, function (no) {
                        // do nothing
                    });
                    break;
            }
        };

    };

    return {
        name: "RoleController",
        fn: ["$rootScope", "$scope", "$location", "RoleService", "dialogs", Controller]
    };

});
