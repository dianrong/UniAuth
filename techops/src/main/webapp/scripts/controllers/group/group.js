define(['../../utils/constant'], function (constant) {
    /**
     * A module representing a User controller.
     * @exports controllers/User
     */
    var Controller = function ($scope, $rootScope, GroupService) {
        $scope.treedata = GroupService.tree;

        $scope.opts = {
            isLeaf: function(node) {
                var isLeaf = node.type !== constant.treeNodeType.group;
                return isLeaf;
            },
            isSelectable: function(node) {
                return node.type == constant.treeNodeType.group;
            },
            equality: function(node1, node2) {
                if(node1 && node2) {
                    return node1.id == node2.id && node1.type == node2.type;
                } else {
                    return false;
                }
            }
        };

        $scope.selected = $rootScope.shareGroup.selected;

        $scope.predicate = '';
        $scope.comparator = false;

        $scope.showSelected = function(node, selected) {
            if(selected) {
                $scope.selected = node;
                $rootScope.shareGroup.selected = $scope.selected;
                // if user is modifying the group, load the group's description
                if($rootScope.$state.current.name == 'group.modify') {
                    GroupService.getGrpDetails({
                        id: $rootScope.shareGroup.selected.id
                    }, function (result) {
                        $scope.selected = result.data;
                        $rootScope.shareGroup.selected = $scope.selected;
                    }, function (err) {
                        console.log(err);
                    });
                }
            } else {
                $scope.selected = {};
                $rootScope.shareGroup.selected = {};
            }
        };
    };

    return {
        name: "GroupController",
        fn: ["$scope", "$rootScope", "GroupService", Controller]
    };

});
