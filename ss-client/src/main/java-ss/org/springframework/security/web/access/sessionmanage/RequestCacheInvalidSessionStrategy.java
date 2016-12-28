package org.springframework.security.web.access.sessionmanage;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.session.InvalidSessionStrategy;

/**
 * 记录请求的上下文信息
 * 
 * @author wanglin
 */
public final class RequestCacheInvalidSessionStrategy implements InvalidSessionStrategy {
    /**
     * . RequestCache
     */
    private RequestCache requestCache = new HttpSessionRequestCache();

    @Override
    public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        requestCache.saveRequest(request, response);
    }
}
