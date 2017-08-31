 <jsp:directive.include file="/WEB-INF/view/jsp/dianrong/common/top.jsp" />
 
 <div class="container find-pwd-container">
    <div class="find-pwd-content ng-scope" style="height: 620px">
        <header class="find-pwd">
            <a href="<%=path %>/login"><spring:message code="screen.password.reset.step.backtofirstpage" /></a>
            &gt;<spring:message code="screen.personal.info.update.password" />
        </header>
        <div class="common-wizard infoedit paddingtop10">
            <div id="update_password_content_div">
                <form class="form-horizontal">
			      <div class="form-group">
			        <label for="update_password_identity" class="col-md-offset-1 col-md-2 control-label"><spring:message code="screen.personal.info.password.update.identity"/></label>
			        <div class="col-md-7">
			          <input type="text" class="form-control update-password-text-check" id="update_password_identity" placeholder="<spring:message code="screen.personal.info.password.update.identity"/>">
			        </div>
			      </div>
			      <div class="form-group">
			        <label for="update_password_original_password" class="col-md-offset-1 col-md-2 control-label"><spring:message code="screen.personal.info.password.update.original.password"/></label>
			        <div class="col-md-7">
			          <input type="password" class="form-control  update-password-text-check" id="update_password_original_password" placeholder="<spring:message code="screen.personal.info.password.update.original.password"/>">
			        </div>
			      </div>
			      <div class="form-group">
			        <label for="update_password_new_password" class="col-md-offset-1 col-md-2 control-label"><spring:message code="screen.personal.info.password.update.new.password"/></label>
			        <div class="col-md-7">
			          <input type="password" class="form-control update-password-text-check" id="update_password_new_password" placeholder="<spring:message code="screen.personal.info.password.update.new.password"/>">
			        </div>
			      </div>
			      <div class="form-group">
			        <label for="update_password_confirm_password" class="col-md-offset-1 col-md-2 control-label"><spring:message code="screen.personal.info.password.update.confirm.password"/></label>
			        <div class="col-md-7">
			          <input type="password" class="form-control  update-password-text-check" id="update_password_confirm_password" placeholder="<spring:message code="screen.personal.info.password.update.confirm.password"/>">
			        </div>
			      </div>
			      <div class="form-group">
			        <label for="input_captcha" class="col-md-offset-1 col-md-2 control-label"><spring:message code="screen.personal.info.password.update.captcha"/></label>
			        <div class="col-md-7" style="padding: 0px">
			            <div class="col-md-6" style="padding-right: 0px">
			                <input type="text" class="form-control  update-password-text-check" id="input_captcha" placeholder="<spring:message code="screen.personal.info.password.update.captcha"/>">
			            </div>
			            <div class="col-sm-6">
			                <div class="captcha-img">
			                    <img alt="picture" src="<%=path %>/uniauth/verification/captcha" id="captcha_pic" title="<spring:message code="screen.personal.info.password.update.refresh.captcha"/>" 
			                    style="margin: 0 auto;margin-top: 1px;margin-left: -20px;border: 1px #F00;width: 100%;height: 42px;"/>
			                </div>
			            </div>
			        </div>
			      </div>
			      <div class="form-group">
			        <div class="col-sm-offset-3 col-sm-7">
			          <button type="button" class="btn btn-primary" disabled="disabled" id="update_password_confirm_btn" style="width: 100%" title="<spring:message code="screen.personal.info.password.update.confirm"/>">
			             <spring:message code="screen.personal.info.password.update.confirm"/>
			          </button>
			        </div>
			      </div>
			    </form>
			    </div>
			    <div id="password_update_success_div" class="hidden-element" style="padding-top: 140px">
			         <div class="row">
                      <div class="col-sm-offset-5 col-sm-3 reset-success-notice">
                        <img src="<%=path %>/images/pwdreset/icon-reset-success.png" class="img-responsive check-ok-icon" alt="Responsive image">
                      </div>
                    </div>
                    <div class="row">
                      <div class="col-sm-offset-4 col-sm-4 h6">
                        <spring:message code="screen.password.reset.step3.success.notice"/>
                                <a href="<%=path %>/login"><spring:message code="screen.password.reset.step3.success.login"/></a>
                      </div>
                    </div>
			    </div>
            </div>
        </div>
</div>
<jsp:directive.include file="/WEB-INF/view/jsp/dianrong/common/bottom.jsp" />
<jsp:directive.include file="/WEB-INF/view/jsp/dianrong/personalinfo/js.jsp" />