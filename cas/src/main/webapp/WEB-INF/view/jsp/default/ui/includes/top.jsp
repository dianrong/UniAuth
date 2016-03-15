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

<%
	String path = request.getContextPath();
%>

<!-- import basepath of cas project -->
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1">
  
  <title>点融网内部统一认证系统</title>
  
  <spring:theme code="standard.custom.css.file" var="customCssFile" />
  <link rel="stylesheet" href="<c:url value="${customCssFile}" />" />
  <link rel="icon" href="<c:url value="/favicon.ico" />" type="image/x-icon" />
  
  <!--[if lt IE 9]>
    <script src="//cdnjs.cloudflare.com/ajax/libs/html5shiv/3.6.1/html5shiv.js" type="text/javascript"></script>
  <![endif]-->
</head>
<body id="cas">
  <div id="container">
      <header>
      	<img alt="点融网" src="<%=path %>/images/logo.png"><br>
      	<c:if test="${not empty principal}"><font color="white">当前登录用户：${principal}</font></c:if>
      	<!-- 
        <a id="logo" href="http://www.dianrong.com" title="<spring:message code="logo.title" />">Dianrong</a>
        <h1>点融网内部统一认证系统</h1>
         -->
      </header>
      <input type="hidden" id="hidden_path_input" value="<%=path %>">
      <div id="content">
