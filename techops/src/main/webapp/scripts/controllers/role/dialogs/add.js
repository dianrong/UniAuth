define(['../../../utils/constant', '../../../utils/utils'], function (constant, utils) {
    var Controller = function ($rootScope, $scope,$uibModalInstance, RoleService, AlertService) {
        //-- Variables --//

        $scope.role = {};
        function getRoleCodes() {
            RoleService.getAllRoleCodes().$promise.then(function(res) {
                var roleCodes = res.data;
                utils.generatorDropdown($scope, 'roleAddRoleCodesDropdown', roleCodes, roleCodes[0]);
            });
        }
        getRoleCodes();

        //-- Methods --//
        $scope.cancel = function(){
            $scope.msg = '';
            $uibModalInstance.dismiss();
        }; // end cancel

        $scope.save = function(){
            var addParam = {};

            if($rootScope.loginDomainsDropdown && $rootScope.loginDomainsDropdown.option && $rootScope.loginDomainsDropdown.option.id){
                addParam.domainId = $rootScope.loginDomainsDropdown.option.id;
            }
            addParam.name = $scope.role.name;
            addParam.description = $scope.role.description;
            addParam.roleCodeId = $scope.roleAddRoleCodesDropdown.option.id;
            RoleService.addRole(addParam,
                function(res) {
                    // user add api successed
                    if(res.info) {
                        $scope.msg = res.info[0].msg;
                    } else {
                        AlertService.addAutoDismissAlert(constant.messageType.info, $rootScope.translate('roleMgr.tips.addRoleSuccess'));
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
        fn: ["$rootScope", "$scope","$uibModalInstance", "RoleService", "AlertService", Controller]
    };

});
