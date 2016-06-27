package com.dianrong.common.uniauth.cas.registry;

import org.jasig.cas.ticket.registry.TicketRegistry;

public class TicketRegistryFactoryBean {
	
	private DefaultTicketRegistry defaultTicketRegistry;
	
	private DefaultTicketRegistryDecorator defaultTicketRegistryDecorator;

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
	
	public TicketRegistry buildTicketRegistryDecorator(){
		if(casIsCluster != null && "true".equalsIgnoreCase(casIsCluster)){
			return redisTicketRegistry;
		}
		else{
			return defaultTicketRegistryDecorator;
		}
	}

	public DefaultTicketRegistryDecorator getDefaultTicketRegistryDecorator() {
		return defaultTicketRegistryDecorator;
	}

	public void setDefaultTicketRegistryDecorator(DefaultTicketRegistryDecorator defaultTicketRegistryDecorator) {
		this.defaultTicketRegistryDecorator = defaultTicketRegistryDecorator;
	}
	
}
