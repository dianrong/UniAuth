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
            modifyUser: {
                method: 'POST',
                params: {
                    method: 'modify'
                },
                isArray: false,
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
