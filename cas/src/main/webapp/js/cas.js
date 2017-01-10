$(function() {
	function areCookiesEnabled() {
		cookieOperation.setCookieEnable('true');
		var value = cookieOperation.getCookieEnable();
		if (value === 'true') {
			cookieOperation.setCookieEnable('');
			return true;
		}
		return false;
	}

	function getUrlParam(name) {
		var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
		var r = window.location.search.substr(1).match(reg);
		if (r != null)
			return r[2];
		return null;
	}

	// init
	if ($(":focus").length === 0) {
		$("input:visible:enabled:first").focus();
	}
	if (areCookiesEnabled()) {
		$('#cookiesDisabled').hide();
	} else {
		$('#cookiesDisabled').show();
		$('#cookiesDisabled').animate({
			backgroundColor : 'rgb(187,0,0)'
		}, 30).animate({
			backgroundColor : 'rgb(255,238,221)'
		}, 500);
	}
	// flash error box
	$('#msg.errors').animate({
		backgroundColor : 'rgb(187,0,0)'
	}, 30).animate({
		backgroundColor : 'rgb(255,238,221)'
	}, 500);
	// flash success box
	$('#msg.success').animate({
		backgroundColor : 'rgb(51,204,0)'
	}, 30).animate({
		backgroundColor : 'rgb(221,255,170)'
	}, 500);

	// flash confirm box
	$('#msg.question').animate({
		backgroundColor : 'rgb(51,204,0)'
	}, 30).animate({
		backgroundColor : 'rgb(221,255,170)'
	}, 500);

	$('#capslock-on').hide();
	$('#password').keypress(function(e) {
		var s = String.fromCharCode(e.which);
		if (s.toUpperCase() === s && s.toLowerCase() !== s && !e.shiftKey) {
			$('#capslock-on').show();
		} else {
			$('#capslock-on').hide();
		}
	});

	if(!($('#login_domain_select').hasClass('hiddenbtn')) && $("#domain").length>0) {
		var service_url = getUrlParam("service");
		if(service_url) {
			$("#domain").val(uriEncodeOnce(service_url));
			logOperation.log("current selected domain:" + getUrlParam("service"));
		} else {
			// get from cookie
			var cookie_service = cookieOperation.getService();
			var _service_url;
			if (!cookie_service) {
				// get default one
				_service_url = $("#domain option:first").val();
				cookieOperation.setService($("#domain option:first").text());
			} else {
				var cookie_option;
				$("#domain option").each(function (){  
					// 自定义登陆页面的 不自动跳转
				    if($(this).text()===cookie_service && $(this).attr('customLoginPage') === 'false'){   
				    	cookie_option = $(this);
				  }});  
				if (cookie_option) {
					_service_url = cookie_option.val();
				} else {
					// default value: domainCode = techops
					_service_url = $("#domain option:first").val();
					cookieOperation.setService($("#domain option:first").text());
				}
			}
			// redirect
			var redirect = getBaseUrl() + "?service=" + _service_url;
			top.window.location = redirect;
		}
	}
	
	// uri_encode once
	function uriEncodeOnce(uri_str) {
		if(!uri_str)return "";
		var d_str = decodeURIComponent(uri_str);
		if (uri_str === d_str) {
			return encodeURIComponent(uri_str);
		}
		// already encode
		return uri_str;
	}
	

	if (typeof (jqueryReady) == "function") {
		jqueryReady();
	}

	function getBaseUrl() {
		return window.location.href.substring(0,window.location.href.indexOf('?') === -1 ? window.location.href.length :window.location.href.indexOf('?'));
	};
	
	// domain change
	$("#domain").change(function() {
		// write cookie
		var selectedDomainCode = $("#domain option:selected").text();
		cookieOperation.setService(selectedDomainCode);
		// redirect
		var selectedDomain = $("#domain").val();
		var redirect = getBaseUrl() + "?service=" + selectedDomain;
		top.window.location = redirect;
	});
	
	// login submit
	$('#btn_cas_submit').click(function(e){
		e.preventDefault();  
		var loginForm = $('#login #fm1');
		var input_tenancy = $("<input type='hidden' name='tenancyCode' />")
	    input_tenancy.attr('value', cookieOperation.getTenancyCode());
		loginForm.append(input_tenancy);
		loginForm.submit();
	});
});
