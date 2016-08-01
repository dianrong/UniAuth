$(function() {
	var processUrl = context_path+"/uniauth/forgetPassword";
	var captchaUrl = context_path+"/uniauth/captcha";
	
	//初始化函数
	var init = function() {
		//添加跳转页面事件
		$('#to_reset_pwd_btn').bind('click', jump_to_step1_page);
		
		// 刷新验证码
		$('#verfypic').click(function(){
			refresh_verfypic($('#verfypic'));
		});
		$('#refreshverfypic').click(function(){
			refresh_verfypic($('#verfypic'));
		});
		
		//显示step1的按钮
		$('#temail').keyup(process_step1_btn);
		$('#tverfynotice').keyup(process_step1_btn);
		
		// 发邮箱验证码
		$('.find-pwd-container .steps #input1').keyup(process_step2_btn);
		$('.find-pwd-container .steps #input2').click(sendEmailVerifyCode);
		
		// 最后一步修改密码
		$('.find-pwd-container .steps #newpwd').keyup(process_step3_btn);
		$('.find-pwd-container .steps #rnewpwd').keyup(process_step3_btn);
		//进一步校验
		$('.find-pwd-container .steps #newpwd').blur(step3pwdvalidate);
		$('.find-pwd-container .steps #rnewpwd').blur(step3rpwdvalidate);
		
		// 第一步的请求
		$('#btn_step1').click(processStep1);
		
		// 第二步的请求
		$('#btn_step2').click(processStep2);
		
		// 第三步的请求
		$('#btn_step3').click(processStep3);
	}
	
	//jump to reset pwd
	var jump_to_step1_page = function() {
		//通过隐藏表单设置背景url
		$('#hidden_savedLoginContext').val(getLocationParameterStr());
		
		var hidden_to_step1_form = $('#hidden_post_form_for_loginurl');
		//设置action
		var action_url = processUrl +"?step=0";
		hidden_to_step1_form.attr("action", action_url);
		hidden_to_step1_form.submit();
		//防止多点
		$(this).unbind('click');
	}
	
	// step1 btn show
	var process_step1_btn = function(){
		var mailval = $('#temail').val();
		var input_verifycode = $('#tverfynotice').val();
		// filter email
		var filter  = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
		var stepbtn = $('#btn_step1');
		if(input_verifycode && mailval && filter.test(mailval)) {
			stepbtn.removeAttr("disabled","disabled");
			stepbtn.removeClass('cursordefault');
		} else {
			stepbtn.attr("disabled","disabled"); 
			stepbtn.addClass('cursordefault');
		}
	}
	
	// step2 btn show
	var process_step2_btn = function(){
		var emailcode = $('.find-pwd-container .steps #input1').val();
		var stepbtn = $('#btn_step2');
		if(emailcode) {
			stepbtn.removeAttr("disabled","disabled");
			stepbtn.removeClass('cursordefault');
		} else {
			stepbtn.attr("disabled","disabled"); 
			stepbtn.addClass('cursordefault');
		}
	}
	
	// step3 btn show
	var process_step3_btn = function(){
		var stepbtn = $('#btn_step3');
		if(step3pwdvalidate(true) && step3rpwdvalidate(true)){
			stepbtn.removeAttr("disabled","disabled");
			stepbtn.removeClass('cursordefault');
		} else {
			stepbtn.attr("disabled","disabled"); 
			stepbtn.addClass('cursordefault');
		}
	}
	
	// pwd validate
	var step3pwdvalidate = function(unshow){
		var show = false;
		if(unshow === true || unshow == 'true'){
			//清除提示
			setWarnLabel($('.find-pwd-container .steps #newpwdwarn'), '');
		} else {
			show = true;
		}
		var pwd = $('.find-pwd-container .steps #newpwd').val();
		if(pwd && pwd.length >= 8 && pwd.length < 20){
			return true;
		} else {
			if(show){
				setWarnLabel($('.find-pwd-container .steps #newpwdwarn'), $.i18n.prop('frontpage.pwdforget.edit.need.pwdlength'));
			}
			return false;
		}
	}
	
	// rpwd validate
	var step3rpwdvalidate = function(unshow){
		var show = false;
		if(unshow === true || unshow == 'true'){
			setWarnLabel($('.find-pwd-container .steps #newpwdwarn'), '');
		} else {
			show = true;
		}
		var rpwd = $('.find-pwd-container .steps #rnewpwd').val();
		if(rpwd && rpwd.length >= 8 && rpwd.length < 20){
			var pwd = $('.find-pwd-container .steps #newpwd').val();
			if(rpwd == pwd) {
				return true;
			}
		} 
		if(show){
			setWarnLabel($('.find-pwd-container .steps #newpwdwarn'), $.i18n.prop('frontpage.pwdforget.edit.need.pwdequal'));
		}
		return false;
	}
	
	// assiat function
	var setWarnLabel = function(obj ,newHtml){
		if(!obj){
			return;
		}
		if(!newHtml || newHtml == ''){
			obj.css('dispaly', 'none');
		} else {
			obj.css('display', 'block');
		}
		obj.html(newHtml);
	}
	
	// change verify status  需要注册到外部  让外部的调用
	window.changeVerifyStats = function(){
		var send_email_btn = $('.find-pwd-container .steps #input2');
		var btn_showval = send_email_btn.attr('showval');
		if(!btn_showval || isNaN(btn_showval)){
			//init 120s
			refresh_send_email_btn_val(send_email_btn, '120');
			//disable 按钮
			send_email_btn.attr("disabled","disabled"); 
			send_email_btn.addClass('cursordefault').addClass('balckfont');
		} else {
			if(btn_showval < 1){
				send_email_btn.removeAttr('showval');
				send_email_btn.html(send_email_btn.attr('backupstr'));
				
				//显示按钮
				send_email_btn.removeAttr("disabled", "disabled");
				send_email_btn.removeClass('cursordefault').removeClass('balckfont');
				return;
			}
			var newbtn_showval = btn_showval -1;
			refresh_send_email_btn_val(send_email_btn, '' + newbtn_showval);
			
		}
		setTimeout("changeVerifyStats()", 1000);
	}
	
	var refresh_send_email_btn_val = function(send_email_btn, showval){
		send_email_btn.attr('showval', showval);
		send_email_btn.html(showval);
	}
	
	var sendEmailVerifyCode = function(){
		$.ajax({  
            type : "POST", 
            url : captchaUrl,
            data : {'captchaType': '1'},
            dataType : 'json',
            success : function(result) {
                if (result.issuccess == 'true') {  
                    if(result.code == '0'){
                    	// 设置60秒之后 可以继续发送
                    	setTimeout("changeVerifyStats()", 1000);
                    	return;
                    }
                    // 重新跳转到首页登录
                    if(result.code == '1') {
                    	window.location = processUrl;
                    	return;
                    }
                    
                    if(result.code == '2') {
                    	setWarnLabel($('#emailverfywarn'), result.msg);
                    	return;
                    }
                } else {  
                    alert($.i18n.prop('frontpage.common.error.msg'));
                }  
            },
            error: function(jqXHR, textStatus, errorMsg){
            	alert(errorMsg);
            },
        });  
	};

	var processStep1 = function(){
		var temalval = $('#temail').val();
		var tverifycode =$('#tverfynotice').val();
		if(!temalval || !tverifycode){
			return;
		}
		
		// filter email
		var filter  = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
		if (!filter.test(temalval)) {
			return;
		}
		
		var turl = $('#step1Post').attr('action');
		var data = {
				'email' : temalval,
				'pageVerifyCode' : tverifycode,
				'step' : '1',
		};
		$.ajax({  
            type : "POST", 
            url : turl,
            data : data,
            dataType : 'json',
            beforeSend : function () {
            	//清空提示信息
            	setWarnLabel($('#temailwarn'), '');
            },
            success : function(result) {// 返回数据根据结果进行相应的处理
                if (result.issuccess == 'true') {  
                    if(result.code == '0'){
                    	// 跳转到第二步
                    	window.location = processUrl + '?step=2';
                    	return;
                    }
                    if(result.code == '1') {
                    	setWarnLabel($('#temailwarn'), $.i18n.prop('frontpage.pwdforget.edit.need.captcha'));
                    	return;
                    }
                    
                    if(result.code == '2') {
                    	setWarnLabel($('#temailwarn'), $.i18n.prop('frontpage.pwdforget.edit.wrong.captcha'));
                    	return;
                    }
                    
                    if(result.code == '3') {
                    	setWarnLabel($('#temailwarn'), $.i18n.prop('frontpage.pwdforget.edit.need.email'));
                    	return;
                    }
                    
                    if(result.code == '4') {
                    	setWarnLabel($('#temailwarn'), result.msg);
                    	return;
                    }
                } else {  
                	 alert($.i18n.prop('frontpage.common.error.msg'));
                }  
            },
            error: function(jqXHR, textStatus, errorMsg){
            	alert(errorMsg);
            },
            complete: function(XMLHttpRequest, textStatus) {
            	refresh_verfypic($('#verfypic'));
            }
        });  
	};
	
	var processStep2 = function(){
		var verifyCode = $('.find-pwd-container .steps #input1').val();
		if(!verifyCode){
			return;
		}
		
		var turl = $('#step2Post').attr('action');
		var data = {
				'verifyCode' : verifyCode,
				'step' : '2'
		};
		$.ajax({  
            type : "POST", 
            url : turl,
            data : data,
            dataType : 'json',
            beforeSend : function () {
            	//清空提示信息
            	setWarnLabel($('#emailverfywarn'), '');
            },
            success : function(result) {// 返回数据根据结果进行相应的处理
                if (result.issuccess == 'true') {  
                    if(result.code == '0'){
                    	// 跳转到第三步
                    	window.location = processUrl + '?step=3';
                    	return;
                    }
                    // 重新跳转到首页登录
                    if(result.code == '1') {
                    	// 跳转到第一步
                    	window.location = processUrl + '?step=1';
                    	return;
                    }
                    
                    if(result.code == '2') {
                    	setWarnLabel($('#emailverfywarn'), $.i18n.prop('frontpage.pwdforget.edit.need.captcha'));
                    	return;
                    }
                    
                    if(result.code == '3') {
                    	setWarnLabel($('#emailverfywarn'), $.i18n.prop('frontpage.pwdforget.edit.wrong.captcha'));
                    	return;
                    }
                } else {  
                	alert($.i18n.prop('frontpage.common.error.msg'));
                }  
            },
            error: function(jqXHR, textStatus, errorMsg){
            	alert(errorMsg);
            }
        });  
	};
	
	var processStep3 = function(){
		var newpwd = $('#newpwd').val();
		var rnewpwd = $('#rnewpwd').val();
		if(!newpwd || !rnewpwd){
			return;
		}
		
		var turl = $('#step3Post').attr('action');
		var data = {
				'newPassword' : newpwd,
				'step' : '3',
		};
		$.ajax({  
            type : "POST", 
            url : turl,
            data : data,
            dataType : 'json',
            beforeSend : function () {
            	//清空提示信息
            	setWarnLabel($('#newpwdwarn'), '');
            },
            success : function(result) {// 返回数据根据结果进行相应的处理
                if (result.issuccess == 'true') {  
                    if(result.code == '0'){
                    	// 跳转到第四步
                    	window.location = processUrl + '?step=4';
                    	return;
                    }
                    // 重新跳转到首页登录
                    if(result.code == '1') {
                    	// 跳转到第一步
                    	window.location = processUrl + '?step=1';
                    	return;
                    }
                    
                    if(result.code == '2') {
                    	setWarnLabel($('#newpwdwarn'), $.i18n.prop('frontpage.pwdforget.edit.need.newpwd'));
                    	return;
                    }
                    
                    if(result.code == '3') {
                    	setWarnLabel($('#newpwdwarn'), result.msg);
                    	return;
                    }
                    
                    if(result.code == '4') {
                    	// 跳转到第一步
                    	window.location = processUrl + '?step=2';
                    	return;
                    }
                } else {  
                	alert($.i18n.prop('frontpage.common.error.msg'));
                }  
            },
            error: function(jqXHR, textStatus, errorMsg){
            	alert(errorMsg);
            }
        });  
	};
	
	//执行init操作
	init();
});