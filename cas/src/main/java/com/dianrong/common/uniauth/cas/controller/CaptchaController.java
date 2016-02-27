package com.dianrong.common.uniauth.cas.controller;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.google.code.kaptcha.CaptchaProducer;
import com.google.code.kaptcha.util.Helper;

public class CaptchaController extends AbstractController {

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Properties props = new Properties();
        props.put("cap.border", "n.");
        props.put("cap.char.arr", "0,2,3,4,5,6,8,9");
        props.put("cap.background.c.to", "white");
        props.put("cap.background.c.from", "white");
        props.put("cap.obscurificator", "com.google.code.kaptcha.impl.WaterRiple");
        props.put("cap.noise.c", "255,96,0");
        CaptchaProducer captchaProducer = (CaptchaProducer) Helper.ThingFactory.loadImpl(6, props);
        String capText = captchaProducer.createText();
        request.getSession().setAttribute(AppConstants.CAS_CAPTCHA_SESSION_KEY, capText);
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

}
