require(['app',
    'controllers/user/user',
    'services/common/logoutSvc',
    'services/user/userSvc',
    'services/common/commonSvc'],

    function(app) {
        var components = Array.prototype.slice.call(arguments, 1);
        for (var i = 0, len = components.length; i < len; i++) {
            if (components[i].svc) {
                // Register Factory
                app.factory(components[i].name, components[i].svc);
            } else if (components[i].fn) {
                // Register Controllder
                app.controller(components[i].name, components[i].fn);
            } else if (components[i].directiveFn) {
                app.directive(components[i].name, components[i].directiveFn);
            } else if (components[i].filterFn) {
                app.filter(components[i].name, components[i].filterFn);
            }
        }
        app.bootstrap();
    }
);