<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.dianrong.common.uniauth.common.cons.AppConstants" %>
<%
	String ajax = request.getHeader(AppConstants.AJAS_HEADER);
	String ajaxCross = request.getHeader(AppConstants.AJAS_CROSS_HEADER);
	String baseUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
	String queryStr =  request.getQueryString();
	queryStr = queryStr == null ? "" : "?" + queryStr;
	String reqUrl = baseUrl + request.getContextPath() + "/login" + queryStr;

	String ajaxCrossStr = ajaxCross == null ? "" : ajaxCross.replaceAll("https|http", "");
	String baseUrlStr = baseUrl.replaceAll("https|http", "");

	if ((ajaxCross == null && ajax == null) || (!"".equals(ajaxCrossStr) && baseUrlStr.startsWith(ajaxCrossStr))) {
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