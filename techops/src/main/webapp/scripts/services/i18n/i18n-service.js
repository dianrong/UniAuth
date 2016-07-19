define(['../../utils/constant'], function (constant) {
    var Service = function ($resource) {
        var svc = $resource(constant.apiBase + '/i18n/:method', null, {
            getLanguages: {
                method: 'POST',
                params: {
                    method: 'query'
                },
                isArray: false,
                timeout: constant.reqTimeout
            }
        });
        return svc;
    };

    return {
        name: "I18NService",
        svc: ["$resource", Service]
    };


});
