define(['../../utils/constant','../../utils/utils'], function (constant, utils) {
    var Controller = function ($scope, $rootScope, $state, DomainService) {
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
            $state.go('user');
        });
    };

    return {
        name: "DomainController",
        fn: ["$scope", "$rootScope", "$state", "DomainService", Controller]
    };

});
