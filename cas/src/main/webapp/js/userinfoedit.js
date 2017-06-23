$(function() {
	var updateInfoUrl = context_path+"/uniauth/userinfo";
	var verifyProcessUrl = context_path+"/uniauth/verification";
	
	// define constant
	var showSuccessTag = "success";
	var showFailTag = "fail";
	//初始化函数
	var init = function(){
		//去改姓名
		$('#go_update_name_btn').click(function(){
			update_user_name_show(true);
		});
		// 改姓名确认
		$('#update_name_confirm_btn').click(update_user_name_process);
		// 取消改姓名
		$('#update_name_cancel_btn').click(function() {
			update_user_name_show(false);
		});
		
		// 去改邮箱
		$('#go_update_email_btn').click(to_update_email);
		// 获取邮箱验证码
		$('#get_email_captcha').click(update_email_get_captcha)
		// 确认修改邮箱
		$('#update_email_btn_confirm').click(update_email_to_check_verifycode);
		
		// 去改电话
		$('#go_update_phone_btn').click(to_update_phone);
		// 获取电话验证码
		$('#get_phone_captcha').click(update_phone_get_captcha)
		// 确认修改电话
		$('#update_phone_btn_confirm').click(update_phone_to_check_verifycode);
		
		//给与电话输入框进行输入事件处理
		$('#update_phone_new_phone').keyup(phone_input_keyup_event);
		
		// 去修改密码
		$('#go_update_password_btn').click(function(){
			$('#modal-new-password').modal('show');
		});
		//更新密码按钮显示与否
		$('#modal-new-password #orign_password').keyup(btn_update_password_ok_show);
		$('#modal-new-password #password').keyup(btn_update_password_ok_show);
		$('#modal-new-password #repassword').keyup(btn_update_password_ok_show);
		//进一步校验
		$('#modal-new-password #orign_password').blur(function(){
			update_password_state_check(true, 1);
		});
		$('#modal-new-password #password').blur(function(){
			update_password_state_check(true, 2);
		});
		$('#modal-new-password #repassword').blur(function(){
			update_password_state_check(true, 3);
		});
		//确定修改密码
		$('#modal_new_password_ok_btn').click(btn_update_password_ok_process);
		//关闭事件
		$('#modal-new-password').on('hidden.bs.modal', modal_update_password_close_event);
		
		// 各种交互性的显示
		// update email
		$('#update_email_captcha').keyup(update_email_verify_btn_process);
		// update phone
		$('#update_phone_captcha').keyup(update_phone_verify_btn_process);
	}
	// 显示和隐藏修改姓名框
	var update_user_name_show = function(show) {
		if (show) {
			$('.info_name_edit').removeClass('hidden-element');
			$('.info_name_show').addClass('hidden-element');
		} else {
			$('.info_name_show').removeClass('hidden-element');
			$('.info_name_edit').addClass('hidden-element');
		}
	}
	// 修改个人姓名
	var update_user_name_process = function() {
		var data = {
				name : $('#update_name_new_name').val()
		};
		$.ajax({  
            type : "POST", 
            url : updateInfoUrl+'/update',
            data : data,
            dataType : 'json',
            success : function(data) {
            	   if(data.info) {
            		   infonotice(showFailTag, data.info[0].msg);
            	   } else {
            		   infonotice(showSuccessTag, $.i18n.prop('frontpage.userinfo.edit.update.ok'));
            		   $('#name_label').html($('#update_name_new_name').val());
            	   }
            },
            complete : function(){
            	update_user_name_show(false);
            },
            error: function(jqXHR, textStatus, errorMsg){
            	logOperation.error(errorMsg);
            }
        });  
	}
	
	// 自动退出
	var autoLogout = function(milles){
		if (isNaN(milles)) {
			milles = 3000;
		}
		if (milles <= 0) {
			window.location=context_path+'/logout';
		} else {
			 setTimeout(function(){
				   window.location=context_path+'/logout';
			   }, 3000);
		}
	}
	// 交互性的验证函数
	function isEmail(email){
	    var filter  = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
	    return filter.test(email);
	}
	
	// update email
	// 显示发送验证码按钮
	var update_email_new_email_check = function(){
		var new_email = $('#update_email_new_email').val();
		if (isEmail(new_email)) {
				return true;
		} else {
				return false;
		}
	}
	
	var update_email_verify_btn_process = function(){
		var verifyCode = $('#update_email_captcha').val();
		var stepbtn = $('#update_email_btn_confirm');
		if(verifyCode && update_email_new_email_check()) {
			stepbtn.removeAttr("disabled","disabled");
		} else {
			stepbtn.attr("disabled","disabled"); 
		}
	}
	
	// ready show update email modal
	var to_update_email = function(){
		$('#update_email_captcha').val('');
		$('#update_email_warninfo').val('');
		$('#modal-new-email').modal('show');
	}
	// get email verify code
	var update_email_get_captcha = function(){
		if (!update_email_new_email_check()) {
			$('#update_email_warninfo').html($.i18n.prop('frontpage.userinfo.edit.update.invalid.email'));
			return;
		}
		var data = {
				identity : $('#update_email_new_email').val()
		};
		$.ajax({  
            type : "POST", 
            url : verifyProcessUrl+'/send',
            data : data,
            dataType : 'json',
            success : function(data) {
            	   if(data.info) {
            		   $('#update_email_warninfo').html(data.info[0].msg);
            	   } else {
            		   if (data.data) {
            			   $('#email_verify_code_div').html(data.data);
            		   }
            		   // success
            		   var count_btn = $('#get_email_captcha');
	                   	countBtn(count_btn, function(new_label){
	                   		count_btn.html(new_label)
	                   	},function(){
	                   		return count_btn.html();
	                   	}, 120);
            	   }
            },error: function(jqXHR, textStatus, errorMsg){
            	logOperation.error(errorMsg);
            }
        });  
	}
	// confirm
	var update_email_to_check_verifycode = function () {
		// check verification
		var data = {
				identity : $('#update_email_new_email').val(),
				verifyCode: $('#update_email_captcha').val()
		};
		$.ajax({  
            type : "POST", 
            url : verifyProcessUrl+'/verify',
            data : data,
            dataType : 'json',
            success : function(data) {
            	   if(data.info) {
            		   $('#update_email_warninfo').html(data.info[0].msg);
            	   } else {
            		   // success, submit update
            		   update_email_confirm();
            	   }
            },error: function(jqXHR, textStatus, errorMsg){
            	logOperation.error(errorMsg);
            }
        });  
	}
	
	// update email confirm
	var update_email_confirm = function() {
		var data = {
				email: $('#update_email_new_email').val()
		};
		$.ajax({  
            type : "POST", 
            url : updateInfoUrl+'/email',
            data : data,
            dataType : 'json',
            success : function(data) {
            	   if(data.info) {
            		   $('#update_email_warninfo').html(data.info[0].msg);
            	   } else {
            		   // success
            			$('#modal-new-email').modal('hide');
            		   $('#email_label').html($('#update_email_new_email').val());
            		   infonotice(showSuccessTag, $.i18n.prop('frontpage.userinfo.edit.update.email.success'));
            		   autoLogout();
            	   }
            },error: function(jqXHR, textStatus, errorMsg){
            	logOperation.error(errorMsg);
            }
        });  
	}
	
	// update phone
	function isPhoneNumber(number){
	    var filter  = /^((13[0-9])|(15[^4])|(18[0-9])|(17[0-8])|(147))\d{8}$/;
	    return filter.test(number);
	}
	
	var update_phone_new_phone_check = function(){
		var new_phone = $('#update_phone_new_phone').val();
		if (isPhoneNumber(new_phone)) {
				return true;
		} else {
			return false;
		}
	}
	
	var update_phone_verify_btn_process = function(){
		var verifyCode = $('#update_phone_captcha').val();
		var stepbtn = $('#update_phone_btn_confirm');
		if(verifyCode && update_phone_new_phone_check()) {
			stepbtn.removeAttr("disabled","disabled");
		} else {
			stepbtn.attr("disabled","disabled"); 
		}
	}
	
	// ready show update phone modal
	var to_update_phone = function(){
		$('#update_phone_captcha').val('');
		$('#update_phone_warninfo').val('');
		$('#modal-new-phone').modal('show');
	}
	// get phone verify code
	var update_phone_get_captcha = function(){
		if (!update_phone_new_phone_check()) {
			$('#update_phone_warninfo').html($.i18n.prop('frontpage.userinfo.edit.update.invalid.phone'));
			return;
		}
		var data = {
				identity : $('#update_phone_new_phone').val()
		};
		$.ajax({  
            type : "POST", 
            url : verifyProcessUrl+'/send',
            data : data,
            dataType : 'json',
            success : function(data) {
            	   if(data.info) {
            		   $('#update_phone_warninfo').html(data.info[0].msg);
            	   } else {
            		   if (data.data) {
            			   $('#phone_verify_code_div').html(data.data);
            		   }
            		   // success
            		   var count_btn = $('#get_phone_captcha');
	                   	countBtn(count_btn, function(new_label){
	                   		count_btn.html(new_label)
	                   	},function(){
	                   		return count_btn.html();
	                   	}, 120);
            	   }
            },error: function(jqXHR, textStatus, errorMsg){
            	logOperation.error(errorMsg);
            }
        });  
	}
	// check verification 
	var update_phone_to_check_verifycode = function () {
		// check verification
		var data = {
				identity : $('#update_phone_new_phone').val(),
				verifyCode: $('#update_phone_captcha').val()
		};
		$.ajax({  
            type : "POST", 
            url : verifyProcessUrl+'/verify',
            data : data,
            dataType : 'json',
            success : function(data) {
            	   if(data.info) {
            		   $('#update_phone_warninfo').html(data.info[0].msg);
            	   } else {
            		   // success, submit confirm
            		   update_phone_confirm();
            	   }
            },error: function(jqXHR, textStatus, errorMsg){
            	logOperation.error(errorMsg);
            }
        });  
	}
	// update phone confirm
	var update_phone_confirm = function() {
		// check verification
		var data = {
				phone: $('#update_phone_new_phone').val()
		};
		$.ajax({  
            type : "POST", 
            url : updateInfoUrl+'/phone',
            data : data,
            dataType : 'json',
            success : function(data) {
            	   if(data.info) {
            		   $('#update_email_warninfo').html(data.info[0].msg);
            	   } else {
            		   // success
            		   $('#modal-new-phone').modal('hide');
            		   $('#phone_label').html($('#update_phone_new_phone').val());
            		   infonotice(showSuccessTag, $.i18n.prop('frontpage.userinfo.edit.update.phone.success'));
            		   autoLogout();
            	   }
            },error: function(jqXHR, textStatus, errorMsg){
            	logOperation.error(errorMsg);
            }
        });  
	}
	
	// update password
	//更新密码框被关闭的时候触发的事件
	var modal_update_password_close_event = function() {
		// 清空还原密码输入框
		$('#modal-new-password #orign_password').val('');
		$('#modal-new-password #password').val('');
		$('#modal-new-password #repassword').val('');
		
		//清空提示框
		warn_info_show($('#update_pwd_warninfo', ''));
		
		//按钮还原 触发一次事件
		btn_update_password_ok_show();
	}
	
	//处理电话输入框只能输入数字的事件
	var phone_input_keyup_event = function() {
		var obj = $(this);
		//如果输入非数字，则替换为''，如果输入数字，则在每4位之后添加一个空格分隔
		obj.val(obj.val().replace(/[^\d]/g, ''));
	}
	
	//检测输入密码框的状态
	// step 位校验因子  加入之后需要考虑它
	var update_password_state_check = function(showinfo, step){
		//获取输入框信息
		var origin_pwd = $('#modal-new-password #orign_password').val();
		var new_pwd = $('#modal-new-password #password').val();
		var renew_pwd = $('#modal-new-password #repassword').val();
		
		//验证原始密码
		if(!origin_pwd && (!step || (step && step == 1))) {
			if(showinfo) {
				warn_info_show($('#update_pwd_warninfo'), $.i18n.prop('frontpage.userinfo.edit.need.orginalpwd'));
			}
			return false;
		}
		
		//验证新密码
		if((!new_pwd || new_pwd.length < 8 || new_pwd.length > 20) && (!step || (step && step == 2))){
			if(showinfo) {
				warn_info_show($('#update_pwd_warninfo'), $.i18n.prop('frontpage.userinfo.edit.need.pwdlength'));
			}
			return false;
		}
		
		//验证重复输入密码
		if(new_pwd != renew_pwd && (!step || (step && step == 3))){
			if(showinfo) {
				warn_info_show($('#update_pwd_warninfo'), $.i18n.prop('frontpage.userinfo.edit.need.pwdequal'));
			}
			return false;
		}
		
		return true;
	}
	
	//用于处理提示信息的function
	var warn_info_show = function(obj, warn_info){
		if(!obj){
			return;
		}
		var parent_group = $(obj).parent().parent('.form-group');
		//隐藏提示框
		if(!warn_info || warn_info == ''){
			if(parent_group){
				parent_group.addClass('hiddenbtn');
			}
			obj.html('');
		} else {
			if(parent_group){
				parent_group.removeClass('hiddenbtn');
			}
			obj.html(warn_info);
		}
	}
	
	//更新密码按钮的显示与否
	var btn_update_password_ok_show = function(){
		var check_btn = $('#modal_new_password_ok_btn');
		//显示按钮
		if(update_password_state_check(false)){
			check_btn.removeClass('cursordefault');
			check_btn.removeAttr('disabled');
		} else {
			check_btn.addClass('cursordefault');
			check_btn.attr('disabled', 'disabled');
		}
	}
	
	//处理编辑信息确认的按钮
	var btn_update_password_ok_process = function(){
		// check verification
		var data = {
				identity : $('#update_phone_hidden_input').val(),
				password: $('#password').val(),
				originPassword: $('#orign_password').val()
		};
		$.ajax({  
            type : "POST", 
            url : updateInfoUrl+'/password',
            data : data,
            dataType : 'json',
            success : function(data) {
               if(data.info) {
          		  infonotice(showFailTag, data.info[0].msg);
          	   } else {
          		   // success
          		   infonotice(showSuccessTag, $.i18n.prop('frontpage.userinfo.edit.update.pwdok'));
          	   }
            }, complete : function(){
            	//关闭模态框
            	$('#modal-new-password').modal('hide');
            },error: function(jqXHR, textStatus, errorMsg){
            	logOperation.error(errorMsg);
            }
        });  
	}
	
	//帮window实现一个自动提示的功能
	var infonotice = (function(){
		// 数据结构
		var notice_div_state_info = {
			// 1 没显示  2 显示
			show_state : "1",
			//当前显示的提示级别
			current_warn_class : '',
			//信息长度管理相关class
			info_length_class : '',
			//定义常量  少量信息的字符串长度
			bg_info_length:9,
			//定义提示信息显示的时长 毫秒
			notice_show_milles : 3000,
			//设置setTimeout返回的handle
			notice_settimeout_handle : '',
			//函数  是否在显示
			isShowState : function() {
				if(this.show_state == '1'){
					return false;
				}
				return true;
			},
			setShowState : function(isshow, warn_class, length_class) {
				this.current_warn_class = warn_class;
				this.info_length_class = length_class;
				if(isshow){
					this.show_state = '2';
				} else {
					//清空样式
					this.show_state = '1';
				}
			}
		}
		
		var infonotice_close = function(){
			//先尝试清除样式
			if(notice_div_state_info.isShowState()){
				var showmodal = $('#window_notice_div');
				//隐藏
				showmodal.toggle(false);
				//清空状态
				showmodal.removeClass(notice_div_state_info.current_warn_class);
				showmodal.removeClass(notice_div_state_info.info_length_class);
				//清空提示信息
				$('#top_show_info').html('');
				
				//重置状态
				notice_div_state_info.setShowState(false, '','');
			}		
		}	
		
		// 延迟关闭modal
		var process_notice_div_handle = function(){
			var currentTimeout = notice_div_state_info.notice_settimeout_handle;
			//有一个事件  先取消掉
			if(currentTimeout){
				clearTimeout(currentTimeout);
			}
			//重新设置显示事件
			var newTimeoutHandle = setTimeout(
					function(){
						infonotice_close();
					}, notice_div_state_info.notice_show_milles);
			//重新赋值
			notice_div_state_info.notice_settimeout_handle = newTimeoutHandle;
		}
		
		return function(infolevel, message){
			//先关闭
			infonotice_close();
			var infoclass = "alert-success";
			//异常级别
			if(!!infolevel && infolevel == showFailTag){
				infoclass = "alert-danger";
			}
			
			var infolength_class="top-browser-bg ";
			//信息长度定义
			if(!!message && message.length > notice_div_state_info.bg_info_length){
				infolength_class="top-browser-md ";
			}
			var showmodal = $('#window_notice_div');
			//展示处理
			showmodal.addClass(infoclass);
			showmodal.addClass(infolength_class);
			//添加内容
			$('#top_show_info').html(message);
			
			//进行展示 采用toggle来做
			showmodal.show();
			
			//设置状态
			notice_div_state_info.setShowState(true, infoclass,infolength_class);
			
			// 定时关闭
			process_notice_div_handle();
		}
	})();
	
	//初始化操作
	init();
});