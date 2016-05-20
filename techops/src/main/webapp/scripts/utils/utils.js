define(['utils/constant'], function (constant) {
    return {
        generatorDropdown: function ($scope, name, items, defaultOpt) {
            $scope[name] = {};
            $scope[name].isOpen = false;
            $scope[name].items = items;
            $scope[name].selectOption = function (option) {
                $scope[name].option = option;
                // console.log($scope.decisionDropdown);
            }
            if (!defaultOpt && items.length) {
                $scope[name].option = items[0];
            } else {
                $scope[name].option = defaultOpt;
            }
        },
        getParentNodeArray: function recur(rootNodes, array) {
            if(rootNodes && rootNodes.length) {
                for (var i = 0; i < rootNodes.length; i++) {
                    var node = rootNodes[i];
                    if(node.type == constant.treeNodeType.group) {
                        array.push(node);
                    }
                    var children = node.children;
                    if(children && children.length) {
                        recur(children, array);
                    }
                }
            }
            return array;
        },
        extractCheckedGrpAndUserIds: function extractCheckedGrpAndUserIds(nodeArray, checkedGroupIds, checkedUserIds) {
            if(nodeArray && nodeArray.length) {
                for (var i = 0; i < nodeArray.length; i++) {
                    var node = nodeArray[i];
                    if(node.checked) {
                        if(constant.treeNodeType.group == node.type) {
                            checkedGroupIds.push(node.id);
                        } else if(constant.treeNodeType.memberUser == node.type) {
                            checkedUserIds.push(node.id);
                        }
                    }
                    if(node.children && node.children && node.children.length>0) {
                        extractCheckedGrpAndUserIds(node.children, checkedGroupIds, checkedUserIds);
                    }
                }
            }
        },
        transformResponse: function (resp) {
            if (resp && resp.respCode && resp.respCode.indexOf('_2') == 0) {
                return resp.result;
            }
            return undefined;
        },
        formatNumbers: function(val){
            return Math.ceil(val*10000)/1000000;
        },
        convertToDate: function (val) {
            if (!val) {
                return null;
            }
            try {
                var res = val.match(/(\d{4})-(\d{1,2})-(\d{1,2})/);
                if (res.length == 4) {
                    var dt = new Date();
                    dt.setFullYear(res[1]);
                    var year = parseInt(res[2]) - 1;
                    year = year > 12 ? 12 : year;
                    dt.setMonth(parseInt(year));
                    var day = parseInt(res[3]);
                    day = day > 31 ? 31 : day;
                    dt.setDate(day);
                    console.log(dt.toJSON());
                    return dt.getTime();
                }
                return null;
            } catch (e) {
                return null;
            }

        },
        convertToMap: function (array) {
            var result = {};
            if (!array) {
                return result;
            }
            for (var i = 0, len = array.length; i < len; i++) {
                var obj = array[i];
                result[obj.value] = obj.name;
            }
            return result;
        }
    }
});
