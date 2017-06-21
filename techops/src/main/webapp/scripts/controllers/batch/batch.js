define([ '../../utils/constant' ], function(constant) {
	var Controller = function($rootScope, $scope, $location, AlertService,
			FileUploader) {
		// params
		$scope.process_param = {};

		$scope.process_result = {};

		// batch process enable
		$scope.process_btn_enable = function() {
			if (!$scope.process_param.type) {
				return false;
			}
			var fileItems = uploader.getNotUploadedItems();
			if (fileItems && fileItems.length > 0) {
				var handler = batch_process_handler.getHander();
				if (handler) {
					return handler.process_btn_enable();
				}
			}
			return false;
		}

		$scope.is_relate_user_grp = function() {
			if ($scope.process_param.type) {
				var handler = batch_process_handler.getHander();
				if (handler) {
					return handler.grp_code_show();
				}
			}
			return false;
		}

		// 各种批量处理的处理逻辑handler
		var batch_process_handler = (function() {
			var handlers = [ {
				type : function() {
					return 'batch-relate-user-grp';
				},
				addParams : function(fileItem) {
					var p = {
						groupCode : $scope.process_param.grp_code
					};
					fileItem.formData.push(p);
				},
				grp_code_show : function() {
					return true;
				},
				process_url : function() {
					return constant.apiBase + "/batch/relate-user-grp";
				},
				process_btn_enable : function() {
					if ($scope.process_param.grp_code) {
						return true;
					}
					return false;
				},
				finish_i18n_code:function() {
					return 'batch.process.finish.relate.user.grp';
				}
			}, {
				type : function() {
					return 'batch-add-user';
				},
				addParams : function(fileItem) {
				},
				grp_code_show : function() {
					return false;
				},
				process_url : function() {
					return constant.apiBase + "/batch/add-user";
				},
				process_btn_enable : function() {
					return true;
				},
				finish_i18n_code:function() {
					return 'batch.process.finish.add.user';
				}
			}, {
				type : function() {
					return 'batch-disable-user';
				},
				addParams : function(fileItem) {
				},
				grp_code_show : function() {
					return false;
				},
				process_url : function() {
					return constant.apiBase + "/batch/disable-user";
				},
				process_btn_enable : function() {
					return true;
				},
				finish_i18n_code:function() {
					return 'batch.process.finish.disable.user';
				}
			} ];
			var getHander = function(type) {
				if (!type) {
					type = $scope.process_param.type;
				}
				for (var i = 0; i < handlers.length; i++) {
					if (handlers[i].type() === type) {
						return handlers[i];
					}
				}
				return undefined;
			};
			var hander = {
				getHander : getHander
			};
			return hander;
		})();

		$scope.batch_process = function() {
			var fileItems = uploader.getNotUploadedItems();
			if (fileItems && fileItems.length > 0) {
				uploader.uploadItem(fileItems[0]);
			}
		};

		var uploader = $scope.uploader = new FileUploader({
			removeAfterUpload: true
		});

		uploader.onBeforeUploadItem = function(fileItem) {
			var process_type = $scope.process_param.type;
			batch_process_handler.getHander().addParams(fileItem);
			fileItem.url = batch_process_handler.getHander().process_url();
			fileItem.onProgress = function(progress) {
				AlertService.addAutoDismissAlert(constant.messageType.info,
						$rootScope.translate('batch.process.tips.processing'));
			}
			fileItem.onComplete = function(response, status, headers) {
				AlertService.addAutoDismissAlert(constant.messageType.info,
						$rootScope.translate('batch.process.tips.procese.success'));
				if (response.info && response.info.length > 0) {
					AlertService.addAlert(constant.messageType.danger, response.info[0].msg);
					return;
				}
				$scope.process_result = response.data;
				$scope.process_result['process_type'] = $rootScope.translate(batch_process_handler.getHander(process_type).finish_i18n_code()); 
			}
			fileItem.onError = function(response, status, headers) {
				AlertService.addAlert(constant.messageType.danger, $rootScope
						.translate('batch.process.tips.process.failure'));
				uploader.clearQueue();
			}
		};

		// 保证只有当前加入的file在队列中
		uploader.onAfterAddingFile = function(fileItem) {
			uploader.queue = [fileItem];
		};
	};
	return {
		name : "BatchController",
		fn : [ "$rootScope", "$scope", "$location", "AlertService",
				"FileUploader", Controller ]
	};

});
