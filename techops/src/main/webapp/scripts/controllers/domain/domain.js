define(['../../utils/constant','../../utils/utils'], function (constant, utils) {
    var Controller = function ($scope, $rootScope, $state, dialogs, DomainService, AlertService) {
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

        $scope.refreshStakeholders = function refreshStakeholders() {
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
                    for(var i=0; i<res.info.length;i++) {
                        AlertService.addAlert(constant.messageType.danger, res.info[i].msg);
                    }
                    // modify error with info.
                    return;
                }
                // modify success.
                AlertService.addAutoDismissAlert(constant.messageType.info, $rootScope.translate('domainMgr.tips.modifyStakeSuccess'));
                $scope.status = 'init';
                $scope.domains = [];
                $scope.refreshDomains();
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
                        // add domain success
                        $scope.domain.selected = close;
                    }, function (dismiss) {
                        //
                    });
                    break;
                case 'addStakeholder':
                    var dlg = dialogs.create('views/domain/dialogs/add-stakeholder.html', 'AddStakeholderController',
                        param, {size: 'md'}
                    );
                    dlg.result.then(function (close) {
                        // addStakeholder success
                        $scope.refreshStakeholders();
                    }, function (dismiss) {
                        //
                    });
                    break;
                case 'modifyStakeholder':
                    var dlg = dialogs.create('views/domain/dialogs/modify-stakeholder.html', 'ModifyStakeholderController',
                        param, {size: 'md'}
                    );
                    dlg.result.then(function (close) {
                        AlertService.addAutoDismissAlert(constant.messageType.info, $rootScope.translate('domainMgr.tips.modifyStakeSuccess'));
                        $scope.refreshStakeholders();
                    }, function (dismiss) {
                        //
                    });
                    break;
                case 'deleteStakeholder':
                    var dlg = dialogs.create('views/common/dialogs/enable-disable.html', 'EnableDisableController',
                        {
                            "header":$rootScope.translate('domainMgr.tips.delStake'),
                            "msg": $rootScope.translate('domainMgr.tips.areUsureDel') + param.email + $rootScope.translate('permMgr.tips.ma')
                        }, {size: 'md'}
                    );
                    dlg.result.then(function (yes) {
                        DomainService.deleteStakeholder(
                            {
                                'id': param.id
                            }
                            , function (res) {
                                AlertService.addAutoDismissAlert(constant.messageType.info, $rootScope.translate('domainMgr.tips.delStakeSuccess'));
                                $scope.stakeholders = [];
                                $scope.refreshStakeholders();
                            }, function (err) {
                                AlertService.addAutoDismissAlert(constant.messageType.danger, $rootScope.translate('domainMgr.tips.delStakeFailure'));
                            }
                        );
                    }, function (no) {
                        // do nothing
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
            $scope.refreshStakeholders();
        });

        $scope.$on('selected-domain-changed', function() {
            DomainService.domainShared.selected = undefined;
            $state.go('user');
        });
        
        $scope.$on('selected-language-changed', $scope.refreshStakeholders);
    };

    return {
        name: "DomainController",
        fn: ["$scope", "$rootScope", "$state", "dialogs", "DomainService", "AlertService", Controller]
    };

});
