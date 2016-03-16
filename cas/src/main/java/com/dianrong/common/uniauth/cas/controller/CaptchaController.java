package com.dianrong.common.uniauth.cas.controller;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;

import com.dianrong.common.uniauth.cas.model.DateSessionObjModel;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.util.StringUtil;
import com.dianrong.common.uniauth.sharerw.message.EmailSender;
import com.google.code.kaptcha.CaptchaProducer;
import com.google.code.kaptcha.util.Helper;

public class CaptchaController extends AbstractBaseController {

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
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
		putValToSession(request.getSession(), AppConstants.CAS_CAPTCHA_SESSION_KEY, capText);
		// request.getSession().setAttribute(AppConstants.CAS_CAPTCHA_SESSION_KEY,
		// capText);
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setContentType("image/jpeg");
		try {
			captchaProducer.createImage(response.getOutputStream(), capText);
		} catch (IOException e) {
			try {
				response.sendError(500, "captcha creation failed");
			} catch (IOException e1) {
				e1.printStackTrace();
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
		String title = "uniauth重置 密码";
		StringBuffer emailInfo = new StringBuffer("您好，本次密码修改的验证码为:" + verifyCode + "。\r\n注意：验证码将在"+AppConstants.PWDFORGET_MAIL_VERIFY_CODE_EXPIRE_MILLES/1000L+"秒后过期。");

		// 发送邮件
		try {
			EmailSender.sendEmail(title, email, emailInfo);
			// 将验证内容放入session
			putValToSession(session, AppConstants.PWDFORGET_MAIL_VERIFY_CODE_KEY,
					new DateSessionObjModel<String>(verifyCode, AppConstants.PWDFORGET_MAIL_VERIFY_CODE_EXPIRE_MILLES));
			// 成功
			setResponseResultJson(response, "0");
		} catch (Exception ex) {
			setResponseResultJson(response, "2", ex.getMessage());
		}
	}
}
