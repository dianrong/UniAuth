<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>welcome to login</title>
</head>
<body>
	<h1>Login Page</h1>
	<form action="javascript:#;">
		<input type="text" name="account" id="account_input_id">
		<br>
		<input type="password" name="password" id="password_input_id">
		<br>
		<input type="hidden" name="lt" value="<%=request.getAttribute("ltval") %>"  id="lt_input_id">
		<input type="hidden" name="service" value="<%=request.getAttribute("service") %>"  id="service_input_id">
		<input type="hidden" name="casUrl" value="<%=request.getAttribute("casUrl") %>"  id="casurl_input_id">
		<input type="hidden" name="customUrl" value="<%=request.getAttribute("customUrl") %>"  id="customurl_input_id">
		<input type="button" value="login" id="login_btn_id"  >
	</form>
</body>
<script src="../js/jquery-1.12.1.min.js"></script>
<script src="../js/login.js"></script>
</html>