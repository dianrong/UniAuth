define(['../../../utils/utils'], function (utils) {
    var Controller = function ($scope,$uibModalInstance, PermService, data) {
        //-- Variables --//

        $scope.perm = data;
        function getPermTypes() {

            PermService.getAllPermTypes().$promise.then(function(res) {
                var permTypes = res.data;
                var selectedPermType;
                for(var i=0; i<permTypes.length;i++) {
                    if(permTypes[i].id == $scope.perm.permTypeId) {
                        selectedPermType = permTypes[i];
                        break;
                    }
                }
                utils.generatorDropdown($scope, 'permModifyPermTypesDropdown', permTypes, selectedPermType);
            });

        }
        getPermTypes();
        //-- Methods --//
        $scope.cancel = function(){
            $scope.msg = '';
            $uibModalInstance.dismiss();
        }; // end cancel

        $scope.save = function(){
            var param = $scope.perm;
            PermService.updatePerm(
                {
                    'id':param.id,
                    'value': param.value,
                    'description': param.description,
                    'permTypeId':param.permTypeId,
                    'domainId':param.domainId,
                    'status':param.status
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
        name: "ModifyPermController",
        fn: ["$scope","$uibModalInstance", "PermService", "data", Controller]
    };

});
