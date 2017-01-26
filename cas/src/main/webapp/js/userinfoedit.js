$(function() {
	var processUrl = context_path+"/login";
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
		// 到第二步
		$('#update_email_btn_step1').click(update_email_to_step2);
		
		// 确认修改邮箱
		
		// 去改电话
		
		// 获取改电话验证码
		
		// 确认修改电话
		
		// 去修改密码
		$('#go_update_password_btn').click(function(){
			$('#modal-new-password').modal('show');
		});
		
		//显示编辑和取消编辑状态
		$('#go_user_info_edit_btn').click( function(){
			window.change_edit_btn_state(true);
		});
		$('#user_info_edit_cancel_btn').click( function(){
			window.change_edit_btn_state(false);
		});
		
		//绑定处理信息编辑确定的按钮事件
		$('#modal_confirm_ok_btn').click(btn_userinfo_ok_process);
		
		//给与电话输入框进行输入事件处理
		$('#inputPhone').keyup(phone_input_keyup_event);
		
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
	
	// 修改密码
	var update_user_name_process = function() {
		var data = {};
		window.infonotice('1', 'process ok');
	}
	
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
	
	// ready show update email modal
	var to_update_email = function(){
		switch_step1_step2(false);
		$('#update_email_captcha').val('');
		
		$('#modal-new-email').modal('show');
	}
	
	// get email captcha
	var update_email_get_captcha = function(){}
	
	// check captcha and to step2
	var update_email_to_step2 = function () {
		// check captcha
		
		// to step2
		switch_step1_step2(true);
	}
	
	// helper function for switch update_email steps
	var switch_step1_step2 = function(toStep2) {
		if (toStep2) {
			$('.update-email-step1').addClass('hidden-element');
			$('.update-email-step2').removeClass('hidden-element');
		} else {
			$('.update-email-step2').addClass('hidden-element');
			$('.update-email-step1').removeClass('hidden-element');
		}
	}
	
	// support
	//修改编辑信息相关按钮的状态
	window.change_edit_btn_state = function(show_edit){
		if(show_edit){
			//显示编辑框
			$('.common-wizard .form-group input[type="text"]').removeAttr("disabled"); 
			//显示确定和取消按钮
			$('.common-wizard .form-group .myedit').removeClass('hiddenbtn');
			$('.common-wizard .form-group .mynoedit').addClass('hiddenbtn');
		} else {
			//disable 编辑框
			$('.common-wizard .form-group input[type="text"]').attr('disabled', 'disabled');
			//显示编辑按钮
			$('.common-wizard .form-group .myedit').addClass('hiddenbtn');
			$('.common-wizard .form-group .mynoedit').removeClass('hiddenbtn');
		}
	}
	
	//处理电话输入框只能输入数字的事件
	var phone_input_keyup_event = function() {
		var obj = $(this);
		//如果输入非数字，则替换为''，如果输入数字，则在每4位之后添加一个空格分隔
		obj.val(obj.val().replace(/[^\d]/g, ''));
	}
	
	//处理编辑信息确认的按钮
	var btn_userinfo_ok_process = function(){
		var posturl = processUrl;
		
		var data = {
				//分之管理数据
				'edituserinfo' : 'go',
				'updatetype' : 'userinfo',
				//用户数据
				'id' : $('#hidden_userinfo_keyid').val(),
				'email' : $('#inputEmail').html(),
				'phone' : $('#inputPhone').val(),
				'name' : $('#inputName').val()
		};
		
		$.ajax({  
            type : "POST", 
            url : processUrl,
            data : data,
            dataType : 'json',
            success : function(data) {
            	if (data.code == '1') { 
                	window.infonotice('1', $.i18n.prop('frontpage.userinfo.edit.update.ok'));
                } else {
                	window.infonotice('2', data.msg);
                }
                
                //添加关闭事件
                window.process_notice_div_handle();
            },
            complete : function(){
            	//关闭模态框
            	$('#modal-confirm-edit').modal('hide');
            	
            	//将编辑框变一下
            	window.change_edit_btn_state(false);
            },
            error: function(jqXHR, textStatus, errorMsg){
            	logOperation.error(errorMsg);
            },
        });  
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
		var posturl = processUrl;
		
		var data = {
				//分支  更新密码
				'edituserinfo' : 'go',
				'updatetype' : 'password',
				//用户数据
				'id' : $('#hidden_userinfo_keyid').val(),
				'password' : $('#modal-new-password #password').val(),
				'orign_password' : $('#modal-new-password #orign_password').val(),
		};
		
		$.ajax({  
            type : "POST", 
            url : processUrl,
            data : data,
            dataType : 'json',
            success : function(data) {
                if (data.code == '1') { 
                	window.infonotice('1',  $.i18n.prop('frontpage.userinfo.edit.update.pwdok'));
                } else {
                	window.infonotice('2', data.msg);
                }
                
                //添加关闭事件
                window.process_notice_div_handle();
            },
            complete : function(){
            	//关闭模态框
            	$('#modal-new-password').modal('hide');
            }
            ,
            error: function(jqXHR, textStatus, errorMsg){
            	logOperation.error(errorMsg);
            },
        });  
	}
	
	//用于控制展示提示框的展示  闭包
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
		notice_show_milles : 2000,
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
	};
	
	//进行setTimeout的显示处理  所有的setTimeout都走这里
	window.process_notice_div_handle = function(){
		var currentTimeout = notice_div_state_info.notice_settimeout_handle;
		//有一个事件  先取消掉
		if(currentTimeout){
			clearTimeout(currentTimeout);
		}
		
		//重新设置显示事件
		var newTimeoutHandle = setTimeout("window.infonotice_close()", notice_div_state_info.notice_show_milles);
		//重新赋值
		notice_div_state_info.notice_settimeout_handle = newTimeoutHandle;
	}
	
	//帮window实现一个自动提示的功能
	window.infonotice = function(infolevel,message){
		//先关闭
		window.infonotice_close();
		
		var infoclass = "alert-success";
		//异常级别
		if(!!infolevel && infolevel == "2"){
			infoclass = "alert-danger";
		}
		
		var infolength_class="top-browser-bg ";
		//信息长度定义
		if(!!message && message.length > notice_div_state_info.bg_info_length){
			infolength_class="top-browser-md ";
		}
		
		var showmodal = $('#window_notice_div');
		//展示处理
		//修改class
		showmodal.addClass(infoclass);
		showmodal.addClass(infolength_class);
		//添加内容
		$('#top_show_info').html(message);
		
		//进行展示 采用toggle来做
		showmodal.show();
		
		//设置状态
		notice_div_state_info.setShowState(true, infoclass,infolength_class);
	}
	
	window.infonotice_close = function(){
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
	//初始化操作
	init();
});