define(['../../../utils/constant'], function (constant) {
    var Controller = function ($rootScope,$scope,$uibModalInstance, UserService, data, AlertService) {
        //-- Variables --//

        $scope.user = data;

        //-- Methods --//
        $scope.cancel = function(){
            $scope.msg = '';
            $uibModalInstance.dismiss();
        }; // end cancel

        $scope.save = function(){
            if(!angular.equals($scope.user.password, $scope.user.password2)) {
                $scope.msg = $rootScope.translate('userMgr.tips.twoPwdEq');
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
                        AlertService.addAutoDismissAlert(constant.messageType.info, $rootScope.translate('userMgr.tips.resetPwdSuccess'));
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
        fn: ["$rootScope","$scope","$uibModalInstance", "UserService", "data", "AlertService", Controller]
    };

});
