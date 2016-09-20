define(function() {
	// name id
	var div_id = 'uniauth_custom_login_div';
	var iframe_name = 'uniauth_custom_login_iframe_name';
	var iframe_id = 'uniauth_custom_login_iframe_id';
	var form_name = 'uniauth_custom_login_form_name';
	var form_id = 'uniauth_custom_login_form_id';
	
	// form表单的相关id
	var form_account_id='uniauth_custom_login_form_account_id';
	var form_password_id='uniauth_custom_login_form_password_id';
	var form_service_id='uniauth_custom_login_form_service_id';
	var form_lt_id='uniauth_custom_login_form_lt_id';
	var form_captcha_id='uniauth_custom_login_form_lt_id';
	
	return {
		// 登陆请求处理
		login: function(account, pwd, apendInfo){
			var service = !!apendInfo?apendInfo.service : undefined;
			var lt =!!apendInfo?apendInfo.lt : undefined;
			var casUrl = !!apendInfo?apendInfo.casUrl : undefined;
			var customUrl = !!apendInfo?apendInfo.customUrl : undefined;
			var failedCallBack = !!apendInfo?apendInfo.failedCallBack : undefined;
			var captcha =  !apendInfo?'': !!apendInfo.captcha?apendInfo.captcha:'' ;
			
			var getQueryParam = function (param) {
				if(!param) {return null};
			     var reg = new RegExp("(^|&)"+ param +"=([^&]*)(&|$)");
			     var r = window.location.search.substr(1).match(reg);
			     if(r!=null)return  unescape(r[2]); return null;
			};
			
			var getBaseUrl = function () {
				return window.location.href.substring(0,window.location.href.indexOf('?') === -1 ? window.location.href.length :window.location.href.indexOf('?'));
			};
			
			var generateUnknownError = function(msgStr){
				return {
					result : 'failed',
					content : '501',
					msg : msgStr || 'unknowError',
					lt : getQueryString('lt') ||''
				};
			};
			
			if(!account || !pwd || !service || !lt  || !casUrl || !customUrl) {
				if(failedCallBack) {
					failedCallBack(generateUnknownError('parameter invalid'));
				} else {
					console.error('login cas : parameters invalid');
				}
				return;
			}
			
			// dom 操作相关的api
			var domOperate = (function(account, pwd, service, lt, casUrl){
				var iframeStatus = 0;
				// 创建隐藏的div
				var assignValuesWithNoIframe = function(){
					// 创建隐藏的div
					var hidden_div = document.createElement('div');
					hidden_div.id = div_id;
					hidden_div.style.display = 'none';
					document.body.appendChild(hidden_div);
					
					// 创建iframe
					var iframe = document.createElement('iframe');
					iframe.name = iframe_name;
					iframe.id = iframe_id;
					hidden_div.appendChild(iframe);
					
					//创建form表单
					var submit_form = document.createElement('form');
					submit_form.method = 'post';
					submit_form.action = casUrl + '/uniauth/serviceticket/customlogin';
					submit_form.name = form_name;
					submit_form.id = form_id;
					submit_form.target = iframe_name;
					hidden_div.appendChild(submit_form);
					
					//创建表单
					var elementAccount = document.createElement('input');
					elementAccount.setAttribute("name", "identity");
					elementAccount.setAttribute("type", "text");
					elementAccount.setAttribute("value", account);
					elementAccount.id=form_account_id;
					var elementPassword = document.createElement('input');
					elementPassword.setAttribute("name", "password");
					elementPassword.setAttribute("type", "password");
					elementPassword.setAttribute("value", pwd);
					elementPassword.id=form_password_id;
					var elementService = document.createElement('input');
					elementService.setAttribute("name", "service");
					elementService.setAttribute("type", "text");
					elementService.setAttribute("value", service);
					elementService.id=form_service_id;
					var elementlt = document.createElement('input');
					elementlt.setAttribute("name", "lt");
					elementlt.setAttribute("type", "text");
					elementlt.setAttribute("value", lt);
					elementlt.id=form_lt_id;
					var elementCaptcha = document.createElement('input');
					elementCaptcha.setAttribute("name", "captcha");
					elementCaptcha.setAttribute("type", "text");
					elementCaptcha.setAttribute("value", captcha);
					elementCaptcha.id=form_captcha_id;
					//添加元素
					submit_form.appendChild(elementAccount);
					submit_form.appendChild(elementPassword);
					submit_form.appendChild(elementService);
					submit_form.appendChild(elementlt);
					submit_form.appendChild(elementCaptcha);
				}
				// parameter assign
				var assignValues =  function(){
					// 重设值
					document.getElementById(form_account_id).value=account;
					document.getElementById(form_password_id).value=pwd;
					document.getElementById(form_service_id).value=service;
					document.getElementById(form_lt_id).value=lt;
					document.getElementById(form_captcha_id).value=captcha;
					//重新新建iframe
					var iframe_ele = document.getElementById(iframe_id);
					if(iframe_ele) {
						iframe_ele.parentNode.removeChild(iframe_ele);
					}
					// 创建iframe
					var iframe = document.createElement('iframe');
					iframe.name = iframe_name;
					iframe.id = iframe_id;
					document.getElementById(div_id).appendChild(iframe);
				}
				
				// 暴露接口
				var domOperateObj = {
					// init login
					initLogin : function() {
						if(document.getElementById(div_id)) {
							assignValues();
						} else {
							assignValuesWithNoIframe();
						}
						// 初始化 不可用
						iframeStatus = 0;
					},
					formSubmit : function(){
						//提交表单
						document.getElementById(form_id).submit();
					},
					bindOnloadEventToIframe : function(callBackFun){
						// iframe 绑定onload事件
						document.getElementById(iframe_id).onload = function(){
							var frameWindow = document.getElementById(iframe_id).contentWindow;
							if( iframeStatus === 1) {
								callBackFun(JSON.parse(frameWindow.name));
							} else {
								iframeStatus  = 1;
								frameWindow.location = window.location.href;
							}
						};
					}
				};
				return domOperateObj;
			})(account, pwd, service, lt, casUrl);
			
			// init dom
			domOperate.initLogin();
			// 业务逻辑处理相关
			domOperate.bindOnloadEventToIframe(function(data){
				if(!data || !data.result) {
					if(failedCallBack){
						failedCallBack(generateUnknownError);
					} else {
						console.error('check whether casUrl is right .no response!');
					}
					return;
				}
				// 有结果
				if(data.result == 'success') {
					var redirectUrl = customUrl +(customUrl.indexOf('?') === -1 ?"?":"&")+ "ticket="+data.content;
					// 成功 直接跳转业务系统
					window.location.href = redirectUrl;
				} else {
					if(failedCallBack) {
						failedCallBack(data);
					} else {
						// 失败  继续登陆
						window.location.href = getBaseUrl() + "?lt="+data.lt+"&result="+data.result+"&content="+data.content+(!!data.msg ? "&msg="+data.msg : '')+(!!data.captchapath ? "&captchaUrl="+data.captchapath : '');
					}
				}
			});
			//提交form表单
			domOperate.formSubmit();
		},
		//获取serviceTicket
		getSt: function(){
		}
	};
});