package com.dianrong.common.uniauth.cas.action;

import com.dianrong.common.uniauth.cas.service.UserInfoManageService;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.enm.CasProtocal;
import com.dianrong.common.uniauth.common.util.StringUtil;
import org.jasig.cas.authentication.principal.Principal;
import org.jasig.cas.web.flow.GenericSuccessViewAction;
import org.jasig.cas.web.support.WebUtils;
import org.springframework.webflow.action.AbstractAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

/**
 * 处理用户信息的管理分之处理.
 */
public class PersonalInfoManageAction extends AbstractAction {

  /**
   * . 用户信息管理服务
   */
  private UserInfoManageService userInfoManageService;

  /**
   * . 获取用户邮箱信息的服务
   */
  private GenericSuccessViewAction genericSuccessViewAction;

  /**
   * . 查询处理结果
   */
  private static final String QUERY_USER_INFO = "query";

  /**
   * . 找不到对应的用户信息
   */
  private static final String NOTFOUND_USER_INFO = "notfound";

  /**
   * TGT does not exist event ID={@value}.
   **/
  private static final String NOT_EXISTS = "notExists";

  @Override
  protected Event doExecute(final RequestContext context) throws Exception {
    // 获取用户的邮箱信息
    final String tgtId = WebUtils.getTicketGrantingTicketId(context);
    if (StringUtil.strIsNullOrEmpty(tgtId)) {
      return result(NOT_EXISTS);
    }

    Principal principal = genericSuccessViewAction.getAuthenticationPrincipal(tgtId);
    if (principal == null || StringUtil.strIsNullOrEmpty(principal.getId())) {
      return result(NOT_EXISTS);
    }

    // 获取用户账号
    String account = principal.getId();
    Long tenancyId = (Long) principal.getAttributes()
        .get(CasProtocal.DianRongCas.getTenancyIdName());
    return queryUserInfo(context, account, tenancyId);
  }

  /**
   * . query user info
   *
   * @param context context
   * @return result
   */
  private Event queryUserInfo(final RequestContext context, String account, Long tenancyId)
      throws Exception {
    UserDto userInfo = null;
    try {
      // 调服务获取用户信息
      userInfo = userInfoManageService.getUserDetailInfo(account, tenancyId);
    } catch (Exception ex) {
      // 将异常信息仍到前端去
      context.getFlowScope()
          .put(AppConstants.CAS_USERINFO_MANAGE_OPERATE_ERRORMSG_TAG, ex.getMessage());
      return result(NOTFOUND_USER_INFO);
    }
    if (userInfo == null) {
      context.getFlowScope()
          .put(AppConstants.CAS_USERINFO_MANAGE_OPERATE_ERRORMSG_TAG, "当前登录用户信息没找到");
      return result(NOTFOUND_USER_INFO);
    }

    // 将信息绑定到前端使用
    context.getFlowScope().put("userinfo", userInfo);
    context.getFlowScope().put("userAccount", account);
    return result(QUERY_USER_INFO);
  }

  public UserInfoManageService getUserInfoManageService() {
    return userInfoManageService;
  }

  public void setUserInfoManageService(UserInfoManageService userInfoManageService) {
    this.userInfoManageService = userInfoManageService;
  }

  public GenericSuccessViewAction getGenericSuccessViewAction() {
    return genericSuccessViewAction;
  }

  public void setGenericSuccessViewAction(GenericSuccessViewAction genericSuccessViewAction) {
    this.genericSuccessViewAction = genericSuccessViewAction;
  }
}
