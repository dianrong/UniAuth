define(['../../utils/constant'], function(constant) {
    /**
     * A module representing a User controller.
     * @exports controllers/User
     */
    var Controller = function ($rootScope, $scope, GroupService, AlertService) {
        $scope.grp = $rootScope.shareGroup;

        var paramsCtlLevel = {};
        paramsCtlLevel.onlyShowGroup = true;
        //paramsCtlLevel.userGroupType = 1;
        $scope.getTree = GroupService.syncTree;
        $scope.getTree(paramsCtlLevel);

        $scope.modifyGroup = function () {

            if(!$rootScope.shareGroup.selected || !$rootScope.shareGroup.selected.id){
                AlertService.addAutoDismissAlert(constant.messageType.warning, $rootScope.translate('groupMgr.tips.selectGroupUedit'));
                return;
            }
            GroupService.modify({
                    "id": $scope.grp.selected.id,
                    "name": $scope.grp.selected.label,
                    "code": $scope.grp.selected.code,
                    "description": $scope.grp.selected.description,
                    "targetGroupId": $scope.grp.selected.id
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
                    id: $rootScope.shareGroup.selected.id
                }, function (result) {
                    $scope.selected = result.data;
                    $rootScope.shareGroup.selected = $scope.selected;
                }, function (err) {
                    console.log(err);
                });
                //sync the tree
                $scope.getTree(paramsCtlLevel);
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
