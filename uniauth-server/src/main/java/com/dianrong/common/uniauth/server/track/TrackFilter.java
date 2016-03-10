package com.dianrong.common.uniauth.server.track;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TrackFilter implements Filter {

	public TrackFilter() {
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) arg0;
        HttpServletResponse response = (HttpServletResponse) arg1;
        String ip = getIp(request);
        String reqUrl = request.getRequestURI();
        String uuid = UUID.randomUUID().toString();
	}

    private String getIp(HttpServletRequest request) {
        String ip = null;
        String xforword = request.getHeader("X-Forwarded-For");
        if (xforword != null && xforword.length() > 0) {
            int n = xforword.indexOf(',');
            if (n > -1) {
                ip = xforword.substring(0, n);
            } else {
                ip = xforword;
            }
        } else {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
    
	@Override
	public void destroy() {

	}
}
