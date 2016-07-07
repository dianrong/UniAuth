package com.dianrong.common.uniauth.cas.controller;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.ModelAndView;

import com.dianrong.common.uniauth.cas.model.DateSessionObjModel;
import com.dianrong.common.uniauth.cas.util.UniBundleUtil;
import com.dianrong.common.uniauth.cas.util.WebScopeUtil;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.util.StringUtil;
import com.dianrong.common.uniauth.sharerw.message.EmailSender;
import com.google.code.kaptcha.CaptchaProducer;
import com.google.code.kaptcha.util.Helper;

public class CaptchaController extends AbstractBaseController {

    /**
     * ./**. 日志对象
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private EmailSender emailSender;

    private MessageSource messageSource;

    public MessageSource getMessageSource() {
        return messageSource;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String captchaType = getParamFromRequest(request, AppConstants.CAS_CAPTCHA_SESSION_TYPE_KEY);

        // send email verify code
        if ("1".equals(captchaType)) {
            sendEmailVerifyCode(request, response);
            return null;
        }

        Properties props = new Properties();
        props.put("cap.border", "n.");
        props.put("cap.char.arr", "0,2,3,4,5,6,8,9");
        props.put("cap.background.c.to", "white");
        props.put("cap.background.c.from", "white");
        props.put("cap.obscurificator", "com.google.code.kaptcha.impl.WaterRiple");
        props.put("cap.noise.c", "255,96,0");
        CaptchaProducer captchaProducer = (CaptchaProducer) Helper.ThingFactory.loadImpl(6, props);
        String capText = captchaProducer.createText();
        WebScopeUtil.putCaptchaToSession(request.getSession(), capText);
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("image/jpeg");
        try {
            captchaProducer.createImage(response.getOutputStream(), capText);
        } catch (IOException e) {
            try {
                response.sendError(500, "captcha creation failed");
            } catch (IOException e1) {
                logger.warn("captcha creation failed", e1);
            }
        }
        return null;
    }

    /**
     * . 用于发送邮箱验证码
     */
    private void sendEmailVerifyCode(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);

        // 必须要有邮箱
        String email = getValFromSession(session, AppConstants.PWDFORGET_MAIL_VAL_KEY, String.class);
        if (StringUtil.strIsNullOrEmpty(email)) {
            setResponseResultJson(response, "1");
            return;
        }

        String verifyCode = StringUtil.generateNumberStr(6);

        // 定义发送的内容
        String title = UniBundleUtil.getMsg(messageSource, "captcha.controller.captcha.email.title");
        StringBuffer emailInfo = new StringBuffer(UniBundleUtil.getMsg(messageSource, "captcha.controller.captcha.email.content", verifyCode, "\r\n", AppConstants.PWDFORGET_MAIL_VERIFY_CODE_EXPIRE_MILLES / (60L * 1000L)));

        // 发送邮件
        try {
            emailSender.sendEmail(title, email, emailInfo);
            // 将验证内容放入session
            WebScopeUtil.putValToSession(session, AppConstants.PWDFORGET_MAIL_VERIFY_CODE_KEY, new DateSessionObjModel<String>(verifyCode, AppConstants.PWDFORGET_MAIL_VERIFY_CODE_EXPIRE_MILLES));
            // 成功
            setResponseResultJson(response, "0");
        } catch (Exception ex) {
            setResponseResultJson(response, "2", ex.getMessage());
        }
    }
}
