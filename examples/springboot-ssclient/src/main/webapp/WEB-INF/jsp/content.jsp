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
	<h1>Content Page</h1>
	this is some content.
	hello <%=request.getRemoteUser() %>
	
	<br>
	<a href="logout/cas">Single Sign Out</a>
</body>
</html>