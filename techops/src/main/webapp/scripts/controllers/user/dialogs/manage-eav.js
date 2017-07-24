define(['../../../utils/constant'], function (constant) {
    var Controller = function ($rootScope, $scope, $uibModalInstance, EvaService, AlertService) {
    	// 查询条件
    	$scope.eavQuery={};
    	 $scope.pagination = {
    			 pageSize: constant.smallPageSize,
    			 showPageNum: constant.showPageNum,
    			 curPage: 1,
    			 totalCount: 0
    	};
    	 
    	// 用户角色列表
     	$scope.userRoleCodes = [];
     	//用户可以操作的域code
     	$scope.userDomainCodes =[];
    	 
    	// 分页查询
    	$scope.queryEavCodes = function (curPage) {
            var params = {};
            $scope.eavCodes = [];
            $scope.eavCodesLoading = constant.loading;
            
            params.code = $scope.eavQuery.code;
            // 分页查询参数
            params.pageNumber = curPage === undefined ? $scope.pagination.curPage - 1 : curPage;
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
                
                $scope.pagination.curPage = result.currentPage + 1;
                $scope.pagination.totalCount = result.totalCount;
                $scope.pagination.pageSize = result.pageSize;
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
                    eavCode.id = result.id;
                    AlertService.addAutoDismissAlert(constant.messageType.info, $rootScope.translate('userMgr.tips.addEavSuccess'));
                }, function () {
                    AlertService.addAutoDismissAlert(constant.messageType.danger, $rootScope.translate('userMgr.tips.addEavFailure'));
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
                    exchangeItemToFirst(eavCode, $scope.eavCodes);
                    AlertService.addAutoDismissAlert(constant.messageType.info, $rootScope.translate('userMgr.tips.modifyEavSuccess'));
                }, function () {
                    AlertService.addAutoDismissAlert(constant.messageType.danger, $rootScope.translate('userMgr.tips.modifyEavFailure'));
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
                ensurePageIsOk('del', 1);
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
                     // 放到最前面
                     $scope.eavCodes.unshift({
                         code:'',
                         description:'',
                         editable: true
                     });
                     ensurePageIsOk('add', 1);
                    break;
            }
        };
        //-- 关闭模态框
        $scope.dialog_ok = function(){
            $scope.msg = '';
            $uibModalInstance.dismiss();
        }; // end cancel

        // 计算用户的各种code
        var computeUserCodes = function(){
    		if($rootScope.userInfo){
    			// 角色codes
    			if($rootScope.userInfo.roles  &&  $rootScope.userInfo.roles.length > 0) {
    				for(var i = 0; i < $rootScope.userInfo.roles.length; i++) {
    					$scope.userRoleCodes.push($rootScope.userInfo.roles[i].roleCode);
    	    		}
    			}
    			//域codes
    			if($rootScope.userInfo.switchableDomains  &&  $rootScope.userInfo.switchableDomains.length > 0) {
    				for(var i = 0; i < $rootScope.userInfo.switchableDomains.length; i++) {
    					$scope.userDomainCodes.push($rootScope.userInfo.switchableDomains[i].code);
    	    		}
    			}
    		} 
    	};
    	
    	// 初始化显示数据
    	computeUserCodes();
    	
        // 操作数组 将指定item放到第一位
        var exchangeItemToFirst = function(item,  itemArray){
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
        };
        
        // 保证分页的数据正确性
        var ensurePageIsOk= function(oper, num) {
        	 switch(oper) {
             case 'add':
            	 // 多余的数据保存为缓存
            	 if($scope.eavCodes.length > constant.smallPageSize) {
            		 if(!$scope.tempCodes) {
            			 $scope.tempCodes = [];
            		 }
            		 var temps = $scope.eavCodes.splice(constant.smallPageSize , $scope.eavCodes.length - constant.smallPageSize);
					if(temps) {
						for(var i = 0 ; i < temps.length ; i++) {
							$scope.tempCodes.push(temps[i]);
						}
					}
            	 }
            	 $scope.pagination.totalCount += num;
                 break;
             case 'del':
            	 // 将缓存中的数据加回来
            	 if($scope.eavCodes.length < constant.smallPageSize) {
            		 if(!$scope.tempCodes ||$scope.tempCodes.length == 0 ) {
            			 return;
            		 }
            		 for(var i = 0 ; i< (constant.smallPageSize - $scope.eavCodes.length); i++ ) {
            			 if($scope.tempCodes.length <= 0) {
            				 return;
            			 }
            			 $scope.eavCodes.push($scope.tempCodes.pop());
            		 }
            	 }
            	 $scope.pagination.totalCount -= num;
                 break;
        	 }
        }
    };

    return {
        name: "ManageEavController",
        fn: ["$rootScope", "$scope", "$uibModalInstance", "EavService", "AlertService", Controller]
    };
});
