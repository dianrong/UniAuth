<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.dianrong.common.uniauth.common.cons.AppConstants" %>
<%@ page import="com.dianrong.common.uniauth.common.util.HttpRequestUtil" %>
<%
	String baseUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
	String queryStr =  request.getQueryString();
	queryStr = queryStr == null ? "" : "?" + queryStr;
	String reqUrl = baseUrl + request.getContextPath() + "/login" + queryStr;

	if (!HttpRequestUtil.isAjaxRequest(request) && !HttpRequestUtil.isCorsRequest(request)) {
%>
<jsp:directive.include file="includes/login.jsp" />
<%
} else {
	response.setContentType("application/json");
	response.addHeader("Cache-Control", "no-store");
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