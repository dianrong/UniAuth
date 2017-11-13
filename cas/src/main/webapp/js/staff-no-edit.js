// 个人信息修改页面
$(function() {
	var confirmStaffNoUrl = context_path+"/uniauth/userinfo/staff-no";
	//初始化函数
	var init = function(){
		// 弹出提示框
		$('#modal_edit_staff_no').modal({
		  backdrop: 'static',
		  keyboard: false
		});

		$('#staff_no_input').keyup(edit_staff_no_confirm_show);
		$('#edit_staff_no_cancel').click(edit_staff_no_cancel);
		$('#modal_edit_staff_no_close').click(edit_staff_no_cancel);
		$('#edit_staff_no_confirm').click(edit_staff_no_confirm);
	}

  var edit_staff_no_cancel = function() {
    var service = $('#staff_no_target_service').val();
    var redirectUrl = context_path + "/login?service=" + service;
    window.location.href = redirectUrl;
  }

	// 确认员工编号
	var edit_staff_no_confirm = function() {
	  var staffNo = $('#staff_no_input').val();
	  if (!testStaffNo(staffNo)) {
	    return;
	  }
		var data = {
      staffNo: staffNo
		};
		var success_div = $('#edit_staff_no_success');
    var warn_div = $('#edit_staff_no_warn');
    success_div.css('display','none');
    warn_div.css('display','none');
		$.ajax({  
      type : "POST",
      url : confirmStaffNoUrl,
      data : data,
      dataType : 'json',
      success : function(data) {
       if(data.info) {
         warn_div.css('display','block');
         warn_div.html(data.info[0].msg);
       } else {
         // 防止重复点击
         $('#edit_staff_no_confirm').attr("disabled", "disabled");
         var msg = $.i18n.prop('userinfo.persist.staff.no.success');
         success_div.css('display','block');
         success_div.html(msg);
         // 跳转登录
         setTimeout(function(){
            edit_staff_no_cancel();
         }, 3000);
       }
      },
      error: function(jqXHR, textStatus, errorMsg){
        logOperation.error(errorMsg);
      }
  });
	}

  // 展示确认按钮
  var edit_staff_no_confirm_show = function() {
    var staffNo = $('#staff_no_input').val();
    if (testStaffNo(staffNo)) {
      $('#edit_staff_no_confirm').css('cursor','pointer');
    } else {
      $('#edit_staff_no_confirm').css('cursor','not-allowed');
    }
  }

  var testStaffNo = function(staffNo) {
    var filter  = /^[a-zA-Z0-9]+$/;
    return filter.test(staffNo);
  }
  //初始化操作
  init();
});
