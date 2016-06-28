package com.dianrong.common.uniauth.cas.registry.support;

import javax.annotation.Resource;

import org.jasig.cas.ticket.ServiceTicket;
import org.jasig.cas.ticket.Ticket;
import org.springframework.stereotype.Component;

import com.dianrong.common.uniauth.common.util.ReflectionUtils;

/**.
 * 专门用户RedisTicketRegistry的不想序列化的数据进行缓存处理
 * RedisTicketRegistry 的专用组件
 * @author wanglin
 */
@Component
public final  class RedisTicketRegistrySTHolder {
	
	/**.
	 * service ticket的policy对象
	 */
	@Resource(name="serviceTicketExpirationPolicy")
	private CasMultiTimeUseOrTimeoutExpirationPolicy serviceTicketExpirationPolicy;
	
	
	/**.
	 * 序列化之前
	 */
	public void beforeSerializable(Ticket ticket){
		// 目前只处理service ticket
		if(!(ticket instanceof ServiceTicket)) {
			return;
		}
		ReflectionUtils.setSuperClassField(ticket, "expirationPolicy", null);
	}
	
	/**.
	 * 反序列化之后
	 */
	public void afterDserializable(Ticket ticket) {
		// 目前只处理service ticket
		if(!(ticket instanceof ServiceTicket)) {
			return;
		}
		ReflectionUtils.setSuperClassField(ticket, "expirationPolicy", serviceTicketExpirationPolicy);
	}
	
}
