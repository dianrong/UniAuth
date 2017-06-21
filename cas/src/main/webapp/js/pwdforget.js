$(function() {
	var processUrl = context_path+"/uniauth/forgetPassword";
	var captchaUrl = context_path+"/uniauth/verification";
	
	//初始化函数
	var init = function() {
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
	
	function isEmailOrPhoneNumber(number){
	    var filter  = /(^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$)|(^((13[0-9])|(15[^4])|(18[0-9])|(17[0-8])|(147))\d{8}$)/;
	    return filter.test(number);
	}
	// step1 btn show
	var process_step1_btn = function(){
		var mailval = $('#temail').val();
		var input_verifycode = $('#tverfynotice').val();
		// filter email 
		var stepbtn = $('#btn_step1');
		if(input_verifycode && mailval && isEmailOrPhoneNumber(mailval)) {
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
	
	var sendEmailVerifyCode = function(){
		$.ajax({  
            type : "GET", 
            url : captchaUrl + '/send/session',
            dataType : 'json',
            success : function(data) {
            	if(data.info) {
         		   if('IDENTITY_REQUIRED' === data.info[0].name) {
         			   // 重新跳转到首页登录
         			  window.location = processUrl;
         		   } else {
         			  setWarnLabel($('#emailverfywarn'), data.info[0].msg);
         		   }
         	   } else {
         		 	var count_btn = $('.find-pwd-container .steps #input2');
         		 	// show verify code
         		 	if (data.data) {
         		 		$('#verify_code_div').html(data.data);
         		 	}
                	countBtn(count_btn, function(new_label){
                		count_btn.html(new_label)
                	},function(){
                		return count_btn.html();
                	}, 120);
         	   }
            },
            error: function(jqXHR, textStatus, errorMsg){
            	logOperation.error(errorMsg);
            }
        });  
	};

	var processStep1 = function(){
		var temalval = $('#temail').val();
		var tverifycode =$('#tverfynotice').val();
		if(!temalval || !tverifycode){
			return;
		}
		
		// filter email
		if (!isEmailOrPhoneNumber(temalval)) {
			return;
		}
		
		var turl = $('#step1Post').attr('action');
		var data = {
				'email' : temalval,
				'pageVerifyCode' : tverifycode,
				'step' : '1',
				'tenancyCode': cookieOperation.getTenancyCode(),
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
                	logOperation.error($.i18n.prop('frontpage.common.error.msg'));
                }  
            },
            error: function(jqXHR, textStatus, errorMsg){
            	logOperation.error(errorMsg);
            },
            complete: function(XMLHttpRequest, textStatus) {
            	refresh_verfypic($('#verfypic'));
            }
        });  
	};
	
	var processStep2 = function(){
		var verifyCode = $('.find-pwd-container .steps #input1').val();
		if (!verifyCode) {
			return;
		}
		$.ajax({  
            type : "POST", 
            url : captchaUrl + '/verify/session',
            data : {verifyCode : verifyCode},
            dataType : 'json',
            beforeSend : function () {
            	//清空提示信息
            	setWarnLabel($('#emailverfywarn'), '');
            },
            success : function(data) {
            	if(data.info) {
          		   if('IDENTITY_REQUIRED' === data.info[0].name) {
          			   // 重新跳转到首页登录
          			  window.location = processUrl;
          		   } else {
          			  setWarnLabel($('#emailverfywarn'), data.info[0].msg);
          		   }
          	   } else {
	          		// 跳转到第三步
	               	window.location = processUrl + '?step=3';
          	   }
            },
            error: function(jqXHR, textStatus, errorMsg){
            	logOperation.error(errorMsg);
            }
        });  
	};
	
	// 提交新密码
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
                	logOperation.error($.i18n.prop('frontpage.common.error.msg'));
                }  
            },
            error: function(jqXHR, textStatus, errorMsg){
            	logOperation.error(errorMsg);
            }
        });  
	};
	
	//执行init操作
	init();
});