package com.dr.cas.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.authentication.principal.Service;
import org.jasig.cas.ticket.ServiceTicket;
import org.jasig.cas.ticket.Ticket;
import org.jasig.cas.ticket.registry.TicketRegistry;
import org.jasig.cas.web.support.ArgumentExtractor;
import org.jasig.cas.web.support.CookieRetrievingCookieGenerator;
import org.jasig.cas.web.support.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * . controller for get service ticket out of cas process
 * 
 * @author wanglin
 */

@Controller
@RequestMapping("/serviceTicket")
public class GetServiceTicketController {
	/**
	 * . 日志对象
	 */
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/** Extractors for finding the service. */
	@Autowired
	private List<ArgumentExtractor> argumentExtractors;

	@Autowired
	private CookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator;

	@Autowired
	private CentralAuthenticationService centralAuthenticationService;
	
	/** Ticket registry searched for TGT by ID. */
    @Autowired
    private TicketRegistry ticketRegistry;

	/**
	 * . 获取一个新的service st
	 */
	@RequestMapping(value = "/query", method = RequestMethod.GET)
	public void refreshServiceTicket(HttpServletRequest request, HttpServletResponse response) {
		try{
			final String tgtId = this.ticketGrantingTicketCookieGenerator.retrieveCookieValue(request);
			// tgtId is not exist
			if (StringUtils.isEmpty(tgtId)) {
				sendNonStResponseValue(response, null);
				return;
			}
			final Ticket ticket = this.ticketRegistry.getTicket(tgtId);
			if(ticket == null || ticket.isExpired()) {
				sendNonStResponseValue(response, "service ticket is invalid");
				return;
			}
			
			final Service service = WebUtils.getService(this.argumentExtractors, request);
			final ServiceTicket serviceTicket = this.centralAuthenticationService.grantServiceTicket(tgtId, service);
			if (StringUtils.isEmpty(serviceTicket)) {
				sendNonStResponseValue(response, "generate service ticket failed");
				return;
			}
			sendValidStResponseValue(response, serviceTicket.getId());
		}catch(Exception ex){
			sendNonStResponseValue(response, ex.getMessage());
			logger.warn("failed to query service ticket", ex);
		}
	}

	/**
	 * . no valid st to return
	 * 
	 * @param response
	 *            HttpServletResponse
	 * @param exceptionStr
	 *            exceptionStr
	 */
	private void sendNonStResponseValue(HttpServletResponse response, String exceptionStr) {
		sendResponseContent(response, getResponseJson(1, "login", exceptionStr));
	}

	/**
	 * . no valid st to return
	 * 
	 * @param response
	 *            HttpServletResponse
	 * @param validSt
	 *            valid service ticket
	 */
	private void sendValidStResponseValue(HttpServletResponse response, String validSt) {
		sendResponseContent(response, getResponseJson(0, validSt, ""));
	}

	/**
	 * . write content to response stream
	 * 
	 * @param response
	 *            HttpServletResponse
	 * @param content
	 *            content to be writed
	 */
	private void sendResponseContent(HttpServletResponse response, String content) {
		try {
			response.getWriter().write(content);
		} catch (IOException e) {
			logger.warn("service ticket query write value to client exception", e);
		}
	}

	/**
	 * . generate response json string
	 * 
	 * @param code
	 *            code(0 success , other failed)
	 * @param msg
	 *            msg(service ticket or error msg)
	 * @return json string
	 */
	private String getResponseJson(int code, String content, String msg) {
		StringBuilder json = new StringBuilder();

		// code
		json.append("{\"code\":");
		json.append(code);

		// content
		json.append(",");
		json.append("\"content\":");
		json.append("\"");
		json.append(content == null ? "" : content);
		json.append("\"");

		// msg
		if (!StringUtils.isEmpty(msg)) {
			json.append(",");
			json.append("\"msg\":");
			json.append("\"");
			json.append(msg == null ? "" : msg);
			json.append("\"");
		}
		json.append("}");
		return json.toString();
	}
}
