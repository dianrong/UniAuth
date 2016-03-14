define(['../../utils/constant', '../../utils/utils'], function (constant, utils) {
    var Service = function ($resource) {
        var svc = $resource(constant.apiBase + '/group/:method/:method2', null, {
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
            }
        });
        svc.grpShared = {};
        svc.tree = {};
        svc.syncTree = function(params) {
            svc.getTree(params, function (res) {
                svc.tree.data = res.data;
                var expandedNodes = [];
                svc.tree.expandedNodes = utils.getParentNodeArray(svc.tree.data, expandedNodes);
                svc.tree.msg = '';
            }, function (res) {
                svc.tree.msg = '同步树查询失败.';
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
