/**
 * Module representing a shirt.
 * @module controllers/login
 */
define(['../../utils/constant'], function (constant) {
    /**
     * A module representing a login controller.
     * @exports controllers/login
     */
    var Service = function ($resource) {
        /*
         Get     /user/:id
         Put     /user/:id/user/:ids
         delete  /user/:id/user/:id
         Get     /user/:id/menu
         Get     /user/:id/url
         */
        var svc = $resource(constant.apiBase + '/user/:method/:id/:role/:roleId', null, {
            /*
             /user
             */
            getUsers: {
                method: 'POST',
                params: {
                    method: 'query'
                },
                isArray: false,
                timeout: constant.reqTimeout
            },
            addUser: {
                method: 'POST',
                params: {},
                isArray: false,
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
