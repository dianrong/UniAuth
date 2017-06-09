define(['../../../utils/constant'], function (constant) {
    var Controller = function ($rootScope,$scope,$uibModalInstance, UserService, AlertService) {
    	// add_btn is enable
    	$scope.add_btn_enable = function() {
    			if (!$scope.user.email && !$scope.user.phone) {
    				return false;
    			}
    			return true;
    	}
    	
    	//-- Variables --//

        $scope.user = {};

        //-- Methods --//
        $scope.cancel = function(){
            $scope.msg = '';
            $uibModalInstance.dismiss();
        }; // end cancel

        $scope.save = function(){
            UserService.addUser($scope.user,
                function(res) {
                    // user add api successed
                    if(res.info) {
                        $scope.msg = res.info[0].msg;
                    } else {
                        AlertService.addAutoDismissAlert(constant.messageType.info, $rootScope.translate('userMgr.tips.addUserSuccess'));
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
        name: "AddUserController",
        fn: ["$rootScope","$scope","$uibModalInstance", "UserService", "AlertService", Controller]
    };

});
