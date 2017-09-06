define(['../../utils/constant'], function (constant) {
    var Service = function ($resource) {
        var svc = $resource(constant.apiBase + '/synchronous/:type/:method', null, {
            queryHrLogs: {
                method: 'POST',
                params: {
                    type: 'hr',
                    method: 'log-query'
                },
                timeout: constant.reqTimeout
            },
            synchronousHrOnce: {
                method: 'POST',
                params: {
                    type: 'hr',
                    method: 'synchronous-once'
                },
                timeout: constant.reqTimeout
            },
            deleteHrExpiredFiles: {
                method: 'POST',
                params: {
                    type: 'hr',
                    method: 'delete-expired-files'
                },
                timeout: constant.reqTimeout
            }
        });
        return svc;
    };

    return {
        name: "SynchronousService",
        svc: ["$resource", Service]
    };

});
