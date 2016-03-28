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
				<div class="steps">
					<form action="" id="initpwd_post_form" class="form-horizontal" method="post">
						<div class="padding-top-bottom-10">
						 	<div class="form-group">
								<div class="col-md-offset-4 col-md-4 text-align-left">
							    	<label for="emailValue" class="h6">xxxxxx@dianrong.com</label>
							    	<input type="hidden" value="wanfei.wang@dianrong.com" name="email" id="user_email">
							    </div>
						  	</div>
						  	<div class="form-group">
						  		<label for="inputOriginPws" class="col-md-offset-2  col-md-2 control-label"><spring:message code="screen.init.password.step1.content.originpwd"/></label>
							  	<div class="col-md-4">
							    	<input type="password" class="form-control"  name="originpwd"  placeholder="<spring:message code="screen.init.password.step1.content.originpwd"/>"  id="origin_password">
							    </div>
						  </div>
					  	</div>
					  	<div class="padding-top-bottom-10">
						  <div class="form-group">
						  		<label for="inputNewPwd" class="col-md-offset-2  col-md-2 control-label"><spring:message code="screen.init.password.step1.content.newpwd"/></label>
							  	<div class="col-md-4">
							    	<input type="password" class="form-control" name="newpwd" placeholder="<spring:message code="screen.init.password.step1.content.newpwd"/>"  id="new_password">
							    </div>
						  </div>
						  <div class="form-group">
						  		<label for="reInputPwd" class="col-md-offset-2  col-md-2 control-label"><spring:message code="screen.init.password.step1.content.renewpwd"/></label>
							  	<div class="col-md-4">
							    	<input type="password" class="form-control" name="renewpwd" placeholder="<spring:message code="screen.init.password.step1.content.renewpwd"/>"  id="re_new_password">
							    </div>
						  </div>
					  </div>
					  <div class="padding-top-bottom-10">
						  <div class="form-group">
						  	<label for="inputVerifyCode" class="col-md-offset-2  col-md-2 control-label"><spring:message code="screen.init.password.step1.content.verifycode"/></label>
						  	<div class="col-md-2">
						    	<input type="text" class="form-control"  name="verify_code"  id="init_pwd_tverfynotice" placeholder="<spring:message code="screen.init.password.step1.content.verifycode"/>">
						  	</div>
						  	<div class="col-md-2">
						  		<div class="captcha-img">
						    		<img alt="picture" src="<%=path %>/uniauth/captcha" title="<spring:message code="screen.init.password.step1.content.verifycode.title"/>"  id="init_pwd_verfypic" >
						    		<button type="button" class="btn btn-primary glyphicon glyphicon-refresh" id="init_pwd_refreshverfypic"></button>
						    	</div>
						  	</div>
						  </div>
						  <div class="form-group">
						  	<div class="col-md-offset-4 col-md-4">
						  		<button type="button" id="btn_init_pwd_process" class="btn btn-wide btn-primary btnstep cursordefault" disabled="disabled"><spring:message code="screen.init.password.step1.confirm.title"/></button>
						  	</div>
						  </div>
					  </div>
					  <div class="form-group">
					  	<div class="col-md-offset-4 col-md-8 showwarninfo">
					  		<label for="initpwdwarn" id="init_pwd_warn_info"></label>
					  	</div>
					  </div>
					</form>
				</div>
			</div>
		</div>
</div>
<jsp:directive.include file="/WEB-INF/view/jsp/dianrong/common/bottom.jsp" />