define(['../../../utils/constant', '../../../utils/utils'], function (constant, utils) {
    var Controller = function ($rootScope,$scope,$uibModalInstance, UserService, AlertService, data) {
    	// update_btn is enable
    	$scope.update_btn_enable = function() {
    			if (!$scope.user.email && !$scope.user.phone) {
    				return false;
    			}
    			if (!$scope.user.staffNo) {
            return false;
          }
    			return true;
    	}
    	
        //-- Variables --//

        $scope.user = data;

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
             var selectedUserType;
             for(var i=0; i<userTypes.length;i++) {
                 if(userTypes[i].value == $scope.user.type) {
                     selectedUserType = userTypes[i];
                     break;
                 }
             }
             utils.generatorDropdown($scope, 'userTypesDropdown', userTypes, selectedUserType);
         };
        $scope.getUserTypes();

        $scope.save = function(){
             var params = { id:$scope.user.id,
                            name:$scope.user.name,
                            email:$scope.user.email,
                            phone:$scope.user.phone,
                            staffNo:$scope.user.staffNo,
                            type: $scope.userTypesDropdown.option.value};
            UserService.modifyUser(params,
                function(res) {
                    // user add api successed
                    if(res.info) {
                        $scope.msg = res.info[0].msg;
                    } else {
                        AlertService.addAutoDismissAlert(constant.messageType.info, $rootScope.translate('userMgr.tips.modifyUserSuccess'));
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
        fn: ["$rootScope","$scope","$uibModalInstance", "UserService", "AlertService", "data", Controller]
    };

});
