define(['../../utils/constant'], function (constant) {
    var Service = function ($resource) {
        var svc = $resource(constant.apiBase + '/audit/:method', null, {
            queryAudits: {
                method: 'POST',
                params: {
                    method: 'query'
                },
                timeout: 1000*300
            },
            deleteAudits: {
                method: 'POST',
                params: {
                    method: 'delete'
                },
                timeout: constant.reqTimeout
            }
        });
        return svc;
    };

    return {
        name: "AuditService",
        svc: ["$resource", Service]
    };

});
