define(['../../../utils/constant'], function (constant) {

    var Controller = function ($rootScope,$scope,$uibModalInstance, DomainService, data, AlertService) {

        $scope.stakeholder = {};
        $scope.domain = data;

        $scope.cancel = function(){
            $scope.msg = '';
            $uibModalInstance.dismiss();
        };

        $scope.save = function(){
            $scope.stakeholder.domainId = $scope.domain.id;
            DomainService.addStakeholder($scope.stakeholder,
                function(res) {
                    if(res.info) {
                        $scope.msg = res.info[0].msg;
                    } else {
                        AlertService.addAutoDismissAlert(constant.messageType.info, $rootScope.translate('domainMgr.tips.addStakeSuccess'));
                        $uibModalInstance.close(res.data);
                    }
                }, function(err) {
                    $scope.msg = $rootScope.translate('domainMgr.tips.addDomainFailure');
                    console.log(err);
                }
            );
        }; // end save

    };

    return {
        name: "AddStakeholderController",
        fn: ["$rootScope","$scope","$uibModalInstance", "DomainService", "data", "AlertService", Controller]
    };

});
