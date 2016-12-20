package com.dianrong.common.uniauth.cas.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.util.Assert;
import org.springframework.web.servlet.ModelAndView;

import com.dianrong.common.uniauth.cas.model.DateSessionObjModel;
import com.dianrong.common.uniauth.cas.service.ForgetPasswordService;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.util.StringUtil;

public class ForgetPasswordController extends AbstractBaseController {

    private ForgetPasswordService forgetPasswordService;

    /**
     * . 进入第一页开始验证 清空所有的验证缓存
     */
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // judge by step parameter
        // it's this controller which guarantees the security of reseting
        // password, so need store the status of each step, in session
        // in other words, you yourself maintain the status, and need check
        // previous step when performing next step
        // cannot invalidate session at the end of this process, because this
        // will affect cas server, they are sharing a same jsessionid.
        String step = getParamFromRequest(request, AppConstants.PWDFORGET_DISPATCHER_STEP_KEY);
        String method = request.getMethod();
        if (StringUtil.strIsNullOrEmpty(step) || (!"post".equalsIgnoreCase(method) && !("get").equalsIgnoreCase(method))) {
            return null;
        }

        // step0
        if ("0".equals(step)) {
            return toStep1(request, response);
        }

        // step1
        if ("1".equals(step)) {
            handleStep1(request, response);
        }

        // step2
        if ("2".equals(step)) {
            if ("get".equalsIgnoreCase(method)) {
                return toStep2(request, response);
            } else {
                handleStep2(request, response);
            }
        }

        // step3
        if ("3".equals(step)) {
            if ("get".equalsIgnoreCase(method)) {
                return toStep3(request, response);
            } else {
                handleStep3(request, response);
            }
        }

        // step4
        if ("4".equals(step)) {
            // 所有method全部提供一样实现
            return toStep4(request, response);
        }

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
    private ModelAndView toStep1(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        // clear session
        clearAllSessionVal(session);
        // refresh or set
        return getPwdForgetStep1Page();
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
        HttpSession session = request.getSession(false);
        // 必须要有邮箱
        String email = getValFromSession(session, AppConstants.PWDFORGET_MAIL_VAL_KEY, String.class);
        if (StringUtil.strIsNullOrEmpty(email)) {
            return getPwdForgetStep1Page();
        }
        return getPwdForgetStep2Page();
    }

    /**
     * . process step 3
     * 
     * @param request request
     * @param response response
     * @return
     * @throws Exception
     */
    private ModelAndView toStep3(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        // 必须要有邮箱
        String email = getValFromSession(session, AppConstants.PWDFORGET_MAIL_VAL_KEY, String.class);
        if (StringUtil.strIsNullOrEmpty(email)) {
            return getPwdForgetStep1Page();
        }

        // 必须要邮箱验证码通过
        Object verfyObj = getValFromSession(session, AppConstants.PWDFORGET_MAIL_VERIFY_EXPIRDATE_KEY);
        if (verfyObj != null && verfyObj instanceof DateSessionObjModel) {
            @SuppressWarnings("unchecked")
            DateSessionObjModel<String> tobj = (DateSessionObjModel<String>) verfyObj;
            if (!tobj.isExpired()) {
                // 进入第三步
                return getPwdForgetStep3Page();
            }
        }
        // 回到第二步
        return getPwdForgetStep2Page();
    }

    /**
     * . process step 4
     * 
     * @param request request
     * @param response response
     * @return
     * @throws Exception
     */
    private ModelAndView toStep4(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);

        String email = getValFromSession(session, AppConstants.PWDFORGET_MAIL_VAL_KEY, String.class);
        // clear session
        clearAllSessionVal(session);

