define(['../../utils/constant','../../utils/utils'], function (constant, utils) {
    var Controller = function ($scope, $rootScope, SynchronousService, AlertService) {
        $scope.synchronousLogParam = {};
        $scope.results = [];
        $scope.refreshResults=function(){
            $scope.results=[
                {
                    name:$rootScope.translate('constant.selectplacehodler'),
                    value:""
                },
                {
                    name:$rootScope.translate('synchronousLog.success'),
                    value:"SUCCESS"
                },
                {
                    name:$rootScope.translate('synchronousLog.failure'),
                    value:"FAILURE"
                }
            ];
            $scope.synchronousLogParam.result=$scope.results[0].value;
        }
        $scope.types = [];
        $scope.refreshTypes=function(){
            $scope.types=[
                {
                    name:$rootScope.translate('constant.selectplacehodler'),
                    value:""
                },
                {
                    name:$rootScope.translate('synchronousLog.synchronous'),
                    value:"SYNCHRONOUS_HR_DATA"
                },
                {
                    name:$rootScope.translate('synchronousLog.deleteFiles'),
                    value:"DELETE_FTP_HR_EXPIRED_DATA"
                }
            ]
            $scope.synchronousLogParam.type=$scope.types[0].value;
        }

        $scope.pagination = {
            pageSize: constant.pageSize,
            showPageNum: constant.showPageNum,
            curPage: 1,
            totalCount: 0
        };

        $scope.querySynchronousLog = function (curPage) {
            var params = $scope.synchronousLogParam;
            if (!params) {
                params = {};
            }
            params.pageNumber = typeof curPage === "number" ? curPage : $scope.pagination.curPage - 1;
            params.pageSize = $scope.pagination.pageSize;

            $scope.synchronousLogs = [];
            $scope.synchronousLogLoading = constant.loading;

            SynchronousService.queryHrLogs(params, function (res) {
                var result = res.data;
                if(res.info) {
                    $scope.synchronousLogLoading = constant.loadError;
                    return;
                }
                if(!result || !result.data || result.data.length === 0) {
                    $scope.synchronousLogLoading = constant.loadEmpty;
                    return;
                }

                $scope.synchronousLogLoading = '';
                $scope.synchronousLogs = result.data;

                $scope.pagination.curPage = result.currentPage + 1;
                $scope.pagination.totalCount = result.totalCount;
                $scope.pagination.pageSize = result.pageSize;

            }, function () {
                $scope.synchronousLogs = [];
                $scope.synchronousLogLoading = constant.loadError;
            });
        };

        $scope.synchronousHrOnce = function() {
            SynchronousService.synchronousHrOnce({asynchronous:true}, function (res) {
                var result = res.data;
                if(res.info) {
                    for(var i=0; i<res.info.length;i++) {
                        AlertService.addAlert(constant.messageType.danger, res.info[i].msg);
                    }
                    return;
                }
                AlertService.addAutoDismissAlert(constant.messageType.info, $rootScope.translate('synchronousLog.processing.success'));
            }, function () {
                AlertService.addAutoDismissAlert(constant.messageType.danger, $rootScope.translate('synchronousLog.processing.failure'));
            });
        }
        $scope.deleteHrExpiredFiles = function() {
            SynchronousService.deleteHrExpiredFiles({asynchronous:true}, function (res) {
                var result = res.data;
                if(res.info) {
                    for(var i=0; i<res.info.length;i++) {
                        AlertService.addAlert(constant.messageType.danger, res.info[i].msg);
                    }
                    return;
                }
                AlertService.addAutoDismissAlert(constant.messageType.info, $rootScope.translate('synchronousLog.processing.success'));
            }, function () {
                AlertService.addAutoDismissAlert(constant.messageType.danger, $rootScope.translate('synchronousLog.processing.failure'));
            });
        }

        // 初始化
        $scope.querySynchronousLog();
        $scope.refreshTypes();
        $scope.refreshResults();

        $scope.$on('selected-language-changed', $scope.refreshTypes);
        $scope.$on('selected-language-changed', $scope.refreshResults);

        $scope.fromDateOptions = {
            maxDate: function(){
                var now = new Date();
                now.setDate(now.getDate() + 1)
                return now;
            }(),
            minDate: function(){
                var now = new Date();
                now.setDate(now.getDate() - 365 * 3)
                return now;
            }(),
            startingDay: 1
        };

        $scope.endDateOptions = {
            maxDate: function(){
                var now = new Date();
                now.setDate(now.getDate() + 1)
                return now;
            }(),
            minDate: function(){
                var now = new Date();
                now.setDate(now.getDate() - 365 * 3)
                return now;
            }(),
            startingDay: 1
        };
        $scope.popupFrom = {
            opened: false
        };

        $scope.popupEnd = {
            opened: false
        };

        $scope.openFrom = function() {
            $scope.popupFrom.opened = true;
        };

        $scope.openEnd = function() {
            $scope.popupEnd.opened = true;
        };
    };
    return {
        name: "SynchronousController",
        fn: ["$scope", "$rootScope", "SynchronousService", "AlertService", Controller]
    };
});
