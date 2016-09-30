package com.dianrong.common.uniauth.common.server.cxf.server;

import org.springframework.core.Ordered;

/**.
 * cxf header 的消费, 优先级高的覆盖优先级低的
 * @author wanglin
 */
public interface HeaderConsumer extends Ordered{
	/**.
	 * the header key
	 * @return can not be null
	 */
	public String key();
	
	/**.
	 * @param value can not be null
	 */
	public void consume(String value);
}
