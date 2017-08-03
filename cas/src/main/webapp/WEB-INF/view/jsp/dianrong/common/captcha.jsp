<%
	String captcha_validate_path = request.getContextPath(); 
    String captcha_validate_version = (String)application.getAttribute("cas_v");
%>
<div class="modal fade text-left" tabindex="-1" role="dialog" id="captcha_validate_modal" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close" id="captcha_validate_close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title"><spring:message code="screen.captcha.validate.modal.title"/></h4>
      </div>
      <div class="modal-body">
        <form class="form-horizontal">
                <div class="form-group">
                        <div class="col-sm-offset-2 col-sm-4">
                            <div class="captcha-img">
                                <img alt="captcha" src="<%=captcha_validate_path %>/uniauth/verification/captcha" title="captcha" id="captcha_validate_modal_captcha" >
                            </div>
                        </div>
                        <div class=" col-sm-4">
                            <input type="text" class="form-control" placeholder="<spring:message code="screen.captcha.placeholder"/>" id="captcha_validate_captcha">
                        </div>
                        
                </div>
            </form>
            <div id="captcha_validate_error" style="color: red; text-align: center;">
            </div>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal" id="captcha_validate_cancel"><spring:message code="screen.btn.cancel"/></button>
        <button type="button" class="btn btn-primary" id="captcha_validate_ok" disabled="disabled"><spring:message code="screen.btn.ok"/></button>
      </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div>
<script type="text/javascript" src="<%=captcha_validate_path %>/js/captcha-validate.js?v=<%=captcha_validate_version %>" ></script>