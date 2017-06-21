 <jsp:directive.include file="/WEB-INF/view/jsp/dianrong/common/top.jsp" />

<div class="container find-pwd-container">
	<div class="find-pwd-content ng-scope">
		<header class="find-pwd">
			<a href="<%=path %>/login"><spring:message code="screen.password.reset.step.backtofirstpage" /></a>
			&gt;<spring:message code="screen.personal.info.edit.title" />
		</header>
		<div class="common-wizard infoedit paddingtop10">
				<input type="hidden" id="hidden_userinfo_keyid" value="${fn:escapeXml(userinfo.id)}">
				<form class="form-horizontal">
					<div class="form-group">
						<label for="inputName" class="col-sm-3 control-label"><spring:message code="screen.personal.info.edit.label.name" /></label>
						<label  class="col-sm-5 control-label infolabel  info_name_show" id="name_label">${fn:escapeXml(userinfo.name)}</label>
						<div class="col-sm-1 info_name_show">
							<button type="button" id="go_update_name_btn" class="btn btn-success">
								<span class="glyphicon glyphicon-user"></span><spring:message code="screen.personal.info.edit.update.name" />
							</button>
						</div>
						
						<div class="col-sm-5 info_name_edit hidden-element">
							 <input type="text" class="form-control  col-sm-5" id="update_name_new_name" placeholder="<spring:message code="screen.personal.info.edit.label.name"/>"
								name="name" value="${fn:escapeXml(userinfo.name)}">
						</div>
						<div class="col-sm-2 info_name_edit hidden-element">
							<button type="button" id="update_name_confirm_btn" class="btn btn-primary">
								<span class="glyphicon glyphicon-ok"></span><spring:message code="screen.personal.info.edit.editok" />
							</button>
						</div>
						<div class="col-sm-1 info_name_edit hidden-element" style="padding-left: 0px">
							<button type="button" id="update_name_cancel_btn" class="btn btn-default">
								<span class="glyphicon glyphicon-remove"></span><spring:message code="screen.personal.info.edit.canceledit" />
							</button>
						</div>
					</div>
				
					<div class="horizontal form-group">
						<div class="col-sm-offset-2 col-sm-10 row-line" ></div>
					</div>
					
					<!-- email -->
					<div class="form-group">
						<label for="email_label" class="col-sm-3 control-label"><spring:message code="screen.personal.info.edit.label.email" /></label>
						<label id="email_label" class="col-sm-5 control-label infolabel">${fn:escapeXml(userinfo.email)}</label>
						<div class="col-sm-1">
							<button type="button" id="go_update_email_btn" class="btn btn-success">
								<span class="glyphicon glyphicon-envelope"></span><spring:message code="screen.personal.info.edit.update.email"/>
							</button>
						</div>
					</div>
					
					<!-- phone -->
					<div class="form-group">
						<label for="phone_label" class="col-sm-3 control-label"><spring:message code="screen.personal.info.edit.label.phone" /></label>
						<label id="phone_label" class="col-sm-5 control-label infolabel">${fn:escapeXml(userinfo.phone)}</label>
						<div class="col-sm-1">
							<button type="button" id="go_update_phone_btn" class="btn btn-success">
								<span class="glyphicon glyphicon-earphone"></span><spring:message code="screen.personal.info.edit.update.phone"/>
							</button>
						</div>
					</div>
					
					<!-- password -->
					<div class="form-group">
						<label for="inputPassword" class="col-sm-3  control-label"><spring:message code="screen.personal.info.edit.label.password" /></label>
						<div class="col-sm-5">
							 <input type="password" class="form-control" id="inputPassword" name="password"  value="***********"  disabled="disabled">
						</div>
						<div class="col-sm-1">
							<button type="button" id="go_update_password_btn" class="btn btn-success">
								<span class="glyphicon glyphicon-asterisk"></span><spring:message code="screen.personal.info.edit.update.password"/>
							</button>
						</div>
					</div>
				</form>
			</div>
		</div>
</div>

<div class="hidden-element" role="alert" id="window_notice_div">
  <div id="top_show_info" class="showinfo"></div>
</div>

<!-- show logout modal -->


