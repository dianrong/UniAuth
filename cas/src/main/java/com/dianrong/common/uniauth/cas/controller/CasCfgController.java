package com.dianrong.common.uniauth.cas.controller;

import com.dianrong.common.uniauth.cas.helper.CasCfgResourceRefreshHelper;
import com.dianrong.common.uniauth.cas.model.CasCfgCacheModel;
import com.dianrong.common.uniauth.cas.model.CasLoginAdConfigModel;
import com.dianrong.common.uniauth.cas.util.CasConstants;
import com.dianrong.common.uniauth.common.bean.dto.ConfigDto;
import com.dianrong.common.uniauth.common.util.StringUtil;

import java.io.IOException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * 处理Cas的个性化定制的controller <br>
 */
@Slf4j
@Controller
@RequestMapping("/cfg")
public class CasCfgController {

  /**
   * 根据Cfg类型从缓存中获取Img的数据流.
   */
  @RequestMapping(value = "/images/{cfgType}", method = RequestMethod.GET)
  public void getCasCfgImgStream(HttpServletRequest request, HttpServletResponse response,
      @PathVariable("cfgType") String cfgType) {
    if (StringUtil.strIsNullOrEmpty(cfgType)) {
      return;
    }
    // 获取缓存
    ConfigDto imgCache = CasCfgResourceRefreshHelper.INSTANCE.getImageCacheDto(cfgType);
    if (imgCache != null) {
      byte[] file = imgCache.getFile();
      if (file != null) {
        String fileName = imgCache.getValue();
        String mimeType = URLConnection.guessContentTypeFromName(fileName);
        if (mimeType == null) {
          mimeType = javax.ws.rs.core.MediaType.APPLICATION_OCTET_STREAM;
        }
        response.setContentType(mimeType);
        response.setHeader("Content-Disposition",
            String.format("attachment; filename=\"" + fileName + "\""));

        // 对浏览器设置图片缓存
        Date now = new Date();
        response.setDateHeader("Last-Modified", now.getTime()); // Last-Modified:页面的最后生成时间
        response.setDateHeader("Expires",
            now.getTime() + CasConstants.CAS_CFG_CACHE_REFRESH_PERIOD_MILLES / 2);
        response.setHeader("Cache-Control",
            "max-age=" + CasConstants.CAS_CFG_CACHE_REFRESH_PERIOD_MILLES / 2);
        response.setHeader("Pragma", "Pragma");

        response.setContentLength(file.length);
        try {
          response.getOutputStream().write(file);
        } catch (IOException e) {
          log.warn("Write image:response outputStream exception occured:" + e.getMessage(), e);
        }
      }
    }
  }

  /**
   * 获取登陆页的滚动图片的图片列表.
   */
  @RequestMapping(value = "/images/login/scrolling", method = RequestMethod.GET)
  public ModelAndView getLoginScrollImges(HttpServletRequest request,
      HttpServletResponse response) {
    CasCfgCacheModel loginAdCaches = CasCfgResourceRefreshHelper.INSTANCE.getCache();
    ModelAndView modelAndView = new ModelAndView("dianrong/login/scrollImges");
    List<CasLoginAdConfigModel> loginAd =
        loginAdCaches == null ? null : loginAdCaches.getLoginPageAd();
    modelAndView.addObject(CasConstants.LOGIN_SCROLL_IMAGES_MODEL_KEY,
        loginAd == null ? new ArrayList<ConfigDto>() : loginAd);
    return modelAndView;
  }
}
