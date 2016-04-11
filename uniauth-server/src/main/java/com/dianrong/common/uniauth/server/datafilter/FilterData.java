package com.dianrong.common.uniauth.server.datafilter;

/**.
 * 用于处理数据过滤的data模型.
 * @author wanglin
 *
 */
public class FilterData {
	/**.
	 * 过滤的字段类型
	 */
	private FieldType type;
	
	/**.
	 * 字段对应的数据
	 */
	private Object value;
	
	/**.
	 * 构造函数
	 * @param type type
	 * @param value value
	 */
	public FilterData(FieldType type, Object value){
		this.type = type;
		this.value = value;
	}

	/**
	 * @return the type
	 */
	public FieldType getType() {
		return type;
	}

	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}
}
