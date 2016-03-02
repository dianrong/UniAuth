define(['../../utils/constant'], function (constant) {
    var Service = function ($resource) {
        var svc = $resource(constant.apiBase + '/group/:method/:method2', null, {
            getTree: {
                method: 'POST',
                params: {
                    method: 'tree'
                },
                timeout: constant.reqTimeout
            }
        });
        svc.tree = {};
        return svc;
    };

    return {
        name: "GroupService",
        svc: ["$resource", Service]
    };

});
