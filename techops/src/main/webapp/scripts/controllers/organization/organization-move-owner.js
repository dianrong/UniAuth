define(['../../utils/constant'], function(constant) {
    /**
     * A module representing a User controller.
     * @exports controllers/organization.move
     */
    var Controller = function ($rootScope, $scope, OrganizationService, AlertService) {

        $scope.move = function () {
            if(!$rootScope.moveUser.user.id){
                AlertService.addAutoDismissAlert(constant.messageType.warning, $rootScope.translate('organizationMgr.tips.selectCurrentOwner'));
                return;
            }

            if(!$rootScope.moveUser.organization.id){
                AlertService.addAutoDismissAlert(constant.messageType.warning, $rootScope.translate('organizationMgr.tips.selectTargetOrganization'));
                return;
            }

            var user = $rootScope.moveUser.user;
            //暂不支持批量移动
            var userIdOrganizationIdPairs = [{
                entry1:user.id,
                entry2:user.parent.id
            }];
            var params = {};
            params.userIdOrganizationIdPairs = userIdOrganizationIdPairs;
            params.normalMember=false;
            params.organizationId = $rootScope.moveUser.organization.id;


            OrganizationService.moveUser(params, function (res) {
                var result = res.data;
                if(res.info) {
                    for(var i=0; i<res.info.length;i++) {
                        AlertService.addAlert(constant.messageType.danger, res.info[i].msg);
                    }
                    return;
                }
                AlertService.addAutoDismissAlert(constant.messageType.info, $rootScope.translate('organizationMgr.tips.organizationMoveSuccess'));
                //reset
                $rootScope.moveUser.user = {};
                $rootScope.moveUser.organization = {};
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
        name: "OrganizationMoveOwnerController",
        fn: ["$rootScope", "$scope", "OrganizationService", "AlertService", Controller]
    };

});
