define(['../../utils/constant', '../../utils/utils'], function (constant, utils) {

    var Controller = function ($scope, $rootScope, RoleService, GroupService) {
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
                $scope.roleUserGrpMsg = '请先选择一个角色';
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
                    $scope.roleUserGrpMsg = constant.submitFail;
                    return;
                }
                $scope.roleUserGrpMsg = '';
                $scope.getRoleUserGrpTree();
            }, function () {
                $scope.roles = [];
                $scope.roleUserGrpMsg = constant.submitFail;
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
        fn: ["$scope", "$rootScope", "RoleService", "GroupService", Controller]
    };

});
