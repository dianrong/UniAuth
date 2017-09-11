define(['../../utils/constant'], function(constant) {
    /**
     * A module representing a User controller.
     * @exports controllers/User
     */
    var Controller = function ($rootScope, $scope, OrganizationService, AlertService) {
        $scope.modifyOrganization = function () {

            if(!$rootScope.targetOrganization || !$rootScope.targetOrganization.id){
                AlertService.addAutoDismissAlert(constant.messageType.warning, $rootScope.translate('organizationMgr.tips.selectOrganizationUedit'));
                return;
            }
            OrganizationService.modify({
                    "id": $rootScope.targetOrganization.id,
                    "name": $rootScope.targetOrganization.label,
                    "code": $rootScope.targetOrganization.code,
                    "description": $rootScope.targetOrganization.description,
                    "targetOrganizationId": $rootScope.targetOrganization.id
                }, function (res) {
                var result = res.data;
                if(res.info) {
                    for(var i=0; i<res.info.length;i++) {
                        AlertService.addAlert(constant.messageType.danger, res.info[i].msg);
                    }
                    return;
                }
                $scope.modifiedOrganization = result.data;
                AlertService.addAutoDismissAlert(constant.messageType.info, $rootScope.translate('organizationMgr.tips.organizationModifySuccess'));

                //sync the selected organization
                OrganizationService.getGrpDetails({
                    id: $rootScope.targetOrganization.id
                }, function (result) {
                    $scope.selected = result.data;
                    $rootScope.targetOrganization = $scope.selected;
                }, function (err) {
                    console.log(err);
                });

                //reset the tree component
                $rootScope.reset();
            }, function () {
                $scope.modifidOrganization = {};
                AlertService.addAutoDismissAlert(constant.messageType.danger, $rootScope.translate('organizationMgr.tips.organizationModifyFailure'));
            });
        };
    };

    return {
        name: "OrganizationModifyController",
        fn: ["$rootScope", "$scope", "OrganizationService", "AlertService", Controller]
    };

});
