package com.dianrong.common.uniauth.common.server.cxf.client;

/**.
 * 生成cxf的header值
 * @author wanglin
 */
public interface HeaderProducer {
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
