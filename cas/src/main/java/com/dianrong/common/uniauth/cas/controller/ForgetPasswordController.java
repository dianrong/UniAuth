package com.dianrong.common.uniauth.cas.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.dianrong.common.uniauth.cas.model.ForgetPasswordModel;
import com.dianrong.common.uniauth.cas.service.ForgetPasswordService;

public class ForgetPasswordController extends AbstractController {

	private ForgetPasswordService forgetPasswordService;
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//judge by step parameter
		//it's this controller which guarantees the security of reseting password, so need store the status of each step, in session
		//in other words, you yourself maintain the status, and need check previous step when performing next step
		//cannot invalidate session at the end of this process, because this will affect cas server, they are sharing a same jsessionid.
		HttpSession session = request.getSession(false);
		
		System.out.println("----------------------------session:" + session);
		String step = request.getParameter("step");

		ModelAndView modelAndView = new ModelAndView("inputEmailView");
		modelAndView.addObject("name", "xuzengwei");
		
		ForgetPasswordModel fpm = new ForgetPasswordModel();
		fpm.setEmail("zengwei.xu@dianrong.com");
		
		modelAndView.addObject("fpm", fpm);
		
		if(session != null){
			request.getSession().invalidate();
		}
		
		return modelAndView;
	}

	public ForgetPasswordService getForgetPasswordService() {
		return forgetPasswordService;
	}

	public void setForgetPasswordService(ForgetPasswordService forgetPasswordService) {
		this.forgetPasswordService = forgetPasswordService;
	}
}
