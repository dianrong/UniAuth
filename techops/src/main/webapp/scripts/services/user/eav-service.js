define(['../../utils/constant'], function (constant) {
    var Service = function ($resource) {
        var svc = $resource(constant.apiBase + '/eav/:element/:method', null, {
            queryEavCodes: {
                method: 'POST',
                params: {
                	element : 'code',
                    method: 'query'
                },
                isArray: false,
                timeout: constant.reqTimeout
            },
            addEavCode: {
                method: 'POST',
                params: {
                	element : 'code',
                    method: 'insert'
                },
                timeout: constant.reqTimeout
            },
            modifyEavCode: {
                method: 'POST',
                params: {
                	element : 'code',
                    method: 'modify'
                },
                timeout: constant.reqTimeout
            },
            queryUserEav: {
                method: 'POST',
                params: {
                	element : 'user',
                    method: 'query'
                },
                timeout: constant.reqTimeout
            },
            modfyUserEav: {
                method: 'POST',
                params: {
                	element : 'user',
                    method: 'modify'
                },
                timeout: constant.reqTimeout
            },
            addUserEav: {
                method: 'POST',
                params: {
                	element : 'user',
                    method: 'insert'
                },
                timeout: constant.reqTimeout
            },
            deleteUserEav: {
                method: 'POST',
                params: {
                	element : 'user',
                    method: 'delete'
                },
                timeout: constant.reqTimeout
            },
        });
        return svc;
    };

    return {
        name: "EavService",
        svc: ["$resource", Service]
    };
});
