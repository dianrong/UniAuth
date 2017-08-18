$(function() {
	//初始化函数
	var init = function(){
		$('#login_btn_id').click(loginProcess);
	}
	
	// 登陆处理逻辑
	var loginProcess = function(){
		var account = document.getElementById('account_input_id').value;
		var password = document.getElementById('password_input_id').value;
		var tenancyCode = document.getElementById('tenancy_code_input_id').value;
		var casUrl = document.getElementById('casurl_input_id').value;
		// 登陆
		var login =  function(account, pwd, tenancyCode){
			// Ajax login
		    var loginUrl  = casUrl + '/api/v2/login';
		    var data = {
		    	identity: account,
		    	password: pwd,
		    	tenancyCode:tenancyCode
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
		            	if (data.result === 'success') {
		            		// 跳主页
		            		window.location.href="content";
		            	} else {
		            		alert(data.errors[0]);
		            	}
		            },
		            error: function(jqXHR, textStatus, errorMsg){
		            	alert(errorMsg);
		            },
		    });  
		}
		
		// login
		login(account, password, tenancyCode);
	};
	//初始化操作
	init();
});