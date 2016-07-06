package com.dianrong.common.uniauth.common.server;

import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * . uniauth locale change interceptor get locale:
 * parameter --> cookie --> seesion --> request.getLocal() --> Locale.getDefault();
 * @author wanglin
 */
public class UniauthLocaleChangeInterceptor extends HandlerInterceptorAdapter {
    /**
     * Default name of the locale specification parameter: "locale".
     */
    public static final String DEFAULT_PARAM_NAME = "locale";

    /**
     * . 可配置的locale parameter name
     */
    private String paramName = DEFAULT_PARAM_NAME;

    /**
     * . cookie Name
     */
    private String cookieName = this.getClass().getName() + ".cookieKey";

    /**
     * . session name
     */
    private String sessionName = this.getClass().getName() + ".sessionKey";

    /**
     * . cookie max age seconds default: 30 days
     */
    private int cookieMaxAge = 30 * 24 * 60 * 60;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException {
        LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
        if (localeResolver == null) {
            throw new IllegalStateException("No LocaleResolver found: not in a DispatcherServlet request?");
        }
        try {
            // step 1: get from request parameter
            String newLocale = request.getParameter(this.paramName);
            if (newLocale != null) {
                localeResolver.setLocale(request, response, StringUtils.parseLocaleString(newLocale));
                return true;
            }

            // step 2: get from cookie
            String cookieLocaleStr = getLocaleStrFromCookie(request);
            if (cookieLocaleStr != null) {
                // 没变 就不管
                Locale cookieLocale = localeResolver.resolveLocale(request);
                if (cookieLocale != null && cookieLocale.toString().equals(cookieLocaleStr)) {
                    return true;
                }
                localeResolver.setLocale(request, response, StringUtils.parseLocaleString(cookieLocaleStr));
                return true;
            }

            // step 3: get from request parameter
            HttpSession session = request.getSession(false);
            if (session != null) {
                Locale sessionLocale = (Locale) session.getAttribute(sessionName);
                if (sessionLocale != null) {
                    localeResolver.setLocale(request, response, sessionLocale);
                    return true;
                }
            }

            // step 4: get from request header
            Locale requestLocale = request.getLocale();
            if (requestLocale != null) {
                localeResolver.setLocale(request, response, requestLocale);
                return true;
            }

            // step 5: use locale.getDefault()
            localeResolver.setLocale(request, response, Locale.getDefault());
            return true;
        } finally {
            Locale newLocale = localeResolver.resolveLocale(request);
            // refresh session
            request.getSession().setAttribute(sessionName, newLocale);
            // refresh cookie
            refreshLocaleCookie(request, response, newLocale == null ? "" : newLocale.toString());
        }
    }

    /**
     * . 获取cookie中设置的locale字符串
     * 
     * @param request HttpServletRequest
     * @return locale str
     */
    private String getLocaleStrFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookieName.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    /**
     * . 刷新 cookie中locale的值
     * 
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param newLocaleStr cookie value
     */
    private void refreshLocaleCookie(HttpServletRequest request, HttpServletResponse response, String newLocaleStr) {
        String cookieLocaleStr = getLocaleStrFromCookie(request);
        if (cookieLocaleStr != null && cookieLocaleStr.equals(newLocaleStr)) {
            return;
        }
        Cookie cookie = new Cookie(cookieName, newLocaleStr);
        cookie.setMaxAge(cookieMaxAge);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public int getCookieMaxAge() {
        return cookieMaxAge;
    }

    public void setCookieMaxAge(int cookieMaxAge) {
        this.cookieMaxAge = cookieMaxAge;
    }


    /**
     * Set the name of the parameter that contains a locale specification in a locale change
     * request. Default is "locale".
     */
    public void setParamName(String paramName) {
        if (paramName == null) {
            throw new IllegalArgumentException("locale's  paramName can not be null");
        }
        this.paramName = paramName;
    }

    /**
     * Return the name of the parameter that contains a locale specification in a locale change
     * request.
     */
    public String getParamName() {
        return this.paramName;
    }
}
