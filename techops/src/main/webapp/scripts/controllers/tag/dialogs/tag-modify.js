define(['../../../utils/constant', '../../../utils/utils'], function (constant, utils) {
    var Controller = function ($rootScope, $scope,$uibModalInstance, TagService, data, AlertService) {
        //-- Variables --//

        $scope.tag = data;
        function getTagTypes() {
            var params = {};
            params.domainId = $rootScope.loginDomainsDropdown.option.id;
            TagService.getTagTypes(params).$promise.then(function(res) {
                var tagTypes = res.data;
                var selectedTagType;
                if(!tagTypes) {
                    tagTypes = [];
                    var empty = {
                        code : $rootScope.translate('constant.selectplacehodler')
                    };
                    tagTypes.unshift(empty);
                    selectedTagType = empty;
                } else {
                    for(var i=0; i<tagTypes.length;i++) {
                        if(tagTypes[i].id == $scope.tag.tagTypeId) {
                            selectedTagType = tagTypes[i];
                            break;
                        }
                    }
                }
                utils.generatorDropdown($scope, 'tagModifyTagTypesDropdown', tagTypes, selectedTagType);
            });
        }
        getTagTypes();

        $scope.cancel = function(){
            $scope.msg = '';
            $uibModalInstance.dismiss();
        };

        $scope.save = function(){
            var modifyParam = {};

            if($rootScope.loginDomainsDropdown && $rootScope.loginDomainsDropdown.option && $rootScope.loginDomainsDropdown.option.id){
                modifyParam.domainId = $rootScope.loginDomainsDropdown.option.id;
            }
            modifyParam.id = $scope.tag.id;
            modifyParam.code = $scope.tag.code;
            modifyParam.status = $scope.tag.status;
            modifyParam.description = $scope.tag.description;
            modifyParam.tagTypeId = $scope.tagModifyTagTypesDropdown.option.id;
            TagService.updateTag(modifyParam,
                function(res) {
                    if(res.info) {
                        $scope.msg = res.info[0].msg;
                    } else {
                        AlertService.addAutoDismissAlert(constant.messageType.info, $rootScope.translate('tagMgr.tips.modifyTagSuccess'));
                        $uibModalInstance.close();
                    }
                }, function(err) {
                    AlertService.addAlert(constant.messageType.danger, $rootScope.translate('tagMgr.tips.modifyTagFailure'));
                }
            );
        };

    };

    return {
        name: "ModifyTagController",
        fn: ["$rootScope", "$scope","$uibModalInstance", "TagService", "data", "AlertService", Controller]
    };

});
