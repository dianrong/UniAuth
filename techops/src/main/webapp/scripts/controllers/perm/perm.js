define(['../../utils/constant', '../../utils/utils'], function (constant, utils) {

    var Controller = function ($rootScope, $scope, $location, PermService, dialogs, $state, AlertService) {

        function getAllPermTypes() {
            PermService.getAllPermTypes().$promise.then(function (res) {
                var permTypes = res.data;
                var empty = {
                    type: $rootScope.translate('constant.selectplacehodler')
                };
                permTypes.unshift(empty);
                utils.generatorDropdown($scope, 'permTypesDropdown', permTypes, empty);
                $rootScope.permTypesDropdown = $scope.permTypesDropdown;
            });
        }

        // bind the history search param to root
        if(!$rootScope.permTypesDropdown) {
            getAllPermTypes();
        }

        // always cache the query role input
        if($rootScope.permQuery) {
            $scope.permQuery = $rootScope.permQuery;
        }
        if($scope.permQuery) {
            $rootScope.permQuery = $scope.permQuery;
        } else {
            $scope.permQuery = {};
            $rootScope.permQuery = $scope.permQuery;
        }
        $scope.pagination = {
            pageSize: constant.pageSize,
            showPageNum: constant.showPageNum,
            curPage: 1,
            totalCount: 0
        };

        $scope.queryPerm = function (curPage) {
            if(!$rootScope.loginDomainsDropdown || !$rootScope.loginDomainsDropdown.option || !$rootScope.loginDomainsDropdown.option.id) {
                $scope.permsLoading = constant.loadEmpty;
                return;
            }
            var params = $scope.permQuery;
            if (!params) {
                params = {};
            }
            params.pageNumber = curPage === undefined ? $scope.pagination.curPage - 1:curPage;
            params.pageSize = $scope.pagination.pageSize;

            $scope.perms = [];
            $scope.permsLoading = constant.loading;
            params.domainId = $rootScope.loginDomainsDropdown.option.id;
            if($rootScope.permTypesDropdown && $rootScope.permTypesDropdown.option && $rootScope.permTypesDropdown.option.id) {
                params.permTypeId = $rootScope.permTypesDropdown.option.id;
            } else {
                params.permTypeId = undefined;
            }
            PermService.getPerms(params, function (res) {
                var result = res.data;
                if(res.info) {
                    $scope.permsLoading = constant.loadError;
                    return;
                }
                if(!result || !result.data || !result.data.length) {
                    $scope.permsLoading = constant.loadEmpty;
                    return;
                }

                $scope.permsLoading = '';
                $scope.perms = result.data;

                $scope.pagination.curPage = result.currentPage + 1;
                $scope.pagination.totalCount = result.totalCount;
                $scope.pagination.pageSize = result.pageSize;

            }, function () {
                $scope.perms = [];
                $scope.permsLoading = constant.loadError;
            });
        };

        $scope.queryPerm();
        $scope.navToPermRole = function(perm) {
            PermService.permShared.selected = perm;
            $state.go('rel.perm--role');
        };
        $scope.launch = function(which, param) {
            switch(which) {
                case 'status':
                    var dlg = dialogs.create('views/common/dialogs/enable-disable.html','EnableDisableController',
                        {
                            "header":param.status?$rootScope.translate('permMgr.tips.permEnable'):$rootScope.translate('permMgr.tips.permDisable'),
                            "msg":$rootScope.translate('permMgr.tips.areUsure') + (param.status?$rootScope.translate('userMgr.enable'):$rootScope.translate('userMgr.disable')) + $rootScope.translate('permMgr.tips.perm')+ param.value + $rootScope.translate('permMgr.tips.ma')
                        }, {size:'md'}
                    );
                    dlg.result.then(function (yes) {
                        PermService.updatePerm(
                            {
                                'id':param.id,
                                'value': param.value,
                                'description': param.description,
                                'permTypeId':param.permTypeId,
                                'domainId':param.domainId,
                                'valueExt':param.valueExt,
                                'status':param.status?0:1
                            }
                            , function(res) {
                                // status change successed
                                if(res.info) {
                                    for(var i=0; i<res.info.length;i++) {
                                        AlertService.addAlert(constant.messageType.danger, res.info[i].msg);
                                    }
                                } else {
                                    AlertService.addAutoDismissAlert(constant.messageType.info, (param.status ?$rootScope.translate('userMgr.enable'):$rootScope.translate('userMgr.disable')) + $rootScope.translate('permMgr.tips.permSuccess'));
                                }
                                $scope.queryPerm();
                            }, function(err) {
                                console.log(err);
                            }
                        );
                    }, function (no) {
                        // do nothing
                    });
                    break;
                case 'modify':
                    var dlg = dialogs.create('views/perm/dialogs/modify.html','ModifyPermController',
                        param, {size:'md'}
                    );
                    dlg.result.then(function (close) {
                        $scope.queryPerm();
                    }, function (dismiss) {
                        //
                    });
                    break;
                case 'add':
                    var dlg = dialogs.create('views/perm/dialogs/add.html','AddPermController',
                        {}, {size:'md'}
                    );
                    dlg.result.then(function (close) {
                        $scope.queryPerm();
                    }, function (dismiss) {
                        //
                    });
                    break;
            }
        };
        $scope.$on('selected-domain-changed', $scope.queryPerm);
        $scope.$on('selected-language-changed', $scope.queryPerm);
    };

    return {
        name: "PermController",
        fn: ["$rootScope", "$scope", "$location", "PermService", "dialogs", "$state", "AlertService", Controller]
    };

});
