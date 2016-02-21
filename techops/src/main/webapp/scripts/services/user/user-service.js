define(['../../utils/constant'], function (constant) {
    var Service = function ($resource) {
        var svc = $resource(constant.apiBase + '/user/:method/:method2/:id/:role/:roleId', null, {
            getLoginDomains: {
                method: 'GET',
                params: {
                    method: 'techops',
                    method2:"domain"
                },
                timeout: constant.reqTimeout
            },
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
            /*
             /user/:id
             */
            getUserById: {
                method: 'GET',
                params: {
                    id: '@id'
                },
                timeout: constant.reqTimeout
            },
            modifyUser: {
                method: 'POST',
                params: {
                    method: 'update'
                },
                isArray: false,
                timeout: constant.reqTimeout
            },
            /*
             /user/:id/role
             */
            getRoleOfUser: {
                method: 'GET',
                params: {
                    id: '@id',
                    role: 'role'
                },
                timeout: constant.reqTimeout
            },
            /*
             /user/addUserRoles
             */
            manageRoleOfUser: {
                method: 'POST',
                params: {
                    method: 'addUserRoles'
                },
                timeout: constant.reqTimeout
            },
            /*
             /user/:id/role/:roleId
             */
            removeRoleOfUser: {
                method: 'POST',
                params: {
                    method: 'rmUserRoles'
                },
                isArray: false,
                timeout: constant.reqTimeout
            },
            /*
             Get     /user/:id/url
             */
            getuserUrls: {
                method: 'GET',
                params: {
                    id: '@id',
                    catalog: 'url'
                },
                isArray: true,
                timeout: constant.reqTimeout
            },
            getProvince: {
                method: 'GET',
                params: {
                    id: '@id',
                    catalog: 'url'
                },
                isArray: true,
                timeout: constant.reqTimeout
            },
            getDistrictByProId: {
                method: 'GET',
                params: {
                    id: '@id',
                    catalog: 'url'
                },
                isArray: true,
                timeout: constant.reqTimeout
            },
            getCityByDistrictId: {
                method: 'GET',
                params: {
                    id: '@id',
                    catalog: 'url'
                },
                isArray: true,
                timeout: constant.reqTimeout
            }
        });
        return svc;
    };

    return {
        name: "UserService",
        svc: ["$resource", Service]
    };


});
