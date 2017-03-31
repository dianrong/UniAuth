define(['../../utils/constant'], function(constant) {
    /**
     * A module representing a User controller.
     * @exports controllers/group.move
     */
    var Controller = function ($rootScope, $scope, GroupService, AlertService) {

        $scope.move = function () {
            if(!$rootScope.moveUser.user.id){
                AlertService.addAutoDismissAlert(constant.messageType.warning, $rootScope.translate('groupMgr.tips.selectCurrentOwner'));
                return;
            }

            if(!$rootScope.moveUser.group.id){
                AlertService.addAutoDismissAlert(constant.messageType.warning, $rootScope.translate('groupMgr.tips.selectTargetGroup'));
                return;
            }

            var user = $rootScope.moveUser.user;
            //暂不支持批量移动
            var userIdGroupIdPairs = [{
                entry1:user.id,
                entry2:user.parent.id
            }];
            var params = {};
            params.userIdGroupIdPairs = userIdGroupIdPairs;
            params.normalMember=false;
            params.groupId = $rootScope.moveUser.group.id;


            GroupService.moveUser(params, function (res) {
                var result = res.data;
                if(res.info) {
                    for(var i=0; i<res.info.length;i++) {
                        AlertService.addAlert(constant.messageType.danger, res.info[i].msg);
                    }
                    return;
                }
                AlertService.addAutoDismissAlert(constant.messageType.info, $rootScope.translate('groupMgr.tips.groupMoveSuccess'));
                //reset
                $rootScope.moveUser.user = {};
                $rootScope.moveUser.group = {};
                //cancel the onMove status before move successfully
                $rootScope.onMove = false;
                //reset the tree component
                $rootScope.reset();
            }, function () {
                AlertService.addAutoDismissAlert(constant.messageType.danger, $rootScope.translate('groupMgr.tips.groupMoveFailure'));
            });
        };

    };

    return {
        name: "GroupMoveOwnerController",
        fn: ["$rootScope", "$scope", "GroupService", "AlertService", Controller]
    };

});
