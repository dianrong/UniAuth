(function() {
	var context_path = $('#hidden_path_input').val();
	var captchaUrl = context_path+"/uniauth/captcha";
	
	//初始化函数
	var init = function() {
		//添加验证码替换功能
		$('#cas_login_captcha_change_img').click(refresh_verfypic);
		$('#cas_login_captcha_change_a').click(refresh_verfypic);
	}
	
	// refresh verifycode
	var refresh_verfypic = function() {
		$('#cas_login_captcha_change_img').attr('src', context_path + '/uniauth/captcha?rnd=' + Math.random());
	}
	
	//执行init操作
	init();
})();