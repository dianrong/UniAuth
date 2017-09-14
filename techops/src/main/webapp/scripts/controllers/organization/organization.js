define(['../../utils/constant'], function (constant) {
    var Controller = function ($scope, $rootScope, $state, OrganizationService) {

        $scope.treedata = OrganizationService.tree;
        //Used to record whether the current operation is moving
        $rootScope.onMove = false;
        //Used to record the move organization operation's 'from' organization and 'to' organization
        $rootScope.moveOrganization = {};
        //Used to record the move organization user operation's 'user' and 'organization'
        $rootScope.moveUser = {};

        $scope.selected = $rootScope.shareOrganization.selected;

        $scope.predicate = '';
        $scope.comparator = false;

        //this function is temporarily not used     2017-03-30 16:24:21
        $scope.showSelected = function(node, selected) {
            if(selected) {
                $scope.selected = node;
                $rootScope.shareOrganization.selected = $scope.selected;
                // if user is modifying the organization, load the organization's description
                if($rootScope.$state.current.name == 'organization.modify') {
                    OrganizationService.getOrganizationDetails({
                        id: $rootScope.shareOrganization.selected.id
                    }, function (result) {
                        $scope.selected = result.data;
                        $rootScope.shareOrganization.selected = $scope.selected;
                    }, function (err) {
                        console.log(err);
                    });
                }
            } else {
                $scope.selected = {};
                $rootScope.shareOrganization.selected = {};
            }
        };

        $scope.organizationOptions = {
            isLeaf: function(node) {
                var isLeaf = node.type !== constant.treeNodeType.organization;
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
        $scope.organizationUserOptions = {
            isLeaf: function(node) {
                var isLeaf = node.type !== constant.treeNodeType.organization;
                return isLeaf;
            },
            injectClasses : {
                labelUnselectable : "disabled-line-through"
            },
            dirSelectable:false,
            multiSelection: true
        };
        //for record the current organization
        $scope.toOperateOrganization = function(node,event){
            $rootScope.targetOrganization = node;
            $rootScope.isTarget = true;
            if($state.includes("organization.this")) {
                OrganizationService.getOrganizationDetails({
                    id: $rootScope.targetOrganization.id
                }, function (result) {
                    $rootScope.targetOrganization = result.data;
                }, function (err) {
                    console.log(err);
                });
            }

            event.stopPropagation();
        };
        //for record the current 'from' organization and 'to' organization that used to move organization
        $scope.toMoveOrganization = function (node,isTarget,event) {
            if(!isTarget){
                $rootScope.onMove = true;
                $rootScope.moveOrganization.from = node;
            }else{
                // $rootScope.onMove = false;
                $rootScope.moveOrganization.to = node;
            }
            event.stopPropagation();
        };
        //for record the current 'user' and 'organization' that used to move organization user
        $scope.toMoveUser = function (node,isTarget,event,parent) {
            if(!isTarget){
                $rootScope.onMove = true;
                $rootScope.moveUser.user = node;
                $rootScope.moveUser.user.parentId = parent.id;
            }else{
                // $rootScope.onMove = false;
                $rootScope.moveUser.organization = node;
            }
            event.stopPropagation();
        };
        $scope.selectedNodes = [];
        $scope.showSelectedNodes = function(node, selected, parent) {
            node.parentId = parent.id;
        };

        $scope.$state = $rootScope.$state;
        $scope.$watch('$state.current.name', function(newValue, oldValue){
            var newAry = newValue.split(".");
            var oldAry = oldValue.split(".");
            if(!(oldAry[0]==newAry[0] && oldAry[1]==newAry[1])){
                //clear something
                $scope.selectedNodes = [];
                $rootScope.onMove = false;
                $rootScope.targetOrganization = {};
                $rootScope.moveOrganization = {};
                $rootScope.moveUser = {};
            }
        });

        //initialize the tree component
        $rootScope.initTree = function(onlyShowOrganizationType, userOrganizationType, isNeedResetExpandedNodes){
            var paramsCtlLevel = {
                onlyShowGroup:onlyShowOrganizationType,
                userGroupType:userOrganizationType
            };
            OrganizationService.syncTree(paramsCtlLevel,isNeedResetExpandedNodes);
        };
        //for determine the icon button status
        $rootScope.judge = {
            isAlAddOrganization:function (node) {
                return $state.current.name.indexOf('organization.this')>-1;
            },
            isAlDelOrganization:function (node) {
                return !node.isRootGrp && $state.current.name.indexOf('organization.this')>-1;
            },
            isAlEditOrganization:function (node) {
                return !node.isRootGrp && $state.current.name.indexOf('organization.this')>-1;
            },
            isAlMoveOrganization:function (node) {
                return !node.isRootGrp && $state.current.name.indexOf('organization.this')>-1;
            },
            isAlMoveToOrganization:function (node) {
                return $state.current.name.indexOf('organization.this')>-1;
            },
            isAlAddOrganizationUser:function (node) {
                return node.type==constant.treeNodeType.organization && $state.current.name.indexOf('organization.user')>-1;
            },
            isAlDelOrganizationUser:function (node) {
                return node.type==constant.treeNodeType.memberUser && $state.current.name.indexOf('organization.user')>-1;
            },
            isAlMoveOrganizationUser:function (node) {
                return node.type==constant.treeNodeType.memberUser && $state.current.name.indexOf('organization.user')>-1;
            },
            isAlAddOrganizationOwner:function (node) {
                return node.type==constant.treeNodeType.organization && $state.current.name.indexOf('organization.owner')>-1;
            },
            isAlDelOrganizationOwner:function (node) {
                return node.type==constant.treeNodeType.ownerUser && $state.current.name.indexOf('organization.owner')>-1;
            },
            isAlMoveOrganizationOwner:function (node) {
                return node.type==constant.treeNodeType.ownerUser && $state.current.name.indexOf('organization.owner')>-1;
            }
        };
        //reset the tree component by '$state'
        $rootScope.reset = function (isNeedResetExpandedNodes,isResetPanel){
            //clear something
            $scope.selectedNodes = [];
            $rootScope.onMove = false;
            // $rootScope.targetOrganization = {};
            $rootScope.moveOrganization = {};
            $rootScope.moveUser = {};

            if ($state.includes("organization.this")) {
                if(isResetPanel){
                    $state.go("organization.this");
                }
                $rootScope.initTree(true, -1, isNeedResetExpandedNodes);

            } else if($state.includes("organization.user")){
                if(isResetPanel){
                    $state.go("organization.user");
                }
                $rootScope.initTree(false, 0, isNeedResetExpandedNodes);

            } else if($state.includes("organization.owner")){
                if(isResetPanel){
                    $state.go("organization.owner");
                }
                $rootScope.initTree(false, 1, isNeedResetExpandedNodes);
            }
        };
        $rootScope.reset(true);
    };

    return {
        name: "OrganizationController",
        fn: ["$scope", "$rootScope", "$state", "OrganizationService", Controller]
    };
});
