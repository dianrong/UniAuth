package com.dianrong.common.uniauth.cas.action;

import javax.servlet.http.HttpServletRequest;

import org.jasig.cas.authentication.principal.Principal;
import org.jasig.cas.web.flow.GenericSuccessViewAction;
import org.jasig.cas.web.support.WebUtils;
import org.springframework.webflow.action.AbstractAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import com.dianrong.common.uniauth.cas.service.UserInfoManageService;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.enm.CasProtocal;
import com.dianrong.common.uniauth.common.util.StringUtil;

/**
 * 处理用户信息的管理分之处理
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
	 * . 更新处理结果
	 */
	private static final String UPDATE_USER_INFO = "update";

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
		Long tenancyId = (Long)principal.getAttributes().get(CasProtocal.DianRongCas.getTenancyIdName());
		
		String requestMethod = StringUtil.getObjectStr(context.getFlowScope().get(AppConstants.CAS_USERINFO_MANAGE_FLOW_REQUEST_METHOD_TYPE_KEY));
		// 简单的以post和其他请求方式来区分去请求数据还是更新数据
		if ("post".equalsIgnoreCase(requestMethod)) {
			// 到更新数据的action去处理
			return updateUserInfo(context, account);
		} else {
			return queryUserInfo(context, account, tenancyId);
		}
	}

	/**
	 * . query user info
	 * 
	 * @param context
	 *            context
	 * @return result
	 * @throws Exception
	 */
	private Event queryUserInfo(final RequestContext context, String account, Long tenancyId) throws Exception {
		UserDto userInfo = null;
		try {
			// 调服务获取用户信息
			userInfo = userInfoManageService.getUserDetailInfo(account, tenancyId);
		} catch (Exception ex) {
			// 将异常信息仍到前端去
			context.getFlowScope().put(AppConstants.CAS_USERINFO_MANAGE_OPERATE_ERRORMSG_TAG, ex.getMessage());
			return result(NOTFOUND_USER_INFO);
		}
		if (userInfo == null) {
			context.getFlowScope().put(AppConstants.CAS_USERINFO_MANAGE_OPERATE_ERRORMSG_TAG, "当前登录用户信息没找到");
			return result(NOTFOUND_USER_INFO);
		}

		// 将信息绑定到前端使用
		context.getFlowScope().put("userinfo", userInfo);
		context.getFlowScope().put("userAccount", account);
		return result(QUERY_USER_INFO);
	}

	/**
	 * . update user info
	 * 
	 * @param context
	 *            context
	 * @return result
	 * @throws Exception
	 */
	private Event updateUserInfo(final RequestContext context, String account) throws Exception {
		// 从request请求中获取请求的数据
		HttpServletRequest request = WebUtils.getHttpServletRequest(context);
		String id = request.getParameter(AppConstants.CAS_USERINFO_MANAGE_USER_ID_TAG);
		if (StringUtil.strIsNullOrEmpty(id)) {
			context.getFlowScope().put(AppConstants.CAS_USERINFO_MANAGE_OPERATE_ERRORMSG_TAG, "获取用户信息id异常");
			return result(NOTFOUND_USER_INFO);
		}

		String updateType = request.getParameter(AppConstants.CAS_USERINFO_MANAGE_UPDATE_METHOD_TYPE_TAG);

		// 走更新密码的分支
		if ("password".equals(updateType)) {
			String password = request.getParameter(AppConstants.CAS_USERINFO_MANGE_UPDATE_PASSWORD_TAG);
			String origin_password = request.getParameter(AppConstants.CAS_USERINFO_MANGE_UPDATE_ORIGINPASSWORD_TAG);
			try {
				userInfoManageService.updateUserPassword(Long.parseLong(id), password, origin_password);
			} catch (Exception ex) {
				context.getFlowScope().put(AppConstants.CAS_USERINFO_MANAGE_OPERATE_ERRORMSG_TAG,
						failJson(StringUtil.getExceptionSimpleMessage(ex.getMessage())));
				return result(UPDATE_USER_INFO);
			}
		} else {
			// 更新基本内容
			String email = request.getParameter(AppConstants.CAS_USERINFO_MANAGE_UPDATE_EMAIL_TAG);
			String phone = request.getParameter(AppConstants.CAS_USERINFO_MANAGE_UPDATE_PHONE_TAG);
			String name = request.getParameter(AppConstants.CAS_USERINFO_MANAGE_UPDATE_NAME_TAG);
			try {
				userInfoManageService.updateUserInfo(Long.parseLong(id), email, name, phone);
			} catch (Exception ex) {
				context.getFlowScope().put(AppConstants.CAS_USERINFO_MANAGE_OPERATE_ERRORMSG_TAG,
						failJson(StringUtil.getExceptionSimpleMessage(ex.getMessage())));
				return result(UPDATE_USER_INFO);

			}
		}
		context.getFlowScope().put(AppConstants.CAS_USERINFO_MANAGE_OPERATE_ERRORMSG_TAG, successJson("success"));
		// 返回json字符串表
		return result(UPDATE_USER_INFO);
	}
	
	/**
	 * . 成功的返回json
	 * 
	 * @param msg
	 *            提示信息
	 * @return json字符串
	 */
	private String successJson(String msg) {
		return generateResultJson("1", msg);
	}

	/**
	 * . 异常的返回json
	 * 
	 * @param code
	 *            状态吗
	 * @param msg
	 *            提示信息
	 * @return json字符串
	 */
	private String failJson(String msg) {
		return generateResultJson("2", msg);
	}

	/**
	 * . 生成返回页面的提示信息json
	 * 
	 * @param code
	 *            状态吗
	 * @param msg
	 *            提示信息
	 * @return json字符串
	 */
	private String generateResultJson(String code, String msg) {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("\"code\":");
		sb.append("\"" + code + "\"");
		sb.append(",");
		sb.append("\"msg\":");
		sb.append("\"" + msg + "\"");
		sb.append("}");
		return sb.toString();
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