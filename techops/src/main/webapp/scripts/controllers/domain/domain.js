define(['../../utils/constant','../../utils/utils'], function (constant, utils) {
    var Controller = function ($scope, $rootScope, $state, dialogs, DomainService) {
        $scope.status = 'init';

        $scope.domain = DomainService.domainShared;
        $scope.refreshDomains = function(name) {
            var params = {displayName: name, pageNumber:0, pageSize: 16};
            return DomainService.queryDomains(params).$promise.then(function(response) {
                if(response.data && response.data.data) {
                    $scope.domains = response.data.data;
                } else {
                    $scope.domains = [];
                }
            });
        };

        utils.generatorDropdown($scope, 'domainStatusDropdown', constant.commonStatus);

        function refreshStakeHolders() {
            if($scope.domain.selected) {
                var stakeHolderParam = {};
                stakeHolderParam.id = $scope.domain.selected.id;
                DomainService.queryStakeholders(stakeHolderParam, function (res) {
                    var result = res.data;
                    if(res.info) {
                        $scope.stakeholdersLoading = constant.loadError;
                        return;
                    }
                    if(!result || result.length == 0) {
                        $scope.stakeholdersLoading = constant.loadEmpty;
                        return;
                    }

                    $scope.stakeholdersLoading = '';
                    $scope.stakeholders = result;

                }, function () {
                    $scope.stakeholders = [];
                    $scope.stakeholdersLoading = constant.loadError;
                });
            }
        }

        $scope.modifyDomain = function() {
            $scope.status = 'modify';
        }

        $scope.cancelModifyDomain = function() {
            $scope.status = 'init';
        }

        $scope.confirmModifyDomain = function() {
            var modifyDomainParam = {};
            modifyDomainParam.id = $scope.domain.selected.id;
            modifyDomainParam.code = $scope.domain.selected.code;
            modifyDomainParam.displayName = $scope.domain.selected.displayName;
            modifyDomainParam.description = $scope.domain.selected.description;
            modifyDomainParam.status = $scope.domainStatusDropdown.option.value;
            DomainService.modifyDomain(modifyDomainParam, function (res) {

                if(res.info) {
                    // modify error with info.
                    return;
                }
                // modify success.
                $scope.status = 'init';

            }, function (err) {
                // modify error with api err.
            });
        }


        $scope.launch = function(which, param) {
            switch (which) {
                case 'addNewDomain':
                    var dlg = dialogs.create('views/domain/dialogs/add-domain.html', 'AddDomainController',
                        {}, {size: 'md'}
                    );
                    dlg.result.then(function (close) {
                        // add domain successed
                        debugger;
                        $scope.domain.selected = close;
                    }, function (dismiss) {
                        //
                    });
                    break;
                case 'status':
                    var dlg = dialogs.create('views/common/dialogs/enable-disable.html', 'EnableDisableController',
                        {
                            "header": param.status ? '用户-启用' : '用户-禁用',
                            "msg": "您确定要" + (param.status ? '启用' : '禁用') + "用户: " + param.email + "吗?"
                        }, {size: 'md'}
                    );
                    dlg.result.then(function (yes) {
                        UserService.enableDisableUser(
                            {
                                'id': param.id,
                                'status': param.status ? 0 : 1
                            }
                            , function (res) {
                                // status change successed
                                $scope.queryUser();
                            }, function (err) {
                                console.log(err);
                            }
                        );
                    }, function (no) {
                        // do nothing
                    });
                    break;
                case 'modify':
                    var dlg = dialogs.create('views/user/dialogs/modify.html', 'ModifyUserController',
                        param, {size: 'md'}
                    );
                    dlg.result.then(function (close) {
                        // maybe give user a friendly message.
                        $scope.queryUser();
                    }, function (dismiss) {
                        //
                    });
                    break;
            }
        }

        $scope.$watch('domain.selected', function(){
            var selectedDomain = $scope.domain.selected;
            $scope.stakeholders = [];
            if(selectedDomain) {
                var selectedDomainStatus = selectedDomain.status;
                for(var i=0;i<constant.commonStatus.length;i++) {
                    if(constant.commonStatus[i].value == selectedDomainStatus) {
                        $scope.domainStatusDropdown.option = constant.commonStatus[i];
                        break;
                    }
                }
            }
            refreshStakeHolders();
        });

        $scope.$on('selected-domain-changed', function() {
            DomainService.domainShared.selected = undefined;
            $state.go('user');
        });
    };

    return {
        name: "DomainController",
        fn: ["$scope", "$rootScope", "$state", "dialogs", "DomainService", Controller]
    };

});
