define(['../../utils/constant'], function (constant) {
    /**
     * A module representing a User controller.
     * @exports controllers/User
     */
    var Controller = function ($scope, CfgService) {


        $scope.queryConfig = function () {
            var params = $scope.cfgQuery;
            if (!params) {
                params = {};
            }
            params.pageNumber = 0;
            params.pageSize = 5000;

            $scope.cfgs = [];
            $scope.cfgsLoading = constant.loading;

            CfgService.queryConfig(params, function (res) {
                var result = res.data;
                if(res.info) {
                    $scope.cfgsLoading = constant.loadError;
                    return;
                }
                if(!result) {
                    $scope.cfgsLoading = constant.loadEmpty;
                    return;
                }

                $scope.cfgsLoading = '';
                $scope.cfgs = result.data;

            }, function () {
                $scope.cfgs = [];
                $scope.cfgsLoading = constant.loadError;
            });
        };

        $scope.queryConfig();
        $scope.download = function(cfg) {
            return constant.apiBase + "/cfg/download/" + cfg.cfgKey;
        }
    };

    return {
        name: "CfgController",
        fn: ["$scope", "CfgService", Controller]
    };

});
