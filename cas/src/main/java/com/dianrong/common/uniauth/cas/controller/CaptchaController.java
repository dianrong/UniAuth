package com.dianrong.common.uniauth.cas.controller;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.ModelAndView;

import com.dianrong.common.uniauth.cas.config.NotificationConfig;
import com.dianrong.common.uniauth.cas.util.UniBundleUtil;
import com.dianrong.common.uniauth.cas.util.WebScopeUtil;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.util.HttpRequestUtil;
import com.dianrong.common.uniauth.common.util.StringUtil;
import com.dianrong.common.uniauth.sharerw.message.EmailSender;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.WaterRipple;
import com.google.code.kaptcha.util.Config;
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

    
 
    private Producer captchaProducer ;
    @Autowired
    private NotificationConfig notifyCfg;
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
    
    @PostConstruct
    public void initCaptcha() {
      Properties props = new Properties();
      props.put(Constants.KAPTCHA_BORDER, "no");
      props.put(Constants.KAPTCHA_TEXTPRODUCER_CHAR_STRING, "02345689");
      props.put(Constants.KAPTCHA_BACKGROUND_CLR_TO, Color.WHITE);
      props.put(Constants.KAPTCHA_BACKGROUND_CLR_FROM, Color.WHITE);
      props.put(Constants.KAPTCHA_OBSCURIFICATOR_IMPL, WaterRipple.class.getName());
      props.put(Constants.KAPTCHA_NOISE_COLOR, Color.BLACK);
      Config config = new Config(props);
      captchaProducer = config.getProducerImpl();
    }
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
            String target = getValFromSession(request.getSession(false), AppConstants.PSWDFORGET_MAIL_VAL_KEY, String.class);
            if (StringUtil.strIsNullOrEmpty(target)) {
                setResponseResultJson(response, "1");
                return null;
            }
            if(!StringUtil.isEmailAddress(target) && !StringUtil.isPhoneNumber(target)){
                setResponseResultJson(response, "invalid email or phone number");
                return null;                
            }
            ChallengeResult codeResult = generateVerifyCode(target, HttpRequestUtil.ipAddress(request));
            if(!codeResult.isOk()){
                setResponseResultJson(response, "2","verify code generate error");
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
        String capText = captchaProducer.createText();
        WebScopeUtil.putCaptchaToSession(request.getSession(), capText);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        try {
          BufferedImage bi = captchaProducer.createImage(capText);
          ImageIO.write(bi, "jpg", response.getOutputStream());
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
     *//*
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
    }*/
    /**
     * generate verify code use challenge
     * @param identity user unique identity  ,may be email,phone number
     * @param ip for ratelimit controlled by challage endpoint, may be 5 times per minutes/ip
     * @return
     */
    private ChallengeResult generateVerifyCode(String identity,String ip){
        ChallengeResult fetchCode = verifyCodeClient.fetchCode(identity,ip, EventType.EVENT_RESET_PASSWORD_DIANRONG);
        return fetchCode;
    }
    
    private void sendSmsVerifyCode(String phoneNumber,String verifyCode){
        log.info("send sms:{} verify code:{}",phoneNumber,verifyCode);
        SendSmsRequest arg1 = new SendSmsRequest();
        HashSet<String> phoneSet = new HashSet<String>(1);
        phoneSet.add(phoneNumber);
        arg1.setCellphones(phoneSet);
        arg1.setTemplateName(notifyCfg.getSmsTemplateName());
        //template parameters
        Map<String, String> param = new HashMap<String,String>(1);
        param.put("CODE", verifyCode);
        arg1.setParams(param);
        //TODO hadle sms send result and return error msg when send failed,
        //but the feature of how to determine result is failed should provided by Notification
        String sendResult = smsClient.send(notifyCfg.getNotificationUserKey(), arg1);
        log.info("send sms:{} result:{}",phoneNumber,sendResult);
    }
    private void sendEmailVerifyCode(String emailAddress,String verifyCode){
        log.info("send email:{} verify code:{}",emailAddress,verifyCode);
        SendEmailRequest arg1 = new SendEmailRequest();
        //定义发送的内容
        String title = UniBundleUtil.getMsg(messageSource, "captcha.controller.captcha.email.title");
        StringBuffer emailInfo = new StringBuffer(UniBundleUtil.getMsg(messageSource, "captcha.controller.captcha.email.content", verifyCode, "\r\n", AppConstants.PSWDFORGET_MAIL_VERIFY_CODE_EXPIRE_MILLES / (60L * 1000L)));
        arg1.setTo(emailAddress);
        arg1.setSubject(title);
        arg1.setFrom(notifyCfg.getInternalSmtpFromEmail());
        arg1.setFromName("TechOps-noreplay");
        arg1.setText(emailInfo.toString());
        String sendResult = emailClient.send(notifyCfg.getNotificationUserKey(), arg1);
        log.info("send mail:{} result:{}",emailAddress,sendResult);
        if(StringUtils.isNotBlank(sendResult)){
            throw new RuntimeException(" send mail failed");
        }
    }
}
