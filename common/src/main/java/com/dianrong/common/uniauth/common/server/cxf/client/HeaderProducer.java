package com.dianrong.common.uniauth.common.server.cxf.client;

import org.springframework.core.Ordered;

/**.
 * 生成cxf的header值, 优先级高的覆盖优先级低的
 * @author wanglin
 */
public interface HeaderProducer extends Ordered {
	/**.
	 * the header key
	 * @return can not be null
	 */
	public String key();
	
	/**.
	 * the header value
	 * @return if null, will not send this header
	 */
	public String produce();
}
