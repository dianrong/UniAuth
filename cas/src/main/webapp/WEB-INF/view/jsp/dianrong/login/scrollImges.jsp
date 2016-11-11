<%@ page pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:directive.include file="/WEB-INF/view/jsp/dianrong/common/version.jsp" />
<%
	String path = request.getContextPath();
	String version = (String)application.getAttribute("cas_v");
%>
<meta charset="UTF-8" />

  <!-- Wrapper for slides -->
  <div class="carousel-inner" role="listbox">
	</div>
<div id="banner_tabs" class="flexslider">
	<ul class="slides">
		<c:forEach var="item" items="${loginImges}" varStatus="status">
				<li>
					<a title="" 
						<c:if test="${item.hasValidHref}">
								target="_blank"
			    		</c:if>
					href="${item.hrefUrl}">
						<img alt="${item.cfgKey}" src="<%=path %>/uniauth/cascfg/imges/${item.cfgKey}">
					</a>
				</li>
		</c:forEach> 
	</ul>
	<ul class="flex-direction-nav">
		<li><a class="flex-prev" href="javascript:;">Previous</a></li>
		<li><a class="flex-next" href="javascript:;">Next</a></li>
	</ul>
	<ol id="bannerCtrl" class="flex-control-nav flex-control-paging">
		<c:forEach var="item" items="${loginImges}" varStatus="status">
			<li
				<c:if test="${status.first}">
						class="active"
			    </c:if>
			><a><c:out value="${status.count}"/></a></li>
		</c:forEach> 
	</ol>
</div>
<script type="text/javascript" src="<%=path %>/imgscroll/js/slider.js?v=<%=version %>" ></script>
<script type="text/javascript" src="<%=path %>/js/loginscroll.js?v=<%=version %>" ></script>