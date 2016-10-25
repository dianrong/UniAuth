<jsp:directive.include file="/WEB-INF/view/jsp/dianrong/common/top.jsp" />
<div class="container find-pwd-container">
	<div class="find-pwd-content ng-scope">
		<header class="find-pwd">
			<a href="<%=path %>/login"><spring:message code="screen.password.reset.step.backtofirstpage" /></a>
			&gt; <spring:message code="screen.personal.info.edit.title" />
		</header>
		<div class="common-wizard infoedit paddingtop10">
				<div style="margin-top: 30px;">
					<p class="text-warning" style="font-size: 16px;">${user_manage_errormsg}</p>
				</div>				
		</div>
	</div>
</div>
<jsp:directive.include file="/WEB-INF/view/jsp/dianrong/common/bottom.jsp" />