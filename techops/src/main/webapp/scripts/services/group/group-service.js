define(['../../utils/constant', '../../utils/utils'], function (constant, utils) {
    var Service = function ($resource) {
        var svc = $resource(constant.apiBase + '/group/:method/:method2', null, {
            queryGroup: {
                method: 'POST',
                params: {
                    method: 'query'
                },
                timeout: constant.reqTimeout
            },
            getTree: {
                method: 'POST',
                params: {
                    method: 'tree'
                },
                timeout: constant.reqTimeout
            },
            add: {
                method: 'POST',
                params: {
                    method: 'add'
                },
                timeout: constant.reqTimeout
            },
            del:{
            	method: 'POST',
                params: {
                    method: 'del'
                },
                timeout: constant.reqTimeout
            },
            modify: {
                method: 'POST',
                params: {
                    method: 'modify'
                },
                timeout: constant.reqTimeout
            },
            getGrpDetails: {
                method: 'GET',
                params: {
                    method: 'get'
                },
                timeout: constant.reqTimeout
            },
            addUser: {
                method: 'POST',
                params: {
                    method: 'adduser'
                },
                timeout: constant.reqTimeout
            },
            deleteUser: {
                method: 'POST',
                params: {
                    method: 'deleteuser'
                },
                timeout: constant.reqTimeout
            },
            replaceRolesToGrp: {
                method: 'POST',
                params: {
                    method: 'replace-roles-to-group'
                },
                isArray: false,
                timeout: constant.reqTimeout
            },
            queryRolesWithCheckedInfo: {
                method: 'POST',
                params: {
                    method: 'group-roles'
                },
                isArray: false,
                timeout: constant.reqTimeout
            },
            queryTagsWithCheckedInfo: {
                method: 'POST',
                params: {
                    method: 'group-tags'
                },
                isArray: false,
                timeout: constant.reqTimeout
            },
            replaceTagsToGrp: {
                method: 'POST',
                params: {
                    method: 'replace-tags-to-group'
                },
                isArray: false,
                timeout: constant.reqTimeout
            }
        });
        svc.grpShared = {};
        svc.tree = {};
        svc.roleUserGrpTree = {};
        svc.tagUserGrpTree = {};
        svc.syncTree = function(params, roleUserGrpTreeOrTree) {
            // separate the variable that different module use.
            if(!roleUserGrpTreeOrTree) {
                // only owner can operate the group in frontend.
                params.needOwnerMarkup = true;
                svc.getTree(params, function (res) {
                    svc.tree.data = res.data;
                    var expandedNodes = [];
                    if(params.userGroupType != undefined && params.userGroupType == 0) {
                      // only expand the first layer of the tree, reduce the rendering nodes for browser when add or delete normal user.
                      expandedNodes.push(svc.tree.data[0]);
                      svc.tree.expandedNodes = expandedNodes;
                    } else {
                      svc.tree.expandedNodes = utils.getParentNodeArray(svc.tree.data, expandedNodes);
                    }
                    svc.tree.msg = '';
                }, function (res) {
                    svc.tree.msg = '同步树查询失败.';
                    console.log('syncTree failed' + res);
                });
            } else {
                svc.getTree(params, function (res) {
                    svc.roleUserGrpTree.data = res.data;
                    //this is for force the tree to update data.
                    svc.roleUserGrpTree.data[0].date = new Date();
                    // only expand the first layer of the tree, reduce the rendering nodes for browser.
                    var expandedNodes = [];
                    expandedNodes.push(svc.roleUserGrpTree.data[0]);
                    svc.roleUserGrpTree.expandedNodes = expandedNodes;
                    svc.roleUserGrpTree.msg = '';
                }, function (res) {
                    svc.roleUserGrpTree.msg = 'Role -- UserGrp 同步树查询失败.';
                    console.log('syncTree failed' + res);
                });
            }
        };
        svc.syncTagTree = function(params) {
            svc.getTree(params, function (res) {
                svc.tagUserGrpTree.data = res.data;
                //this is for force the tree to update data.
                svc.tagUserGrpTree.data[0].date = new Date();
                // only expand the first layer of the tree, reduce the rendering nodes for browser.
                var expandedNodes = [];
                expandedNodes.push(svc.tagUserGrpTree.data[0]);
                svc.tagUserGrpTree.expandedNodes = expandedNodes;
                svc.tagUserGrpTree.msg = '';
            }, function (res) {
                svc.tagUserGrpTree.msg = 'Role -- UserGrp 同步树查询失败.';
                console.log('syncTree failed' + res);
            });
        };
        return svc;
    };

    return {
        name: "GroupService",
        svc: ["$resource", Service]
    };

});
