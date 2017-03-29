define(['../../utils/constant'], function(constant) {
    /**
     * A module representing a User controller.
     * @exports controllers/User
     */
    var Controller = function ($rootScope, $scope, GroupService, AlertService) {
        $scope.modifyGroup = function () {

            if(!$rootScope.targetGroup || !$rootScope.targetGroup.id){
                AlertService.addAutoDismissAlert(constant.messageType.warning, $rootScope.translate('groupMgr.tips.selectGroupUedit'));
                return;
            }
            GroupService.modify({
                    "id": $rootScope.targetGroup.id,
                    "name": $rootScope.targetGroup.label,
                    "code": $rootScope.targetGroup.code,
                    "description": $rootScope.targetGroup.description,
                    "targetGroupId": $rootScope.targetGroup.id
                }, function (res) {
                var result = res.data;
                if(res.info) {
                    for(var i=0; i<res.info.length;i++) {
                        AlertService.addAlert(constant.messageType.danger, res.info[i].msg);
                    }
                    return;
                }
                $scope.modifiedGroup = result.data;
                AlertService.addAutoDismissAlert(constant.messageType.info, $rootScope.translate('groupMgr.tips.groupModifySuccess'));

                //sync the selected group
                GroupService.getGrpDetails({
                    id: $rootScope.targetGroup.id
                }, function (result) {
                    $scope.selected = result.data;
                    $rootScope.targetGroup = $scope.selected;
                }, function (err) {
                    console.log(err);
                });

                //reset the tree component
                $rootScope.reset();
            }, function () {
                $scope.modifidGroup = {};
                AlertService.addAutoDismissAlert(constant.messageType.danger, $rootScope.translate('groupMgr.tips.groupModifyFailure'));
            });
        };
    };

    return {
        name: "GroupModifyController",
        fn: ["$rootScope", "$scope", "GroupService", "AlertService", Controller]
    };

});
