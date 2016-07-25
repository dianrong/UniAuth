<jsp:directive.include file="/WEB-INF/view/jsp/dianrong/common/top.jsp" />

<div class="container find-pwd-container">
		<div class="find-pwd-content ng-scope">
			<header class="find-pwd">
				<a href="javascript:void(0);" id="init_pwd_to_firstpage_a"><spring:message code="screen.init.password.navigation.firstpage"/></a>
				&gt;<spring:message code="screen.init.password.navigation.initpwd" />
			</header>
				<!-- content -->
				<div class="steps  initpwd">
					<p ><spring:message code="screen.init.password.step1.content.user.notice"/></p>
					<form action="" id="initpwd_post_form" class="form-horizontal" method="post">
						 	<div class="form-group">
								<div class="col-sm-offset-4 col-sm-4 text-align-left">
							    	<label for="emailValue" class="h6">
							    		${credential.username} 
							    	</label>
							    	<input type="hidden" value="${credential.username} " name="email" id="user_email">
							    	<input type="hidden" name="savedLoginContext" id="login_redirec_url_id">
							    	<input type="hidden" name="expire"  value="${expire }">
							    </div>
						  	</div>
						  	<div class="form-group">
						  		<label for="inputOriginPws" class="col-sm-offset-2  col-sm-2 control-label"><spring:message code="screen.init.password.step1.content.originpwd"/></label>
							  	<div class="col-sm-4">
							    	<input type="password" class="form-control"  name="originpwd"   id="origin_password">
							    </div>
							     <div class="col-sm-4 showwarninfo">
							  		<label for="initpwdwarn" id="originpwd_warn_info"></label>
							  	</div>
						  </div>
						  <div class="form-group">
						  		<label for="inputNewPwd" class="col-sm-offset-2  col-sm-2 control-label"><spring:message code="screen.init.password.step1.content.newpwd"/></label>
							  	<div class="col-sm-4">
							    	<input type="password" class="form-control" name="newpwd"  id="new_password">
							    </div>
							     <div class="col-sm-4 showwarninfo">
							  		<label for="initpwdwarn" id="newpwd_warn_info"></label>
							  	</div>
						  </div>
						  <div class="form-group">
						  		<label for="reInputPwd" class="col-sm-offset-2  col-sm-2 control-label"><spring:message code="screen.init.password.step1.content.renewpwd"/></label>
							  	<div class="col-sm-4">
							    	<input type="password" class="form-control" name="renewpwd"  id="re_new_password">
							    </div>
							    <div class="col-sm-4 showwarninfo">
							  		<label for="initpwdwarn" id="renewpwd_warn_info"></label>
							  	</div>
						  </div>
						  <div class="form-group">
						  	<label for="inputVerifyCode" class="col-sm-offset-2  col-sm-2 control-label"><spring:message code="screen.init.password.step1.content.verifycode"/></label>
						  	<div class="col-sm-2">
						    	<input type="text" class="form-control"  name="verify_code"  id="init_pwd_tverfynotice">
						  	</div>
						  	<div class="col-sm-2">
						  		<div class="captcha-img">
						    		<img alt="picture" src="<%=path %>/uniauth/captcha" title="<spring:message code="screen.init.password.step1.content.verifycode.title"/>"  id="init_pwd_verfypic" >
						    		<button type="button" class="btn btn-primary glyphicon glyphicon-refresh" id="init_pwd_refreshverfypic"></button>
						    	</div>
						  	</div>
						  	 <div class="col-sm-4 showwarninfo">
							  		<label for="initpwdwarn" id="verifycode_warn_info"></label>
							  	</div>
						  </div>
						  <div class="form-group">
						  	<div class="col-sm-offset-4 col-sm-4">
						  		<button type="button" id="btn_init_pwd_process" class="btn btn-wide btn-primary btnstep cursordefault" disabled="disabled"><spring:message code="screen.init.password.step1.confirm.title"/></button>
						  	</div>
						  </div>
					  <div class="form-group">
					  	<div class="col-sm-offset-4 col-sm-8 showwarninfo">
					  		<label for="initpwdwarn" id="init_pwd_warn_info"></label>
					  	</div>
				  </div>
			</form>
		</div>
	</div>
</div>
<jsp:directive.include file="/WEB-INF/view/jsp/dianrong/common/bottom.jsp" />