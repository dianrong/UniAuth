require.config({
  baseUrl: 'scripts',
  paths: {
    'angular': 'lib/angular/angular.min',
    'angular.ui.bootstrap': 'lib/angular-bootstrap/ui-bootstrap-tpls.min',
    'ngResource': 'lib/angular-resource/angular-resource.min',
    'ngLocalStorage': 'lib/angular-local-storage/dist/angular-local-storage.min',
    'ngRoute': 'lib/angular-route/angular-route.min',
    'angular.ui.router': 'lib/angular-ui-router/release/angular-ui-router.min',
    'ngCookies': 'lib/angular-cookies/angular-cookies.min',
    'ngTranslate': 'lib/angular-translate/angular-translate.min',
    'ngTranslateLoad': 'lib/angular-translate-loader-url/angular-translate-loader-url.min',
    'ngTranslateLoadUrl':'lib/angular-translate-loader-url/angular-translate-loader-url.min',
    'ngSanitize': 'lib/angular-sanitize/angular-sanitize.min',
    'ngAnimate': 'lib/angular-animate/angular-animate.min',
    'angularFileUpload': 'lib/angular-file-upload/dist/angular-file-upload.min',
    'jQuery': 'lib/jquery/jquery-2.1.4.min',
    'datepicker': 'lib/bootstrap-datepicker/bootstrap-datepicker.min',
    'dialogs':'lib/angular-dialog-service/dist/dialogs.min',
    'ngTreeController':'lib/angular-tree-control/angular-tree-control',
    'ui-select':'lib/ui-select/dist/select'
  },

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
      deps: ['ngSanitize']
    },
    ngTranslateLoad: {
      deps: ['ngTranslate']
    },
    ngTranslateLoadUrl: {
        deps: ['ngTranslate']
      },
    ngRoute: {
      deps: ['angular']
    },
    'angular.ui.router': {
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
    dialogs: {
      deps: ['ngTranslate']
    },
    ngTreeController: {
      deps:['angular']
    },
    'ui-select': {
      deps:['angular']
    },
    'ngAnimate': {
      deps:['angular']
    },
    'uniauth-transfer': {
        deps:['angular']
    },
    angular: {
      exports: 'angular'
    }
  },
  waitSeconds: 0
});