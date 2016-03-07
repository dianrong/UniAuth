define(function () {
    var Controller = function ($scope,$uibModalInstance, RoleService) {
        //-- Variables --//

        $scope.role = {};

        //-- Methods --//
        $scope.cancel = function(){
            $scope.msg = '';
            $uibModalInstance.dismiss();
        }; // end cancel

        $scope.save = function(){
            var addParam = {};
            RoleService.addRole(addParam,
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
        name: "AddRoleController",
        fn: ["$scope","$uibModalInstance", "RoleService", Controller]
    };

});
