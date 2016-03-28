(function() {
	var context_path = $('#hidden_path_input').val();
	var processUrl = context_path+"/uniauth/initPassword";
	var captchaUrl = context_path+"/uniauth/captcha";
	
	//初始化函数
	var init = function() {
		//post提交
		$('#btn_init_pwd_process').bind('click', init_pwd_process);
		
		// 刷新验证码
		$('#init_pwd_verfypic').click(refresh_verfypic);
		$('#init_pwd_refreshverfypic').click(refresh_verfypic);
		
		//确定按钮的显示控制
		$('#origin_password').keyup(btn_init_pwd_process_show);
		$('#new_password').keyup(btn_init_pwd_process_show);
		$('#re_new_password').keyup(btn_init_pwd_process_show);
		$('#init_pwd_tverfynotice').keyup(btn_init_pwd_process_show);
		
		//信息提示
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
	
	// refresh verifycode
	var refresh_verfypic = function() {
		$('#init_pwd_verfypic').attr('src', captchaUrl + '?rnd=' + Math.random());
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
		var originpwd = $('#origin_password').val();
		//验证原始密码
		if(!originpwd){
			//查看是否需要显示
			if(!!notice && !!index && index == 1){
				setWarnLabel('原始密码不符合要求');
			}
			return false;
		}
		
		//验证新密码
		var newpwd= $('#new_password').val();
		if(!newpwd || newpwd.length < 8 || newpwd.length > 20){
			if(!!notice && !!index && index == 2){
				setWarnLabel('新密码长度在8-20位之间');
			}
			return false;
		}
		
		var renewpwd= $('#re_new_password').val();
		if(!renewpwd || renewpwd.length < 8 || renewpwd.length > 20 || newpwd!= renewpwd){
			if(!!notice && !!index && index == 3){
				setWarnLabel('两次输入密码不一致');
			}
			return false;
		}
		
		var verifycode= $('#init_pwd_tverfynotice').val();
		if(!verifycode){
			if(!!notice && !!index && index == 4){
				setWarnLabel('请输入验证码');
			}
			return false;
		}
		return true;
	}
	
	// show info 
	var setWarnLabel = function(newHtml){
		var obj = $('#init_pwd_warn_info');
		if(!newHtml || newHtml == ''){
			obj.css('dispaly', 'none');
		} else {
			obj.css('display', 'block');
		}
		obj.html(newHtml);
	}
	
	//点击进行初始化密码操作
	var init_pwd_process = function(){
		var turl = processUrl;
		//数据
		var data = $('#initpwd_post_form').serialize() ;
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
                    } else {
                    	setWarnLabel(result.msg);
                    }
                } else {  
                    alert('异常');
                }  
            },
            error: function(jqXHR, textStatus, errorMsg){
            	alert(errorMsg);
            },
            complete: function(XMLHttpRequest, textStatus) {
            	refresh_verfypic();
            }
        });  
	};
	
	//执行init操作
	init();
})();