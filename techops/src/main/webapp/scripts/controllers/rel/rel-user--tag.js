define(['../../utils/constant'], function (constant, AlertService) {
    /**
     * A module representing a rel controller.
     * @exports controllers/rel
     */
    var Controller = function ($scope, $rootScope, UserService, AlertService) {
        $scope.user = UserService.userShared;
        
        // 获取用户信息function
        $scope.refreshUsers = function(email) {
            var params = {email: email, status:0, pageNumber:0, pageSize: 16};
            return UserService.getUsers(params).$promise.then(function(response) {
                if(response.data && response.data.data) {
                    $scope.users = response.data.data;
                } else {
                    $scope.users = [];
                }
            });
        }

        $scope.userTagsMsg = constant.loadEmpty;
        $scope.getUserTagsWithCheckedInfo = function () {
            $scope.userTagsMsg = constant.loading;
            var params = {};
            
            //根据userid查询用户关联tag信息
            if(!$scope.user.selected) {
                $scope.tags = undefined;
                $scope.userTagsMsg = constant.loadEmpty;
                return;
            }
            params.id = $scope.user.selected.id;
            UserService.queryTagsWithCheckedInfo(params, function (res) {
                var result = res.data;
                if(res.info) {
                    $scope.userTagsMsg = constant.loadError;
                    return;
                }
                if(!result) {
                    $scope.userTagsMsg = constant.loadEmpty;
                    $scope.tags = undefined;
                    return;
                }

                $scope.userTagsMsg = '';
                $scope.tags = result;
            }, function () {
                $scope.tags = undefined;
                $scope.userTagsMsg = constant.loadError;
            });
        };
        if($scope.user.selected) {
            $scope.getUserTagsWithCheckedInfo();
        }
        $scope.replaceTagsToUser = function() {
            var params = {};
            var checkedTagIds = [];
            var tags = $scope.tags;
            for(var i=0; i<tags.length;i++) {
                if(tags[i].tagUserChecked) {
                	checkedTagIds.push(tags[i].id);
                }
            }
            params.tagIds = checkedTagIds;
            params.id = $scope.user.selected.id;
            UserService.replaceTagsToUser(params, function (res) {
                if(res.info) {
                    for(var i=0; i<res.info.length;i++) {
                        AlertService.addAlert(constant.messageType.danger, res.info[i].msg);
                    }
                    return;
                }
                AlertService.addAutoDismissAlert(constant.messageType.info, '替换用户对应的标签成功.');
                $scope.getUserTagsWithCheckedInfo();
            }, function () {
                $scope.tags = [];
                AlertService.addAutoDismissAlert(constant.messageType.danger, '替换用户对应的标签失败, 请联系系统管理员.');
            });
        }
        $scope.selectAllTagsForUser = function() {
            for(var i=0;i<$scope.tags.length;i++) {
                $scope.tags[i].tagUserChecked = true;
            }
        }
        var watch = $scope.$watch('user.selected', $scope.getUserTagsWithCheckedInfo);

        $scope.$on('selected-domain-changed', function(){
            $scope.tags = [];
            $scope.refreshUsers();
            $scope.getUserTagsWithCheckedInfo();
        });
        //watch();
        $scope.predicate = '';
        $scope.comparator = false;
    };

    return {
        name: "RelUserTagController",
        fn: ["$scope", "$rootScope", "UserService", "AlertService", Controller]
    };

});
