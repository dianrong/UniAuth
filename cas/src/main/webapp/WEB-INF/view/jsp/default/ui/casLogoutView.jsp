<%--

    Licensed to Apereo under one or more contributor license
    agreements. See the NOTICE file distributed with this work
    for additional information regarding copyright ownership.
    Apereo licenses this file to you under the Apache License,
    Version 2.0 (the "License"); you may not use this file
    except in compliance with the License.  You may obtain a
    copy of the License at the following location:

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing,
    software distributed under the License is distributed on an
    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
    KIND, either express or implied.  See the License for the
    specific language governing permissions and limitations
    under the License.

--%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.dianrong.common.uniauth.common.cons.AppConstants" %>
<%@ page import="com.dianrong.common.uniauth.common.util.HttpRequestUtil" %>
<%
	String baseUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
	String queryStr =  request.getQueryString();
	queryStr = queryStr == null ? "" : "?" + queryStr;
	String reqUrl = baseUrl + request.getContextPath() + "/login" + queryStr;

	if (!HttpRequestUtil.isAjaxRequest(request) && !HttpRequestUtil.isCORSRequest(request)) {
%>

<jsp:directive.include file="includes/top.jsp" />
  <div id="msg" class="success">
    <h2><spring:message code="screen.logout.header" />  </h2> 
    <p><spring:message code="screen.logout.success" /></p>
    <p><spring:message code="screen.logout.security" /></p>
    <br>
    <p><a href="<%=path %>/login"><spring:message code="screen.logout.login.again" /></a></p>
  </div>
<jsp:directive.include file="includes/bottom.jsp" />

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

