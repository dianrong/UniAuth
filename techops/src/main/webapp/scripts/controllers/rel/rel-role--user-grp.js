define(['../../utils/constant', '../../utils/utils'], function (constant, utils) {

    var Controller = function ($scope, $rootScope, RoleService, GroupService, AlertService) {
        $scope.role = RoleService.roleUserGrpShared;
        $scope.refreshRoles = function(name) {
            var params = {name: name, status:0, pageNumber:0, pageSize: 16};
            params.domainId = $rootScope.loginDomainsDropdown.option.id;
            return RoleService.getRoles(params).$promise.then(function(response) {
                if(response.data && response.data.data) {
                    $scope.roles = response.data.data;
                } else {
                    $scope.roles = [];
                }
            });
        };

        $scope.treedata = GroupService.roleUserGrpTree;
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
        $scope.checkClick = function($event, node) {
            if(node.type == constant.treeNodeType.memberUser) {
                utils.syncAllCheckBoxForTheSameRoleToUser($scope.treedata.data, node);
            }
            $event.stopPropagation();
        };
        $scope.predicate = '';
        $scope.comparator = false;

        $scope.getRoleUserGrpTree = function() {
            var params = {};
            params.onlyShowGroup = false;
            params.userGroupType = 0;
            if(!$scope.role.selected || !$scope.role.selected.id) {
                $scope.treedata.data = undefined;
                $scope.roleUserGrpMsg = constant.loadEmpty;
                return;
            }
            params.roleId = $scope.role.selected.id;
            GroupService.syncTree(params, true);
            $scope.roleUserGrpMsg = '';
        }
        $scope.getRoleUserGrpTree();

        $scope.saveRolesToUserAndGrp = function() {
            if(!$scope.role.selected || !$scope.role.selected.id) {
                AlertService.addAutoDismissAlert(constant.messageType.warning, '请先选择一个角色');
                return;
            }
            var params = {};
            params.id = $scope.role.selected.id;
            var nodeArray = $scope.treedata.data;
            var checkedGroupIds = [];
            var checkedUserIds = [];
            utils.extractCheckedGrpAndUserIds(nodeArray[0], checkedGroupIds, checkedUserIds);
            params.grpIds = checkedGroupIds;
            params.userIds = checkedUserIds;
            RoleService.replaceGroupsAndUsersToRole(params, function (res) {
                if(res.info) {
                    for(var i=0; i<res.info.length;i++) {
                        AlertService.addAlert(constant.messageType.danger, res.info[i].msg);
                    }
                    return;
                }
                AlertService.addAutoDismissAlert(constant.messageType.info, '替换角色对应的组/用户成功.');
                $scope.getRoleUserGrpTree();
            }, function () {
                $scope.roles = [];
                AlertService.addAutoDismissAlert(constant.messageType.danger, '替换角色对应的组/用户失败, 请联系系统管理员.');
            });
        };

        $scope.$watch('role.selected', $scope.getRoleUserGrpTree);
        $scope.$on('selected-domain-changed', function(){
            $scope.perms = [];
            $scope.refreshRoles();
            $scope.getRoleUserGrpTree();
        });

    };

    return {
        name: "RelRoleUserGroupController",
        fn: ["$scope", "$rootScope", "RoleService", "GroupService", "AlertService", Controller]
    };

});
