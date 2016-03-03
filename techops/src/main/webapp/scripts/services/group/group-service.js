define(['../../utils/constant', '../../utils/utils'], function (constant, utils) {
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
        svc.syncTree = function(params) {
            svc.getTree(params, function (res) {
                svc.tree.data = res.data;
                var expandedNodes = [];
                svc.tree.expandedNodes = utils.getParentNodeArray(svc.tree.data, expandedNodes);
            }, function (res) {
                console.log('syncTree failed' + res);
            });
        };
        return svc;
    };

    return {
        name: "GroupService",
        svc: ["$resource", Service]
    };

});
