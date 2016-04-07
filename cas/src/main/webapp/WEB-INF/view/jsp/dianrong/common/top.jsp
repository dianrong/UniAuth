<!DOCTYPE html>

<%@ page pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
	String path = request.getContextPath();
%>
<html lang="en">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1">

<title>${cascfg_cache_key.pageTitle.value}</title>

<link rel="icon" href="<%=path %>/uniauth/cascfg/imges/CAS_ICON"  type="image/x-icon" />
<!-- 	type="image/x-icon" /> -->
<!-- Bootstrap -->
<link href="<%=path %>/bootstrap-3.3.5/css/bootstrap.min.css" rel="stylesheet">

<!-- system -->
<link href="<%=path %>/css/main.css" rel="stylesheet">
<link href="<%=path %>/css/component.css" rel="stylesheet">
<link href="<%=path %>/css/system.css" rel="stylesheet">

<!--[if lt IE 9]>
    <script src="//cdnjs.cloudflare.com/ajax/libs/html5shiv/3.6.1/html5shiv.js" type="text/javascript"></script>
  <![endif]-->
</head>
<body style="background-color: ${cascfg_cache_key.backgroundColorText.value}">
	<header class="header">
		<img alt="点融网" src="<%=path %>/uniauth/cascfg/imges/CAS_LOGO"><br>
		<c:if test="${not empty principal}">
			<font color="white">当前登录用户：${principal}</font>
		</c:if>
		<!-- 
        <a id="logo" href="http://www.dianrong.com" title="<spring:message code="logo.title" />">Dianrong</a>
        <h1>点融网内部统一认证系统</h1>
         -->
	</header>
	<input type="hidden" id="hidden_path_input" value="<%=path %>">
	<div class="content">