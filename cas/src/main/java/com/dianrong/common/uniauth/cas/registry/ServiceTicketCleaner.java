package com.dianrong.common.uniauth.cas.registry;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.jasig.cas.ticket.Ticket;
import org.jasig.cas.ticket.registry.RegistryCleaner;
import org.jasig.cas.ticket.registry.TicketRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dianrong.common.uniauth.cas.registry.model.ServiceTicketPackage;

/**.
 * 自定义实现一个ticket的clean类
 * @author wanglin
 */
public class ServiceTicketCleaner implements RegistryCleaner{
	 /** Logger instance. */
    private final Logger logger = LoggerFactory.getLogger(getClass());
	/**.
	 * 存放ticket的容器
	 */
	private TicketRegistry ticketContainer;
	
	/**.
	 * service ticket 默认的存活毫秒数
	 */
	private long serviceTicketExistMilles = 10L * 1000L;

	/**.
	 * lock
	 */
	private Lock lock = new ReentrantLock();
	
	public void setServiceTicketExistMilles(long serviceTicketExistMilles) {
		this.serviceTicketExistMilles = serviceTicketExistMilles;
	}

	
	@Override
	public Collection<Ticket> clean() {
        try {
        	if(!isSupport()) {
        		return Collections.emptyList();
        	}
            logger.info("Beginning ticket cleanup.");
            logger.debug("Attempting to acquire ticket cleanup lock.");
            if (!this.lock.tryLock()) {
                logger.info("Could not obtain lock.  Aborting cleanup.");
                return Collections.emptyList();
            }
            logger.debug("Acquired lock.  Proceeding with cleanup.");
            DefaultTicketRegistry container = (DefaultTicketRegistry)this.ticketContainer;
            
            Set<String> deleteIds = new HashSet<String>();
            final Collection<Ticket> c = new HashSet<>(container.getTickets());
            final Iterator<Ticket> it = c.iterator();
            while (it.hasNext()) {
            	Ticket t =  it.next();
            	if(t instanceof ServiceTicketPackage && ((ServiceTicketPackage)t).isExpired(serviceTicketExistMilles)) {
            		deleteIds.add(t.getId());
            	}
            }
            
            logger.info("{} expired tickets found to be removed.", deleteIds.size());
            
            for (String tid: deleteIds) {
               try {
                	container.deleteServiceTicket(tid);
	            } catch (final Exception e) {
	                logger.error(e.getMessage(), e);
	            }
            }
            return Collections.emptyList();
        } finally {
            logger.debug("Releasing ticket cleanup lock.");
            this.lock.unlock();
            logger.info("Finished ticket cleanup.");
        }
	}
        
        /**.
         * 判断是否支持clean对象
         * @return
         */
        private boolean isSupport(){
        	return  this.ticketContainer instanceof DefaultTicketRegistry;
        }
        
		public TicketRegistry getTicketContainer() {
			return ticketContainer;
		}
	
		public void setTicketContainer(TicketRegistry ticketContainer) {
			this.ticketContainer = ticketContainer;
		}
}
