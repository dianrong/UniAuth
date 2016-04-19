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
	
	/**.
	 * 通过表的value字段来找
	 */
	FIELD_TYPE_VALUE,
	
	/**.
	 * 通过表的cfg_key字段来找
	 */
	FIELD_TYPE_CFG_KEY,
	
	/**.
	 * type_id
	 */
	FIELD_TYPE_PERM_TYPE_ID,
	
	/**.
	 * domain_id
	 */
	FIELD_TYPE_DOMAIN_ID,
	
	/**.
	 * tag_type_id
	 */
	FIELD_TYPE_TAG_TYPE_ID;
	
	/**.
	 * 获取fieldType的字符串描述符
	 * @param type type
	 * @return 描述符
	 */
	public static String getTypeDes(FieldType type){
		switch(type){
		case FIELD_TYPE_ID:
			return "id";
		case FIELD_TYPE_CODE:
			return "code";
		case FIELD_TYPE_EMAIL:
			return "email";
		case FIELD_TYPE_PHONE:
			return "phone";
		case FIELD_TYPE_VALUE:
			return "value";
		case FIELD_TYPE_CFG_KEY:
			return "cfg_key";
		case FIELD_TYPE_PERM_TYPE_ID:
			return "permissonTypeId";
		case FIELD_TYPE_DOMAIN_ID:
			return "domainId";
		case FIELD_TYPE_TAG_TYPE_ID:
			return "tagTypeId";
			default:
				break;
		}
		return "";
	}
}
