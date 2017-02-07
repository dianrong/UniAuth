package com.dianrong.common.uniauth.cas.controller.support;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.authentication.principal.Principal;
import org.jasig.cas.ticket.InvalidTicketException;
import org.jasig.cas.ticket.Ticket;
import org.jasig.cas.ticket.TicketGrantingTicket;
import org.jasig.cas.ticket.registry.TicketRegistry;
import org.jasig.cas.web.support.ArgumentExtractor;
import org.jasig.cas.web.support.CookieRetrievingCookieGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dianrong.common.uniauth.common.exp.NotLoginException;
import com.dianrong.common.uniauth.common.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * . 抽取cas登陆相关操作的公用代码，其他模块采用 组合方式使用
 * 
 * @author wanglin
 */
@Component
@Slf4j
public class CasLoginSupport {

    @Autowired
    private CentralAuthenticationService centralAuthenticationService;

    @Autowired
    private CookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator;

    @Resource(name = "argumentExtractors")
    private List<ArgumentExtractor> argumentExtractors;

    /** Ticket registry searched for TGT by ID. */
    @Autowired
    private TicketRegistry ticketRegistry;

    /**
     *  在登陆的状态下获取tgt对象
     * @return TicketGrantingTicket 对象
     * @throws NotLoginException if not login
     */
    public TicketGrantingTicket queryTgtWithLogined(HttpServletRequest request, HttpServletResponse response) {
        final String tgtId = ticketGrantingTicketCookieGenerator.retrieveCookieValue(request);
        // tgtId is not exist
        if (StringUtil.strIsNullOrEmpty(tgtId)) {
            throw new NotLoginException("not login");
        }
        final Ticket ticket = ticketRegistry.getTicket(tgtId);
        if (ticket == null || ticket.isExpired()) {
            throw new NotLoginException("not login");
        }
        // 校验登陆完成 获取当前登陆账号
        return (TicketGrantingTicket) ticket;
    }
    
    /**
     * 获取当前登陆用户的信息
     * @param ticketGrantingTicketId tgt
     * @return Principal
     * @throws NotLoginException if not login
     */
    public Principal getAuthenticationPrincipal(final String ticketGrantingTicketId) {
        try {
            final TicketGrantingTicket ticketGrantingTicket = this.centralAuthenticationService.getTicket(ticketGrantingTicketId, TicketGrantingTicket.class);
            return ticketGrantingTicket.getAuthentication().getPrincipal();
        } catch (final InvalidTicketException e){
            log.warn(e.getMessage());
            throw new NotLoginException("not login");
        }
    }
}
