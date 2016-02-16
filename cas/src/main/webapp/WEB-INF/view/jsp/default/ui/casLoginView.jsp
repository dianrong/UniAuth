
<%
	String ajaxReqType = request.getHeader("X-Requested-With");
	if (ajaxReqType == null) {
%>
<jsp:directive.include file="includes/login.jsp" />
<%
	} else {
		response.setContentType("application/json");
		response.addHeader("Cache-Control", "max-age=0");
		
		String reqUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort() + request.getContextPath() + "login?" + request.getQueryString();
%>
		{ 
			"relogin":true,
			"location:":"<%= reqUrl %>"
		}
<%
	}
%>