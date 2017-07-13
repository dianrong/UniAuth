define(['../../utils/constant'], function (constant) {
    /**
     * A module representing a User controller.
     * @exports controllers/User
     */
    var Controller = function ($rootScope, $scope, $location, UserService, dialogs, $state, AlertService) {
        // always cache the query user input
        if($rootScope.userQuery) {
            $scope.userQuery = $rootScope.userQuery;
        }
        if($scope.userQuery) {
            $rootScope.userQuery = $scope.userQuery;
        } else {
            $scope.userQuery = {};
            $rootScope.userQuery = $scope.userQuery;
        }
        $scope.pagination = {
            pageSize: constant.pageSize,
            showPageNum: constant.showPageNum,
            curPage: 1,
            totalCount: 0
        };

        $scope.queryUser = function (curPage) {
            var params = $scope.userQuery;
            if (!params) {
                params = {};
            }
            params.pageNumber = curPage === undefined ? $scope.pagination.curPage - 1 : curPage;
            params.pageSize = $scope.pagination.pageSize;

            $scope.users = [];
            $scope.usersLoading = constant.loading;

            UserService.getUsers(params, function (res) {
                var result = res.data;
                if(res.info) {
                    $scope.usersLoading = constant.loadError;
                    return;
                }
                if(!result) {
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
        $scope.navigate = function(selectedUser) {
            UserService.userShared.selected = selectedUser;
            $state.go('rel.user--role');
        };
        
        // navigate to user - tag
        $scope.navToTag = function(selectedUser) {
            UserService.userShared.selected = selectedUser;
            $state.go('rel.user--tag');
        };
        
        $scope.launch = function(which, param) {
            switch(which) {
                case 'status':
                    var dlg = dialogs.create('views/common/dialogs/enable-disable.html','EnableDisableController',
                        {
                            "header":param.status?$rootScope.translate('userMgr.tips.userEnable'):$rootScope.translate('userMgr.tips.userDisable'),
                            "msg":$rootScope.translate('permMgr.tips.areUsure') + (param.status?$rootScope.translate('constant.enable'):$rootScope.translate('constant.disable')) + $rootScope.translate('userMgr.tips.user') + param.email + $rootScope.translate('permMgr.tips.ma')
                        }, {size:'md'}
                    );
                    dlg.result.then(function (yes) {
                        UserService.enableDisableUser(
                            {
                                'id':param.id,
                                'status':param.status?0:1
                            }
                            , function(res) {
                                if(res.info) {
                                    for(var i=0; i<res.info.length;i++) {
                                        AlertService.addAlert(constant.messageType.danger, res.info[i].msg);
                                    }
                                } else {
                                    AlertService.addAutoDismissAlert(constant.messageType.info, (param.status ? $rootScope.translate('userMgr.tips.enableUser') :$rootScope.translate('userMgr.tips.disableUser')) + $rootScope.translate('userMgr.tips.success'));
                                }
                                $scope.queryUser();
                            }, function(err) {
                                AlertService.addAutoDismissAlert(constant.messageType.danger, (param.status? $rootScope.translate('userMgr.tips.enableUser') :$rootScope.translate('userMgr.tips.disableUser')) + $rootScope.translate('userMgr.tips.failure'));
                                console.log(err);
                            }
                        );
                    }, function (no) {
                        // do nothing
                    });
                    break;
                case 'add':
                    var dlg = dialogs.create('views/user/dialogs/add.html','AddUserController',
                        {}, {size:'md'}
                    );
                    dlg.result.then(function (close) {
                        $scope.queryUser();
                    }, function (dismiss) {
                        //
                    });
                    break;
                case 'unlock':
                    var dlg = dialogs.create('views/common/dialogs/enable-disable.html','EnableDisableController',
                        {
                            "header":$rootScope.translate('userMgr.tips.unlockUser'),
                            "msg":$rootScope.translate('userMgr.tips.areUsureUnlock') + param.email + $rootScope.translate('permMgr.tips.ma')
                        }, {size:'md'}
                    );
                    dlg.result.then(function (yes) {
                        UserService.unlock(
                            {
                                'id':param.id
                            },
                            function(res) {
                                AlertService.addAutoDismissAlert(constant.messageType.info, $rootScope.translate('userMgr.tips.unlockUserSuccess'));
                            },
                            function(err) {
                                AlertService.addAutoDismissAlert(constant.messageType.info, $rootScope.translate('userMgr.tips.unlockUserFailure'));
                            }
                        );
                    }, function (no) {
                        // do nothing
                    });
                    break;
                case 'resetpwd':
                    var dlg = dialogs.create('views/user/dialogs/reset-password.html','ResetPasswordController',
                        param, {size:'md'}
                    );
                    dlg.result.then(function (close) {
                        //
                    }, function (dismiss) {
                        //
                    });
                    break;
                case 'modify':
                    var dlg = dialogs.create('views/user/dialogs/modify.html','ModifyUserController',
                        param, {size:'md'}
                    );
                    dlg.result.then(function (close) {
                        $scope.queryUser();
                    }, function (dismiss) {
                        //
                    });
                    break;
                case 'manage-eav':
                    var dlg = dialogs.create('views/user/dialogs/manage-eav.html','ManageEavController',
                        {}, {size:'lg', windowClass:'top-80px'}
                    );
                    dlg.result.then(function (close) {
                       //
                    }, function (dismiss) {
                        //
                    });
                    break;
                case 'user-eav':
                    var dlg = dialogs.create('views/user/dialogs/user-eav.html','UserEavController',
                        param, {size:'lg', windowClass:'top-80px'}
                    );
                    dlg.result.then(function (close) {
                       //
                    }, function (dismiss) {
                        //
                    });
                    break;
            }
        };
        
        $scope.$on('selected-language-changed', $scope.queryUser);

    };

    return {
        name: "UserController",
        fn: ["$rootScope", "$scope", "$location", "UserService", "dialogs", "$state", "AlertService", Controller]
    };

});
