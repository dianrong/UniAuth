<!DOCTYPE html>
<%@ page pageEncoding="UTF-8" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page import="com.dianrong.common.uniauth.cas.helper.CasCfgResourceRefreshHelper"%>       
<jsp:directive.include file="/WEB-INF/view/jsp/dianrong/common/version.jsp" />
<%
String path = request.getContextPath(); 
String version = (String)application.getAttribute("cas_v");
%>

<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title><%=CasCfgResourceRefreshHelper.INSTANCE.getImageCacheDto("CAS_TITLE")==null?"":CasCfgResourceRefreshHelper.INSTANCE.getImageCacheDto("CAS_TITLE").getValue()%></title>
  
  <spring:theme code="standard.custom.css.file" var="customCssFile" />
  <link rel="stylesheet" href="<c:url value="${customCssFile}" />" />
  <link rel="icon" href="<%=path %>/uniauth/cascfg/imges/CAS_ICON"  type="image/x-icon" />
	<!-- for ad scroll -->
	<link href="<%=path %>/imgscroll/css/scroll.css?v=<%=version %>" rel="stylesheet">
	<link href="<%=path %>/css/main.css?v=<%=version %>" rel="stylesheet">
  <!--[if lt IE 9]>
    <script src="//cdnjs.cloudflare.com/ajax/libs/html5shiv/3.6.1/html5shiv.js" type="text/javascript"></script>
  <![endif]-->
</head>
<body   id="cas"  style="background-color:<%=CasCfgResourceRefreshHelper.INSTANCE.getImageCacheDto("CAS_BACKGROUND_COLOR")==null?"":CasCfgResourceRefreshHelper.INSTANCE.getImageCacheDto("CAS_BACKGROUND_COLOR").getValue()%>">
   	<!-- 租户选择 -->
	<div class="tenancy_show" id="tenancy_show">
		<p>
			<a href="javascript:void(0);" title="<spring:message code="screen.main.tenancy.show.title" />"  id="tenancy_set">loading...</a>&nbsp;<spring:message code="screen.main.tenancy.show.welcome" />
		</p>
	</div>
   	<div id="tenancy_select" class="tenancy_select hiddenbtn">
		<div class="cas_content">
			<div class="cas_title">
				<label for="tenancy_code"><spring:message code="screen.main.tenancy.select.title" /></label>
			</div>
			<div class="cas_info">
				<div class="float-left">
					<input type="text" id="input_tenancy_code"/>
				</div>
				<div>
					<input type="button" value="<spring:message code="screen.main.tenancy.select.info.confirm" />" class="btn-submit" id="btn_confirm_tenancy">
				</div>
			</div>
			<div class="cas_msg">
				<p id="warn_info">
					<spring:message code="screen.main.tenancy.msg.warn" />
				</p>
			</div>
			</div>
   </div>	   		
  <div id="container">
      <header>
      	<img alt="cas-login-logo" src="<%=path %>/uniauth/cascfg/imges/CAS_LOGO"><br>
      	<c:if test="${not empty principal}">
      		<font color="white"><spring:message code="screen.welcome.label.current.loginuser" />${principal}</font>
      	</c:if>
      </header>
      <input type="hidden" id="hidden_path_input" value="<%=path %>">
      <div id="content">
