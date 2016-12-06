define(['../../utils/constant'], function (constant) {
    var Service = function ($resource) {
        var svc = $resource(constant.apiBase + '/role/:method', null, {
            getRoles: {
                method: 'POST',
                params: {
                    method: 'query'
                },
                isArray: false,
                timeout: constant.reqTimeout
            },
            getAllRoleCodes: {
                method: 'GET',
                params: {
                    method: 'codes'
                },
                isArray: false,
                timeout: constant.reqTimeout
            },
            addRole: {
                method: 'POST',
                params: {
                    method: 'add'
                },
                isArray: false,
                timeout: constant.reqTimeout
            },
            updateRole: {
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
                    method: 'replace-perms-to-role'
                },
                isArray: false,
                timeout: constant.reqTimeout
            },
            queryPermsWithCheckedInfo: {
                method: 'POST',
                params: {
                    method: 'query-perms-with-checked-info'
                },
                isArray: false,
                timeout: constant.reqTimeout
            },
            replaceGroupsAndUsersToRole: {
                method: 'POST',
                params: {
                    method: 'replace-grps-users-to-role'
                },
                isArray: false,
                timeout: constant.reqTimeout
            },
            queryRoleUser: {
                method: 'POST',
                params: {
                    method: 'query-role-user'
                },
                isArray: false,
                timeout: constant.reqTimeout
            }
        });
        svc.roleShared = {};
        svc.roleUserGrpShared = {};
        return svc;
    };

    return {
        name: "RoleService",
        svc: ["$resource", Service]
    };

});
