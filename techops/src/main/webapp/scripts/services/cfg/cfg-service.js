define(['../../utils/constant'], function (constant) {
    var Service = function ($resource) {
        var svc = $resource(constant.apiBase + '/cfg/:method', null, {
            queryConfig: {
                method: 'POST',
                params: {
                    method: 'query'
                },
                isArray: false,
                timeout: constant.reqTimeout
            },
            addOrUpdateConfig: {
                method: 'POST',
                params: {
                    method: 'add-or-update'
                },
                headers : {'Content-Type': undefined},
                transformRequest : angular.identity,
                isArray: false,
                timeout: constant.reqTimeout
            },
            delCfg: {
                method: 'POST',
                params: {
                    method: 'del'
                },
                isArray: false,
                timeout: constant.reqTimeout
            },
            getAllCfgTypes: {
                method: 'GET',
                params: {
                    method: 'cfg-types'
                },
                isArray: false,
                timeout: constant.reqTimeout
            }
        });
        return svc;
    };

    return {
        name: "CfgService",
        svc: ["$resource", Service]
    };


});
