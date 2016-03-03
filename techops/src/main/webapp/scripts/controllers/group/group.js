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
            }
        };

        $scope.selected = $rootScope.groupSelected;

        $scope.showSelected = function(node, selected) {
            if(selected) {
                $scope.selected = node;
                $rootScope.groupSelected = $scope.selected;
            } else {
                $scope.selected = {};
                $rootScope.groupSelected = {};
            }
        };
    };

    return {
        name: "GroupController",
        fn: ["$scope", "$rootScope", "GroupService", Controller]
    };

});
