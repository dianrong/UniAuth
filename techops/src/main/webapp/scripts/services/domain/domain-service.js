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
            modifyDomain: {
                method: 'POST',
                params: {
                    method: 'modify'
                },
                timeout: constant.reqTimeout
            },
            addDomain: {
                method: 'POST',
                params: {
                    method: 'add'
                },
                timeout: constant.reqTimeout
            },
            queryStakeholders: {
                method: 'POST',
                params: {
                    method: 'stakeholder-query'
                },
                timeout: constant.reqTimeout
            },
            addStakeholder: {
                method: 'POST',
                params: {
                    method: 'stakeholder-add'
                },
                timeout: constant.reqTimeout
            },
            deleteStakeholder: {
                method: 'POST',
                params: {
                    method: 'stakeholder-delete'
                },
                timeout: constant.reqTimeout
            },
            modifyStakeholder: {
                method: 'POST',
                params: {
                    method: 'stakeholder-modify'
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
