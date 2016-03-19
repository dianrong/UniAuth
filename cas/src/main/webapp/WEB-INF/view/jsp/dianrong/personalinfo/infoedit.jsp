<jsp:directive.include file="/WEB-INF/view/jsp/dianrong/common/top.jsp" />

<div class="container find-pwd-container">
	<div class="find-pwd-content ng-scope">
		<header class="find-pwd">
			<%
				Object savedLoginContext = request.getSession().getAttribute("pwdg_savedLoginContext");
				if (savedLoginContext == null) {
			%>
			<a href="<%=path%>/login"><spring:message
					code="screen.password.reset.step.backtofirstpage" /></a>
			<%
				} else {
			%>
			<a href="${sessionScope.pwdg_savedLoginContext}"><spring:message
					code="screen.password.reset.step.backtofirstpage" /></a>
			<%
				}
			%>
			&gt;
			<spring:message code="screen.personal.info.edit.title" />
		</header>
		<div class="common-wizard infoedit paddingtop50">
				<input type="hidden" id="hidden_userinfo_keyid" value="${userinfo.id}">
				<form action="" class="form-horizontal">
					<div class="form-group paddingbottom30">
						<label for="inputPassword" class="col-md-3 control-label showlabel"><spring:message
								code="screen.personal.info.edit.label.password" /></label>
						<div class="col-md-6">
							<input type="password" class="form-control" id="inputPassword"
								placeholder="<spring:message code="screen.personal.info.edit.label.password"/>" name="password" value="xxxxxxx" disabled="disabled">
						</div>
						<div class="col-md-1">
							<button type="button" id="go_update_password_btn" class="btn btn-wide btn-info">
								<spring:message code="screen.personal.info.edit.update.password"/>
							</button>
						</div>
					</div>
					
					<div class="form-group horizontal">
						<div class="col-md-offset-2 col-md-10 row-line" ></div>
					</div>
				
					<div class="form-group paddingtop20">
						<label for="inputEmail" class="col-md-3 control-label"><spring:message
								code="screen.personal.info.edit.label.email" /></label> <label
							id="inputEmail" class="col-md-6 control-label infolabel">${userinfo.email}</label>
					</div>
					<div class="form-group">
						<label for="inputName" class="col-md-3 control-label"><spring:message
								code="screen.personal.info.edit.label.name" /></label>
						<div class="col-md-6">
							<input type="text" class="form-control" id="inputName"
								placeholder="<spring:message code="screen.personal.info.edit.label.name"/>"
								name="name" value="${userinfo.name}" disabled="disabled">
						</div>
					</div>
					<div class="form-group">
						<label for="inputPhone" class="col-md-3 control-label"><spring:message
								code="screen.personal.info.edit.label.phone" /></label>
						<div class="col-md-6">
							<input type="text" class="form-control" id="inputPhone"
								placeholder="<spring:message code="screen.personal.info.edit.label.phone"/>"
								name="phone" value="${userinfo.phone}" disabled="disabled">
						</div>
					</div>
					<div class="form-group paddingtop20">
						<div class="col-md-offset-3 col-md-3 mynoedit">
							<button type="button" id="go_user_info_edit_btn"
								class="btn btn-wide btn-primary">
								<spring:message code="screen.personal.info.edit.goedit" />
							</button>
						</div>
						<div class="col-md-offset-3 col-md-3 hiddenbtn myedit">
							<button type="button" id="user_info_edit_cancel_btn"
								class="btn btn-wide btn-default">
								<spring:message code="screen.personal.info.edit.canceledit" />
							</button>
						</div>
						<div class="col-md-3 hiddenbtn myedit">
							<button type="button" id="user_info_edit_ok_btn"
								class="btn btn-wide btn-primary">
								<spring:message code="screen.personal.info.edit.editok" />
							</button>
						</div>
					</div>
					<div class="form-group paddingtop10">
						<div class="col-md-offset-3 col-md-9 showwarninfo">
							<label for="warninfo" id="update_userinfo_warninfo"></label>
						</div>
					</div>
				</form>
			</div>
		</div>
</div>

<div class="top-browser hiddenbtn" role="alert" id="window_notice_div">
  <div id="top_show_info" class="paddingtop5"></div>
</div>

<!-- Modal -->
<div class="modal fade text-left" id="modal-confirm-edit" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title"><spring:message code="screen.personal.info.edit.confirm.modal.title" /></h4>
      </div>
      <div class="modal-body">
      		<p>
      			<spring:message code="screen.personal.info.edit.confirm.modal.content" />
      		</p>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code="screen.personal.info.edit.canceledit" /></button>
        <button type="button" class="btn btn-primary" id="modal_confirm_ok_btn"><spring:message code="screen.personal.info.edit.editok" /></button>
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
						<div class="col-md-offset-1 col-md-10">
							<input type="password" class="form-control" id="orign_password"
								placeholder="<spring:message code="screen.personal.info.edit.updatepwd.modal.content.originpwd"/>"
								name="orign_password">
						</div>
					</div>
					<div class="form-group">
						<div class="col-md-offset-1 col-md-10">
							<input type="password" class="form-control" id="password"
								placeholder="<spring:message code="screen.personal.info.edit.updatepwd.modal.content.newpwd"/>"
								name="password">
						</div>
					</div>
					<div class="form-group">
						<div class="col-md-offset-1 col-md-10">
							<input type="password" class="form-control" id="repassword"
								placeholder="<spring:message code="screen.personal.info.edit.updatepwd.modal.content.rnewpwd"/>"
								name="repassword">
						</div>
					</div>
					<div class="form-group hiddenbtn">
						<div class="col-md-offset-1 col-md-11 showwarninfo">
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