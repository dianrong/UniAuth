define(['../../utils/constant'],function(constant) {
    /**
     * A module representing a User controller.
     * @exports controllers/User
     */
    var Controller = function ($rootScope, $scope, GroupService, AlertService) {
        $scope.addGroup = function () {
            var params = $scope.grp;
            if(!$rootScope.targetGroup || !$rootScope.targetGroup.id){
                AlertService.addAutoDismissAlert(constant.messageType.info, $rootScope.translate('groupMgr.tips.selectedParentGroup'));
                return;
            }
            params.targetGroupId=$rootScope.targetGroup.id;

            GroupService.add(params, function (res) {
                var result = res.data;
                if(res.info) {
                    for(var i=0; i<res.info.length;i++) {
                        AlertService.addAlert(constant.messageType.danger, res.info[i].msg);
                    }
                    return;
                }
                $scope.addedGroup = result.data;
                AlertService.addAutoDismissAlert(constant.messageType.info, $rootScope.translate('groupMgr.tips.addGroupSuccess'));
                //reset the tree component
                $rootScope.reset();
            }, function () {
                $scope.addedGroup = {};
                AlertService.addAutoDismissAlert(constant.messageType.danger, $rootScope.translate('groupMgr.tips.addGroupFailure'));
            });
        };

    };

    return {
        name: "GroupAddController",
        fn: ["$rootScope","$scope", "GroupService", "AlertService", Controller]
    };

});
