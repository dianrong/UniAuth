define(['../../utils/constant','../../utils/utils'], function (constant, utils) {
    var Controller = function ($scope, $rootScope, AuditService, AlertService) {
        $scope.query = function() {
            var param = $scope.auditQuery;
            if(!param) {
                param = {};
            }
            param.reqSuccess = $scope.successDropdown.option.value;
            param.orderBy = $scope.orderByDropdown.option.value;
            if($scope.ascDescDropdown.option.value) {
                param.ascOrDesc = $scope.ascDescDropdown.option.value=='asc' ? true:false;
            }
            param.pageSize = 1000;
            param.pageNumber = 0;
            AlertService.addAutoDismissAlert(constant.messageType.info, $rootScope.translate('auditMgr.tips.search'));
            AuditService.queryAudits(param, function (res) {
                var result = res.data;
                if(res.info) {
                    for(var i=0; i<res.info.length;i++) {
                        AlertService.addAlert(constant.messageType.danger, res.info[i].msg);
                    }
                    return;
                }
                if(!result) {
                    AlertService.addAutoDismissAlert(constant.messageType.info, $rootScope.translate('auditMgr.tips.resultEmpty'));
                    $scope.audits = [];
                    return;
                }
                $scope.audits = result.data;
                $scope.totalCount = result.totalCount;
            }, function () {
                $scope.audits = [];
                AlertService.addAutoDismissAlert(constant.messageType.danger, $rootScope.translate('auditMgr.tips.searchFailure'));
            });
        };
        $scope.delete = function() {
            var param = $scope.auditQuery;
            if(!param) {
                param = {};
            }
            AuditService.deleteAudits(param, function (res) {
                var result = res.data;
                if(res.info) {
                    for(var i=0; i<res.info.length;i++) {
                        AlertService.addAlert(constant.messageType.danger, res.info[i].msg);
                    }
                    return;
                }
                AlertService.addAutoDismissAlert(constant.messageType.warning, $rootScope.translate('auditMgr.tips.logSum') + result + $rootScope.translate('auditMgr.tips.logDeleted'));
            }, function () {
                AlertService.addAutoDismissAlert(constant.messageType.danger, $rootScope.translate('auditMgr.tips.delFailure'));
            });
        }
        $scope.predicate = '';
        $scope.comparator = false;

        $scope.fromDateOptions = {
            maxDate: function(){
                var now = new Date();
                now.setDate(now.getDate() + 1)
                return now;
            }(),
            minDate: function(){
                var now = new Date();
                now.setDate(now.getDate() - 365*3)
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
                now.setDate(now.getDate() - 365*3)
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

        utils.generatorDropdown($scope, 'successDropdown', constant.success);
        utils.generatorDropdown($scope, 'orderByDropdown', constant.auditOrderBy);
        utils.generatorDropdown($scope, 'ascDescDropdown', constant.ascDesc);

    };

    return {
        name: "AuditController",
        fn: ["$scope", "$rootScope", "AuditService", "AlertService", Controller]
    };

});