<!-- update email -->
<div class="modal fade text-left" id="modal-new-email" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel"><spring:message code="screen.personal.info.edit.updateemail.modal.title"/></h4>
      </div>
      <div class="modal-body">
      		<form action="" class="form-horizontal">
      			<input type="hidden"  value="${fn:escapeXml(userinfo.email)}"  id="update_email_hidden_input" >
					<div class="form-group">
							<label  for="update-email-original-email" class="col-sm-3 control-label"><spring:message code="screen.personal.info.edit.label.email" /></label>
							<label  class="col-sm-8 control-label infolabel">${fn:escapeXml(userinfo.email)}</label>
					</div>
					<div class="form-group">
							<label  for="update-email-original-email" class="col-sm-3 control-label"><spring:message code="screen.personal.info.edit.label.new.email" /></label>
							<div class="col-sm-8">
								<input type="text" class="form-control" id="update_email_new_email">
							</div>
					</div>
					<div class="form-group">
							<label for="update-email-captcha" class="col-sm-3 control-label"><spring:message code="screen.personal.info.edit.updateemail.label.captcha" /></label>
							<div class="col-sm-4">
								<input type="text" class="form-control" id="update_email_captcha"
								placeholder="<spring:message code="screen.personal.info.edit.updateemail.label.captcha"/>">
							</div>
							<button type="button" class="btn btn-primary col-sm-2"  id="get_email_captcha">
								<spring:message code="screen.personal.info.edit.updateemail.btn.getcaptcha" />
							</button>
							<div id="email_verify_code_div"></div>
					</div>
					<div class="form-group">
							<div class="showwarninfo" style="text-align: center;">
									<label for="warninfo"  id="update_email_warninfo">
									</label>
							</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-3 col-sm-6">
							<button type="button" class="btn btn-success btn-wide"  id="update_email_btn_confirm" disabled="disabled">
								<spring:message code="screen.personal.info.edit.updateemail.btn.confirm"/>
							</button>
						</div>
					</div>
			</form>
      </div>
    </div>
  </div>
</div>

<!-- update phone -->
<div class="modal fade text-left" id="modal-new-phone" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel"><spring:message code="screen.personal.info.edit.updatephone.modal.title"/></h4>
      </div>
      <div class="modal-body">
      		<form action="" class="form-horizontal">
      				<input type="hidden"  value="${fn:escapeXml(userinfo.phone)}"  id="update_phone_hidden_input" >
						<div class="form-group">
								<label  for="update-phone-original-phone" class="col-sm-3 control-label"><spring:message code="screen.personal.info.edit.label.phone" /></label>
								<label  class="col-sm-8 control-label infolabel">${fn:escapeXml(userinfo.phone)}</label>
						</div>
						<div class="form-group">
								<label  for="update-phone-original-phone" class="col-sm-3 control-label"><spring:message code="screen.personal.info.edit.label.new.phone" /></label>
								<div class="col-sm-8">
									<input type="text" class="form-control" id="update_phone_new_phone">
								</div>
						</div>
						<div class="form-group">
								<label for="update-phone-captcha" class="col-sm-3 control-label"><spring:message code="screen.personal.info.edit.updatephone.label.captcha" /></label>
								<div class="col-sm-4">
									<input type="text" class="form-control" id="update_phone_captcha"
									placeholder="<spring:message code="screen.personal.info.edit.updatephone.label.captcha"/>">
								</div>
								<button type="button" class="btn btn-primary col-sm-2"  id="get_phone_captcha">
									<spring:message code="screen.personal.info.edit.updatephone.btn.getcaptcha" />
								</button>
								<div id="phone_verify_code_div"></div>
						</div>
						<div class="form-group">
								<div class="showwarninfo" style="text-align: center;">
										<label for="warninfo"  id="update_phone_warninfo">
										</label>
								</div>
						</div>
						<div class="form-group">
							<div class="col-sm-offset-3 col-sm-6">
								<button type="button" class="btn btn-success btn-wide"  id="update_phone_btn_confirm" disabled="disabled">
									<spring:message code="screen.personal.info.edit.updatephone.btn.confirm"/>
								</button>
							</div>
						</div>
			</form>
      </div>
    </div>
  </div>
</div>

<!-- update password -->
<div class="modal fade text-left" id="modal-new-password" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel"><spring:message code="screen.personal.info.edit.updatepwd.modal.title"/></h4>
      </div>
      <div class="modal-body">
      		<form action="" class="form-horizontal">
					<div class="form-group">
						<div class="col-sm-offset-1 col-sm-10">
							<input type="password" class="form-control" id="orign_password"
								placeholder="<spring:message code="screen.personal.info.edit.updatepwd.modal.content.originpwd"/>"
								name="orign_password">
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-1 col-sm-10">
							<input type="password" class="form-control" id="password"
								placeholder="<spring:message code="screen.personal.info.edit.updatepwd.modal.content.newpwd"/>"
								name="password">
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-1 col-sm-10">
							<input type="password" class="form-control" id="repassword"
								placeholder="<spring:message code="screen.personal.info.edit.updatepwd.modal.content.rnewpwd"/>"
								name="repassword">
						</div>
					</div>
					<div class="form-group hiddenbtn">
						<div class="col-sm-offset-1 col-sm-11 showwarninfo">
							<label for="warninfo" id="update_pwd_warninfo"></label>
						</div>
					</div>
				</form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="screen.personal.info.edit.canceledit" /></button>
        <button type="button" class="btn btn-primary cursordefault" id="modal_new_password_ok_btn" disabled="disabled"><spring:message code="screen.personal.info.edit.editok" /></button>
      </div>
    </div>
  </div>
</div>

<jsp:directive.include
	file="/WEB-INF/view/jsp/dianrong/common/bottom.jsp" />