define(['../../utils/constant', '../../utils/utils'], function (constant, utils) {

    var Controller = function ($scope, $rootScope, TagService, GroupService, AlertService) {
        $scope.tag = TagService.tagShared;
        $scope.refreshTags = function(code) {
            var params = {fuzzyCode: code, status:0, pageNumber:0, pageSize: 16};
            params.domainId = $rootScope.loginDomainsDropdown.option.id;
            return TagService.getTags(params).$promise.then(function(response) {
                if(response.data && response.data.data) {
                    $scope.tags = response.data.data;
                } else {
                    $scope.tags = [];
                }
            });
        };

        $scope.treedata = GroupService.tagUserGrpTree;
        $scope.opts = {
            isLeaf: function(node) {
                var isLeaf = node.type !== constant.treeNodeType.group;
                return isLeaf;
            },
            isSelectable: function(node) {
                return false;
            },
            equality: function(node1, node2) {
                if(node1 && node2) {
                    return node1.id == node2.id && node1.type == node2.type;
                } else {
                    return false;
                }
            },
            dirSelectable: false
        };
        $scope.checkClick = function($event) {
            $event.stopPropagation();
        };
        $scope.predicate = '';
        $scope.comparator = false;

        $scope.getTagUserGrpTree = function() {
            var params = {};
            params.onlyShowGroup = true;
            if(!$scope.tag.selected || !$scope.tag.selected.id) {
                $scope.treedata.data = undefined;
                $scope.tagUserGrpMsg = constant.loadEmpty;
                return;
            }
            params.tagId = $scope.tag.selected.id;
            GroupService.syncTagTree(params);
            $scope.tagUserGrpMsg = '';
        }
        $scope.getTagUserGrpTree();

        $scope.saveTagsToUserAndGrp = function() {
            if(!$scope.tag.selected || !$scope.tag.selected.id) {
                AlertService.addAutoDismissAlert(constant.messageType.warning, $rootScope.translate('relMgr.tips.plaseChooseTag'));
                return;
            }
            var params = {};
            params.id = $scope.tag.selected.id;
            var nodeArray = $scope.treedata.data;
            var checkedGroupIds = [];
            var checkedUserIds = [];
            utils.extractCheckedGrpAndUserIds(nodeArray, checkedGroupIds, checkedUserIds);
            params.grpIds = checkedGroupIds;
            params.userIds = checkedUserIds;
            TagService.replaceGroupsAndUsersToTag(params, function (res) {
                if(res.info) {
                    for(var i=0; i<res.info.length;i++) {
                        AlertService.addAlert(constant.messageType.danger, res.info[i].msg);
                    }
                    return;
                }
                AlertService.addAutoDismissAlert(constant.messageType.info, $rootScope.translate('relMgr.tips.replaceTagGroupSuccess'));
                $scope.getTagUserGrpTree();
            }, function () {
                $scope.tags = [];
                AlertService.addAutoDismissAlert(constant.messageType.danger, $rootScope.translate('relMgr.tips.replaceTagGroupFailure'));
            });
        };

        $scope.$watch('tag.selected', $scope.getTagUserGrpTree);
        $scope.$on('selected-domain-changed', function(){
            $scope.refreshTags();
            $scope.getTagUserGrpTree();
        });

        $scope.$on('selected-language-changed', $scope.getTagUserGrpTree);
    };

    return {
        name: "RelTagUserGroupController",
        fn: ["$scope", "$rootScope", "TagService", "GroupService", "AlertService", Controller]
    };

});
