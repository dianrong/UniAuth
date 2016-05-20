define(['../../utils/constant'], function (constant) {
    var Service = function ($resource) {
        var svc = $resource(constant.apiBase + '/perm/:method', null, {
            getPerms: {
                method: 'POST',
                params: {
                    method: 'query'
                },
                isArray: false,
                timeout: constant.reqTimeout
            },
            getAllPermTypes: {
                method: 'GET',
                params: {
                    method: 'types'
                },
                isArray: false,
                timeout: constant.reqTimeout
            },
            addPerm: {
                method: 'POST',
                params: {
                    method: 'add'
                },
                isArray: false,
                timeout: constant.reqTimeout
            },
            updatePerm: {
                method: 'POST',
                params: {
                    method: 'update'
                },
                isArray: false,
                timeout: constant.reqTimeout
            },
            replacePermsToRole: {
                method: 'POST',
                params: {
                    method: 'replace-roles-to-perm'
                },
                isArray: false,
                timeout: constant.reqTimeout
            },
            queryRolesWithCheckedInfo: {
                method: 'POST',
                params: {
                    method: 'all-roles-to-perm'
                },
                isArray: false,
                timeout: constant.reqTimeout
            }
        });
        svc.permShared = {};
        return svc;
    };

    return {
        name: "PermService",
        svc: ["$resource", Service]
    };

});
