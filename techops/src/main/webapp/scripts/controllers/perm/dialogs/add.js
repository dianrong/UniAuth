define(['../../../utils/constant', '../../../utils/utils'], function (constant, utils) {
    var Controller = function ($rootScope, $scope,$uibModalInstance, PermService) {
        //-- Variables --//

        $scope.perm = {};
        function getPermTypes() {
            PermService.getAllPermTypes().$promise.then(function(res) {
                var permTypes = res.data;
                utils.generatorDropdown($scope, 'permAddPermTypesDropdown', permTypes, permTypes[1]);
            });
        }
        getPermTypes();

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
            addParam.value = $scope.perm.value;
            addParam.description = $scope.perm.description;
            addParam.permTypeId = $scope.permAddPermTypesDropdown.option.id;
            PermService.addPerm(addParam,
                function(res) {
                    // user add api successed
                    if(res.info) {
                        $scope.msg = res.info[0].msg;
                    } else {
                        $uibModalInstance.close();
                    }
                }, function(err) {
                    console.log(err);
                }
            );
        }; // end save

    };

    return {
        name: "AddPermController",
        fn: ["$rootScope", "$scope","$uibModalInstance", "PermService", Controller]
    };

});
