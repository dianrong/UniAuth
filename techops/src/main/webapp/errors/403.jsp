<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.dianrong.common.uniauth.common.cons.AppConstants" %>
<%@ page import="com.dianrong.common.uniauth.common.util.HttpRequestUtil" %>
<%@ page import="com.dianrong.common.uniauth.common.server.UniauthI18NHolder" %>
<%

	if(!HttpRequestUtil.isAjaxRequest(request) && !HttpRequestUtil.isCorsRequest(request)) {
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
<a href="#" onclick="logout()"><%=UniauthI18NHolder.getProperties(request,"header.logout") %></a>
<script type="text/javascript">
function logout() {
    var origin = window.document.location.origin;
    var logoutUrl;
    if(origin.indexOf("techops") > 0) {
    	logoutUrl =  origin + "/logout/cas";
    } else {
    	logoutUrl =  origin + "/techops/logout/cas";
    }
    window.location = logoutUrl;
};
</script>
