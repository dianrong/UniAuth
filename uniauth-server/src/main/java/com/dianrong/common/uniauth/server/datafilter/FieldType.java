package com.dianrong.common.uniauth.server.datafilter;

import org.apache.commons.lang3.StringUtils;

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
	FIELD_TYPE_TAG_TYPE_ID,
	
	/**.
	 * user_id
	 */
	FIELD_TYPE_USER_ID,
	
	/**.
	 * extend_id
	 */
	FIELD_TYPE_EXTEND_ID,
	/**.
	 * roleTypeId
	 */
	FIELD_TYPE_ROLE_CODE_ID,
	/**.
	 * name
	 */
	FIELD_TYPE_NAME;
	
	/**.
	 * 获取fieldType的字符串描述符
	 * @param type type
	 * @return 描述符
	 */
	public static String getTypeDes(FieldType type){
		if(type == null) {
			return "";
		}
		String value = type.toString();
		int index = value.indexOf("FIELD_TYPE_");
		if(index != -1) {
			value = value.substring(index);
		}
		//转化为小写
		value = value.toLowerCase();
		//驼峰写法
		char[] chars = value.toCharArray();
		StringBuilder toValue = new StringBuilder();
		if(chars != null && chars.length > 0) {
			boolean toUpper = false;
			for(int i = 0 ; i < chars.length ; i ++) {
				if(chars[i] == '_') {
					toUpper = true;
				} else {
					if(toUpper) {
						toValue.append(lowerToUpper(chars[i]));
					} else {
						toValue.append(chars[i]);
					}
					toUpper = false;
				}
			}
		}
		return toValue.toString();
	}
	
	/**.
	 * 根据枚举获取属性名称
	 * @param type
	 * @return 名称
	 */
	public static String getFieldName(FieldType type){
		String filedTypeDesc = getTypeDes(type);
		if(StringUtils.isEmpty(filedTypeDesc)) {
			return filedTypeDesc;
		}
		String prefix = "fieldType";
		int index = filedTypeDesc.indexOf(prefix);
		if (index != -1) {
			return filedTypeDesc.substring(index + prefix.length());
		}
		return filedTypeDesc;
	}
	
	/**.
	 * 小写转大写
	 * @param c
	 * @return
	 */
	private static char lowerToUpper(char c) {
		if(c >= 'a' && c <= 'z') {
			return (char)(c - 32);
		}
		return c;
	}
}
