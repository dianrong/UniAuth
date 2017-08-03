// 定义Captcha验证处理模块
var captcha_validate_modal = (function(){
	// 获取验证码
	var get_captcha_val = function() {
		return $('#captcha_validate_captcha').val();
	}
	
	// 处理异常信息的回调函数
	var confirm_error_process = function(error) {
		if (error) {
			$('#captcha_validate_error').html(error);
		}
	}
	
	// 刷新验证码
	var refresh_captcha = function() {
		refresh_verfypic($('#captcha_validate_modal_captcha'));
	}
	
	// 入参为确定之后的处理函数, 取消的处理函数
	var process_function = function(confirm_callback, cancel_callback) {
		// 刷新验证码
		$('#captcha_validate_modal_captcha').click(function(){
			refresh_captcha();
		});
		
		// 实现初始化各个元素
		$('#captcha_validate_error').html('');
	
		$('#captcha_validate_captcha').val('');
		$('#captcha_validate_ok').attr("disabled","disabled"); 
		
		// 确认按钮控制
		$('#captcha_validate_captcha').keyup(function(){
			if(get_captcha_val()) {
				$('#captcha_validate_ok').removeAttr("disabled","disabled");
			} else {
				$('#captcha_validate_ok').attr("disabled","disabled");
			}
		});
		
		// 取消模态框事件处理
		var captcha_validate_modal_dismiss_fun = function() {
			$('#captcha_validate_error').html('');
			if (cancel_callback) { 
				cancel_callback(get_captcha_val(), refresh_captcha);
			}
		}
		
		// 确认模态事件
		var captcha_validate_modal_confirm_fun = function() {
			$('#captcha_validate_error').html('');
			if (confirm_callback) {
				confirm_callback(get_captcha_val(), confirm_error_process, refresh_captcha);
			}
		}
		// 绑定处理函数
		$('#captcha_validate_close').click(captcha_validate_modal_dismiss_fun);
		$('#captcha_validate_ok').click(captcha_validate_modal_confirm_fun);
		// show modal
		$('#captcha_validate_modal').modal('show');
	};
	return {
						process: process_function,
						dismiss: function() {
							$('#captcha_validate_modal').modal('hide');
						}
					};
}
)();