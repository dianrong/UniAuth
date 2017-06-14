define(['../../../utils/constant'], function (constant) {
    var Controller = function ($rootScope,$scope, $uibModalInstance, EvaService, AlertService, data) {
    	  //-- Variables --//
        $scope.user = data;
        // 查询条件
    	$scope.eavQuery={};
    	// 已经使用的扩展属性列表
    	$scope.userEvaUsed = [];
    	// 未使用的属性列表
    	$scope.userEvaToUse = [];
    	
    	// 默认是不显示点击按钮的
    	$scope.addUserEav = false;
    	
    	// 分页查询
    	$scope.queryUserEaves = function () {
            var params = {};
            $scope.userEvaCodes = [];
            $scope.userEavLoading = constant.loading;
            params.extendCode = $scope.eavQuery.code;
            params.userId = $scope.user.id;
            // 一次性全部查出来
            params.pageNumber = 0;
            params.pageSize = 2000;
            
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

                // 对查询的数据进行处理(分类,排序)
                filterData(result.data);
                // 初始化选择框(新增数据的选择框)
                initSelectEavCode();
            }, function () {
            	$scope.userEvaUsed = [];
            	$scope.userEvaToUse = [];
                $scope.userEvaCodes = constant.loadError;
            });
        };
        // init
        $scope.queryUserEaves();
        
        // 添加
        $scope.addNewUserEav = function() {
        	var param = {};
            param.userId = $scope.user.id;
            param.extendId = $scope.newUserEav.extendId;
            param.value=$scope.newUserEav.value;
            
        	EvaService.addUserEav(param, function (res) {
                var result = res.data;
                if(res.info) {
                    for(var i=0; i<res.info.length;i++) {
                        AlertService.addAlert(constant.messageType.danger, res.info[i].msg);
                    }
                    $scope.cancelAddEav();
                    return;
                }
                // 新加一个
                var userEav = {};
                userEav.extendId = $scope.newUserEav.extendId;
                userEav.extendCode=$scope.newUserEav.extendCode;
                userEav.value=$scope.newUserEav.value;
                userEav.id = result.id;
                userEav.userId = $scope.user.id;
                // 将从未使用中把数据放到使用中的
                getElemFromToUseToUsed(userEav);
                // 重新计算前端的辅助值
                computeNewData(true);
                AlertService.addAutoDismissAlert(constant.messageType.info, $rootScope.translate('userMgr.tips.addEavSuccess'));
                // 关闭添加框
                $scope.cancelAddEav();
                // 初始化选择框
                initSelectEavCode();
            }, function () {
                AlertService.addAutoDismissAlert(constant.messageType.danger, $rootScope.translate('userMgr.tips.addEavFailure'));
            });
        }
        
        // 确定修改
        $scope.confirmEdit = function(userEav) {
            var param = {};
            param.id = userEav.id;
            param.userId = $scope.user.id;
            param.extendId = userEav.extendId;
            param.value=userEav.value;
            // 添加
            if(userEav.id) {
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
                    
                    // 更新数据了 使用中的数据进行排序
                    computeNewData(true);
                    AlertService.addAutoDismissAlert(constant.messageType.info, $rootScope.translate('userMgr.tips.modifyEavSuccess'));
                }, function () {
                    AlertService.addAutoDismissAlert(constant.messageType.danger, $rootScope.translate('userMgr.tips.modifyEavFailure'));
                });
            }
            userEav.editable = false;
        };
        
        // 初始化添加编辑
        $scope.addEav = function() {
        	$scope.addUserEav = true;
        };
        
     //取消添加
        $scope.cancelAddEav = function() {
        	$scope.addUserEav = false;
        	// 重新初始化
        	initSelectEavCode();
        };
        
        // 编辑数据
        $scope.initEdit = function(userEav) {
        	userEav.editable = true;
        };
        
        // 取消编辑
        $scope.cancelEdit = function(userEav) {
            userEav.editable = false;
        };

        // 禁用
        $scope.deleteUserEav = function(userEav) {
        	 var param = {};
             param.id = userEav.id;
             EvaService.deleteUserEav(param, function (res) {
            	 var result = res.data;
                 if(res.info) {
                     for(var i=0; i<res.info.length;i++) {
                         AlertService.addAlert(constant.messageType.danger, res.info[i].msg);
                     }
                     return;
                 }
                 AlertService.addAutoDismissAlert(constant.messageType.info, $rootScope.translate('userMgr.tips.deleteEavSuccess'));
                 pushElemFromUsedToToUse(userEav);
                 computeNewData(false);
             }, function () {
                 AlertService.addAutoDismissAlert(constant.messageType.danger, $rootScope.translate('userMgr.tips.deleteEavFailure'));
             });
        };
        
        //-- 关闭模态框
        $scope.dialog_ok = function(){
            $scope.msg = '';
            $uibModalInstance.dismiss();
        }; // end cancel
        
        // private method
        // 重新动态计算长度值
        var computeNewData = function(computeData){
        	if(computeData) {
	        	// 计算显示长度
	        	for(var i =0;i < $scope.userEvaUsed.length; i++) {
	        		// 设置附加属性
					setElementShowLength($scope.userEvaUsed[i]);
	        	}
	        }
        	// 对数据进行排序  按照extendId排序
    		sortArrays($scope.userEvaUsed, 'status', false, 'id', true);
        }
        
        // 初始化选择框
        var initSelectEavCode = function() {
        	if($scope.userEvaToUse && $scope.userEvaToUse.length > 0) {
        		$scope.newUserEav = $scope.userEvaToUse[0];
        	} else {
        		$scope.newUserEav= {};
        	}
        };
        
        // 将数据分为已使用和未使用两个数组
    	var filterData = function(elements) {
    		if(!elements || elements.length == 0) {
    			// 已经使用的扩展属性列表
    	    	$scope.userEvaUsed = [];
    	    	// 未使用的属性列表
    	    	$scope.userEvaToUse = [];
    			return;
    		}
    		var used = [];
    		var toUse = [];
    		for(var i = 0; i < elements.length; i++) {
    			if(elements[i].id) {
    				// 设置附加属性
    				setElementShowLength(elements[i]);
    				used.push(elements[i]);
    			} else {
    				toUse.push(elements[i]);
    			}
    		}
    		// 对数据进行排序  按照extendId排序
    		sortArrays(used, 'id', true);
    		sortArrays(toUse, 'extendId', true);
    		
    		// 赋值
        	$scope.userEvaUsed = used;
        	$scope.userEvaToUse = toUse;
    	};
    	
    	// 排序
    	var sortArrays = function(datas, tag, tag_sort ,tagappend, tag_append_sort) {
    		if(!datas || datas.length == 0 || !tag) {
    			return;
    		}
    		datas.sort(function(e1, e2) {
    			if(tag_sort){
    				var t1 =  e2[tag] - e1[tag];
    				if(tagappend && t1 == 0) {
    					if(tag_append_sort) {
    						return e2[tagappend] - e1[tagappend];
    					} else {
    						return e1[tagappend] - e2[tagappend];
    					}
    				}
    				return t1;
    			} else {
    				var t2 = e1[tag] - e2[tag];
    				if(tagappend && t2 == 0) {
    					if(tag_append_sort) {
    						return e2[tagappend] - e1[tagappend];
    					} else {
    						return e1[tagappend] - e2[tagappend];
    					}
    				}
    				return t2;
    			}
    		});
    	};
    	
    	// 从未使用中放到已使用中
    	var getElemFromToUseToUsed = function(ele) {
    		if(!$scope.userEvaToUse || $scope.userEvaToUse.length == 0) {
    			return;
    		}
    		var index = -1;
    		for(var i = 0; i <  $scope.userEvaToUse.length; i++) {
    			if($scope.userEvaToUse[i].extendId === ele.extendId) {
    				index = i;
    				break;
    			}
    		}
    		 if(index > -1) {
    			 // 去除
    			 $scope.userEvaToUse.splice(index, 1);
    			 // 加入到已使用的列表中
    			 if(!$scope.userEvaUsed) {
    				 $scope.userEvaUsed = [];
    			 }
    			 $scope.userEvaUsed.push(ele);
    		 }
    	};
    	
    	// 从已使用中放到未使用中
    	var pushElemFromUsedToToUse = function(ele) {
    		if(!$scope.userEvaUsed || $scope.userEvaUsed.length == 0) {
    			return;
    		}
    		var index = -1;
    		for(var i = 0; i <  $scope.userEvaUsed.length; i++) {
    			if($scope.userEvaUsed[i].extendId === ele.extendId) {
    				index = i;
    				break;
    			}
    		}
    		 if(index > -1) {
    			 $scope.userEvaUsed.splice(index, 1);
    			 // 加入到已使用的列表中
    			 if(!$scope.userEvaToUse) {
    				 $scope.userEvaToUse = [];
    			 }
    			 $scope.userEvaToUse.push(ele);
    		 }
    	};
        
        // 工具方法
        // 为已使用的数据计算显示宽度等数据
    	var setElementShowLength = function(ele) {
    		if(!ele) { return;}
    		var min = 2,  max = 12 ,size = 4.5, valueWeight =1.4, valuePix = 5;
    		var oprLength = 1;
    		var codeLength = Math.ceil(byteLength(ele.extendCode));
    		var valueLength = Math.ceil(byteLength(ele.value) );
    		var totalLength =oprLength + codeLength + valueLength;
    		ele.totalLen =Math.round( (totalLength / size) < min ? min :  (totalLength / size)  > max ? max : (totalLength / size));
    		
    		// 内部计算
    		var valueLength_inner = Math.round(valueLength * valueWeight) + valuePix;
    		var totalLength_inner =oprLength + codeLength + valueLength_inner;
    		ele.codeLen =Math.round((codeLength / totalLength_inner) * max) ;
    		ele.valueLen = Math.ceil((valueLength_inner / totalLength_inner) * max) ;
			if((max - ele.codeLen -  ele.valueLen) <= 0 ) {
				ele.valueLen = max - ele.codeLen - 1;
			}
    	};
    	// 计算字符的显示字长
    	var byteLength = function(str) {
    		 if( !str ) return 0;
    		 var byteLen = 0, len = str.length;
    		 for( var i=0; i<len; i++ ) {
    			 byteLen += str.charCodeAt(i) > 255 ? 1.6 : 1;
    		 }
    		return byteLen;
    	};
    };

    return {
        name: "UserEavController",
        fn: ["$rootScope","$scope", "$uibModalInstance", "EavService", "AlertService", "data", Controller]
    };
});
