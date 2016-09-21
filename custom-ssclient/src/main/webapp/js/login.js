$(function() {
	//初始化函数
	var init = function(){
		$('#login_btn_id').click(loginProcess);
	}
	
	// 登陆处理逻辑
	var loginProcess = function(){
		var account = document.getElementById('account_input_id').value;
		var password = document.getElementById('password_input_id').value;
		var service = document.getElementById('service_input_id').value;
		var lt = document.getElementById('lt_input_id').value;
		var casUrl = document.getElementById('casurl_input_id').value;
		var customUrl = document.getElementById('customurl_input_id').value;
		var appendInfo = {
				service : service,
				lt : lt,
				casUrl : casUrl,
				customUrl : customUrl
		};
		
		// 登陆
		var login =  function(account, pwd, apendInfo){
			var service = !!apendInfo?apendInfo.service : undefined;
			var lt =!!apendInfo?apendInfo.lt : undefined;
			var casUrl = !!apendInfo?apendInfo.casUrl : undefined;
			var customUrl = !!apendInfo?apendInfo.customUrl : undefined;
			var captcha =  !apendInfo?'': !!apendInfo.captcha?apendInfo.captcha:'' ;
			
			if(!account || !pwd || !service || !lt  || !casUrl || !customUrl) {
				console.error('login cas : parameters invalid');
				return;
			}
			
			var getBaseUrl = function (url) {
				if(!url) {
					url = window.location.href;
				}
				return url.substring(0,url.indexOf('?') === -1 ? url.length :url.indexOf('?'));
			};
			
			// 业务逻辑处理相关
			var callback = function(data){
				if(!data || !data.result) {
					console.error('check whether casUrl is right. server no response!');
					return;
				}
				// 有结果
				if(data.result == 'success') {
					//  /login/cas保持与spring配置中的casAuthenticationFilter的filterProcessesUrl一致
					var redirectUrl = getBaseUrl(customUrl)+"/login/cas?ticket="+data.content;
					// 成功 直接跳转业务系统
					window.location.href = redirectUrl;
				} else {
						// 失败  继续登陆
						window.location.href = getBaseUrl() + "?lt="+data.lt+"&result="+data.result+"&content="+data.content+(!!data.msg ? "&msg="+data.msg : '')+(!!data.captchapath ? "&captchaUrl="+data.captchapath : '');
					}
			}
			
			// ajax login
		    var loginUrl  = casUrl + '/uniauth/serviceticket/api/login';
		    var data = {
		    	'identity' : account,
		    	'password': pwd,
		    	'service':service,
		    	'lt': lt,
		    	'captcha':captcha
		    };
			$.ajax({ 
		            type : "POST", 
		            url : loginUrl,
		            data: data,
		            dataType: "json",
		            xhrFields: {
	                      withCredentials: true
	                },
		            success : function(data) {
		            	callback(data);
		            },
		            error: function(jqXHR, textStatus, errorMsg){
		            	alert(errorMsg);
		            },
		    });  
		}
		
		// login
		login(account, password, appendInfo);
	};
	//初始化操作
	init();
});