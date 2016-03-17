define(function () {
    var Controller = function ($scope,$uibModalInstance, DomainService) {

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
        name: "AddDomainController",
        fn: ["$scope","$uibModalInstance", "DomainService", Controller]
    };

});
