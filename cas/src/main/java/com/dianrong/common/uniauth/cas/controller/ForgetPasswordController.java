package com.dianrong.common.uniauth.cas.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.dianrong.common.uniauth.cas.model.ForgetPasswordModel;

public class ForgetPasswordController extends AbstractController {

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		ModelAndView modelAndView = new ModelAndView("forgetPasswordStep1View");
		modelAndView.addObject("name", "xuzengwei");
		
		ForgetPasswordModel fpm = new ForgetPasswordModel();
		fpm.setEmail("zengwei.xu@dianrong.com");
		
		modelAndView.addObject("fpm", fpm);
		return modelAndView;
	}

}
