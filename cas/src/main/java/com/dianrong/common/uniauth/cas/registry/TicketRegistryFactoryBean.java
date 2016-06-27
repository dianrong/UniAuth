package com.dianrong.common.uniauth.cas.registry;

import org.jasig.cas.ticket.registry.TicketRegistry;

public class TicketRegistryFactoryBean {
	
	private DefaultTicketRegistry defaultTicketRegistry;
	
	private DefaultTicketRegistry defaultTicketRegistryWithNoExtra;

	private RedisTicketRegistry redisTicketRegistry;
	
	private String casIsCluster;
	
	public DefaultTicketRegistry getDefaultTicketRegistry() {
		return defaultTicketRegistry;
	}

	public void setDefaultTicketRegistry(DefaultTicketRegistry defaultTicketRegistry) {
		this.defaultTicketRegistry = defaultTicketRegistry;
	}

	public RedisTicketRegistry getRedisTicketRegistry() {
		return redisTicketRegistry;
	}

	public void setRedisTicketRegistry(RedisTicketRegistry redisTicketRegistry) {
		this.redisTicketRegistry = redisTicketRegistry;
	}

	public String getCasIsCluster() {
		return casIsCluster;
	}

	public void setCasIsCluster(String casIsCluster) {
		this.casIsCluster = casIsCluster;
	}

	public TicketRegistry buildTicketRegistry(){
		if(casIsCluster != null && "true".equalsIgnoreCase(casIsCluster)){
			return redisTicketRegistry;
		}
		else{
			return defaultTicketRegistry;
		}
	}
	
	public TicketRegistry buildTicketRegistryWithNoExtra(){
		if(casIsCluster != null && "true".equalsIgnoreCase(casIsCluster)){
			return redisTicketRegistry;
		}
		else{
			return defaultTicketRegistryWithNoExtra;
		}
	}

	public DefaultTicketRegistry getDefaultTicketRegistryWithNoExtra() {
		return defaultTicketRegistryWithNoExtra;
	}

	public void setDefaultTicketRegistryWithNoExtra(DefaultTicketRegistry defaultTicketRegistryWithNoExtra) {
		this.defaultTicketRegistryWithNoExtra = defaultTicketRegistryWithNoExtra;
	}
}
