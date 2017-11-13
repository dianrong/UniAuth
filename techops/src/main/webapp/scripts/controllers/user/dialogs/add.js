define(['../../../utils/constant', '../../../utils/utils'], function (constant, utils) {
    var Controller = function ($rootScope,$scope,$uibModalInstance, UserService, AlertService) {
    	// add_btn is enable
    	$scope.add_btn_enable = function() {
    			if (!$scope.user.email && !$scope.user.phone) {
    				return false;
    			}
    			if (!$scope.user.staffNo) {
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

        $scope.getUserTypes = function getUserTypes() {
            var userTypes = [
                {code : $rootScope.translate('userMgr.type.normal'), value : 0},
                {code : $rootScope.translate('userMgr.type.system'), value : 1}
            ]
            utils.generatorDropdown($scope, 'userTypesDropdown', userTypes, userTypes[0]);
        };
        $scope.getUserTypes();

        $scope.save = function(){
            var params = $scope.user;
            params.type = $scope.userTypesDropdown.option.value;
            UserService.addUser(params,
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
