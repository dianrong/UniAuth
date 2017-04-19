define(['../../utils/constant'], function (constant) {
    /**
     * A module representing a User controller.
     * @exports controllers/User
     */
    var Controller = function ($scope, $rootScope, $state, GroupService) {

        $scope.treedata = GroupService.tree;
        //Used to record whether the current operation is moving
        $rootScope.onMove = false;
        //Used to record the move group operation's 'from' group and 'to' group
        $rootScope.moveGroup = {};
        //Used to record the move group user operation's 'user' and 'group'
        $rootScope.moveUser = {};

        $scope.selected = $rootScope.shareGroup.selected;

        $scope.predicate = '';
        $scope.comparator = false;

        //this function is temporarily not used     2017-03-30 16:24:21
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

        $scope.groupOptions = {
            isLeaf: function(node) {
                var isLeaf = node.type !== constant.treeNodeType.group;
                return isLeaf;
            },
            injectClasses : {
                labelUnselectable : "disabled-line-through"
            },
            dirSelectable:false,
            equality: function(node1, node2) {
                if(node1 && node2) {
                    return node1.id == node2.id && node1.type == node2.type;
                } else {
                    return false;
                }
            }
        };
        $scope.groupUserOptions = {
            isLeaf: function(node) {
                var isLeaf = node.type !== constant.treeNodeType.group;
                return isLeaf;
            },
            injectClasses : {
                labelUnselectable : "disabled-line-through"
            },
            dirSelectable:false,
            multiSelection: true
        };
        //for record the current group
        $scope.toOperateGroup = function(node,event){
            $rootScope.targetGroup = node;
            $rootScope.isTarget = true;
            if($state.includes("group.this")) {
                GroupService.getGrpDetails({
                    id: $rootScope.targetGroup.id
                }, function (result) {
                    $rootScope.targetGroup = result.data;
                }, function (err) {
                    console.log(err);
                });
            }

            event.stopPropagation();
        };
        //for record the current 'from' group and 'to' group that used to move group
        $scope.toMoveGroup = function (node,isTarget,event) {
            if(!isTarget){
                $rootScope.onMove = true;
                $rootScope.moveGroup.from = node;
            }else{
                // $rootScope.onMove = false;
                $rootScope.moveGroup.to = node;
            }
            event.stopPropagation();
        };
        //for record the current 'user' and 'group' that used to move group user
        $scope.toMoveUser = function (node,isTarget,event,parent) {
            if(!isTarget){
                $rootScope.onMove = true;
                $rootScope.moveUser.user = node;
                $rootScope.moveUser.user.parent = parent;
            }else{
                // $rootScope.onMove = false;
                $rootScope.moveUser.group = node;
            }
            event.stopPropagation();
        };
        $scope.selectedNodes = [];
        $scope.showSelectedNodes = function(node, selected, parent) {
            node.parent = parent;
        };

        $scope.$state = $rootScope.$state;
        $scope.$watch('$state.current.name', function(newValue, oldValue){
            var newAry = newValue.split(".");
            var oldAry = oldValue.split(".");
            if(!(oldAry[0]==newAry[0] && oldAry[1]==newAry[1])){
                //clear something
                $scope.selectedNodes = [];
                $rootScope.onMove = false;
                $rootScope.targetGroup = {};
                $rootScope.moveGroup = {};
                $rootScope.moveUser = {};
            }
        });

        //initialize the tree component
        $rootScope.initTree = function(onlyShowGroupType, userGroupType, isNeedResetExpandedNodes){
            var paramsCtlLevel = {
                onlyShowGroup:onlyShowGroupType,
                userGroupType:userGroupType
            };
            GroupService.syncTree(paramsCtlLevel,false,isNeedResetExpandedNodes);
        };
        //for determine the icon button status
        $rootScope.judge = {
            isAlAddGroup:function (node) {
                return $state.current.name.indexOf('group.this')>-1;
            },
            isAlDelGroup:function (node) {
                return !node.isRootGrp && $state.current.name.indexOf('group.this')>-1;
            },
            isAlEditGroup:function (node) {
                return !node.isRootGrp && $state.current.name.indexOf('group.this')>-1;
            },
            isAlMoveGroup:function (node) {
                return !node.isRootGrp && $state.current.name.indexOf('group.this')>-1;
            },
            isAlMoveToGroup:function (node) {
                return $state.current.name.indexOf('group.this')>-1;
            },
            isAlAddGroupUser:function (node) {
                return node.type==constant.treeNodeType.group && $state.current.name.indexOf('group.user')>-1;
            },
            isAlDelGroupUser:function (node) {
                return node.type==constant.treeNodeType.memberUser && $state.current.name.indexOf('group.user')>-1;
            },
            isAlMoveGroupUser:function (node) {
                return node.type==constant.treeNodeType.memberUser && $state.current.name.indexOf('group.user')>-1;
            },
            isAlAddGroupOwner:function (node) {
                return node.type==constant.treeNodeType.group && $state.current.name.indexOf('group.owner')>-1;
            },
            isAlDelGroupOwner:function (node) {
                return node.type==constant.treeNodeType.ownerUser && $state.current.name.indexOf('group.owner')>-1;
            },
            isAlMoveGroupOwner:function (node) {
                return node.type==constant.treeNodeType.ownerUser && $state.current.name.indexOf('group.owner')>-1;
            }
        };
        //reset the tree component by '$state'
        $rootScope.reset = function (isNeedResetExpandedNodes,isResetPanel){
            //clear something
            $scope.selectedNodes = [];
            $rootScope.onMove = false;
            // $rootScope.targetGroup = {};
            $rootScope.moveGroup = {};
            $rootScope.moveUser = {};

            if ($state.includes("group.this")) {
                if(isResetPanel){
                    $state.go("group.this");
                }
                $rootScope.initTree(true, -1, isNeedResetExpandedNodes);

            } else if($state.includes("group.user")){
                if(isResetPanel){
                    $state.go("group.user");
                }
                $rootScope.initTree(false, 0, isNeedResetExpandedNodes);

            } else if($state.includes("group.owner")){
                if(isResetPanel){
                    $state.go("group.owner");
                }
                $rootScope.initTree(false, 1, isNeedResetExpandedNodes);
            }
        };
        $rootScope.reset(true);
    };

    return {
        name: "GroupController",
        fn: ["$scope", "$rootScope", "$state", "GroupService", Controller]
    };
});
