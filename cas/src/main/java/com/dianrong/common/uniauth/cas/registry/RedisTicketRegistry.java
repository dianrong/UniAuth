package com.dianrong.common.uniauth.cas.registry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.jasig.cas.ticket.ServiceTicket;
import org.jasig.cas.ticket.Ticket;
import org.jasig.cas.ticket.TicketGrantingTicket;
import org.jasig.cas.ticket.registry.AbstractDistributedTicketRegistry;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisTicketRegistry extends AbstractDistributedTicketRegistry{
    @NotNull
    private final RedisTemplate<String,Object> reidsTemplate;

    /**
     * TGT cache entry timeout in seconds.
     */
    @Min(0)
    private final int tgtTimeout;

    /**
     * ST cache entry timeout in seconds.
     */
    @Min(0)
    private final int stTimeout;
    
    public RedisTicketRegistry(RedisTemplate<String,Object> reidsTemplate,int tgtTimeout,int stTimeout){
        this.reidsTemplate=reidsTemplate;
        this.tgtTimeout=tgtTimeout;
        this.stTimeout=stTimeout;
    }
    @Override
    public void addTicket(Ticket ticket) {
        logger.debug("Adding ticket {}", ticket);
        try {
            reidsTemplate.opsForValue().set(ticket.getId(),ticket, getTimeout(ticket), TimeUnit.SECONDS);
        } catch (final Exception e) {
            logger.error("Failed adding {}", ticket, e);
            throw e;
        }
    }

    @Override
    public Ticket getTicket(String ticketId) {
         try {
                final Ticket t = (Ticket) this.reidsTemplate.opsForValue().get(ticketId);
                if (t != null) {
                    return getProxiedTicketInstance(t);
                }
            } catch (final Exception e) {
                logger.error("Failed fetching {} ", ticketId, e);
                throw e;
            }
            return null;
    }

    @Override
    public boolean deleteTicket(String ticketId) {
         logger.debug("Deleting ticket {}", ticketId);
            try {
                 this.reidsTemplate.delete(ticketId);
                 return true;
            } catch (final Exception e) {
                logger.error("Failed deleting {}", ticketId, e);
                throw e;
            }
    }

    @Override
    public Collection<Ticket> getTickets() {
         //throw new UnsupportedOperationException("GetTickets not supported.");
    	return new ArrayList<Ticket>();
    }

    @Override
    protected void updateTicket(Ticket ticket) {
     logger.debug("Updating ticket {}", ticket);
        try {
              this.reidsTemplate.delete(ticket.getId());
              reidsTemplate.opsForValue().set(ticket.getId(),ticket, getTimeout(ticket), TimeUnit.SECONDS);
        } catch (final Exception e) {
            logger.error("Failed updating {}", ticket, e);
            throw e;
        }
    }

    @Override
    protected boolean needsCallback() {
        return true;
    }
   private int getTimeout(final Ticket t) {
        if (t instanceof TicketGrantingTicket) {
            return this.tgtTimeout;
        } else if (t instanceof ServiceTicket) {
            return this.stTimeout;
        }
        throw new IllegalArgumentException("Invalid ticket type");
    }
}
