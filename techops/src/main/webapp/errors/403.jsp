<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.dianrong.common.uniauth.common.cons.AppConstants" %>
<%@ page import="com.dianrong.common.uniauth.common.util.HttpRequestUtil" %>
<%

	if(!HttpRequestUtil.isAjaxRequest(request) && !HttpRequestUtil.isCORSRequest(request)) {
		response.setContentType("text/html; charset=UTF-8");
%>
		对不起，你没有权限访问当前页面资源！
<%
	} else {
		response.setContentType("application/json");
		response.addHeader("Cache-Control", "no-store");
%>
{
	"info":
		[
			{
				"name": "<%=AppConstants.NO_PRIVILEGE%>",
				"msg": "对不起，你没有权限访问当前页面资源！"
			}
		]
}
<%
	}
%>