package com.dianrong.common.uniauth.cas.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.MessageSource;
import org.springframework.web.servlet.ModelAndView;

import com.dianrong.common.uniauth.cas.service.UserInfoManageService;
import com.dianrong.common.uniauth.cas.util.FirstPageUrlProcessUtil;
import com.dianrong.common.uniauth.cas.util.UniBundleUtil;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.util.StringUtil;

/**
 * . 初始化密码的处理controller
 * 
 * @author wanglin
 *
 */
public class InitPasswordController extends AbstractBaseController {
    /**
     * . 用户信息管理服务
     */
    private UserInfoManageService userInfoManageService;

    private MessageSource messageSource;

    public MessageSource getMessageSource() {
        return messageSource;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * . 进入第一页开始验证 清空所有的验证缓存
     */
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 获取执行的请求内容
        String initStep = getParamFromRequest(request, AppConstants.PWDFORGET_DISPATCHER_STEP_KEY);
        if (StringUtil.strIsNullOrEmpty(initStep) || "1".equals(initStep) || "0".equals(initStep)) {
            String method = request.getMethod();
            if ("post".equalsIgnoreCase(method)) {
                processInitPassword(request, response);
            }
            // 不支持get方式的操作
        } else {
            // 有对应参数的 跳转到修改成功页面
            return toStep2(request, response);
        }
        // 不处理
        return null;
    }

    /**
     * . process step 2
     * 
     * @param request request
     * @param response response
     * @return
     * @throws Exception
     */
    private ModelAndView toStep2(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return getPwdForgetStep2Page();
    }

    /**
     * . 用于处理post请求的初始化密码操作
     * 
     * @param request
     * @param response
     */
    private void processInitPassword(HttpServletRequest request, HttpServletResponse response) {
        // 刷新session中的回调url
        FirstPageUrlProcessUtil.refreshLoginContextInsession(request);

        // 验证验证码
        String req_verifycode = getParamFromRequest(request, "verify_code");
        if (StringUtil.strIsNullOrEmpty(req_verifycode)) {
            setResponseResultJson(response, "1", UniBundleUtil.getMsg(messageSource, "inipassword.controller.initpassword.captcha.empty"));
            return;
        }
        // 判断验证码
        String captcha = getValFromSession(request.getSession(), AppConstants.CAS_CAPTCHA_SESSION_KEY, String.class);
        if (!req_verifycode.equals(captcha)) {
            // 验证码不对
            setResponseResultJson(response, "1", UniBundleUtil.getMsg(messageSource, "inipassword.controller.initpassword.captcha.wrong"));
            return;
        }

        // 获取请求的参数信息
        String req_account = getParamFromRequest(request, "email");
        String req_originpwd = getParamFromRequest(request, "originpwd");
        String req_newpwd = getParamFromRequest(request, "newpwd");

        // 后端验证
        if (StringUtil.strIsNullOrEmpty(req_account)) {
            setResponseResultJson(response, "1", UniBundleUtil.getMsg(messageSource, "inipassword.controller.initpassword.userpassword.empty"));
            return;
        }
        if (StringUtil.strIsNullOrEmpty(req_originpwd)) {
            setResponseResultJson(response, "1", UniBundleUtil.getMsg(messageSource, "inipassword.controller.initpassword.originpassword.empty"));
            return;
        }
        if (StringUtil.strIsNullOrEmpty(req_newpwd)) {
            setResponseResultJson(response, "1", UniBundleUtil.getMsg(messageSource, "inipassword.controller.initpassword.newpassword.notempty"));
            return;
        }

        // 根据邮箱获取用户原始信息
        UserDto userinfo = null;
        try {
            userinfo = userInfoManageService.getUserDetailInfo(req_account);
        } catch (Exception ex) {
            setResponseResultJson(response, "1", StringUtil.getExceptionSimpleMessage(ex.getMessage()));
            return;
        }

        // 用户不存在
        if (userinfo == null) {
            setResponseResultJson(response, "1", UniBundleUtil.getMsg(messageSource, "inipassword.controller.initpassword.user.not.exsist", req_account));
            return;
        }

        // 修改密码
        try {
            userInfoManageService.updateUserPassword(userinfo.getId(), req_newpwd, req_originpwd);
        } catch (Exception ex) {
            setResponseResultJson(response, "1", StringUtil.getExceptionSimpleMessage(ex.getMessage()));
            return;
        }

        if (request.getParameter("expire") != null) {
            request.getSession().setAttribute(AppConstants.PWDEXPIRE_SESSION_KEY, request.getParameter("expire"));
        }
        // 返回修改密码成功
        setResponseResultJson(response, "0");
    }

    /**
     * . get initpwd step 2 page
     * 
     * @return step 2 page
     */
    private ModelAndView getPwdForgetStep2Page() {
        return new ModelAndView("dianrong/initpwd/initPwdSuccess");
    }

    // service getter and setter
    public UserInfoManageService getUserInfoManageService() {
        return userInfoManageService;
    }

    public void setUserInfoManageService(UserInfoManageService userInfoManageService) {
        this.userInfoManageService = userInfoManageService;
    }
}
