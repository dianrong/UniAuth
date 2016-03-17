define(function () {
    var Controller = function ($scope,$uibModalInstance, DomainService) {

        $scope.domain = {};

        $scope.cancel = function(){
            $scope.msg = '';
            $uibModalInstance.dismiss();
        };

        $scope.save = function(){
            DomainService.addDomain($scope.domain,
                function(res) {
                    if(res.info) {
                        $scope.msg = res.info[0].msg;
                    } else {
                        $uibModalInstance.close();
                    }
                }, function(err) {
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
