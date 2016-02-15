/**
 * Module representing a shirt.
 * @module controllers/login
 */
define(['utils/Constant'], function (Constant) {
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
        var svc = $resource(Constant.apiBase + '/user/:method/:id/:role/:roleId', null, {
            /*
             /user
             */
            getUsers: {
                method: 'POST',
                params: {
                    method: 'query'
                },
                isArray: false,
                timeout: Constant.reqTimeout
            },
            addUser: {
                method: 'POST',
                params: {},
                isArray: false,
                timeout: Constant.reqTimeout
            },
            /*
             /user/:id
             */
            getUserById: {
                method: 'GET',
                params: {
                    id: '@id'
                },
                timeout: Constant.reqTimeout
            },
            modifyUser: {
                method: 'POST',
                params: {
                    method: 'update'
                },
                isArray: false,
                timeout: Constant.reqTimeout
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
                timeout: Constant.reqTimeout
            },
            /*
             /user/addUserRoles
             */
            manageRoleOfUser: {
                method: 'POST',
                params: {
                    method: 'addUserRoles'
                },
                timeout: Constant.reqTimeout
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
                timeout: Constant.reqTimeout
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
                timeout: Constant.reqTimeout
            },
            getProvince: {
                method: 'GET',
                params: {
                    id: '@id',
                    catalog: 'url'
                },
                isArray: true,
                timeout: Constant.reqTimeout
            },
            getDistrictByProId: {
                method: 'GET',
                params: {
                    id: '@id',
                    catalog: 'url'
                },
                isArray: true,
                timeout: Constant.reqTimeout
            },
            getCityByDistrictId: {
                method: 'GET',
                params: {
                    id: '@id',
                    catalog: 'url'
                },
                isArray: true,
                timeout: Constant.reqTimeout
            }
        });
        return svc;
    };

    return {
        name: "UserSvc",
        svc: ["$resource", Service]
    };


});
