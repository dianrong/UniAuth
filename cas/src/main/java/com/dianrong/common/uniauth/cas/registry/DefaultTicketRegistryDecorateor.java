package com.dianrong.common.uniauth.cas.registry;

import java.util.Collection;

import org.jasig.cas.ticket.Ticket;
import org.springframework.util.Assert;

/**.
 * 实现default ticket registry
 * @author wanglin
 */
public class DefaultTicketRegistryDecorateor extends DefaultTicketRegistry {
	/**.
	 * 默认的ticket registry
	 */
	private DefaultTicketRegistry  ticketRegistry;

	public DefaultTicketRegistry getTicketRegistry() {
		return ticketRegistry;
	}

	public void setTicketRegistry(DefaultTicketRegistry ticketRegistry) {
		this.ticketRegistry = ticketRegistry;
	}

	/**.
	 * 构造函数
	 * @param ticketRegistry
	 */
	public DefaultTicketRegistryDecorateor(DefaultTicketRegistry  ticketRegistry){
		Assert.notNull(ticketRegistry, "ticket registry can not be null");
		this.ticketRegistry = ticketRegistry;
	}
	
    @Override
    public boolean deleteTicket(final String ticketId) {
    	return ticketRegistry.deleteServiceTicket(ticketId);
    }

	@Override
	public void addTicket(Ticket ticket) {
		this.ticketRegistry.addTicket(ticket);
	}

	@Override
	public Ticket getTicket(String ticketId) {
		return this.ticketRegistry.getTicket(ticketId);
	}

	@Override
	public Collection<Ticket> getTickets() {
		return ticketRegistry.getTickets();
	}
	
	 @Override
	  public int sessionCount() {
		 return this.ticketRegistry.sessionCount();
	  }

	    @Override
	  public int serviceTicketCount() {
	  	return this.ticketRegistry.serviceTicketCount();
	  }
}
