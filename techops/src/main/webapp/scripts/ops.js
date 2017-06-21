require(['app',
    'controllers/common/dialogs/enable-disable',
    'controllers/user/user',
    'controllers/user/dialogs/add',
    'controllers/user/dialogs/reset-password',
    'controllers/user/dialogs/modify',
    'controllers/user/dialogs/manage-eav',
    'controllers/user/dialogs/user-eav',
    'controllers/group/group',
    'controllers/group/group-add',
    'controllers/group/group-modify',
    'controllers/group/group-move',
    'controllers/group/group-delete',
    'controllers/group/group-add-user',
    'controllers/group/group-delete-user',
    'controllers/group/group-move-user',
    'controllers/group/group-add-owner',
    'controllers/group/group-delete-owner',
    'controllers/group/group-move-owner',
    'controllers/role/role',
    'controllers/role/dialogs/add',
    'controllers/role/dialogs/modify',
    'controllers/perm/perm',
    'controllers/perm/dialogs/add',
    'controllers/perm/dialogs/modify',
    'controllers/rel/rel',
    'controllers/rel/rel-role--perm',
    'controllers/rel/rel-perm--role',
    'controllers/rel/rel-user--role',
    'controllers/rel/rel-grp--role',
    'controllers/rel/rel-role--grp',
    'controllers/rel/rel-role--user',
    'controllers/rel/rel-tag--grp',
    'controllers/rel/rel-tag--user',
    'controllers/rel/rel-user--tag',
    'controllers/rel/rel-grp--tag',
    'controllers/domain/domain',
    'controllers/domain/dialogs/add-domain',
    'controllers/domain/dialogs/add-stakeholder',
    'controllers/domain/dialogs/modify-stakeholder',
    'controllers/batch/batch',
    'controllers/audit/audit',
    'controllers/cfg/cfg',
    'controllers/cfg/dialogs/add',
    'controllers/cfg/dialogs/modify',
    'controllers/tag/tag',
    'controllers/tag/tag-tag-info',
    'controllers/tag/tag-tag-type',
    'controllers/tag/dialogs/tag-add',
    'controllers/tag/dialogs/tag-modify',
    'services/common/http-interceptor-service',
    'services/common/alert-service',
    'services/user/user-service',
    'services/user/eav-service',
    'services/group/group-service',
    'services/role/role-service',
    'services/perm/perm-service',
    'services/domain/domain-service',
    'services/audit/audit-service',
    'services/cfg/cfg-service',
    'services/i18n/i18n-service',
    'services/tag/tag-service',
    'directive/common/uniauth-transfer'],

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