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
            injectClasses : {
                labelUnselectable : "disabled-line-through"
            },
            isSelectable: function(node) {
                if(node.type !== constant.treeNodeType.group) {
                    return false;
                } else if(!node.ownerMarkup) {
                    return false;
                } else {
                    return true;
                }
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

        // for delete users and delete owners
        $scope.treeOptions = {
            isLeaf: function(node) {
                var isLeaf = node.type !== constant.treeNodeType.group;
                return isLeaf;
            },
            injectClasses : {
                labelUnselectable : "disabled-line-through"
            },
            isSelectable: function(node) {
                if(node.type == constant.treeNodeType.group) {
                    return false;
                } else {
                    return true;
                }
            },
            multiSelection: true
        };
        $scope.selectedNodes = [];
        $scope.showSelectedNodes = function(node, selected, parent) {
            node.parent = parent;
        };

        $scope.$state = $rootScope.$state;
        $scope.$watch('$state.current.name', function(){
            $scope.selectedNodes = [];
        });

    };

    return {
        name: "GroupController",
        fn: ["$scope", "$rootScope", "GroupService", Controller]
    };

});
