define(function () {
    var Controller = function ($scope,$uibModalInstance,$translate,data) {

        $scope.header = data.header;
        $scope.msg = data.msg;

        $scope.no = function () {
            $uibModalInstance.dismiss();
        };

        $scope.yes = function () {
            $uibModalInstance.close();
        };
    };

    return {
        name: "EnableDisableController",
        fn: ["$scope","$uibModalInstance","$translate", "data", Controller]
    };

});
