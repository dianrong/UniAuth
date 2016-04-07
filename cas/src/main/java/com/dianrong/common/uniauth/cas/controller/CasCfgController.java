package com.dianrong.common.uniauth.cas.controller;

import java.io.IOException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.dianrong.common.uniauth.cas.helper.CasCfgResourceRefreshHelper;
import com.dianrong.common.uniauth.cas.model.CasCfgCacheModel;
import com.dianrong.common.uniauth.cas.model.CasLoginAdConfigModel;
import com.dianrong.common.uniauth.common.bean.dto.ConfigDto;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.util.StringUtil;

/**
 * 处理cas的个性化定制的controller
 * ps : 普通spring mvc controller 不走web flow
 */
@Controller
@RequestMapping("/cascfg")
public class CasCfgController{
	/**.
	 * 日志对象
	 */
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**.
	 * 根据cfg类型从缓存中获取img的数据流
	 */
	@RequestMapping(value="/imges/{cfgType}",method = RequestMethod.GET)
	public void getCascfgImgStream(HttpServletRequest request, HttpServletResponse response, @PathVariable("cfgType") String cfgType){
		if(StringUtil.strIsNullOrEmpty(cfgType)){
			return;
		}
		//获取缓存
		ConfigDto imgCache = CasCfgResourceRefreshHelper.instance.getImageCacheDto(cfgType);
		if(imgCache != null){
	        byte[] file = imgCache.getFile();
	        if(file != null) {
	            String fileName = imgCache.getValue();
	            String mimeType = URLConnection.guessContentTypeFromName(fileName);
	            if (mimeType == null) {
	                mimeType = javax.ws.rs.core.MediaType.APPLICATION_OCTET_STREAM;
	            }
	            response.setContentType(mimeType);
	            response.setHeader("Content-Disposition", String.format("attachment; filename=\"" + fileName + "\""));
	            response.setContentLength(file.length);
	            try {
					response.getOutputStream().write(file);
				} catch (IOException e) {
					logger.warn("reponse write image outputstream exception:"+e.getMessage());
				}
	        }
        }
	}
	
	/**.
	 * 获取登陆页的滚动图片的图片列表
	 */
	@RequestMapping(value="/imges/login/scrolling",method = RequestMethod.GET)
	public ModelAndView getLoginScrollImges(HttpServletRequest request, HttpServletResponse response){
		CasCfgCacheModel loginAdCaches = CasCfgResourceRefreshHelper.instance.getCache();
		ModelAndView modelAndView = new ModelAndView("dianrong/login/scrollImges");  
		List<CasLoginAdConfigModel> loginAd = loginAdCaches == null ? null : loginAdCaches.getLoginPageAd();
        modelAndView.addObject(AppConstants.LOGIN_SCROLL_IMAGES_MODEL_KEY, loginAd == null ? new ArrayList<ConfigDto>() : loginAd);  
		return modelAndView;  
	}
}