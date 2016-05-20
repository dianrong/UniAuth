define(function () {
    var Controller = function ($scope,$uibModalInstance, DomainService, data) {
        //-- Variables --//

        $scope.stakeholder = data;

        //-- Methods --//
        $scope.cancel = function(){
            $scope.msg = '';
            $uibModalInstance.dismiss();
        }; // end cancel

        $scope.save = function(){
            DomainService.modifyStakeholder($scope.stakeholder,
                function(res) {
                    // user add api successed
                    if(res.info) {
                        $scope.msg = res.info[0].msg;
                    } else {
                        $uibModalInstance.close();
                    }
                }, function(err) {
                    //restful 404 or other not 200+ response
                    console.log(err);
                }
            );
        }; // end save

    };

    return {
        name: "ModifyStakeholderController",
        fn: ["$scope","$uibModalInstance", "DomainService", "data", Controller]
    };

});
