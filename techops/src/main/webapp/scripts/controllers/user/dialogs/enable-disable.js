define(function () {
    var Controller = function ($scope,$uibModalInstance,$translate,UserService,data) {

        $scope.header = data.status?'用户-启用':'用户-禁用';
        var message = "您确定要" + (data.status?'启用':'禁用') + "用户:" + data.email + "吗?";
        $scope.msg = message;

        $scope.no = function () {
            $uibModalInstance.dismiss('no');
        };

        $scope.yes = function () {
            UserService.enableDisableUser(
                {
                    'id':data.id,
                    'status':data.status?0:1
                }
            , function(res) {
                $uibModalInstance.close(res)
            }, function(err) {
                $uibModalInstance.close(err)
                console.log(err);
            });
        };
    };

    return {
        name: "EnableDisableController",
        fn: ["$scope","$uibModalInstance","$translate", "UserService", "data", Controller]
    };

});
