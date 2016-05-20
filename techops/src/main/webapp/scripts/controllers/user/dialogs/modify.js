define(['../../../utils/constant'], function (constant) {
    var Controller = function ($scope,$uibModalInstance, UserService, AlertService, data) {
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
                        AlertService.addAutoDismissAlert(constant.messageType.info, '用户信息修改成功!');
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
        fn: ["$scope","$uibModalInstance", "UserService", "AlertService", "data", Controller]
    };

});
