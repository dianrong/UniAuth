requirejs.config({
    paths: {
    	uniauthcommon: 'http://localhost:8080/cas/js/uniauth-common'
    }
});

var myApi = {};
requirejs(['uniauthcommon'], function(uniauthcommon){
	myApi.login = function(){
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
		uniauthcommon.login(account, password, appendInfo);
	};
});
