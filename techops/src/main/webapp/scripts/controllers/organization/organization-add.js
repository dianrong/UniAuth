define(['../../utils/constant'],function(constant) {
    /**
     * A module representing a User controller.
     * @exports controllers/User
     */
    var Controller = function ($rootScope, $scope, OrganizationService, AlertService) {
        $scope.addOrganization = function () {
            var params = $scope.grp;
            if(!$rootScope.targetOrganization || !$rootScope.targetOrganization.id){
                AlertService.addAutoDismissAlert(constant.messageType.info, $rootScope.translate('organizationMgr.tips.selectedParentOrganization'));
                return;
            }
            params.targetOrganizationId=$rootScope.targetOrganization.id;

            OrganizationService.add(params, function (res) {
                var result = res.data;
                if(res.info) {
                    for(var i=0; i<res.info.length;i++) {
                        AlertService.addAlert(constant.messageType.danger, res.info[i].msg);
                    }
                    return;
                }
                $scope.addedOrganization = result.data;
                AlertService.addAutoDismissAlert(constant.messageType.info, $rootScope.translate('organizationMgr.tips.addOrganizationSuccess'));
                //reset the tree component
                $rootScope.reset();
            }, function () {
                $scope.addedOrganization = {};
                AlertService.addAutoDismissAlert(constant.messageType.danger, $rootScope.translate('organizationMgr.tips.addOrganizationFailure'));
            });
        };

    };

    return {
        name: "OrganizationAddController",
        fn: ["$rootScope","$scope", "OrganizationService", "AlertService", Controller]
    };

});
