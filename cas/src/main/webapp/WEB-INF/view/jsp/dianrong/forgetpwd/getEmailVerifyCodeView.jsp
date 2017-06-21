<jsp:directive.include file="/WEB-INF/view/jsp/dianrong/common/top.jsp" />

<div class="container find-pwd-container">
		<div class="find-pwd-content ng-scope">
			<header class="find-pwd">
				<a href="<%=path %>/login"><spring:message code="screen.password.reset.step.backtofirstpage"/></a>
				&gt;<spring:message code="screen.password.reset.step1.title" />
			</header>
			<div class="common-wizard password-reset-wizard ng-isolate-scope">
				<div class="modal-header">
					<ul class="steps-indicator steps-4">
						<li class="ng-scope done">
							<a class="ng-binding ng-pristine ng-valid">
								<span class="step-num ng-binding">1</span>
								<spring:message code="screen.password.reset.step1.notice"/>
							</a>
							</li>
						<li class="ng-scope current">
							<a class="ng-binding ng-pristine ng-valid">
								<span class="step-num ng-binding">2</span>
								<spring:message code="screen.password.reset.step2.notice"/>
							</a>
						</li>
						<li class="ng-scope default">
							<a class="ng-binding ng-pristine ng-valid">
								<span class="step-num ng-binding">3</span>
								<spring:message code="screen.password.reset.step3.notice"/>
							</a>
						</li>
						<li class="ng-scope default">
							<a class="ng-binding ng-pristine ng-valid">
								<span class="step-num ng-binding">4</span>
								<spring:message code="screen.password.reset.step4.notice"/>
							</a>
						</li>
					</ul>
				</div>
				
				<!-- content -->
				<div class="steps">
					<form action="<%=path %>/uniauth/forgetPassword" id="step2Post" class="form-horizontal">
					 	<div class="form-group">
							<div class="col-sm-offset-4 col-sm-4">
						    <label for="emailValue" class="h6">${fn:escapeXml(sessionScope.pwdg_emailVal)}</label>
						    </div>
					  	</div>
					  	<div class="form-group">
						  	<div class="col-sm-offset-4 col-sm-2">
						    	<input type="text" class="form-control" id="input1" placeholder="<spring:message code="screen.password.reset.step1.verfy.notice"/>">
						  	</div>
						  	<div class="col-sm-2">
						    		<button type="button" class="btn btn-primary btn-sendemail" id="input2"><spring:message code="screen.password.reset.get.verfycode"/></button>
						    		<div id="verify_code_div"></div>
						  	</div>
					  </div>
					  <div class="form-group">
					  	<div class="col-sm-offset-4 col-sm-4">
					  		<button type="button" id="btn_step2" class="btn btn-wide btn-primary btnstep cursordefault" disabled="disabled"><spring:message code="screen.password.reset.nextstep"/></button>
					  	</div>
					  </div>
					  <div class="form-group">
					  	<div class="col-sm-offset-4 col-sm-8 showwarninfo">
					  		<label for="emailverfycode" id="emailverfywarn"></label>
					  	</div>
					  </div>
					</form>
				</div>
			</div>
		</div>
</div>
<jsp:directive.include file="/WEB-INF/view/jsp/dianrong/common/bottom.jsp" />