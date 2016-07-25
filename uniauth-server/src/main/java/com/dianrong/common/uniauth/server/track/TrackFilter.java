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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.server.aop.ServerExAOPHandler;

@Component("trackFilter")
public class TrackFilter implements Filter {
	private static Logger logger = LoggerFactory.getLogger(ServerExAOPHandler.class);
	public TrackFilter() {
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) arg0;
        String ip = getIp(request);
        String reqUrl = request.getRequestURI();

        String uuid = request.getHeader(AppConstants.API_UUID);
        if(uuid == null) {
            uuid = UUID.randomUUID().toString();
        }
        
        GlobalVar gv = new GlobalVar();
        gv.setIp(ip);
        gv.setReqUrl(reqUrl);
        gv.setUuid(uuid);
        gv.setSuccess(AppConstants.ONE_Byte);
        gv.setInvokeSeq(-1L);
        
        RequestManager.setGlobalVar(gv);
        try{
        	chain.doFilter(arg0, arg1);
        }catch(Exception e){
        	logger.error("Unknown exception in TrackFilter.", e);
        }
        RequestManager.closeRequest();
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
