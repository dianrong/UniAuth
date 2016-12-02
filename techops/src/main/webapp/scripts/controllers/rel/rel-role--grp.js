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
        $scope.checkClick = function($event) {
            $event.stopPropagation();
        };
        $scope.predicate = '';
        $scope.comparator = false;

        $scope.getRoleGrpTree = function() {
            var params = {};
            params.onlyShowGroup = true;
            if(!$scope.role.selected || !$scope.role.selected.id) {
                $scope.treedata.data = undefined;
                $scope.roleUserGrpMsg = constant.loadEmpty;
                return;
            }
            params.roleId = $scope.role.selected.id;
            params.onlyNeedGrpInfo = true;
            GroupService.syncTree(params, true);
            $scope.roleUserGrpMsg = '';
        }
        $scope.getRoleGrpTree();

        $scope.saveRolesToGrp = function() {
            if(!$scope.role.selected || !$scope.role.selected.id) {
                AlertService.addAutoDismissAlert(constant.messageType.warning, $rootScope.translate('relMgr.tips.pleaseChooseRole'));
                return;
            }
            var params = {};
            params.id = $scope.role.selected.id;
            var nodeArray = $scope.treedata.data;
            var checkedGroupIds = [];
            utils.extractCheckedGrpAndUserIds(nodeArray, checkedGroupIds, []);
            params.grpIds = checkedGroupIds;
            params.needProcessUserIds = false;
            RoleService.replaceGroupsAndUsersToRole(params, function (res) {
                if(res.info) {
                    for(var i=0; i<res.info.length;i++) {
                        AlertService.addAlert(constant.messageType.danger, res.info[i].msg);
                    }
                    return;
                }
                AlertService.addAutoDismissAlert(constant.messageType.info, $rootScope.translate('relMgr.tips.replaceGroupSuccess'));
                $scope.getRoleGrpTree();
            }, function () {
                $scope.roles = [];
                AlertService.addAutoDismissAlert(constant.messageType.danger, $rootScope.translate('relMgr.tips.replaceGroupFailure'));
            });
        };

        $scope.$watch('role.selected', $scope.getRoleGrpTree);
        $scope.$on('selected-domain-changed', function(){
            $scope.refreshRoles();
            $scope.getRoleGrpTree();
        });

        $scope.$on('selected-language-changed', $scope.getRoleGrpTree);
    };

    return {
        name: "RelRoleGroupController",
        fn: ["$scope", "$rootScope", "RoleService", "GroupService", "AlertService", Controller]
    };

});
