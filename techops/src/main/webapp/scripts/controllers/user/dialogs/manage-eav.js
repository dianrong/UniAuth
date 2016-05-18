define(['../../../utils/constant'], function (constant) {
    var Controller = function ($scope, $uibModalInstance, EvaService, AlertService) {
    	// 查询条件
    	$scope.eavQuery={};
    	 $scope.pagination = {
    	            pageSize: constant.smallPageSize,
    	            curPage: 1,
    	            totalCount: 0
    	};
    	// 分页查询
    	$scope.queryEavCodes = function () {
            var params = {};
            $scope.eavCodes = [];
            $scope.eavCodesLoading = constant.loading;
            
            params.code = $scope.eavQuery.code;
            // 分页查询参数
            params.pageNumber = $scope.pagination.curPage - 1;
            params.pageSize = $scope.pagination.pageSize;
            
            EvaService.queryEavCodes(params, function (res) {
                var result = res.data;
                if(res.info) {
                    $scope.eavCodesLoading = res.info[0].msg;
                    return;
                }
                if(!result || !result.data || result.data.length == 0) {
                    $scope.eavCodesLoading = constant.loadEmpty;
                    return;
                }
                $scope.eavCodesLoading = '';
                $scope.eavCodes = result.data;
            }, function () {
                $scope.eavCodes = [];
                $scope.eavCodesLoading = constant.loadError;
            });
        };
        // init
        $scope.queryEavCodes();

        // 确定修改
        $scope.confirmEdit = function(eavCode) {
            var param = {};
            param.id = eavCode.id;
            param.code = eavCode.code;
            param.description=eavCode.description;
            
            // 添加
            if(!eavCode.id) {
            	EvaService.addEavCode(param, function (res) {
                    var result = res.data;
                    if(res.info) {
                        for(var i=0; i<res.info.length;i++) {
                            AlertService.addAlert(constant.messageType.danger, res.info[i].msg);
                        }
                        $scope.cancelEdit(eavCode);
                        return;
                    }
                    AlertService.addAutoDismissAlert(constant.messageType.info, "EAV-CODE添加成功.");
                    eavCode.id = result.id;
                }, function () {
                    AlertService.addAutoDismissAlert(constant.messageType.danger, "EAV-CODE添加失败, 请联系系统管理员.");
                });
            } else {
            	// 修改
            	EvaService.modifyEavCode(param, function (res) {
                    var result = res.data;
                    if(res.info) {
                        for(var i=0; i<res.info.length;i++) {
                            AlertService.addAlert(constant.messageType.danger, res.info[i].msg);
                        }
                        $scope.cancelEdit(eavCode);
                        return;
                    }
                    AlertService.addAutoDismissAlert(constant.messageType.info, "EAV-CODE更新成功.");
                }, function () {
                    AlertService.addAutoDismissAlert(constant.messageType.danger, "EAV-CODE更新失败, 请联系系统管理员.");
                });
            }
            eavCode.editable = false;
        };
        
        // 初始化编辑
        $scope.initEdit = function(eavCode) {
        	eavCode.editable = true;
        };
        
        // 取消编辑
        $scope.cancelEdit = function(eavCode) {
            if(!eavCode.id) {
                var index = $scope.eavCodes.indexOf(eavCode);
                $scope.eavCodes.splice(index, 1);
                if($scope.eavCodes.length == 0) {
                    $scope.eavCodesLoading = constant.loadEmpty;
                }
                return;
            }
            eavCode.editable = false;
        };

        $scope.launch = function(which, param) {
            switch(which) {
                case 'add':
                	 if(!$scope.eavCodes) {
                         $scope.eavCodes = [];
                     }
                     $scope.eavCodesLoading = '';
                     $scope.eavCodes.push({
                         code:'',
                         description:'',
                         editable: true
                     });
                    break;
            }
        };
        //-- 关闭模态框
        $scope.dialog_ok = function(){
            $scope.msg = '';
            $uibModalInstance.dismiss();
        }; // end cancel
    };

    return {
        name: "ManageEavController",
        fn: ["$scope", "$uibModalInstance", "EavService", "AlertService", Controller]
    };
});
