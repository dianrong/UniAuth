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
<!DOCTYPE html>

<%@ page pageEncoding="UTF-8" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page import="com.dianrong.common.uniauth.cas.helper.CasCfgResourceRefreshHelper"%>       
<%
	String path = request.getContextPath();
%>

<!-- import basepath of cas project -->
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1">
  
  <title><%=CasCfgResourceRefreshHelper.instance.getImageCacheDto("CAS_TITLE")==null?"":CasCfgResourceRefreshHelper.instance.getImageCacheDto("CAS_TITLE").getValue()%></title>
  
  <spring:theme code="standard.custom.css.file" var="customCssFile" />
  <link rel="stylesheet" href="<c:url value="${customCssFile}" />" />
  <link rel="icon" href="<%=path %>/uniauth/cascfg/imges/CAS_ICON"  type="image/x-icon" />
	<!-- for ad scroll -->
	<link href="<%=path %>/imgscroll/css/scroll.css" rel="stylesheet">
	<link href="<%=path %>/css/main.css" rel="stylesheet">
  <!--[if lt IE 9]>
    <script src="//cdnjs.cloudflare.com/ajax/libs/html5shiv/3.6.1/html5shiv.js" type="text/javascript"></script>
  <![endif]-->
</head>
<body   id="cas"  style="background-color:<%=CasCfgResourceRefreshHelper.instance.getImageCacheDto("CAS_BACKGROUND_COLOR")==null?"":CasCfgResourceRefreshHelper.instance.getImageCacheDto("CAS_BACKGROUND_COLOR").getValue()%>">
   	<!-- 租户选择 -->
	<div class="tenancy_show" id="tenancy_show">
		<p>
			<a href="javascript:void(0);" title="点击切换租户"  id="tenancy_set">点融网</a>&nbsp;欢迎您的访问
		</p>
	</div>
   	<div id="tenancy_select" class="tenancy_select hiddenbtn">
		<div class="content">
			<div class="title">
				<label for="tenancy_code">tenancy_Code</label>
			</div>
			<div class="info">
				<div class="float-left">
					<input type="text"/>
				</div>
				<div>
					<input type="button" value="confirm" class="btn-submit" id="btn_confirm_tenancy">
				</div>
			</div>
			<div class="msg">
				<p>
					the tenancy code you input is not exist, please check is right or not.
				</p>
			</div>
			</div>
   </div>	   		
  <div id="container">
      <header>
      	<img alt="cas-login-logo" src="<%=path %>/uniauth/cascfg/imges/CAS_LOGO"><br>
      	<c:if test="${not empty principal}">
      		<font color="white"><spring:message code="sscreen.welcome.label.current.loginuser" />${principal}</font>
      	</c:if>
      </header>
      <input type="hidden" id="hidden_path_input" value="<%=path %>">
      <div id="content">
