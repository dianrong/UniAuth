define(['../../../utils/constant'], function (constant) {
    var Controller = function ($scope,$uibModalInstance, UserService, data, AlertService) {
        //-- Variables --//

        $scope.user = data;

        //-- Methods --//
        $scope.cancel = function(){
            $scope.msg = '';
            $uibModalInstance.dismiss();
        }; // end cancel

        $scope.save = function(){
            if(!angular.equals($scope.user.password, $scope.user.password2)) {
                $scope.msg = '请确保您两次输入的密码相等';
                return;
            }
            UserService.resetpassword(
                {
                    "id":$scope.user.id,
                    "password":$scope.user.password
                },
                function(res) {
                    // user add api successed
                    if(res.info) {
                        $scope.msg = res.info[0].msg;
                    } else {
                        AlertService.addAutoDismissAlert(constant.messageType.info, '用户密码重置成功!');
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
        name: "ResetPasswordController",
        fn: ["$scope","$uibModalInstance", "UserService", "data", "AlertService", Controller]
    };

});
