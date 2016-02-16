define(function () {
  function fn() {
    return {
      restrict: 'E',
      scope: {
        options: '=',
        pattern: '@',
        selectedOption: '=',
        selectedOptionText: '@'
      },
      templateUrl: 'views/directives/dropdown.html',
      link: function (scope) {
        scope.select = function (opt, text) {
          scope.selectedOption = opt;
          if (angular.isDefined(text)) {
            scope.selectedOptionText = text;
          } else {
            scope.selectedOptionText = opt;
          }
        };

        // case 1: Array and items in array are string or int
        if (!scope.pattern && angular.isArray(scope.options)) {
          scope.caseCode = 1;
          return false;
        }

        // case 2: Object whose values are string or int
        if (!scope.pattern && angular.isObject(scope.options)) {
          scope.caseCode = 2;
          return false;
        }
        // case 3: Array whose items are objects
        scope.caseCode = 3;
        scope.patternName = scope.pattern.split('for')[0].replace(/\s/g, '');
        scope.patternVal = scope.pattern.split('for')[1].replace(/\s/g, '');
      }
    };
  }

  return {
    name: 'dropdown',
    directiveFn: [fn]
  };

});
