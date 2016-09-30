package com.dianrong.common.uniauth.common.server.cxf.server;

/**.
 * cxf header 的消费
 * @author wanglin
 */
public interface HeaderConsumer {
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
