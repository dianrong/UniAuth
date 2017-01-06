package com.dianrong.common.uniauth.cas.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.ModelAndView;

import com.dianrong.common.uniauth.cas.model.DateSessionObjModel;
import com.dianrong.common.uniauth.cas.util.UniBundleUtil;
import com.dianrong.common.uniauth.cas.util.WebScopeUtil;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.util.StringUtil;
import com.dianrong.common.uniauth.sharerw.message.EmailSender;
import com.dianrong.platform.challenge.domain.ChallengeResult;
import com.dianrong.platform.challenge.facade.DefaultChallengeClient;
import com.dianrong.platform.challenge.facade.EventType;
import com.dianrong.platform.notification.email.EmailHttpClient;
import com.dianrong.platform.notification.email.SendEmailRequest;
import com.dianrong.platform.notification.sms.SendSmsRequest;
import com.dianrong.platform.notification.sms.SmsHttpClient;
import com.google.code.kaptcha.CaptchaProducer;
import com.google.code.kaptcha.util.Helper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CaptchaController extends AbstractBaseController {

    private static final String UNIAUTH_CAS = "UNIAUTH_CAS";
    private static final String FIND_PWD = "FIND_PWD";
    private static final String SMS_TEMPLATE_NAME = "SMS_BOST_NOTIFY_TEST";
    @Value("#{uniauthConfig['notification_key']}")
    private String notificationUserKey; 
    @Autowired
    private EmailSender emailSender;
    /**
     * verify code generator and verify the code
     */
    @Autowired
    @Qualifier("defaultChallengeClient")
    private DefaultChallengeClient verifyCodeClient;
    /**
     * sms sender
     */
    @Autowired
    private SmsHttpClient smsClient;
    @Autowired
    private EmailHttpClient emailClient;
    
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
            String target = getValFromSession(request.getSession(false), AppConstants.PWDFORGET_MAIL_VAL_KEY, String.class);
            if (StringUtil.strIsNullOrEmpty(target)) {
                setResponseResultJson(response, "1");
                return null;
            }
            if(!StringUtil.isEmailAddress(target) && !StringUtil.isPhoneNumber(target)){
                setResponseResultJson(response, "invalid Email Or Phone Number");
                return null;                
            }
            ChallengeResult codeResult = generateVerifyCode(target);
            if(!codeResult.isOk()){
                setResponseResultJson(response, "2", "send ");
                return null;
            }
            String code = codeResult.getCode();
            if(StringUtil.isPhoneNumber(target)){
                sendSmsVerifyCode(target, code);
            }else{
                sendEmailVerifyCode(target,code);
            }
            setResponseResultJson(response, "0");
            return null;
        }else{
            captcha(request, response);
        }
        return null;
    }
    /**
     * image captcha
     * @param request
     * @param response
     */
    private void captcha(HttpServletRequest request, HttpServletResponse response) {
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
                log.warn("captcha creation failed", e1);
            }
        }
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
    /**
     * generate verify code use challenge
     * @param targetId user unique identity  ,may be email,phone number
     * @return
     */
    private ChallengeResult generateVerifyCode(String targetId){
        //phone/email or unique id，ip address，biz type
        //ip address affect rate limit 
        ChallengeResult fetchCode = verifyCodeClient.fetchCode(targetId, UNIAUTH_CAS+FIND_PWD, EventType.EVENT_RESET_PASSWORD_DIANRONG);
        return fetchCode;
    }
    
    private void sendSmsVerifyCode(String phoneNumber,String verifyCode){
        log.info("send phone:{} verify code:{}",phoneNumber,verifyCode);
        SendSmsRequest arg1 = new SendSmsRequest();
        HashSet<String> phoneSet = new HashSet<String>(1);
        phoneSet.add(phoneNumber);
        arg1.setCellphones(phoneSet);
        arg1.setTemplateName(SMS_TEMPLATE_NAME);
        Map<String, String> param = new HashMap<String,String>(1);
        param.put("VERIFY_CODE", verifyCode);
        arg1.setParams(param);
        smsClient.send(notificationUserKey, arg1);
    }
    private void sendEmailVerifyCode(String emailAddress,String verifyCode){
        log.info("send email:{} verify code:{}",emailAddress,verifyCode);
        SendEmailRequest arg1 = new SendEmailRequest();
        //定义发送的内容
        String title = UniBundleUtil.getMsg(messageSource, "captcha.controller.captcha.email.title");
        StringBuffer emailInfo = new StringBuffer(UniBundleUtil.getMsg(messageSource, "captcha.controller.captcha.email.content", verifyCode, "\r\n", AppConstants.PWDFORGET_MAIL_VERIFY_CODE_EXPIRE_MILLES / (60L * 1000L)));
        arg1.setTo(emailAddress);
        arg1.setSubject(title);
        arg1.setText(emailInfo.toString());
        emailClient.send(notificationUserKey, arg1);
    }
}
