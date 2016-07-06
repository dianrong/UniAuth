// common function for cas
var context_path = $('#hidden_path_input').val();

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

// load i18n
(function(){
	var url = context_path+"/uniauth/i18n/getLanguage";
	var i18nName = "";
	$.get(url, function(data){
		 jQuery.i18n.properties({
	         name : i18nName,
	         path : '/i18n/', 
	         mode : 'map', 
	         language : data
		});
})();
