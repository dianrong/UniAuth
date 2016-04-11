define(['../../utils/constant', '../../utils/utils'], function (constant, utils) {

    var Controller = function ($rootScope, $scope, TagService, dialogs, $state, AlertService) {

        function getTagTypes() {
            var params = {};
            params.domainId = $rootScope.loginDomainsDropdown.option.id;
            TagService.getTagTypes(params).$promise.then(function(res) {
                var tagTypes = res.data;
                var empty = {
                    code : '请选择'
                };
                tagTypes.unshift(empty);
                utils.generatorDropdown($scope, 'tagTypesDropdown', tagTypes, tagTypes[0]);
                $scope.getTagCode = function(tag) {
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

        $scope.pagination = {
            pageSize: constant.pageSize,
            curPage: 1,
            totalCount: 0
        };

        $scope.queryTag = function () {
            if(!$rootScope.loginDomainsDropdown || !$rootScope.loginDomainsDropdown.option || !$rootScope.loginDomainsDropdown.option.id) {
                $scope.tagsLoading = constant.loadEmpty;
                return;
            }
            var params = $scope.tagQuery;
            if (!params) {
                params = {};
            }
            params.pageNumber = $scope.pagination.curPage - 1;
            params.pageSize = $scope.pagination.pageSize;

            $scope.tags = [];
            $scope.tagsLoading = constant.loading;

            params.domainId = $rootScope.loginDomainsDropdown.option.id;
            params.tagTypeId = $scope.tagTypesDropdown.option.id;


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

        $scope.$on('selected-domain-changed', $scope.queryTag);
    };

    return {
        name: "TagInfoController",
        fn: ["$rootScope", "$scope", "TagService", "dialogs", "$state", "AlertService", Controller]
    };

});
