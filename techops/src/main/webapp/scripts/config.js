// Configure RequireJS
require.config({

  // Base url for script files
  baseUrl: 'js',
  // Alias libraries paths
  // http://stackoverflow.com/questions/19307198/angular-js-with-require-js-getting-uncaught-error-injectormodulerr
  // https://github.com/angular-ui/bootstrap/issues/1512
  paths: {
    'angular': 'lib/angular/angular.min',
    'angular.ui.bootstrap': 'lib/angular-bootstrap/ui-bootstrap-tpls.min',
    'ngResource': 'lib/angular-resource/angular-resource.min',
    'ngLocalStorage': 'lib/angular-local-storage/dist/angular-local-storage.min',
    'ngRoute': 'lib/angular-route/angular-route.min',
    'ngCookies': 'lib/angular-cookies/angular-cookies.min'
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
    ngRoute: {
      deps: ['angular']
    },
    angular: {
      exports: 'angular'
    }
  },
  waitSeconds: 0
});
