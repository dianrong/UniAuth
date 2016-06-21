package com.dianrong.common.uniauth.cas.action;

import javax.servlet.http.HttpServletRequest;

import org.jasig.cas.util.UniqueTicketIdGenerator;
import org.jasig.cas.web.support.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.webflow.execution.RequestContext;

import com.dianrong.common.uniauth.common.cons.AppConstants;

/**.
 * 用于生成客户自定义的登陆页面的登陆ticket的生成
 * @author wanglin
 */
public class CustomLoginGenerateLoginTicketAction {
    /** Logger instance. */
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("loginTicketUniqueIdGenerator")
    private UniqueTicketIdGenerator ticketIdGenerator;

    /**
     * Generate the login ticket.
     *
     * @param context the context
     * @return <code>"generated"</code>
     */
    public final String generate(final RequestContext context) {
        final String loginTicket = this.ticketIdGenerator.getNewTicketId(AppConstants.CAS_LOGIN_TICKET_PREFIX);
        logger.debug("Generated login ticket {}", loginTicket);
        // 放session中
        final HttpServletRequest request = WebUtils.getHttpServletRequest(context);
        request.getSession().setAttribute(AppConstants.CAS_CUSTOM_LOGIN_LT_KEY, loginTicket);
        return "generated";
    }
}
