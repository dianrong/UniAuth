define(['../../../utils/utils','../../../utils/constant'], function (utils,constant) {
    var Controller = function ($rootScope,$scope,$uibModalInstance, RoleService, data, AlertService) {
        //-- Variables --//

        $scope.role = data;
        function getRoleCodes() {
            RoleService.getAllRoleCodes().$promise.then(function(res) {
                var roleCodes = res.data;
                var selectedRoleCode;
                for(var i=0; i<roleCodes.length;i++) {
                    if(roleCodes[i].id == $scope.role.roleCodeId) {
                        selectedRoleCode = roleCodes[i];
                        break;
                    }
                }
                utils.generatorDropdown($scope, 'roleModifyRoleCodesDropdown', roleCodes, selectedRoleCode);
            });
        }
        getRoleCodes();
        //-- Methods --//
        $scope.cancel = function(){
            $scope.msg = '';
            $uibModalInstance.dismiss();
        }; // end cancel

        $scope.save = function(){
            var param = $scope.role;
            RoleService.updateRole(
                {
                    'id':param.id,
                    'name': param.name,
                    'description': param.description,
                    'roleCodeId':$scope.roleModifyRoleCodesDropdown.option.id,
                    'status':param.status
                },
                function(res) {
                    // user add api successed
                    if(res.info) {
                        $scope.msg = res.info[0].msg;
                    } else {
                        AlertService.addAutoDismissAlert(constant.messageType.info, $rootScope.translate('roleMgr.tips.roleModifySuccess'));
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
        name: "ModifyRoleController",
        fn: ["$rootScope","$scope","$uibModalInstance", "RoleService", "data", "AlertService", Controller]
    };

});
