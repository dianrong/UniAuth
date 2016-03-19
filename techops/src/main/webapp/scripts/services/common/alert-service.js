/**
 * Module representing a shirt.
 * @module controllers/login
 */
define(['../../utils/constant'], function (constant) {
    /**
     * A module representing a login controller.
     * @exports controllers/login
     */
    var Service = function ($rootScope) {
        var alertService = {};

        // 创建一个全局的 alert 数组
        $rootScope.alerts = [];

        alertService.add = function(type, msg) {
            $rootScope.alerts.push({'type': type, 'msg': msg, 'close': function(){ alertService.closeAlert(this); }});
        };

        alertService.closeAlert = function(alert) {
            alertService.closeAlertIdx($rootScope.alerts.indexOf(alert));
        };

        alertService.closeAlertIdx = function(index) {
            $rootScope.alerts.splice(index, 1);
        };

        return alertService;
    };

    return {
        name: "AlertService",
        svc: ["$rootScope", Service]
    }

});
