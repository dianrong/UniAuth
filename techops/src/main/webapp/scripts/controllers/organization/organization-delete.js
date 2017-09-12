define(['../../utils/constant'], function(constant) {

    var Controller = function ($rootScope, $scope, OrganizationService, AlertService) {
        $scope.confirm = function () {

            if(!$rootScope.targetOrganization || !$rootScope.targetOrganization.id || !$rootScope.targetOrganization.code){
                AlertService.addAutoDismissAlert(constant.messageType.warning, $rootScope.translate('organizationMgr.tips.selectOrganizationUdel'));
                return;
            }
            OrganizationService.del({
                "id": $rootScope.targetOrganization.id,
                "code": $rootScope.targetOrganization.code,
                "status": 1,
                "targetGroupId": $rootScope.targetOrganization.id
            }, function (res) {
                if(res.info) {
                    for(var i=0; i<res.info.length;i++) {
                        AlertService.addAlert(constant.messageType.danger, res.info[i].msg);
                    }
                    return;
                }
                AlertService.addAutoDismissAlert(constant.messageType.info, $rootScope.translate('organizationMgr.tips.organizationDelSuccess'));
                //reset the tree component
                $rootScope.reset();
            }, function () {
                AlertService.addAutoDismissAlert(constant.messageType.danger, $rootScope.translate('organizationMgr.tips.organizationDelFailure'));
            });
        };
    };

    return {
        name: "OrganizationDeleteController",
        fn: ["$rootScope", "$scope", "OrganizationService", "AlertService", Controller]
    };

});
