define(['../../utils/constant', '../../utils/utils'], function (constant, utils) {

    var Controller = function ($rootScope, $scope, TagService, dialogs, $state, AlertService) {

        function getTagTypes() {
            var params = {};
            params.domainId = $rootScope.loginDomainsDropdown.option.id;
            TagService.getTagTypes(params).$promise.then(function(res) {
                var tagTypes = res.data;
                if(!tagTypes) {
                    tagTypes = [];
                }
                var empty = {
                    code : '请选择'
                };
                tagTypes.unshift(empty);
                utils.generatorDropdown($scope, 'tagTypesDropdown', tagTypes, tagTypes[0]);
                $scope.getTagTypeCode = function(tag) {
                    var items = $scope.tagTypesDropdown.items;
                    for(var i=0; i<items.length; i++) {
                        if(tag.tagTypeId == items[i].id) {
                            return items[i].code;
                        }
                    }
                    return 'error';
                };
                $scope.queryTag();
            });
        }
        getTagTypes();

        $scope.navToTagUserGrp = function(tag) {
            TagService.tagShared.selected = tag;
            $state.go('rel.tag--user-grp');
        };

        $scope.pagination = {
            pageSize: constant.pageSize,
            curPage: 1,
            totalCount: 0
        };
        $scope.tagQuery = {};
        $scope.queryTag = function () {
            if(!$rootScope.loginDomainsDropdown || !$rootScope.loginDomainsDropdown.option || !$rootScope.loginDomainsDropdown.option.id) {
                $scope.tagsLoading = constant.loadEmpty;
                return;
            }
            var params = {};
            params.pageNumber = $scope.pagination.curPage - 1;
            params.pageSize = $scope.pagination.pageSize;

            $scope.tags = [];
            $scope.tagsLoading = constant.loading;

            params.domainId = $rootScope.loginDomainsDropdown.option.id;
            params.tagTypeId = $scope.tagTypesDropdown.option.id;
            params.fuzzyCode = $scope.tagQuery.code;

            TagService.getTags(params, function (res) {
                var result = res.data;
                if(res.info) {
                    $scope.tagsLoading = constant.loadError;
                    return;
                }
                if(!result) {
                    $scope.tagsLoading = constant.loadEmpty;
                    return;
                }

                $scope.tagsLoading = '';
                $scope.tags = result.data;

                $scope.pagination.curPage = result.currentPage + 1;
                $scope.pagination.totalCount = result.totalCount;
                $scope.pagination.pageSize = result.pageSize;

            }, function () {
                $scope.tags = [];
                $scope.tagsLoading = constant.loadError;
            });
        };

        $scope.launch = function(which, param) {
            switch(which) {
                case 'status':
                    var dlg = dialogs.create('views/common/dialogs/enable-disable.html','EnableDisableController',
                        {
                            "header":param.status?'标签-启用':'标签-禁用',
                            "msg":"您确定要" + (param.status?'启用':'禁用') + "标签: " + param.code + "吗?"
                        }, {size:'md'}
                    );
                    dlg.result.then(function (yes) {
                        TagService.updateTag(
                            {
                                'id':param.id,
                                'code': param.code,
                                'description': param.description,
                                'tagTypeId':param.tagTypeId,
                                'domainId':param.domainId,
                                'status':param.status?0:1
                            }
                            , function(res) {
                                AlertService.addAutoDismissAlert(constant.messageType.info, (param.status?'启用':'禁用') + "标签成功.");
                                $scope.queryTag();
                            }, function(err) {
                                console.log(err);
                            }
                        );
                    }, function (no) {
                        // do nothing
                    });
                    break;
                case 'modify':
                    var dlg = dialogs.create('views/tag/dialogs/tag-modify.html','ModifyTagController',
                        param, {size:'md'}
                    );
                    dlg.result.then(function (close) {
                        $scope.queryTag();
                    }, function (dismiss) {
                        //
                    });
                    break;
                case 'add':
                    var dlg = dialogs.create('views/tag/dialogs/tag-add.html','AddTagController',
                        {}, {size:'md'}
                    );
                    dlg.result.then(function (close) {
                        $scope.queryTag();
                    }, function (dismiss) {
                        //
                    });
                    break;
            }
        };

        $scope.$on('selected-domain-changed', $scope.queryTag);
    };

    return {
        name: "TagInfoController",
        fn: ["$rootScope", "$scope", "TagService", "dialogs", "$state", "AlertService", Controller]
    };

});
