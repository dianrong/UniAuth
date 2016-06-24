package com.dianrong.common.uniauth.cas.registry.model;

import java.security.InvalidParameterException;
import java.util.Date;

import org.jasig.cas.ticket.ServiceTicket;
import org.jasig.cas.ticket.Ticket;
import org.jasig.cas.ticket.TicketGrantingTicket;

public class ServiceTicketPackage implements Ticket{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8376659362953391048L;

	/**.
	 * service ticket
	 */
	private final ServiceTicket st;
	
	/**.
	 * generateMilles
	 */
	private final long generateMilles;
	
	// 默认10s后就删除
	private long defaultExsistMills = 10L * 1000L;
	
	public ServiceTicketPackage(ServiceTicket st){
		if(st == null) {
			throw new InvalidParameterException("st can not be null");
		}
		this.st = st ;
		this.generateMilles = new Date().getTime();
	}
	
	/**.
	 * 判断是否可以删除了
	 * @param milles
	 * @return
	 */
	public boolean isExpired() {
		return isExpired(defaultExsistMills);
	}
	
	/**.
	 * 判断是否可以删除了
	 * @param milles
	 * @return
	 */
	public boolean isExpired(long milles) {
		if(milles < 0) {
			milles = defaultExsistMills;
		}
		long currentMilles = new Date().getTime();
		return (generateMilles + milles) <= currentMilles;
	}

	/**.
	 * 返回st
	 * @return ServiceTicket
	 */
	public ServiceTicket getSt() {
		return st;
	}

	@Override
	public String getId() {
		return st.getId();
	}

	@Override
	public TicketGrantingTicket getGrantingTicket() {
		return st.getGrantingTicket();
	}

	@Override
	public long getCreationTime() {
		return st.getCreationTime();
	}

	@Override
	public int getCountOfUses() {
		return st.getCountOfUses();
	}
}
