package com.dianrong.common.uniauth.cas.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dianrong.common.uniauth.common.server.UniauthLocaleInfoHolder;

/**
 * . 国际化处理相关的请求处理
 * 
 * @author wanglin
 */
@Controller
@RequestMapping("/i18n")
public class CasI18nController {
    /**
     * . 日志对象
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * . 获取当前系统的语言信息
     * 
     * @param request
     * @param response
     */
    @RequestMapping(value = "/getLanguage", method = RequestMethod.GET)
    public void getLocaleLanguage(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.getWriter().write(UniauthLocaleInfoHolder.getLocale().toString());
        } catch (IOException e) {
            logger.warn("query i18n language failed");
        } ;
    }
    
    /**
     * 设置当前的用户的语言信息
     * @param request
     * @param response
     */
    @RequestMapping(value = "/setLanguage", method = {RequestMethod.GET, RequestMethod.POST})
    public void setLocaleLanguage(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.getWriter().write("success");
        } catch (IOException e) {
            logger.warn("refresh i18n language failed");
        } ;
    }
}
