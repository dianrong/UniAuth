// common function for cas
var context_path = $('#hidden_path_input').val();

//i18n lang set
function i18nset(localestr){
	if(!localestr) {return};
	var url = context_path+"/uniauth/i18n/setLanguage?locale="+localestr;
	$.get(url, function(data){
		// refresh
		window.location.href=window.location.href;
  });
}

// get window location string‘s parameter part
function getLocationParameterStr(){
	// copy current url
	var current_url = window.location.search;
	// 只取参数部分
	var pindex =  current_url.indexOf("?");
	if(pindex > -1) {
		current_url = current_url.substring(pindex+1);
	}
	return current_url;
} 

// refresh capatcha
var refresh_verfypic = function(capatchElement) {
	if(!capatchElement) {
		return;
	}
	// context
	if(context_path == undefined){
		return;
	}
	$(capatchElement).attr('src', context_path + '/uniauth/captcha?rnd=' + Math.random());
}

$(function() {
	var loadI18n = function() {
		var url = context_path+"/uniauth/i18n/getLanguage";
		var i18nName = "messages";
		$.get(url, function(data){
			var i18npath = context_path +'/i18n/';
			 jQuery.i18n.properties({
		         name : i18nName,
		         path : i18npath,
		         mode : 'map', 
		         language : data
			});
	  })
	}
	
	var bind_tenancy_select_event = function() {
			$('#tenancy_set').click(function() {
				$('#tenancy_show').addClass('hiddenbtn');
				$('#tenancy_select').removeClass('hiddenbtn');
			});
			$('#btn_confirm_tenancy').click(function(){
				$('#tenancy_select').addClass('hiddenbtn');
				$('#tenancy_show').removeClass('hiddenbtn');
			});
	}
	//load i18n
	loadI18n();
	bind_tenancy_select_event();
});

