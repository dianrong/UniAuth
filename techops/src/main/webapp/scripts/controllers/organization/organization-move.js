define(['../../utils/constant'], function(constant) {
    /**
     * A module representing a User controller.
     * @exports controllers/organization.move
     */
    var Controller = function ($rootScope, $scope, OrganizationService, AlertService) {

        $scope.move = function () {
            if(!$rootScope.moveOrganization.from.id){
                AlertService.addAutoDismissAlert(constant.messageType.warning, $rootScope.translate('organizationMgr.tips.selectCurrentOrganization'));
                return;
            }

            if(!$rootScope.moveOrganization.to.id){
                AlertService.addAutoDismissAlert(constant.messageType.warning, $rootScope.translate('organizationMgr.tips.selectTargetOrganization'));
                return;
            }
            OrganizationService.move({
                "id": $rootScope.moveOrganization.from.id,
                "targetOrganizationId": $rootScope.moveOrganization.to.id
            }, function (res) {
                var result = res.data;
                if(res.info) {
                    for(var i=0; i<res.info.length;i++) {
                        AlertService.addAlert(constant.messageType.danger, res.info[i].msg);
                    }
                    return;
                }
                AlertService.addAutoDismissAlert(constant.messageType.info, $rootScope.translate('organizationMgr.tips.organizationMoveSuccess'));
                //reset
                $rootScope.moveOrganization.from = {};
                $rootScope.moveOrganization.to = {};
                //cancel the onMove status before move successfully
                $rootScope.onMove = false;
                //reset the tree component
                $rootScope.reset();
            }, function () {
                AlertService.addAutoDismissAlert(constant.messageType.danger, $rootScope.translate('organizationMgr.tips.organizationMoveFailure'));
            });
        };

    };

    return {
        name: "OrganizationMoveController",
        fn: ["$rootScope", "$scope", "OrganizationService", "AlertService", Controller]
    };

});
