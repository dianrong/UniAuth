define(['../../utils/constant'], function (constant) {
    var Controller = function ($scope, $rootScope, DomainService) {
        $scope.status = 'init';
        $scope.stakeholders = [{name:"AA",email:"shenglong.qian@dianrong.com",phone:"13032323232"},{name:"BB",email:"chenglong.qian@dianrong.com",phone:"13132323232"}]
    };

    return {
        name: "DomainController",
        fn: ["$scope", "$rootScope", "DomainService", Controller]
    };

});
