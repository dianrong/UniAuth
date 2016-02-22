define(function () {
    var Controller = function ($scope,$uibModalInstance, UserService, data) {
        //-- Variables --//

        $scope.user = data;

        //-- Methods --//
        $scope.cancel = function(){
            $scope.msg = '';
            $uibModalInstance.dismiss();
        }; // end cancel

        $scope.save = function(){
            UserService.modifyUser(
                {
                    "id":$scope.user.id,
                    "name":$scope.user.name,
                    "email":$scope.user.email,
                    "phone":$scope.user.phone
                },
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
        name: "ModifyUserController",
        fn: ["$scope","$uibModalInstance", "UserService", "data", Controller]
    };

});
