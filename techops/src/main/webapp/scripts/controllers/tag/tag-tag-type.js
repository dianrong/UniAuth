define(['../../utils/constant', '../../utils/utils'], function (constant, utils) {

    var Controller = function ($rootScope, $scope, TagService, dialogs, $state, AlertService) {

        $scope.queryTagTypes = function () {
            var params = {};
            $scope.tagTypes = [];
            $scope.tagTypesLoading = constant.loading;

            if(!$rootScope.loginDomainsDropdown || !$rootScope.loginDomainsDropdown.option) {
                $scope.tagTypesLoading = constant.loadEmpty;
                return;
            }
            params.domainId = $rootScope.loginDomainsDropdown.option.id;

            TagService.getTagTypes(params, function (res) {

                var result = res.data;
                if(res.info) {
                    $scope.tagTypesLoading = constant.loadError;
                    return;
                }
                if(!result) {
                    $scope.tagTypesLoading = constant.loadEmpty;
                    return;
                }
                $scope.tagTypesLoading = '';
                $scope.tagTypes = result;

            }, function () {
                $scope.tagTypes = [];
                $scope.tagTypesLoading = constant.loadError;
            });
        };
        $scope.queryTagTypes();

        $scope.addNewTagType = function() {
            if(!$scope.tagTypes) {
                $scope.tagTypes = [];
            }
            if($scope.tagTypes.length > 0) {
                var tagType = $scope.tagTypes[$scope.tagTypes.length - 1];
                // the tagType without id = newly added tagType.
                if(!tagType.id){
                    return;
                }
            }
            $scope.tagTypesLoading = '';
            $scope.tagTypes.push({
                code:'',
                domainId:$rootScope.loginDomainsDropdown.option.id,
                editable: true
            });
        };
        $scope.confirmEdit = function(tagType) {
            var param = {};
            param.id = tagType.id;
            param.code = tagType.code;
            param.domainId = tagType.domainId;
            if(!tagType.id) {
                TagService.addNewTagType(param, function (res) {
                    var result = res.data;
                    if(res.info) {
                        for(var i=0; i<res.info.length;i++) {
                            AlertService.addAlert(constant.messageType.danger, res.info[i].msg);
                        }
                        $scope.cancelEdit(tagType);
                        return;
                    }
                    AlertService.addAutoDismissAlert(constant.messageType.info, $rootScope.translate('tagMgr.tips.createTagSuccess'));
                    tagType.id = result.id;
                }, function () {
                    AlertService.addAutoDismissAlert(constant.messageType.danger, $rootScope.translate('tagMgr.tips.createTagFailure'));
                });
            } else {
                TagService.updateTagType(param, function (res) {
                    var result = res.data;
                    if(res.info) {
                        for(var i=0; i<res.info.length;i++) {
                            AlertService.addAlert(constant.messageType.danger, res.info[i].msg);
                        }
                        $scope.cancelEdit(tagType);
                        return;
                    }
                    AlertService.addAutoDismissAlert(constant.messageType.info, $rootScope.translate('tagMgr.tips.modifyTagSuccess'));
                }, function () {
                    AlertService.addAutoDismissAlert(constant.messageType.danger, $rootScope.translate('tagMgr.tips.modifyTagFailure'));
                });
            }
            tagType.editable = false;
        };
        $scope.initEdit = function(tagType) {
            tagType.editable = true;
            tagType.originId = tagType.id;
            tagType.originDomainId = tagType.domainId;
            tagType.originCode = tagType.code;
        };
        $scope.cancelEdit = function(tagType) {
            if(!tagType.id) {
                var index = $scope.tagTypes.indexOf(tagType);
                $scope.tagTypes.splice(index, 1);
                if($scope.tagTypes.length == 0) {
                    $scope.tagTypesLoading = constant.loadEmpty;
                }
                return;
            }
            tagType.editable = false;
            tagType.id = tagType.originId;
            tagType.domainId = tagType.originDomainId;
            tagType.code = tagType.originCode;
        };

        $scope.launch = function(which, param) {
            switch(which) {
                case 'del':
                    var dlg = dialogs.create('views/common/dialogs/enable-disable.html','EnableDisableController',
                        {
                            "header":$rootScope.translate('tagMgr.tips.delTagType'),
                            "msg":$rootScope.translate('tagMgr.tips.confirmMsgPre')+ param.code + $rootScope.translate('tagMgr.tips.confirmMsgSuffix')
                        }, {size:'md'}
                    );
                    dlg.result.then(function (yes) {
                        TagService.deleteTagType(
                            {
                                'id':param.id
                            }
                            , function(res) {
                                if(res.info) {
                                    for(var i=0; i<res.info.length;i++) {
                                        AlertService.addAlert(constant.messageType.danger, res.info[i].msg);
                                    }
                                    return;
                                }
                                var index = $scope.tagTypes.indexOf(param);
                                $scope.tagTypes.splice(index, 1);
                                if($scope.tagTypes.length == 0) {
                                    $scope.tagTypesLoading = constant.loadEmpty;
                                }
                                AlertService.addAutoDismissAlert(constant.messageType.info, $rootScope.translate('tagMgr.tips.delTagSuccess'));
                            }, function(err) {
                                AlertService.addAutoDismissAlert(constant.messageType.info, $rootScope.translate('tagMgr.tips.delTagFailure'));
                            }
                        );
                    }, function (no) {
                        // do nothing
                    });
                    break;
            }
        };

        $scope.$on('selected-domain-changed', $scope.queryTagTypes);
        $scope.$on('selected-language-changed', $scope.queryTagTypes);
        

    };

    return {
        name: "TagTypeController",
        fn: ["$rootScope", "$scope", "TagService", "dialogs", "$state", "AlertService", Controller]
    };

});
