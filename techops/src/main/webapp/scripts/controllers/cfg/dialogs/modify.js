define(['../../../utils/constant', '../../../utils/utils'], function (constant, utils) {
    var Controller = function ($rootScope, $scope, $uibModalInstance, CfgService, AlertService, data, FileUploader) {

        var cfg =$scope.cfg = data;

        function getCfgTypes() {
            CfgService.getAllCfgTypes().$promise.then(function(res) {
                var cfgTypes = res.data;
                var cfgTypesArray = [];
                var selectedCfgType;
                for(var prop in cfgTypes) {
                    var cfgType = {id:prop, code:cfgTypes[prop]};
                    cfgTypesArray.push(cfgType);
                    if(cfg.cfgTypeId == prop) {
                        selectedCfgType = cfgType;
                    }
                }
                utils.generatorDropdown($scope, 'cfgModifyTypesDropdown', cfgTypesArray, selectedCfgType);
            });
        }
        getCfgTypes();

        $scope.cancel = function(){
            $scope.msg = '';
            $uibModalInstance.dismiss();
        }; // end cancel


        $scope.save = function(){
            cfg.cfgTypeId = $scope.cfgModifyTypesDropdown.option.id;
            var formData = new FormData();
            formData.append('id', cfg.id);
            formData.append('cfgTypeId', cfg.cfgTypeId);
            formData.append('cfgKey', cfg.cfgKey);
            formData.append('value', cfg.value);
            CfgService.addOrUpdateConfig(formData,
                function(res) {
                    // user add api successed
                    if(res.info) {
                        $scope.msg = res.info[0].msg;
                        return;
                    } else {
                        AlertService.addAutoDismissAlert(constant.messageType.info, '配置修改成功.');
                        $uibModalInstance.close();
                    }
                }, function(err) {
                    AlertService.addAutoDismissAlert(constant.messageType.danger, '配置修改失败.');
                    $uibModalInstance.close();
                }
            );
        };

        $scope.upload = function(){
            uploader.uploadAll();
        };

        var uploader = $scope.uploader = new FileUploader({
            url: constant.apiBase + "/cfg/add-or-update",
            removeAfterUpload: true
        });

        var strs = constant.cfgFields;
        uploader.onBeforeUploadItem = function(fileItem) {
            for(var index in strs) {
                var str = strs[index];
                var obj = {};
                if(str != 'cfgTypeId') {
                    obj[str] = cfg[str];
                } else {
                    obj[str] = $scope.cfgModifyTypesDropdown.option.id;
                }
                fileItem.formData.push(obj);
            }
            var valueObj = {};
            valueObj['value'] = fileItem.file.name;
            fileItem.formData.push(valueObj);

            fileItem.onProgress = function(progress) {
                AlertService.addAutoDismissAlert(constant.messageType.info, '配置文件上传中...');
            }
            fileItem.onComplete = function(response, status, headers) {
                if(response.info) {
                    $scope.msg = response.info[0].msg;
                    return;
                } else {
                    AlertService.addAutoDismissAlert(constant.messageType.info, '配置文件上传成功.');
                    $uibModalInstance.close();
                }
            }
            fileItem.onError = function(response, status, headers) {
                AlertService.addAlert(constant.messageType.danger, '配置文件上传失败.');
                $uibModalInstance.close();
            }
        };

    };

    return {
        name: "ModifyCfgController",
        fn: ["$rootScope", "$scope","$uibModalInstance", "CfgService", "AlertService", "data", "FileUploader", Controller]
    };

});
