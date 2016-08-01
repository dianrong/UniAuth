$(function() {
	function areCookiesEnabled() {
		$.cookie('cookiesEnabled', 'true');
		var value = $.cookie('cookiesEnabled');
		if (value != undefined) {
			$.removeCookie('cookiesEnabled');
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

	$("#domain").val(uriEncodeOnce(getUrlParam("service")));
	if (console && console.log) {
		console.log("current selected domain:" + getUrlParam("service"));
	}
	
	// uriencode once
	function uriEncodeOnce(uri_str) {
		if(!uri_str){return ""};
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

	$("#domain").change(function() {
		var selectedValue = $("#domain").val();
		var location = window.location.href;
		var pos = location.indexOf("?service=");
		var url = location.substring(0, pos);
		var redirect = url + "?service=" + selectedValue;
		top.window.location = redirect;
	});
});
