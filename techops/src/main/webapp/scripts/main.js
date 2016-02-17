// Configure RequireJS
require.config({

  // Base url for script files
  baseUrl: 'scripts',
  // Alias libraries paths
  // http://stackoverflow.com/questions/19307198/angular-js-with-require-js-getting-uncaught-error-injectormodulerr
  // https://github.com/angular-ui/bootstrap/issues/1512
  paths: {
    'angular': 'lib/angular/angular.min',
    'angular.ui.bootstrap': 'lib/angular-bootstrap/ui-bootstrap-tpls.min',
    'ngResource': 'lib/angular-resource/angular-resource.min',
    'ngLocalStorage': 'lib/angular-local-storage/dist/angular-local-storage.min',
    'ngRoute': 'lib/angular-route/angular-route.min',
    'ngCookies': 'lib/angular-cookies/angular-cookies.min',
    'ngTranslate': 'lib/angular-translate/angular-translate.min',
    'ngTranslateLoad': 'lib/angular-translate-loader-static-files/angular-translate-loader-static-files.min',
    'ngSanitize': 'lib/angular-sanitize/angular-sanitize.min',
    'angularFileUpload': 'lib/angular-file-upload/dist/angular-file-upload.min',
    'jQuery': 'lib/jquery/jquery-2.1.4.min',
    'datepicker': 'lib/bootstrap-datepicker/bootstrap-datepicker.min',
    'dialogs':'lib/angular-dialog-service/dist/dialogs.min',
    'dialogsDefaultTranslations':'lib/angular-dialog-service/dist/dialogs-default-translations.min'
  },

  // Angular does not support AMD out of the box, put it in a shim
  shim: {
    ngResource: {
      deps: ['angular']
    },
    ngLocalStorage: {
      deps: ['angular']
    },
    'angular.ui.bootstrap': {
      deps: ['angular']
    },
    ngCookies: {
      deps: ['angular']
    },
    ngTranslate: {
      deps: ['angular']
    },
    ngTranslateLoad: {
      deps: ['ngTranslate']
    },
    ngRoute: {
      deps: ['angular']
    },
    angularFileUpload: {
      deps: ['angular']
    },
    ngSanitize: {
      deps:['angular']
    },
    datepicker: {
      deps: ['jQuery']
    },
    dialogsDefaultTranslations: {
      deps: ['angular']
    },
    dialogs: {
      deps: ['angular']
    },
    angular: {
      exports: 'angular'
    }
  },
  waitSeconds: 0
});