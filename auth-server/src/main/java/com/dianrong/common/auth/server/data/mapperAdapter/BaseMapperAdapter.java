package com.dianrong.common.auth.server.data.mapperAdapter;

/**.
 * 所有的adapter的只实现了需要的方法 如果需要新的方法 可以在adapter中添加
 * 直接调用mapper中已有的方法即可
 * @author wanglin
 */
public class BaseMapperAdapter {
	/**.
	 * 方言常量定义
	 */
	private final String ORACLE_DIALECT = "oracle";
	private final String MYSQL_DIALECT = "mysql";
	
	/**.
	 * 数据库方言---为了支持mysql和oracle之间的转换
	 * 默认为oracle
	 */
	private String dialect = "oracle";

	public String getDialect() {
		return dialect;
	}

	public void setDialect(String dialect) {
		this.dialect = dialect;
	}
	
	/**.
	 * 判断当前方言是否为oracle
	 * @return
	 */
	protected boolean isOracle(){
		return ORACLE_DIALECT.equalsIgnoreCase(this.dialect);
	}
	
	/**.
	 * 判断当前方言是否为mysql
	 * @return
	 */
	protected boolean isMysql(){
		return MYSQL_DIALECT.equalsIgnoreCase(this.dialect);
	}
}
