package com.dianrong.common.uniauth.cas.registry;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.jasig.cas.authentication.principal.Service;
import org.jasig.cas.ticket.ServiceTicket;
import org.jasig.cas.ticket.Ticket;
import org.jasig.cas.ticket.TicketGrantingTicket;
import org.jasig.cas.ticket.registry.AbstractTicketRegistry;
import org.springframework.util.Assert;

import com.dianrong.common.uniauth.common.cons.AppConstants;

/**.
 * 实现default ticket registry
 * @author wanglin
 */
public class DefaultTicketRegistry  extends AbstractTicketRegistry {

    /** A HashMap to contain the tickets. */
    private final Map<String, Ticket> cache;
    

    /**.
     * zk config map
     */
    @Resource(name="uniauthConfig")
	private Map<String, String> allZkNodeMap;

    /**
     * Instantiates a new default ticket registry.
     */
    public DefaultTicketRegistry() {
        this.cache = new ConcurrentHashMap<>();
    }

    /**
     * Creates a new, empty registry with the specified initial capacity, load
     * factor, and concurrency level.
     *
     * @param initialCapacity - the initial capacity. The implementation
     * performs internal sizing to accommodate this many elements.
     * @param loadFactor - the load factor threshold, used to control resizing.
     * Resizing may be performed when the average number of elements per bin
     * exceeds this threshold.
     * @param concurrencyLevel - the estimated number of concurrently updating
     * threads. The implementation performs internal sizing to try to
     * accommodate this many threads.
     */
    public DefaultTicketRegistry(final int initialCapacity, final float loadFactor, final int concurrencyLevel) {
        this.cache = new ConcurrentHashMap<>(initialCapacity, loadFactor, concurrencyLevel);
    }

    /**
     * {@inheritDoc}
     * @throws IllegalArgumentException if the Ticket is null.
     */
    @Override
    public void addTicket(final Ticket ticket) {
        Assert.notNull(ticket, "ticket cannot be null");
        logger.debug("Added ticket [{}] to registry.", ticket.getId());
        this.cache.put(ticket.getId(), ticket);
    }

    @Override
    public Ticket getTicket(final String ticketId) {
        if (ticketId == null) {
            return null;
        }

        logger.debug("Attempting to retrieve ticket [{}]", ticketId);
        final Ticket ticket = this.cache.get(ticketId);

        if (ticket != null) {
            logger.debug("Ticket [{}] found in registry.", ticketId);
        }
        return ticket;
    }

    @Override
    public boolean deleteTicket(final String ticketId) {
        if (ticketId == null) {
            return false;
        }

        final Ticket ticket = getTicket(ticketId);
        if (ticket == null) {
            return false;
        }

        if (ticket instanceof TicketGrantingTicket) {
            logger.debug("Removing children of ticket [{}] from the registry.", ticket);
            deleteChildren((TicketGrantingTicket) ticket);
        }

        // if it is ServiceTicket and don't delete it 
    	if(ticket instanceof ServiceTicket && !notAllowedStReUsed()) {
    		return true;
    	}
        
        logger.debug("Removing ticket [{}] from the registry.", ticket);
        return (this.cache.remove(ticketId) != null);
    }

    /**
     * Delete TGT's service tickets.
     *
     * @param ticket the ticket
     */
    private void deleteChildren(final TicketGrantingTicket ticket) {
        // delete service tickets
        final Map<String, Service> services = ticket.getServices();
        if (services != null && !services.isEmpty()) {
            for (final Map.Entry<String, Service> entry : services.entrySet()) {
                if (this.cache.remove(entry.getKey()) != null) {
                    logger.trace("Removed service ticket [{}]", entry.getKey());
                } else {
                    logger.trace("Unable to remove service ticket [{}]", entry.getKey());
                }
            }
        }
    }
    
    /**.
     * 直接删除service ticket
     * @param ticketId
     * @return
     */
    protected boolean deleteServiceTicket(final String ticketId) {
        if (ticketId == null) {
            return false;
        }
        
        final Ticket ticket = getTicket(ticketId);
        if (ticket == null) {
            return false;
        }
        
        if(ticket instanceof ServiceTicket) {
        	logger.debug("Removing service ticket [{}] from the registry.", ticketId);
        	return (this.cache.remove(ticketId) != null);
        }
        return false;
    }
    
    public Collection<Ticket> getTickets() {
        return Collections.unmodifiableCollection(this.cache.values());
    }

    @Override
    public int sessionCount() {
        int count = 0;
        for (final Ticket t : this.cache.values()) {
            if (t instanceof TicketGrantingTicket) {
                count++;
            }
        }
        return count;
    }

    @Override
    public int serviceTicketCount() {
        int count = 0;
        for (final Ticket t : this.cache.values()) {
            if (t instanceof ServiceTicket) {
                count++;
            }
        }
        return count;
    }
    
    /**.
     * 动态判断是否需要主动删除service ticket
     * @return true  or false
     */
    private boolean notAllowedStReUsed(){
 	   String val = allZkNodeMap.get(AppConstants.ZK_NODE_NAME_REUSE_ST_NOT_ALLOWED);
 	   if(val == null || !"true".equals(val)) {
 		   return false;
 	   } 
 	   return true;
    }
}
