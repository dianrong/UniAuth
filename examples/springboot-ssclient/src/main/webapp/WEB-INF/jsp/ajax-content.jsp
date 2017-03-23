<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
    <base href="<%=basePath%>">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>home page</title>
</head>
<body>
    <div id="ajax_content">
    </div>
	<br>
	<a href="logout/cas">Single Sign Out</a>
</body>
<script type="text/javascript" src="js/jquery-1.12.1.min.js"></script>
<script type="text/javascript">
// get decoded uri
var getDecodedUri = function(uri_str) {
    if(!uri_str)return "";
    return decodeURIComponent(uri_str);
}

var validateTicket = function(ticket) {
	// 注意:ticket验证请求路径为/login/cas?ticket=xxxx
	// 可参考CasAuthenticationFilter的filterProcessesUrl参数配置
	// 参考CasAuthenticationFilterConfigure的实现
	var url = "/login/cas?ticket="+ticket;
	$.ajax({
        type:"GET",
        url:url,
        success:function(data){
        	// 返回结果两种:
            // 成功:{"code":200,"result":"success","message":"200","errors":[null]}
            // 失败:{"code":500,"result":"error","message":"500","errors":["Service ticket validation failed"]}
            $('#ajax_content').html(JSON.stringify(data));
        }, error: function(){
            console.log('error');
        }         
     });
}

var login = function(service) {
	var url = 'http://localhost:8080/cas/api/v2/ticket';
	$.ajax({
        type:"POST",
        data: {service:service},
        url:url,
        dataType: 'json',
        xhrFields: {
            withCredentials: true
        },
        success:function(data){
        	if (data && data.code === 200) {
        		var ticket = data.content;
        		validateTicket(ticket);
        	} else {
        		console.error('login failed');
        		$('#ajax_content').html(JSON.stringify(data));
        	}
        }, error: function(){
            console.log('error');
        }         
     });
}

$(document).ready(function(){
	$.ajax({
        type:"POST",
        url:"ajax-request",
        success:function(data){
        	if (data && data.info&&data.info[0]&&data.info[0].name&&data.info[0].name==='LOGIN_REDIRECT_URL') {
        		var service = data.info[0].msg.split('=')[1];
        		login(getDecodedUri(service));
        	} else {
        		$('#ajax_content').html(JSON.stringify(data));
        	}
        }, error: function(){
            console.log('error');
        }         
     });
});
</script>
</html>