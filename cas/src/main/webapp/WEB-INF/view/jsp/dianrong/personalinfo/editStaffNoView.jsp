<jsp:directive.include file="/WEB-INF/view/jsp/dianrong/common/top.jsp" />
<!-- edit-staff-no-dialog -->
<div class="modal fade text-left" id="modal_edit_staff_no" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close" id="modal_edit_staff_no_close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel"><spring:message code="screen.notice.staff.no.requires.modal.title"/></h4>
      </div>
      <div class="modal-body">
        <div class="alert alert-info" role="alert"><spring:message code="screen.notice.staff.no.requires.info"/></div>
        <form>
          <input type="hidden" value = '<c:out value="${service}" escapeXml="true"/>' id="staff_no_target_service"/>
          <div class="form-group">
            <input type="text" class="form-control" id="staff_no_input" placeholder='<spring:message code="screen.notice.staff.no" />'>
          </div>
          <div class="alert alert-success" role="alert" id="edit_staff_no_success" style="display:none"></div>
          <div class="alert alert-danger" role="alert" id="edit_staff_no_warn" style="display:none">...</div>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal" id="edit_staff_no_cancel"><spring:message code="screen.notice.staff.no.cancel" /></button>
        <button type="button" class="btn btn-primary" id="edit_staff_no_confirm" style="cursor: not-allowed"><spring:message code="screen.notice.staff.no.yes" /></button>
      </div>
    </div>
  </div>
</div>
<jsp:directive.include file="/WEB-INF/view/jsp/dianrong/common/bottom.jsp" />
<script type="text/javascript" src="<%=path %>/js/staff-no-edit.js?v=<%=version %>" ></script>
