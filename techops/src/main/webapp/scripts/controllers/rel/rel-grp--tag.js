define(['../../utils/constant'], function (constant, AlertService) {
    /**
     * A module representing a relation controller.
     * @exports controllers/grp-tag
     */
	var Controller = function ($scope, $rootScope, GroupService, AlertService) {
        // 获取组信息function
        $scope.grp = GroupService.grpShared;
        $scope.refreshGrps = function(name) {
            var params = {name: name, status:0, pageNumber:0, pageSize: 16};
            return GroupService.queryGroup(params).$promise.then(function(response) {
                if(response.data && response.data.data) {
                    $scope.grps = response.data.data;
                } else {
                    $scope.grps = [];
                }
            });
        }

        $scope.grpTagsMsg = constant.loadEmpty;
        $scope.getGroupTagsWithCheckedInfo = function () {
            $scope.grpTagsMsg = constant.loading;
            var params = {};
            
            //根据groupId查询组关联tag信息
            if(!$scope.grp.selected) {
                $scope.tags = undefined;
                $scope.grpTagsMsg = constant.loadEmpty;
                return;
            }
            params.id = $scope.grp.selected.id;
            params.domainId = $rootScope.loginDomainsDropdown.option.id;
            GroupService.queryTagsWithCheckedInfo(params, function (res) {
                var result = res.data;
                if(res.info) {
                    $scope.grpTagsMsg = constant.loadError;
                    return;
                }
                if(!result) {
                    $scope.grpTagsMsg = constant.loadEmpty;
                    $scope.tags = undefined;
                    return;
                }

                $scope.grpTagsMsg = '';
                $scope.tags = result;
            }, function () {
                $scope.tags = undefined;
                $scope.grpTagsMsg = constant.loadError;
            });
        };
        if($scope.grp.selected) {
            $scope.getGroupTagsWithCheckedInfo();
        }
        $scope.replaceTagsToGroup = function() {
            var params = {};
            var checkedTagIds = [];
            var tags = $scope.tags;
            for(var i=0; i<tags.length;i++) {
                if(tags[i].tagGrouprChecked) {
                	checkedTagIds.push(tags[i].id);
                }
            }
            params.tagIds = checkedTagIds;
            params.id = $scope.grp.selected.id;
            GroupService.replaceTagsToGrp(params, function (res) {
                if(res.info) {
                    for(var i=0; i<res.info.length;i++) {
                        AlertService.addAlert(constant.messageType.danger, res.info[i].msg);
                    }
                    return;
                }
                AlertService.addAutoDismissAlert(constant.messageType.info, $rootScope.translate('relMgr.tips.replaceTagSuccess'));
                $scope.getGroupTagsWithCheckedInfo();
            }, function () {
                $scope.tags = [];
                AlertService.addAutoDismissAlert(constant.messageType.danger, $rootScope.translate('relMgr.tips.replaceTagFailure'));
            });
        }
        $scope.selectAllTagsForGrp = function(tag) {
        	tag = tag !== false
            for(var i=0;i<$scope.tags.length;i++) {
                $scope.tags[i].tagGrouprChecked = tag;
            }
        }
        var watch = $scope.$watch('grp.selected', $scope.getGroupTagsWithCheckedInfo);

        $scope.$on('selected-domain-changed', function(){
            $scope.tags = [];
            $scope.refreshGrps();
            $scope.getGroupTagsWithCheckedInfo();
        });
        //watch();
        $scope.predicate = '';
        $scope.comparator = false;
        
        $scope.$on('selected-language-changed', $scope.getGroupTagsWithCheckedInfo);
    };

    return {
        name: "RelGroupTagController",
        fn: ["$scope", "$rootScope", "GroupService", "AlertService", Controller]
    };

});
