define(['../../../utils/utils','../../../utils/constant'], function (utils, constant) {
    var Controller = function ($rootScope,$scope,$uibModalInstance, PermService, AlertService, data) {
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
        utils.generatorDropdown($scope, 'permModifyPermValueExtDropdown', constant.httpMethods, $scope.perm.valueExt);

        //-- Methods --//
        $scope.cancel = function(){
            $scope.msg = '';
            $uibModalInstance.dismiss();
        }; // end cancel

        $scope.save = function(){
            var param = $scope.perm;
            var obj = {
                'id':param.id,
                'value': param.value,
                'description': param.description,
                'permTypeId':$scope.permModifyPermTypesDropdown.option.id,
                'domainId':param.domainId,
                'status':param.status
            }
            if($scope.permModifyPermTypesDropdown.option.type == constant.permType.URI_PATTERN || $scope.permModifyPermTypesDropdown.option.type == constant.permType.REGULAR_PATTERN ) {
                obj.valueExt = $scope.permModifyPermValueExtDropdown.option;
            } else {
                obj.valueExt = param.valueExt
            }
            PermService.updatePerm(obj,
                function(res) {
                    // user add api successed
                    if(res.info) {
                        $scope.msg = res.info[0].msg;
                    } else {
                        AlertService.addAutoDismissAlert(constant.messageType.info, $rootScope.translate('permMgr.tips.modifyPermSuccess'));
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
        fn: ["$rootScope","$scope","$uibModalInstance", "PermService", "AlertService", "data", Controller]
    };

});
