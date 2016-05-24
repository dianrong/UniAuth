<%@ page session="false" contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html manifest="/auth-server/borrower.appcache">
    <head>
	<script type="text/javascript" src="/auth-server/js/hero.js"></script>
	<script type="text/javascript" src="/auth-server/js/ajax.js"></script>
	<script type="text/javascript">

	localStorage.sessionid = '';
	var username = localStorage.username?localStorage.username:''+'';
	var password = localStorage.password?localStorage.password:''+'';
	var secured = true;
	var authServer = 'https://borrower.dianrong.com'
	if (host.search(/localhost|10/)>0) {
		authServer = host;
	}if (host.search(/dev/)>0) {
		authServer = 'https://borrower-dev.dianrong.com';
	}else if(host.search(/demo/)>0){
		authServer = 'https://borrower-demo.dianrong.com';
	}
    API.boot = function(data){
    	if (data.service) {
			API.in({http:{'url':'/auth-server/serviceTicket/query',method:'GET',data:{service:data.service}}});
    	}else{
	    	if (username || password) {
	            API.out({datas:[
	            {
	            	name:'username',
	            	text:username,
	            },
	            {
	            	name:'password',
	            	text:password,
	            }
	            ]});
	    	};
	        if (data.childStep) {
	            API.out({appearance:{rightItems:{}}});
	            API.out({appearance:{leftItems:{}}});
	        } else {
	            API.out({appearance:{leftItems:[{title:"取消",click:{click:'dismiss'}}]}});
	            API.out({appearance:{rightItems:[{title:"注册",click:{showregister:'click'}}]}});
	        }
    	}
    }
    API.reloadData = function(data){
    	if (data.api === '/auth-server/users/login') {
    		if (data.serviceTicket) {
    			localStorage.username = username;
    			API.out({command:'load:'+authServer+'/borrower-static/wallet/v4/login_sso.html?ticket='+data.serviceTicket+'&username='+ username});
    		};
    	}else if(data.api === '/auth-server/serviceTicket/query'){
    		API.out({sso:data});
    	}
    }
	API.special_logic = function(data){
		if (data.username) {
            if (data.value.length > 0) {
                API.out({datas:{name:'usernameBorder',backgroundColor:'ffffff'}});
            } else {
                API.out({datas:{name:'usernameBorder',backgroundColor:'7c8aa1'}});
            }
			username = data.value;
		};
		if (data.usernameStart) {
          API.out({datas:{name:'usernameBorder',backgroundColor:'ffffff'}});
		}
		if (data.passwordStart) {
          API.out({datas:{name:'passwordBorder',backgroundColor:'ffffff'}});
		}
		if (data.password) {
            if (data.value.length > 0) {
                API.out({datas:{name:'passwordBorder',backgroundColor:'ffffff'}});
            } else {
                API.out({datas:{name:'passwordBorder',backgroundColor:'7c8aa1'}});
            }
			password = data.value;
		};
		if (data.login === 'click') {
            if (username === '' || password === '' || username === undefined || password === undefined) {
                API.out({datas:[
                    {
                        name:'toast',
                        hidden:false,
                        text:'错误消息:请输入用户名和密码'
                    },
                ]});
            }else{
				API.out({command:'showLoading'});
				var service = (authServer.search(/3000/) > 0)?'https://borrower-dev.dianrong.com':authServer;
				service = (authServer.search(/3001/) > 0)?'https://borrower-demo.dianrong.com':service;
				service = (authServer.search(/3002/) > 0)?'https://borrower.dianrong.com':service;
				service = (authServer.search(/3003/) > 0)?'https://www-dev.dianrong.com':service;
				service = (authServer.search(/3004/) > 0)?'https://www-demo.dianrong.com':service;
				service = (authServer.search(/3005/) > 0)?'https://www.dianrong.com':service;
				API.in({http:{'url':'/auth-server/users/login',method:'POST',data:{'identity':username,'password':password,service:service+'/api/v2/j_spring_cas_security_check'}}});
            }
		};
		if (data.hidepassword === 'click') {
			secured = !secured;
			if (secured) {
				API.out({datas:{name:'hidepassword',imageN:'invisible'}});
			} else {
				API.out({datas:{name:'hidepassword',imageN:'visible_login'}});
			}
			API.out({datas:{name:'password',secure:secured}});
		}
        if (data.showregister === 'click') {
            API.out({command:'goto:'+authServer+'/borrower-static/wallet/v4/register1.html?childStep=true'});
        }
        if (data.click === 'dismiss') {
            API.out({globle:{key:'tabSelect',value:0}});
            if (API.getDeviceType()=== 'ANDROID') {
	            setTimeout(function(){
	                API.out({command:'dismiss'});
	            },10);
            }else{
	            API.out({command:'dismiss'});
            }
        }
	};
	var ui = {
	version:0,
	backgroundColor:'303e53',
	tintColor:'303e53',
	nav:{
		title:"",
	},
	views:
[
{
	class:"HeroImageView",
	frame:{w:"172",h:"103"},
	center:{x:"0.5x",y:"0.18x"},
	image:'login_logo'
},
{
	class:'UIView',
	name:'nameArea',
	frame:{x:"0.1x",w:"0.8x",h:"44",t:"0.35x"},
	subViews:[
		{
			class:"HeroTextField",
			name:'username',
			type:'email',
			textColor:'ffffff',
			placeHolderColor:'7c8aa1',
			placeHolder:"手机/用户名/邮箱",
		    whileEditing:true,
			frame:{w:"1x",h:"1x"},
			textFieldDidEditing:{'username':'a'},
			textFieldDidBeginEditing:{'usernameStart':'b'},
		},
		{
			class:'UIView',
			name:'usernameBorder',
			frame:{w:"1x",h:"0.5",t:"0.99x"},
			backgroundColor:'7c8aa1',
		},
	],
},
{
	class:'UIView',
	name:'passwordArea',
	frame:{x:"0.1x",w:"0.8x",h:"44"},
	yOffset:'nameArea+10',
	subViews:[
		{
			class:"HeroTextField",
			name:"password",
			placeHolder:"登录密码",
			textColor:'ffffff',
			placeHolderColor:'7c8aa1',
			frame:{w:"1x",h:"1x"},
			secure:1,
			textFieldDidEditing:{'password':'b'},
			textFieldDidBeginEditing:{'passwordStart':'b'},
			'return':{login:'c'},
		},
		{
			class:'UIView',
			name:'passwordBorder',
			frame:{w:"1x",h:"0.5",t:"0.99x"},
			backgroundColor:'7c8aa1',
		},
		{
			class:'HeroButton',
			name:'hidepassword',
			frame:{r:'0',y:'10',w:'30',h:'25'},
			imageN:'invisible',
			click:{hidepassword:'click'},
		},
	]
},
{
	class:"HeroButton",
	frame:{x:"0.1x",w:"65",h:"32"},
	yOffset:'passwordArea+5',
	title:"忘记密码?",
	size:14,
	titleColor:'aaaaaa',
	click:{command:'goto:'+authServer+'/borrower-static/wallet/v4/forget.html'}
},
{
	class:"HeroLabel",
	name:'toast',
	size:12,
	frame:{x:"0.1x",w:"0.8x",h:"30",y:'0'},
	yOffset:'passwordArea+35',
	text:" ",
	textColor:'cc0000',
	alignment:'center',
	hidden:false,
},
{
	class:"HeroButton",
	frame:{x:"0.1x",w:"0.8x",h:"45"},
	yOffset:'passwordArea+75',
	title:"登录",
	size:20,
	titleColor:'ffffff',
	borderColor:'ffffff',
	cornerRadius:4,
	click:{login:'click'},
},
]
};
</script>
</head>
</html>
