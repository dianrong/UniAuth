<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.dianrong.common.uniauth.common.cons.AppConstants" %>
<%
	String ajax = request.getHeader(AppConstants.AJAX_HEADER);
	String ajaxCross = request.getHeader(AppConstants.CROSS_RESOURCE_ORIGIN_HEADER);
	String baseUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
	String queryStr =  request.getQueryString();
	queryStr = queryStr == null ? "" : "?" + queryStr;
	String reqUrl = baseUrl + request.getContextPath() + "/login" + queryStr;

	String ajaxCrossStr = ajaxCross == null ? "" : ajaxCross.replaceAll("https|http", "");
	String baseUrlStr = baseUrl.replaceAll("https|http", "");
    // (   ( (是否有跨域字符串)   ||   (是否跨域) )   &&  (是否是Ajax call)   )    不是跨域 且 不是AjaxCall则返回登陆页,否则返回json
	if (((ajaxCross == null) || (!"".equals(ajaxCrossStr) && baseUrlStr.startsWith(ajaxCrossStr))) && !AppConstants.JQUERY_XMLHttpRequest_HEADER.equalsIgnoreCase(ajax)) {
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