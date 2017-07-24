define(
		[ '../../utils/constant' ],
		function(constant) {
			var Controller = function($rootScope, $scope, $location,
					AlertService, FileUploader, GroupService, TagService,
					RoleService) {
				// params
				var process_param = $scope.process_param = {};

				// 条件查询结果
				var query_result = $scope.query_result = {};

				// 批处理结果
				var process_result = $scope.process_result = {};

				// batch process enable
				$scope.process_btn_enable = function() {
					if (!$scope.process_param.type) {
						return false;
					}
					var fileItems = uploader.getNotUploadedItems();
					if (fileItems && fileItems.length > 0) {
						var handler = batch_process_handler.getHander();
						if (handler) {
							if (!handler.process_btn_enable) {
								return true;
							}
							return handler.process_btn_enable();
						}
					}
					return false;
				}

				// grp 输入框是否显示.
				$scope.is_show_grp_query = function() {
					if ($scope.process_param.type) {
						var handler = batch_process_handler.getHander();
						if (handler && handler.grp_query_show) {
							return handler.grp_query_show();
						}
					}
					return false;
				}
				// role 输入框是否显示.
				$scope.is_show_role_query = function() {
					if ($scope.process_param.type) {
						var handler = batch_process_handler.getHander();
						if (handler && handler.role_query_show) {
							return handler.role_query_show();
						}
					}
					return false;
				}
				// tag 输入框是否显示.
				$scope.is_show_tag_query = function() {
					if ($scope.process_param.type) {
						var handler = batch_process_handler.getHander();
						if (handler && handler.tag_query_show) {
							return handler.tag_query_show();
						}
					}
					return false;
				}

				// 查询组
				$scope.refresh_grps = function(name) {
					var params = {
						name : name,
						status : 0,
						pageNumber : 0,
						pageSize : 16
					};
					return GroupService.queryGroup(params).$promise
							.then(function(response) {
								if (response.data && response.data.data) {
									$scope.query_result.grps = response.data.data;
								} else {
									$scope.query_result.grps = [];
								}
							});
				}

				// 查询角色
				$scope.refresh_roles = function(name) {
					var params = {
						name : name,
						status : 0,
						pageNumber : 0,
						pageSize : 16
					};
					params.domainId = $rootScope.loginDomainsDropdown.option.id;
					return RoleService.getRoles(params).$promise.then(function(
							response) {
						if (response.data && response.data.data) {
							$scope.query_result.roles = response.data.data;
						} else {
							$scope.query_result.roles = [];
						}
					});
				}

				// 查询标签
				$scope.refresh_tags = function(code) {
					var params = {
						fuzzyCode : code,
						status : 0,
						pageNumber : 0,
						pageSize : 16
					};
					params.domainId = $rootScope.loginDomainsDropdown.option.id;
					return TagService.getTags(params).$promise.then(function(
							response) {
						if (response.data && response.data.data) {
							$scope.query_result.tags = response.data.data;
						} else {
							$scope.query_result.tags = [];
						}
					});
				}

				// 各种批量处理的处理逻辑handler
				var batch_process_handler = (function() {
					var handlers = [
						{
							// 批量关联用户和组
							type : function() {
								return 'batch-relate-user-grp';
							},
							addParams : function(fileItem) {
								var p = {
									groupId : $scope.process_param.grp_param.selected.id
								};
								fileItem.formData.push(p);
							},
							grp_query_show : function() {
								return true;
							},
							process_url : function() {
								return constant.apiBase + "/batch/relate-user-grp";
							},
							process_btn_enable : function() {
								if ($scope.process_param.grp_param && $scope.process_param.grp_param.selected) {
									return true;
								}
								return false;
							},
							finish_i18n_code : function() {
								return 'batch.process.finish.relate.user.grp';
							}
						}, 
						{
							// 批量添加用户
							type : function() {
								return 'batch-add-user';
							},
							process_url : function() {
								return constant.apiBase + "/batch/add-user";
							},
							finish_i18n_code : function() {
								return 'batch.process.finish.add.user';
							}
						}, 
						{
							// 批量禁用用户
							type : function() {
								return 'batch-disable-user';
							},
							process_url : function() {
								return constant.apiBase + "/batch/disable-user";
							},
							finish_i18n_code : function() {
								return 'batch.process.finish.disable.user';
							}
						}, 
						{
							// 批量添加角色
							type : function() {
								return 'batch-add-role';
							},
							addParams : function(fileItem) {
								var p = {
										domainId : $rootScope.loginDomainsDropdown.option.id
								};
								fileItem.formData.push(p);
							},
							process_url : function() {
								return constant.apiBase + "/batch/add-role";
							},
							finish_i18n_code : function() {
								return 'batch.process.finish.add.role';
							}
						}, 
						{
							// 批量添加权限
							type : function() {
								return 'batch-add-permission';
							},
							addParams : function(fileItem) {
								var p = {
										domainId : $rootScope.loginDomainsDropdown.option.id
								};
								fileItem.formData.push(p);
							},
							process_url : function() {
								return constant.apiBase + "/batch/add-permission";
							},
							finish_i18n_code : function() {
								return 'batch.process.finish.add.permission';
							}
						}, 
						{
							// 批量关联用户和标签
							type : function() {
								return 'batch-relate-user-tag';
							},
							tag_query_show : function() {
								return true;
							},
							addParams : function(fileItem) {
								var p = {
									tagId : $scope.process_param.tag_param.selected.id
								};
								fileItem.formData.push(p);
							},
							process_url : function() {
								return constant.apiBase + "/batch/relate-user-tag";
							},
							process_btn_enable : function() {
								if ($scope.process_param.tag_param && $scope.process_param.tag_param.selected) {
									return true;
								}
								return false;
							},
							finish_i18n_code : function() {
								return 'batch.process.finish.relate.user.tag';
							}
						}, 
						{
							// 批量关联用户和角色
							type : function() {
								return 'batch-relate-user-role';
							},
							role_query_show : function() {
								return true;
							},
							addParams : function(fileItem) {
								var p = {
									roleId : $scope.process_param.role_param.selected.id
								};
								fileItem.formData.push(p);
							},
							process_url : function() {
								return constant.apiBase + "/batch/relate-user-role";
							},
							process_btn_enable : function() {
								if ($scope.process_param.role_param && $scope.process_param.role_param.selected) {
									return true;
								}
								return false;
							},
							finish_i18n_code : function() {
								return 'batch.process.finish.relate.user.role';
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
					removeAfterUpload : true
				});

				uploader.onBeforeUploadItem = function(fileItem) {
					var process_type = $scope.process_param.type;
					var handler = batch_process_handler.getHander(); 
					if (handler && handler.addParams) {
						batch_process_handler.getHander().addParams(fileItem);
					}
					fileItem.url = handler.process_url();
					fileItem.onProgress = function(progress) {
						AlertService
								.addAutoDismissAlert(
										constant.messageType.info,
										$rootScope
												.translate('batch.process.tips.processing'));
					}
					fileItem.onComplete = function(response, status, headers) {
						AlertService
								.addAutoDismissAlert(
										constant.messageType.info,
										$rootScope
												.translate('batch.process.tips.procese.success'));
						if (response.info && response.info.length > 0) {
							AlertService.addAlert(constant.messageType.danger,
									response.info[0].msg);
							return;
						}
						$scope.process_result = response.data;
						if (process_type) {
							$scope.process_result['process_type'] = $rootScope
							.translate(batch_process_handler.getHander(
									process_type).finish_i18n_code());
						} else {
							$scope.process_result['process_type'] = "Error";
						}
					}
					fileItem.onError = function(response, status, headers) {
						AlertService
								.addAlert(
										constant.messageType.danger,
										$rootScope
												.translate('batch.process.tips.process.failure'));
						uploader.clearQueue();
					}
				};

				// 保证只有当前加入的file在队列中
				uploader.onAfterAddingFile = function(fileItem) {
					uploader.queue = [ fileItem ];
				};
			};
			return {
				name : "BatchController",
				fn : [ "$rootScope", "$scope", "$location", "AlertService",
						"FileUploader", "GroupService", "TagService",
						"RoleService", Controller ]
			};

		});
