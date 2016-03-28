<jsp:directive.include file="/WEB-INF/view/jsp/dianrong/common/top.jsp" />

<div class="container find-pwd-container">
		<div class="find-pwd-content ng-scope">
			<header class="find-pwd">
						<% 
								Object savedLoginContext = request.getSession().getAttribute("pwdg_savedLoginContext");
								if(savedLoginContext == null) {
									%>
										<a href="<%=path %>/login"><spring:message code="screen.init.password.navigation.firstpage"/></a>
									<% 
								} else {
									%>
										<a href="${fn:escapeXml(sessionScope.pwdg_savedLoginContext)}"><spring:message code="screen.init.password.navigation.firstpage"/></a>
									<% 
								}
						%>
				&gt;<spring:message code="screen.init.password.navigation.initpwd" />
			</header>
				<!-- content -->
				<div class="padding-top-120">
					<div class="row">
					  <div class="col-md-offset-5 col-md-3 reset-success-notice">
						<img src="<%=path %>/images/pwdreset/icon-reset-success.png" class="img-responsive check-ok-icon" alt="Responsive image">
					  </div>
					</div>
					<div class="row">
					  <div class="col-md-offset-4 col-md-4 h6">
						<spring:message code="screen.init.password.step2.content.link"/>
								<% 
									Object savedLoginContext1 = request.getSession().getAttribute("pwdg_savedLoginContext");
									if(savedLoginContext1 == null) {
										%>
											<a href="<%=path %>/login"><spring:message code="screen.password.reset.step3.success.login"/></a>
										<% 
									} else {
										%>
											<a href="${fn:escapeXml(sessionScope.pwdg_savedLoginContext)}"><spring:message code="screen.init.password.navigation.firstpage"/></a>
										<% 
									}
								%>
					  </div>
					</div>
				<div>
			</div>
		</div>
	</div>
	</div>
</div>

<jsp:directive.include file="/WEB-INF/view/jsp/dianrong/common/bottom.jsp" />