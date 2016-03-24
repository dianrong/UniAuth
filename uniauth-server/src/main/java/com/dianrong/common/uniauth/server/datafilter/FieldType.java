package com.dianrong.common.uniauth.server.datafilter;

/**.
 * 枚举关键字段取值的类型
 * @author wanglin
 *
 */
public enum FieldType {
	/**.
	 * 通过id去查找
	 */
	FIELD_TYPE_ID,
	
	/**.
	 * 通过code去查找
	 */
	FIELD_TYPE_CODE,

	/**.
	 * 通过email去查找
	 */
	FIELD_TYPE_EMAIL,

	/**.
	 * 通过phone去查找
	 */
	FIELD_TYPE_PHONE,
}
