<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.dianrong.common.uniauth.common.cons.AppConstants" %>
<%
	String ajaxReqType = request.getHeader(AppConstants.AJAS_CROSS_HEADER);

	if (ajaxReqType == null) {
%>
		<jsp:directive.include file="includes/login.jsp" />
<%
	} else {
		response.setContentType("application/json");
		response.addHeader("Cache-Control", "no-store");
		
		String reqUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort() + request.getContextPath() + "/login?" + request.getQueryString();
%>
{
	"info":
		[
			{
				"name": "<%=AppConstants.LOGIN_REDIRECT_URL%>",
				"msg": "<%= reqUrl %>"
			}
		]
}
<%
	}
%>