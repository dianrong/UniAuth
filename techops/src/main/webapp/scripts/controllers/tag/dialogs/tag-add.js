define(['../../../utils/constant', '../../../utils/utils'], function (constant, utils) {
    var Controller = function ($rootScope, $scope,$uibModalInstance, TagService, AlertService) {
        //-- Variables --//

        $scope.tag = {};
        function getTagTypes() {
            var params = {};
            params.domainId = $rootScope.loginDomainsDropdown.option.id;
            TagService.getTagTypes(params).$promise.then(function(res) {
                var tagTypes = res.data;
                if(!tagTypes) {
                    tagTypes = [];
                    var empty = {
                        code : $rootScope.translate('constant.selectplacehodler')
                    };
                    tagTypes.unshift(empty);
                }
                utils.generatorDropdown($scope, 'tagAddTagTypesDropdown', tagTypes, tagTypes[0]);
            });
        }
        getTagTypes();

        $scope.cancel = function(){
            $scope.msg = '';
            $uibModalInstance.dismiss();
        };

        $scope.save = function(){
            var addParam = {};

            if($rootScope.loginDomainsDropdown && $rootScope.loginDomainsDropdown.option && $rootScope.loginDomainsDropdown.option.id){
                addParam.domainId = $rootScope.loginDomainsDropdown.option.id;
            }
            addParam.code = $scope.tag.code;
            addParam.description = $scope.tag.description;
            addParam.tagTypeId = $scope.tagAddTagTypesDropdown.option.id;
            TagService.addNewTag(addParam,
                function(res) {
                    if(res.info) {
                        $scope.msg = res.info[0].msg;
                    } else {
                        AlertService.addAutoDismissAlert(constant.messageType.info, $rootScope.translate('tagMgr.tips.addTagSuccess'));
                        $uibModalInstance.close();
                    }
                }, function(err) {
                    AlertService.addAlert(constant.messageType.danger, $rootScope.translate('tagMgr.tips.addTagFailure'));
                }
            );
        };

    };

    return {
        name: "AddTagController",
        fn: ["$rootScope", "$scope","$uibModalInstance", "TagService", "AlertService", Controller]
    };

});
