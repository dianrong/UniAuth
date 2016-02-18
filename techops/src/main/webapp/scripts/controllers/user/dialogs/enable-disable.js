define(function () {
    var Controller = function ($scope,$uibModalInstance,$translate,data) {
        debugger;
        $scope.header = (angular.isDefined(data.header)) ? data.header : $translate.instant('DIALOGS_CONFIRMATION');
        $scope.msg = (angular.isDefined(data.msg)) ? data.msg : $translate.instant('DIALOGS_CONFIRMATION_MSG');
        $scope.icon = (angular.isDefined(data.fa) && angular.equals(data.fa, true)) ? 'fa fa-check' : 'glyphicon glyphicon-check';

        $scope.no = function () {
            $uibModalInstance.dismiss('no');
        };

        $scope.yes = function () {
            $uibModalInstance.close('yes');
        };
    };

    return {
        name: "EnableDisableController",
        fn: ["$scope","$uibModalInstance","$translate", Controller]
    };

});
