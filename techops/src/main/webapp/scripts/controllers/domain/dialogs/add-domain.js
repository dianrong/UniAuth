define(['../../../utils/constant'], function (constant) {
    var Controller = function ($rootScope,$scope, $uibModalInstance, DomainService, AlertService) {

        $scope.domain = {};

        $scope.cancel = function(){
            $scope.msg = '';
            $uibModalInstance.dismiss();
        };

        $scope.save = function(){
            $scope.domain.status = 0;
            DomainService.addDomain($scope.domain,
                function(res) {
                    if(res.info) {
                        $scope.msg = res.info[0].msg;
                    } else {
                        AlertService.addAutoDismissAlert(constant.messageType.info, $rootScope.translate('domainMgr.tips.addDomainSuccess'));
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
        name: "AddDomainController",
        fn: ["$rootScope","$scope", "$uibModalInstance", "DomainService", "AlertService", Controller]
    };

});
