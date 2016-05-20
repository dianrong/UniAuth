define(['../../../utils/constant'], function (constant) {

    var Controller = function ($scope,$uibModalInstance, DomainService, data, AlertService) {

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
                        AlertService.addAutoDismissAlert(constant.messageType.info, 'Stakeholder新增成功.');
                        $uibModalInstance.close(res.data);
                    }
                }, function(err) {
                    $scope.msg = '新增失败,请联系系统管理员';
                    console.log(err);
                }
            );
        }; // end save

    };

    return {
        name: "AddStakeholderController",
        fn: ["$scope","$uibModalInstance", "DomainService", "data", "AlertService", Controller]
    };

});
