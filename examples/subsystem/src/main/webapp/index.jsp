<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'index.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
  </head>
  
  <body>
    anyone can see this page(including anonymous). <br>  
    <a href="/subsystem/user/user.jsp">role user page</a> <br>
    <a href="/subsystem/admin/admin.jsp">role admin page</a> 
	<sec:authorize access="hasRole('ROLE_ADMIN')">
		Admin can see this.
	</sec:authorize>
	<br>
	<sec:authorize access="hasRole('ROLE_SUPER_ADMIN')">
		Super_Admin can see this.
	</sec:authorize>
	<br>
	<sec:authorize url="/DISPLAY_ADD_BUTTON">
		DISPLAY_ADD_BUTTON
	</sec:authorize>
	<br>	
	<sec:authorize url="/abc/123/abc">
		reg expression
	</sec:authorize>
	<br>
	
	<sec:accesscontrollist hasPermission="1" domainObject="test">
		this is acl
	</sec:accesscontrollist>
		
	<br>
	<%
	Object obj1 = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	out.println(obj1);
	out.println("<br>");
	com.dianrong.common.uniauth.client.custom.UserExtInfo userExtInfo = (com.dianrong.common.uniauth.client.custom.UserExtInfo)obj1;
	out.println(userExtInfo.getDomainDto());
	out.println("<br>");
	out.println("<br>");
	Object obj2 = session.getAttribute("SPRING_SECURITY_CONTEXT");
	out.println(obj2);
	%>
  </body>
</html>
