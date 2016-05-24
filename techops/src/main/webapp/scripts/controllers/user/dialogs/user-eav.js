define(['../../../utils/constant'], function (constant) {
    var Controller = function ($scope, $uibModalInstance, EvaService, AlertService, data) {
    	  //-- Variables --//
        $scope.user = data;
        // 查询条件
    	$scope.eavQuery={};
    	 $scope.pagination = {
    	            pageSize: constant.smallPageSize,
    	            curPage: 1,
    	            totalCount: 0
    	};
    	 
    	// 分页查询
    	$scope.queryUserEaves = function () {
            var params = {};
            $scope.userEvaCodes = [];
            $scope.userEavLoading = constant.loading;
            params.extendCode = $scope.eavQuery.code;
            params.userId = $scope.user.id;
            // 分页查询参数
            params.pageNumber = $scope.pagination.curPage - 1;
            params.pageSize = $scope.pagination.pageSize;
            
            EvaService.queryUserEav(params, function (res) {
                var result = res.data;
                if(res.info) {
                    $scope.userEavLoading = res.info[0].msg;
                    return;
                }
                if(!result || !result.data || result.data.length == 0) {
                    $scope.userEavLoading = constant.loadEmpty;
                    return;
                }
                $scope.userEavLoading = '';
                $scope.userEvaCodes = result.data;
                
                //设置分页属性
                $scope.pagination.curPage = result.currentPage + 1;
                $scope.pagination.totalCount = result.totalCount;
                $scope.pagination.pageSize = result.pageSize;
            }, function () {
                $scope.userEvaCodes = [];
                $scope.userEvaCodes = constant.loadError;
            });
        };
        // init
        $scope.queryUserEaves();
        
        // 操作数组 将指定item放到第一位
        var exchangeItemToFirst = function( itemArray, item){
        	if(!item || !itemArray) {
        		return;
        	}
        	 var index = -1;
        	 for(var i = 0 ;i < itemArray.length; i++) {
        		 if(itemArray[i] === item) {
        			 index = i;
        			 break;
        		 }
        	 }
        	 if(index != -1) {
        		 itemArray.splice(index, 1);
        	 }
        	 itemArray.unshift(item);
        }
        
        // 确定修改
        $scope.confirmEdit = function(userEav) {
            var param = {};
            param.id = userEav.id;
            param.userId = $scope.user.id;
            param.extendId = userEav.extendId;
            param.value=userEav.value;
            // 添加
            if(!userEav.id) {
            	EvaService.addUserEav(param, function (res) {
                    var result = res.data;
                    if(res.info) {
                        for(var i=0; i<res.info.length;i++) {
                            AlertService.addAlert(constant.messageType.danger, res.info[i].msg);
                        }
                        $scope.cancelEdit(userEav);
                        return;
                    }
                    userEav.id = result.id;
                    userEav.status = '0';
                    userEav.userId = $scope.user.id;
                    
                    exchangeItemToFirst($scope.userEvaCodes, userEav);
                    AlertService.addAutoDismissAlert(constant.messageType.info, "添加成功.");
                }, function () {
                    AlertService.addAutoDismissAlert(constant.messageType.danger, "添加失败, 请联系系统管理员.");
                });
            } else {
            	// 修改
            	EvaService.modfyUserEav(param, function (res) {
                    var result = res.data;
                    if(res.info) {
                        for(var i=0; i<res.info.length;i++) {
                            AlertService.addAlert(constant.messageType.danger, res.info[i].msg);
                        }
                        $scope.cancelEdit(userEav);
                        return;
                    }
                    exchangeItemToFirst($scope.userEvaCodes, userEav);
                    AlertService.addAutoDismissAlert(constant.messageType.info, "更新成功.");
                }, function () {
                    AlertService.addAutoDismissAlert(constant.messageType.danger, "更新失败, 请联系系统管理员.");
                });
            }
            userEav.editable = false;
        };
        
        // 初始化编辑
        $scope.initEdit = function(userEav) {
        	userEav.editable = true;
        };
        
        // 取消编辑
        $scope.cancelEdit = function(userEav) {
            userEav.editable = false;
        };

        // 禁用
        $scope.disableUserEav = function(userEav) {
        	 var param = {};
             param.id = userEav.id;
             EvaService.disableUserEav(param, function (res) {
            	 var result = res.data;
                 if(res.info) {
                     for(var i=0; i<res.info.length;i++) {
                         AlertService.addAlert(constant.messageType.danger, res.info[i].msg);
                     }
                     return;
                 }
                 AlertService.addAutoDismissAlert(constant.messageType.info, "禁用成功.");
                 userEav.status = '1';
             }, function () {
                 AlertService.addAutoDismissAlert(constant.messageType.danger, "禁用失败, 请联系系统管理员.");
             });
        };
        
        // 启用
        $scope.enableUserEav = function(userEav) {
        	 var param = {};
             param.id = userEav.id;
             EvaService.enableUserEav(param, function (res) {
            	 var result = res.data;
                 if(res.info) {
                     for(var i=0; i<res.info.length;i++) {
                         AlertService.addAlert(constant.messageType.danger, res.info[i].msg);
                     }
                     return;
                 }
                 AlertService.addAutoDismissAlert(constant.messageType.info, "启用成功.");
                 userEav.status = '0';
             }, function () {
                 AlertService.addAutoDismissAlert(constant.messageType.danger, "启用失败, 请联系系统管理员.");
             });
        };
        
        //-- 关闭模态框
        $scope.dialog_ok = function(){
            $scope.msg = '';
            $uibModalInstance.dismiss();
        }; // end cancel
    };

    return {
        name: "UserEavController",
        fn: ["$scope", "$uibModalInstance", "EavService", "AlertService", "data", Controller]
    };
});
