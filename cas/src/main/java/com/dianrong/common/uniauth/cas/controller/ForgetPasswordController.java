package com.dianrong.common.uniauth.cas.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.dianrong.common.uniauth.cas.model.ForgetPasswordModel;
import com.dianrong.common.uniauth.cas.service.ForgetPasswordService;

public class ForgetPasswordController extends AbstractController {

	private ForgetPasswordService forgetPasswordService;
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//judge by step parameter
		//it's this controller which guarantees the security of reseting password, so need store the status of each step, maybe in session
		String step = request.getParameter("step");

		ModelAndView modelAndView = new ModelAndView("forgetPasswordStep1View");
		modelAndView.addObject("name", "xuzengwei");
		
		ForgetPasswordModel fpm = new ForgetPasswordModel();
		fpm.setEmail("zengwei.xu@dianrong.com");
		
		modelAndView.addObject("fpm", fpm);
		forgetPasswordService.testService();
		return modelAndView;
	}

	public ForgetPasswordService getForgetPasswordService() {
		return forgetPasswordService;
	}

	public void setForgetPasswordService(ForgetPasswordService forgetPasswordService) {
		this.forgetPasswordService = forgetPasswordService;
	}
}
