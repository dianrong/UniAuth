define(['../../utils/constant'], function (constant) {
    var Service = function ($resource) {
        var svc = $resource(constant.apiBase + '/user/:method/:method2/:id/:role/:roleId', null, {
            getUsers: {
                method: 'POST',
                params: {
                    method: 'query'
                },
                isArray: false,
                timeout: constant.reqTimeout
            },
            enableDisableUser: {
                method: 'POST',
                params: {
                    method: 'enable-disable'
                },
                isArray: false,
                timeout: constant.reqTimeout
            },
            addUser: {
                method: 'POST',
                params: {
                    method: 'add'
                },
                isArray: false,
                timeout: constant.reqTimeout
            },
            unlock: {
                method: 'POST',
                params: {
                    method: 'unlock'
                },
                timeout: constant.reqTimeout
            },
            resetpassword: {
                method: 'POST',
                params: {
                    method: 'resetpassword'
                },
                timeout: constant.reqTimeout
            },
            modifyUser: {
                method: 'POST',
                params: {
                    method: 'modify'
                },
                isArray: false,
                timeout: constant.reqTimeout
            },
            replaceRolesToUser: {
                method: 'POST',
                params: {
                    method: 'replace-roles-to-user'
                },
                isArray: false,
                timeout: constant.reqTimeout
            },
            queryRolesWithCheckedInfo: {
                method: 'POST',
                params: {
                    method: 'user-roles'
                },
                isArray: false,
                timeout: constant.reqTimeout
            },
            queryTagsWithCheckedInfo: {
                method: 'POST',
                params: {
                    method: 'user-tags'
                },
                isArray: false,
                timeout: constant.reqTimeout
            },
            replaceTagsToUser : {
            	method: 'POST',
                params: {
                    method: 'replace-tags-to-user'
                },
                isArray: false,
                timeout: constant.reqTimeout
            }
        });
        svc.userShared = {};
        return svc;
    };

    return {
        name: "UserService",
        svc: ["$resource", Service]
    };


});
