/**
 * Module representing a shirt.
 * @module controllers/login
 */
define(function () {
    /**
     * A module representing a login controller.
     * @exports controllers/login
     */
    var Service = function ($rootScope) {
        var alertService = {};

        $rootScope.alerts = [];
        $rootScope.autoDismissAlerts = [];

        alertService.addAlert = function(type, msg) {
            $rootScope.alerts.unshift({'type': type, 'msg': msg, 'close': function(){ alertService.closeAlert(this); }});
        };

        alertService.closeAlert = function(alert) {
            alertService.closeAlertIdx($rootScope.alerts.indexOf(alert));
        };

        alertService.closeAlertIdx = function(index) {
            $rootScope.alerts.splice(index, 1);
        };

        alertService.addAutoDismissAlert = function(type, msg) {
            $rootScope.autoDismissAlerts.unshift({'type': type, 'msg': msg, 'close': function(){ alertService.closeAutoDismissAlert(this); }});
        };

        alertService.closeAutoDismissAlert = function(alert) {
            alertService.closeAutoDismissAlertIdx($rootScope.autoDismissAlerts.indexOf(alert));
        };

        alertService.closeAutoDismissAlertIdx = function(index) {
            $rootScope.autoDismissAlerts.splice(index, 1);
        };

        return alertService;
    };

    return {
        name: "AlertService",
        svc: ["$rootScope", Service]
    }

});
