define(['../../utils/constant', '../../utils/utils'], function (constant, utils) {
    var Service = function ($resource) {
        var svc = $resource(constant.apiBase + '/domain/:method', null, {
            queryDomains: {
                method: 'POST',
                params: {
                    method: 'query'
                },
                timeout: constant.reqTimeout
            },
            queryStakeholders: {
                method: 'POST',
                params: {
                    method: 'stakeholder-query'
                },
                timeout: constant.reqTimeout
            }
        });
        svc.domainShared = {};
        return svc;
    };

    return {
        name: "DomainService",
        svc: ["$resource", Service]
    };

});
