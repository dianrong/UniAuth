<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.dianrong.common.uniauth.common.cons.AppConstants" %>
<%@ page import="com.dianrong.common.uniauth.common.util.HttpRequestUtil" %>
<%@ page import="com.dianrong.common.uniauth.common.server.UniauthI18NHolder" %>
<%

	if(!HttpRequestUtil.isAjaxRequest(request) && !HttpRequestUtil.isCORSRequest(request)) {
		response.setContentType("text/html; charset=UTF-8");
%>
		<%=UniauthI18NHolder.getProperties(request,"constant.noPermission") %>
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
				"msg": "<%=UniauthI18NHolder.getProperties(request,"constant.noPermission") %>"
			}
		]
}
<%
	}
%>