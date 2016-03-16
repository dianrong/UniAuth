define(['../../utils/constant', '../../utils/utils'], function (constant, utils) {
    var Service = function ($resource) {
        var svc = $resource(constant.apiBase + '/domain/:method', null, {
            queryDomain: {
                method: 'POST',
                params: {
                    method: 'query'
                },
                timeout: constant.reqTimeout
            }
        });
        svc.domainSelected = {};
        return svc;
    };

    return {
        name: "DomainService",
        svc: ["$resource", Service]
    };

});
