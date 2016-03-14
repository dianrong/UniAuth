define(['../../utils/constant'], function (constant) {

    var Controller = function ($scope, $rootScope, RoleService, GroupService) {
        $scope.role = RoleService.roleUserGrpShared;
        $scope.refreshRoles = function(name) {
            var params = {name: name, pageNumber:0, pageSize: 16};
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
            GroupService.syncTree(params, true)
        }
        $scope.getRoleUserGrpTree();

        $scope.saveRolesToUserAndGrp = function() {

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