        // 保存一下当前修改完成密码的邮箱地址到session中
        putValToSession(session, AppConstants.PWDFORGET_MAIL_VAL_KEY, StringUtil.strIsNullOrEmpty(email) ? "" : email);
        return getPwdForgetStep4Page();
    }

    /**
     * . process step 1
     * 
     * @param request request
     * @param response response
     * @return
     * @throws Exception
     */
    private void handleStep1(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        // 验证验证码
        String verifyCode = getParamFromRequest(request, AppConstants.PWDFORGET_PAGE_VERIFY_CODE_CLIENT_KEY);
        if (StringUtil.strIsNullOrEmpty(verifyCode)) {
            // 验证码为空了
            setResponseResultJson(response, "1");
            return;
        } else {
            // 判断验证码
            String captcha = getValFromSession(session, AppConstants.CAS_CAPTCHA_SESSION_KEY, String.class);
            if (!verifyCode.equals(captcha)) {
                // 验证码不对
                setResponseResultJson(response, "2");
                return;
            }
        }

        // 从request中获取邮箱
        String email = getParamFromRequest(request, AppConstants.PWDFORGET_MAIL_VAL_CLIENT_KEY);
        // 返回step 1
        if (StringUtil.strIsNullOrEmpty(email)) {
            // email 为空了
            setResponseResultJson(response, "3");
            return;
        }
        String tenancyCode = getParamFromRequest(request, AppConstants.REQUEST_PARAMETER_KEY_TENANCY_CODE);
        UserDto user = null;
        try {
            // 验证邮箱是否存在
        	user =  forgetPasswordService.checkUser(email, tenancyCode);
        	Assert.notNull(user, "can not find user, email :" +email + ", tenancyCode:" +tenancyCode);
        } catch (Exception ex) {
            // 验证用户失败了
            setResponseResultJson(response, "4", StringUtil.getExceptionSimpleMessage(ex.getMessage()));
            return;
        }

        // 往session里面放email
        putValToSession(session, AppConstants.PWDFORGET_MAIL_VAL_KEY, email);
        putValToSession(session, AppConstants.PWDFORGET_TENAYC_ID_KEY, user.getTenancyId());
        // 成功进入第二步
        setResponseResultJson(response, "0");
    }

    /**
     * . process step 2
     * 
     * @param request request
     * @param response response
     * @return
     * @throws Exception
     */
    private void handleStep2(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        // 必须要有邮箱
        String email = getValFromSession(session, AppConstants.PWDFORGET_MAIL_VAL_KEY, String.class);
        if (StringUtil.strIsNullOrEmpty(email)) {
            // 跳到第一步
            setResponseResultJson(response, "1");
            return;
        }

        // 获取验证码
        String verifyCode = getParamFromRequest(request, AppConstants.PWDFORGET_MAIL_VERIFY_CODE_CLIENT_KEY);
        if (StringUtil.strIsNullOrEmpty(verifyCode)) {
            // 验证码为空了
            setResponseResultJson(response, "2");
            return;
        }

        // 验证验证码
        Object verfyObj = getValFromSession(session, AppConstants.PWDFORGET_MAIL_VERIFY_CODE_KEY);
        if (verfyObj != null && verfyObj instanceof DateSessionObjModel) {
            @SuppressWarnings("unchecked")
            DateSessionObjModel<String> tobj = (DateSessionObjModel<String>) verfyObj;
            if (!tobj.isExpired()) {
                // 比较验证码是否一致
                if (verifyCode.equals(tobj.getContent())) {
                    // 验证通过 进入步骤3
                    // 将验证通过的结果放入session中
                    putValToSession(session, AppConstants.PWDFORGET_MAIL_VERIFY_EXPIRDATE_KEY, new DateSessionObjModel<String>("pass", AppConstants.PWDFORGET_MAIL_VERIFY_EXPIRDATE_MILLES));
                    // 成功进入第二步
                    setResponseResultJson(response, "0");
                    return;
                }

            }
        }
        // 验证没通过 需要继续验证
        setResponseResultJson(response, "3");
    }

    /**
     * . step3 process, update password
     * 
     * @param request request
     * @param response response
     * @throws Exception Exception
     */
    private void handleStep3(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        // 必须要有邮箱
        String email = getValFromSession(session, AppConstants.PWDFORGET_MAIL_VAL_KEY, String.class);
        Long tenancyId = getValFromSession(session, AppConstants.PWDFORGET_TENAYC_ID_KEY, Long.class);
        if (StringUtil.strIsNullOrEmpty(email)) {
            setResponseResultJson(response, "1");
            return;
        }

        // 新密码
        String newPwd = getParamFromRequest(request, AppConstants.PWDFORGET_NEW_PASSWORD_KEY);
        if (StringUtil.strIsNullOrEmpty(newPwd) || tenancyId == null) {
            setResponseResultJson(response, "2");
            return;
        }

        // 必须要邮箱验证码通过
        Object verfyObj = getValFromSession(session, AppConstants.PWDFORGET_MAIL_VERIFY_EXPIRDATE_KEY);
        if (verfyObj != null && verfyObj instanceof DateSessionObjModel) {
            @SuppressWarnings("unchecked")
            DateSessionObjModel<String> tobj = (DateSessionObjModel<String>) verfyObj;
            if (!tobj.isExpired()) {
                // 后端修改密码
                try {
                    forgetPasswordService.resetPassword(email, tenancyId, newPwd);
                } catch (Exception ex) {
                    setResponseResultJson(response, "3", StringUtil.getExceptionSimpleMessage(ex.getMessage()));
                    return;
                }
                // 修改成功
                setResponseResultJson(response, "0");
                return;
            }
        }
        // 验证已过期
        setResponseResultJson(response, "4");
    }

    /**
     * . get pwdforget step 1 page
     * 
     * @return step 1 page
     */
    private ModelAndView getPwdForgetStep1Page() {
        ModelAndView step1Page = new ModelAndView("dianrong/forgetpwd/inputEmailView");
        return step1Page;
    }

    /**
     * . get pwdforget step 2 page
     * 
     * @return step 2 page
     */
    private ModelAndView getPwdForgetStep2Page() {
        return new ModelAndView("dianrong/forgetpwd/getEmailVerifyCodeView");
    }

    /**
     * . get pwdforget step 3 page
     * 
     * @return step 3 page
     */
    private ModelAndView getPwdForgetStep3Page() {
        return new ModelAndView("dianrong/forgetpwd/resetPasswordView");
    }

    /**
     * . get pwdforget step 4 page
     * 
     * @return step 4 page
     */
    private ModelAndView getPwdForgetStep4Page() {
        return new ModelAndView("dianrong/forgetpwd/resetPwdSuccess");
    }

    /**
     * . clear all session parameter about password forget
     * 
     * @param session httpSession
     */
    private void clearAllSessionVal(HttpSession session) {
        if (session != null) {
            session.removeAttribute(AppConstants.PWDFORGET_MAIL_VAL_KEY);

            session.removeAttribute(AppConstants.CAS_CAPTCHA_SESSION_KEY);

            session.removeAttribute(AppConstants.PWDFORGET_MAIL_VERIFY_CODE_KEY);

            session.removeAttribute(AppConstants.PWDFORGET_MAIL_VERIFY_EXPIRDATE_KEY);
        }
    }

    // 服务对象
    public ForgetPasswordService getForgetPasswordService() {
        return forgetPasswordService;
    }

    public void setForgetPasswordService(ForgetPasswordService forgetPasswordService) {
        this.forgetPasswordService = forgetPasswordService;
    }
}
