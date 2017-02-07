$(function() {
	var processUrl = context_path+"/uniauth/initPassword/process";
	var successUrl = context_path+"/uniauth/initPassword/initSuccess";
	var captchaUrl = context_path+"/uniauth/verification/captcha";
	
	//初始化函数
	var init = function() {
		//post提交
		$('#btn_init_pwd_process').bind('click', init_pwd_process);
		
		// 刷新验证码
		$('#init_pwd_verfypic').click(function(){
			refresh_verfypic($('#init_pwd_verfypic'));
		});
		$('#init_pwd_refreshverfypic').click(function(){
			refresh_verfypic($('#init_pwd_verfypic'));
		});
		
		//确定按钮的显示控制
		$('#origin_password').keyup(btn_init_pwd_process_show);
		$('#new_password').keyup(btn_init_pwd_process_show);
		$('#re_new_password').keyup(btn_init_pwd_process_show);
		$('#init_pwd_tverfynotice').keyup(btn_init_pwd_process_show);
		
		//信息提示
		$('#username_warn_info').blur(function(){
			valid_input_value(true, 0);
		});
		$('#origin_password').blur(function(){
			valid_input_value(true, 1);
		});
		$('#new_password').blur(function(){
			valid_input_value(true, 2);
		});
		$('#re_new_password').blur(function(){
			valid_input_value(true, 3);
		});
	}
	
	//refresh page
	var refresh_page = function(){
		window.location = window.location;
	}
	
	//控确定按钮的显示
	var btn_init_pwd_process_show = function(){
		var stepbtn = $('#btn_init_pwd_process');
		if(valid_input_value(false)){
			//显示确定按钮
			stepbtn.removeAttr("disabled","disabled");
			stepbtn.removeClass('cursordefault');
		} else {
			stepbtn.attr("disabled","disabled"); 
			stepbtn.addClass('cursordefault');
		}
	}
	
	// 验证输入的数据
	var valid_input_value = function(notice, index){
		var valid_result  = true;
		
		var useremail = $('#user_email').val();
		//验证原始密码
		if(!useremail){
			valid_result = false;
			//查看是否需要显示
			if(!!notice && !!index && index == 0){
				setWarnLabel($.i18n.prop('frontpage.initpwd.edit.need.username'), $('#username_warn_info'));
			}
			if(!!index && index == 0){
				return false;
			}
		}
		
		var originpwd = $('#origin_password').val();
		//验证原始密码
		if(!originpwd){
			valid_result = false;
			//查看是否需要显示
			if(!!notice && !!index && index == 1){
				setWarnLabel($.i18n.prop('frontpage.initpwd.edit.wrong.orginalpwd'), $('#originpwd_warn_info'));
			}
			if(!!index && index == 1){
				return false;
			}
		}
		
		//验证新密码
		var newpwd= $('#new_password').val();
		if(!newpwd || newpwd.length < 8 || newpwd.length > 20){
			valid_result = false;
			
			if(!!notice && !!index && index == 2){
				setWarnLabel($.i18n.prop('frontpage.initpwd.edit.need.pwdlength'), $('#newpwd_warn_info'));
			}
		
			if(!!index && index == 2){
				return false;
			}
		}
		
		var renewpwd= $('#re_new_password').val();
		if(!renewpwd || renewpwd.length < 8 || renewpwd.length > 20 || newpwd!= renewpwd){
			valid_result = false;
			
			if(!!notice && !!index && index == 3){
				setWarnLabel($.i18n.prop('frontpage.initpwd.need.pwdequal') , $('#renewpwd_warn_info'));
			}
			if(!!index && index == 3){
				return false;
			}
		}
		
		var verifycode= $('#init_pwd_tverfynotice').val();
		if(!verifycode){
			valid_result = false;
			
			if(!!notice && !!index && index == 4){
				setWarnLabel($.i18n.prop('frontpage.initpwd.need.captch'), $('#verifycode_warn_info'));
			}
			if(!!index && index == 4){
				return false;
			}
		}
		return valid_result;
	}
	
	// show info 
	var setWarnLabel = function(newHtml, obj){
		if(!obj) {
			return;
		}
		
		//首先清空其他所有的提示
		$('.showwarninfo label').each(function(){
			$(this).html('');
		});
		obj.html(newHtml);
	}
	
	//点击进行初始化密码操作
	var init_pwd_process = function(){
		var turl = processUrl;
		var tenancyCode = $('#input_tenancy_code_val').val();
		if (!tenancyCode) {
			$('#input_tenancy_code_val').val(cookieOperation.getTenancyCode());
		}
		//数据
		var data = $('#initpwd_post_form').serialize() ;
		$.ajax({  
            type : "POST", 
            url : turl,
            data : data,
            dataType : 'json',
            beforeSend : function () {
            	//清空提示信息
            	setWarnLabel('', $('#init_pwd_warn_info'));
            },
            success : function(result) {// 返回数据根据结果进行相应的处理
                if (result.success) {  
                    if(result.code ===  0){
                    	// success
                    	window.location = successUrl;
                    } else {
                    	setWarnLabel(result.msg, $('#init_pwd_warn_info'));
                    }
                } else {  
                	logOperation.error($.i18n.prop('frontpage.common.error.msg'));
                }  
            },
            error: function(jqXHR, textStatus, errorMsg){
            	logOperation.error(errorMsg);
            },
            complete: function(XMLHttpRequest, textStatus) {
            	refresh_verfypic($('#init_pwd_verfypic'));
            }
        });  
	};
	
	//执行init操作
	init();
});