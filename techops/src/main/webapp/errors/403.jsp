<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.dianrong.common.uniauth.common.cons.AppConstants" %>
<%
	String ajaxReqType = request.getHeader(AppConstants.AJAS_HEADER);

	if (ajaxReqType == null) {
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
				"name": "NO_PRIVILEGE",
				"msg": "对不起，你没有权限访问当前页面资源！"
			}
		]
}
<%
	}
%>