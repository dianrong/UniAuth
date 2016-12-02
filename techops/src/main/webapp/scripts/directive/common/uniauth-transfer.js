define(['angular'], function (angular) {
    var uniauthTransferDirective = function ($timeout) {
    	var ele_document = angular.element(document);
		// Ctrl key
    	var ctrl_key_press=false, ctrl_key_code = 17;
		// Shift key
    	var shift_key_press = false, shift_key_code = 16;
    	ele_document.bind('keydown', function(event){
    		keyboardEventProcess(event, true);
    	});
    	ele_document.bind('keyup', function(event){
    		keyboardEventProcess(event, false);
    	});
    	function keyboardEventProcess(event, isKeyDown) {
    		isKeyDown = isKeyDown === true;
    		if (event && event.keyCode){
				if (event.keyCode === ctrl_key_code) {
					ctrl_key_press = isKeyDown;
				}
				if (event.keyCode === shift_key_code) {
					shift_key_press = isKeyDown;
				}
			}
    	}
        return {
            restrict: 'AE',
            replace: false,
            template: '' +
              '<input ng-show="config.showInputFilter" class="uniauth-transfer-fiter-input form-control"  type="text"  ng-model="uniauthTransferConfig.filter_input_predicate" placeholder="{{uniauthTransferConfig.filter_input_placeholder}}" />' + 
              '<div class="uniauth-transfer">' +
              '<div class="row">' +
	              '<div ng-bind="uniauthTransferConfig.srcTitle" class="uniauth-transfer-list-title"></div>' +
	              '<div class="uniauth-transfer-operation"></div>' +
	              '<div ng-bind="uniauthTransferConfig.targetTitle" class="uniauth-transfer-list-title"></div>' +
	           '</div>' +
	           '<div class="row">' +
                '<div class="uniauth-transfer-list">' +
                  '<div ng-repeat="item in uniauthTransferConfig.srcItems" ng-class="{true: \'uniauth-transfer-item focused\', false: \'uniauth-transfer-item\'}[itemFocused(item, true)]" ng-click="itemSelect(item, $index, true)"  ng-dblclick="itemMove(item, $index, true)">' +
                    '{{item[config.displayKey]}}' +
                  '</div>' +
                '</div>' +
                '<div class="uniauth-transfer-operation">' +
                			'<div class="uniauth-transfer-btn-move">' +
	      						'<button class="uniauth-transfer-operation-btn btn" ng-click="moveRight()" ng-disabled="moveBtnDisable(false)"> > </button>' +
	      						'<button class="uniauth-transfer-operation-btn btn" ng-click="moveLeft()" ng-disabled="moveBtnDisable(true)"> < </button>' +
      						'</div>' +
      						'<div class="uniauth-transfer-btn-move-all">' +
	      						'<button class="uniauth-transfer-operation-btn btn" ng-click="moveAllRight()"> >> </button>' +
	      						'<button class="uniauth-transfer-operation-btn btn" ng-click="moveAllLeft()"> << </button>' +
      						'</div>' +
      			'</div>' +
                '<div class="uniauth-transfer-list">' +
                  '<div ng-repeat="item in uniauthTransferConfig.targetItems" ng-class="{true: \'uniauth-transfer-item focused\', false: \'uniauth-transfer-item\'}[itemFocused(item, false)]" ng-click="itemSelect(item, $index, false)" ng-dblclick="itemMove(item, $index, false)">' +
                    '{{item[config.displayKey]}}' +
                  '</div>' +
                '</div>' +
                '</div>' +
              '</div>',
            scope: {
              uniauthTransferConfig: '='
            },
            link: function(scope, el, attrs) {
            		//  config init
            		scope.config = {
            				displayKey : scope.uniauthTransferConfig.displayKey || 'name',
            				showInputFilter : scope.uniauthTransferConfig.showInputFilter === true,
            				filter_input_refresh_fun : scope.uniauthTransferConfig.filter_input_refresh_fun,
            				filter_input_refresh_delay : (isNaN(scope.uniauthTransferConfig.filter_input_refresh_delay) || scope.uniauthTransferConfig.filter_input_refresh_delay < 0) ? 
            						500 : scope.uniauthTransferConfig.filter_input_refresh_delay,
            		}
            		
            		// 对外暴露的
            		scope.uniauthTransferConfig.srcItems = scope.uniauthTransferConfig.srcItems || [];
            		scope.uniauthTransferConfig.targetItems = scope.uniauthTransferConfig.targetItems || [];
            		scope.uniauthTransferConfig.srcTitle = scope.uniauthTransferConfig.srcTitle|| 'from';
            		scope.uniauthTransferConfig.targetTitle = scope.uniauthTransferConfig.targetTitle|| 'to';
            		scope.uniauthTransferConfig.filter_input_placeholder = scope.uniauthTransferConfig.filter_input_placeholder|| '';
            		
            		scope.uniauthTransferConfig.filter_input_predicate = '';
            		
            	
            		// 选中的数据 
            		var srcSelectedItems = [];
            		var targetSelectedItems = [];
            		var shift_left_last_click_index = undefined;
            		var shift_right_last_click_index = undefined;
            		
            		// 单击事件处理
      				scope.itemSelect = function (item, index, isSrcArray) {
      					if (!item || isNaN(index) || index < 0) return;
      					isSrcArray = isSrcArray === true;
      					// 选中的items 集合
      					var items = isSrcArray ? srcSelectedItems : targetSelectedItems;
      					// 左边框或右边框的数据集合
      					var orginalItems = isSrcArray ? scope.uniauthTransferConfig.srcItems : scope.uniauthTransferConfig.targetItems;
      					item = constructItemWithIndex(item, index);
      					var items = null;
      					if ( isSrcArray === false) {
      						items = targetSelectedItems;
      						srcSelectedItems.splice(0, srcSelectedItems.length);
      					} else {
      						items = srcSelectedItems;
      						targetSelectedItems.splice(0, targetSelectedItems.length);
      					}
      					if (ctrl_key_press) {
      						var contains = false;
      						for (var i = 0 ; i < items.length ; i++) {
          						var _item =  items[i];
          						// remove
          						if (_item.item === item.item) {
          							items.splice(i, 1);
          							contains = true;
          							break;
          						}
          					}
      						if(!contains) {
      							items.push(item);
      							sortItemArray(items);
      						}
      					} else {
      						// clear
      						items.splice(0, items.length);
      						items.push(item);
      					}
      					// shift key process
      					if (shift_key_press) {
      						var process_index = isSrcArray ? shift_left_last_click_index : shift_right_last_click_index;
      						if (isNaN(process_index)) {
      							if (isSrcArray) {
      								shift_left_last_click_index = index;
      							} else {
      								shift_right_last_click_index = index;
      							}
      						} else {
      							var start_index = process_index > index ? index : process_index;
      							var end_index = start_index ===  process_index ? index : process_index;
      							for (var i = start_index; i <= end_index; i++) {
      								pushOriginalItemToSelectedItms(items, orginalItems[i], i);
      							}
      							// sort
      							sortItemArray(items);
      						}
      					} else {
      						if ( isSrcArray ) {
      							shift_left_last_click_index = index;
      		            		shift_right_last_click_index = undefined;
      						} else {
      							shift_left_last_click_index = undefined;
      		            		shift_right_last_click_index = index;
      						}
      					}
      				}
      				
      				// 辅助shift键处理function
      				function pushOriginalItemToSelectedItms(selectedItems, orginalItem, index) {
      					if (!selectedItems || !orginalItem || isNaN(index)) return;
      					for (var i=0; i < selectedItems.length; i++) {
      						var _item = selectedItems[i].item;
      						if (_item === orginalItem) {
      							// contains true
      							return;
      						}
      					}
      					selectedItems.push(constructItemWithIndex(orginalItem, index));
      				}
      				
      				scope.itemFocused = function (item,  isSrcArray) {
      					if (!item) return;
      					var items = isSrcArray === false ? targetSelectedItems : srcSelectedItems ;
      					for (var i = 0 ; i < items.length ; i++) {
      						if (items[i].item === item) {
      							return true;
      						}
      					}
      					return false;
      				}
      				
      				scope.moveBtnDisable = function (isMoveLeft) {
      					isMoveLeft = isMoveLeft === true;
      					if (isMoveLeft) {
      						return targetSelectedItems.length == 0;
      					} else {
      						return srcSelectedItems.length == 0;
      					}
      				}
      				
      				// 双击处理
      				scope.itemMove = function(item, index, isSrcItem) {
      					if (!item) return;
      					// 清空选中数据
      					targetSelectedItems.splice(0, targetSelectedItems.length);
      					srcSelectedItems.splice(0, srcSelectedItems.length);
      					var items = isSrcItem === false ? targetSelectedItems : srcSelectedItems;
      					item = constructItemWithIndex(item, index);
      					items.push(item);
      					if (isSrcItem === false) {
      						scope.moveLeft();
      					} else {
      						scope.moveRight();
      					}
      				}
      				
      				scope.moveRight = function () {
      					for (var i = srcSelectedItems.length - 1 ; i >= 0 ; i--) {
      							var selectedItem = srcSelectedItems[i].item;
      							scope.uniauthTransferConfig.targetItems.unshift(selectedItem);
      							removeItem(scope.uniauthTransferConfig.srcItems, selectedItem);
      					}
      					targetSelectedItems = srcSelectedItems.splice(0, srcSelectedItems.length);
      				}
      				scope.moveLeft = function () {
      					for (var i = targetSelectedItems.length - 1 ; i >= 0 ; i--) {
  								var selectedItem = targetSelectedItems[i].item;
	  							scope.uniauthTransferConfig.srcItems.unshift(selectedItem);
	  							removeItem(scope.uniauthTransferConfig.targetItems, selectedItem);
	  					}
      					srcSelectedItems = targetSelectedItems.splice(0, targetSelectedItems.length);
      				}
      				scope.moveAllRight = function () {
      					var items = scope.uniauthTransferConfig.srcItems.splice(0, scope.uniauthTransferConfig.srcItems.length);
      					scope.uniauthTransferConfig.targetItems = items.concat(scope.uniauthTransferConfig.targetItems);
      					srcSelectedItems.splice(0, srcSelectedItems.length);
      					for (var i =0; i < items.length; i++) {
      						targetSelectedItems.push(constructItemWithIndex(items[i], i));
      					}
      				}
      				scope.moveAllLeft = function () {
      					var items = scope.uniauthTransferConfig.targetItems.splice(0, scope.uniauthTransferConfig.targetItems.length);
      					scope.uniauthTransferConfig.srcItems = items.concat(scope.uniauthTransferConfig.srcItems);
                		 targetSelectedItems.splice(0, targetSelectedItems.length);
      					for (var i =0; i < items.length; i++) {
      						srcSelectedItems.push(constructItemWithIndex(items[i], i));
      					}
      				}
      				
      				// remove item
      				function removeItem(itemArray, item){
      					if (!itemArray || !item) return;
      					for (var i = 0 ; i < itemArray.length; i++) {
      						if(itemArray[i] === item) {
      							itemArray.splice(i, 1);
      							return;
      						}
      					}
      				}
      				// construct itemline-block
      				function constructItemWithIndex(item, index){
      					return { item: item,item_index:index};
      				}
      				// construct item
      				function sortItemArray(itemArray){
      					if (!itemArray || itemArray.length < 2) return;
      					itemArray.sort(function(item1, item2) {
      						return  item1.item_index -  item2.item_index;
      					});
      				}
      				
      				var last_refresh_milles = undefined;
      				function filter_input_refresh_fun_delegate(needRecall){
      					if (!scope.config.filter_input_refresh_fun) return;
      					needRecall = !(needRecall === false)
      					var predicate = scope.uniauthTransferConfig.filter_input_predicate;
      					var currentMilles = new Date().getTime();
      					if (isNaN(last_refresh_milles) || (currentMilles - last_refresh_milles > scope.config.filter_input_refresh_delay )) {
      						scope.config.filter_input_refresh_fun(predicate);
      						 last_refresh_milles = currentMilles;
      					} else {
      						if (needRecall) {
      							// call it after a delay
      							$timeout(function(){filter_input_refresh_fun_delegate(false)}, scope.config.filter_input_refresh_delay);
      						}
      					}
      				}
            		// watch
      				scope.$watch('uniauthTransferConfig.filter_input_predicate', filter_input_refresh_fun_delegate);
               }
          }
    };
    return {
    	name:'uniauthTransfer',
    	directiveFn: uniauthTransferDirective
    }
});