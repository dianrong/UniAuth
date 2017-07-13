define(['../../utils/constant', '../../utils/utils'], function (constant, utils) {

    var Controller = function ($rootScope, $scope, RoleService, dialogs, $state, AlertService) {

        function bindAllRoleCodesRoleToRoot() {
            RoleService.getAllRoleCodes().$promise.then(function(res) {
                var roleCodes = res.data;
                var empty = {
                    code : $rootScope.translate('constant.selectplacehodler')
                };
                roleCodes.unshift(empty);
                utils.generatorDropdown($scope, 'roleCodesDropdown', roleCodes, empty);
                $rootScope.roleCodesDropdown = $scope.roleCodesDropdown;
            });
        }

        // bind the history search param to root
        if(!$rootScope.roleCodesDropdown) {
            bindAllRoleCodesRoleToRoot();
        }

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
            showPageNum: constant.showPageNum,
            curPage: 1,
            totalCount: 0
        };

        $scope.queryRole = function (curPage) {
            if(!$rootScope.loginDomainsDropdown || !$rootScope.loginDomainsDropdown.option || !$rootScope.loginDomainsDropdown.option.id) {
                $scope.rolesLoading = constant.loadEmpty;
                return;
            }
            var params = $scope.roleQuery;
            if (!params) {
                params = {};
            }
            params.pageNumber = curPage === undefined ? $scope.pagination.curPage - 1 : curPage;
            params.pageSize = $scope.pagination.pageSize;

            $scope.roles = [];
            $scope.rolesLoading = constant.loading;

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

        $scope.navToRoleUser = function(role) {
            RoleService.roleUserGrpShared.selected = role;
            $state.go('rel.role--user');
        };
        $scope.navToRoleGroup = function(role) {
            RoleService.roleUserGrpShared.selected = role;
            $state.go('rel.role--grp');
        };
        $scope.navToRolePerm = function(role) {
            RoleService.roleShared.selected = role;
            $state.go('rel.role--perm');
        };

        $scope.launch = function(which, param) {
            switch(which) {
                case 'status':
                    var dlg = dialogs.create('views/common/dialogs/enable-disable.html','EnableDisableController',
                        {
                            "header":param.status?$rootScope.translate('roleMgr.tips.roleEnable'):$rootScope.translate('roleMgr.tips.roleDisable'),
                            "msg":$rootScope.translate('permMgr.tips.areUsure') + (param.status?$rootScope.translate('userMgr.enable'):$rootScope.translate('userMgr.disable')) + $rootScope.translate('roleMgr.tips.role') + param.name + $rootScope.translate('permMgr.tips.ma')
                        }, {size:'md'}
                    );
                    dlg.result.then(function (yes) {
                        RoleService.updateRole(
                            {
                                'id':param.id,
                                'name': param.name,
                                'description': param.description,
                                'roleCodeId':param.roleCodeId,
                                'status':param.status?0:1
                            }
                            , function(res) {
                                // status change successed
                                if(res.info) {
                                    for(var i=0; i<res.info.length;i++) {
                                        AlertService.addAlert(constant.messageType.danger, res.info[i].msg);
                                    }
                                } else {
                                    AlertService.addAutoDismissAlert(constant.messageType.info, (param.status ? $rootScope.translate('userMgr.enable'):$rootScope.translate('userMgr.disable')) + $rootScope.translate('roleMgr.tips.roleSuccess'));
                                }
                                $scope.queryRole();
                            }, function(err) {
                                console.log(err);
                            }
                        );
                    }, function (no) {
                        // do nothing
                    });
                    break;
                case 'modify':
                    var dlg = dialogs.create('views/role/dialogs/modify.html','ModifyRoleController',
                        param, {size:'md'}
                    );
                    dlg.result.then(function (close) {
                        $scope.queryRole();
                    }, function (dismiss) {
                        //
                    });
                    break;
                case 'add':
                    var dlg = dialogs.create('views/role/dialogs/add.html','AddRoleController',
                        {}, {size:'md'}
                    );
                    dlg.result.then(function (close) {
                        $scope.queryRole();
                    }, function (dismiss) {
                        //
                    });
                    break;
            }
        };
        $scope.$on('selected-domain-changed', $scope.queryRole);
        $scope.$on('selected-language-changed', $scope.queryRole);
        
    };

    return {
        name: "RoleController",
        fn: ["$rootScope", "$scope", "RoleService", "dialogs", "$state", "AlertService", Controller]
    };

});
