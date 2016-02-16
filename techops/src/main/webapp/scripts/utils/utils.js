define(function () {
    return {
        generatorDropdown: function ($scope, name, items, defaultOpt) {
            $scope[name] = {};
            $scope[name].isOpen = false;
            $scope[name].items = items;
            $scope[name].selectOption = function (option) {
                $scope[name].option = option;
                // console.log($scope.decisionDropdown);
            }
            if (!defaultOpt && items.length) {
                $scope[name].option = items[0];
            } else {
                $scope[name].option = defaultOpt;
            }
        },
        formatNumbers: function(val){
            return Math.ceil(val*10000)/1000000;
        },
        filterParams: function () {
            // var args = Array.prototype.slice.call(arguments, 0);
            // return function (data) {
            //   if (!data) {
            //     return data;
            //   }
            //   var params = angular.copy(data);
            //   for (var i = 0, len = args.length; i < len; i++) {
            //     delete params[args[i]];
            //   }
            //   return JSON.stringify(params);
            // }
            // return function(data){
            //   return data;
            // }
        },
        convertToDate: function (val) {
            if (!val) {
                return null;
            }
            try {
                var res = val.match(/(\d{4})-(\d{1,2})-(\d{1,2})/);
                if (res.length == 4) {
                    var dt = new Date();
                    dt.setFullYear(res[1]);
                    var year = parseInt(res[2]) - 1;
                    year = year > 12 ? 12 : year;
                    dt.setMonth(parseInt(year));
                    var day = parseInt(res[3]);
                    day = day > 31 ? 31 : day;
                    dt.setDate(day);
                    console.log(dt.toJSON());
                    return dt.getTime();
                }
                return null;
            } catch (e) {
                return null;
            }

        },
        /*
         [{
         name: '请选择',
         value: ''
         }, {
         name: '增加额度',
         value: 'increase'
         }, {
         name: '减少额度',
         value: 'decrease'
         }, {
         name: '冻结额度',
         value: 'freeze'
         }, {
         name: '恢复额度',
         value: 'recovery'
         }, {
         name: '更新额度期限',
         value: 'updateExpireDate'
         }]
         */
        convertToMap: function (array) {
            var result = {};
            if (!array) {
                return result;
            }
            for (var i = 0, len = array.length; i < len; i++) {
                var obj = array[i];
                result[obj.value] = obj.name;
            }
            return result;
        }
    }
});
