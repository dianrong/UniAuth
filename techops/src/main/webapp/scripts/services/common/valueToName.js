define(function () {
  var fn = function () {
    return function (value, array) {
      for (var i = 0, len = array.length; i < len; i++) {
        if (array[i].value === value) {
          return array[i].name;
        }
      }
    }
  }
  return {
    name: 'valueToname',
    svc: [fn]
  };
});
