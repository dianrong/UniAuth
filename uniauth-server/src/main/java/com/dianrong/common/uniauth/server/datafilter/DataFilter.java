package com.dianrong.common.uniauth.server.datafilter;

/**.
 * uniauth-server 所有数据过滤的接口
 * @author wanglin
 *
 */
public interface DataFilter {
		/**.
		 * 过滤数据的接口.
		 * @param fieldName  过滤的字段名
		 * @param fieldValue  对应字段的值
		 * @param ftype 过滤的方式
		 */
		void dataFilter(FieldType type, Object fieldValue, FilterType ftype);
	
		/**.
		 * 数据过滤并且伴随字段相等的情况。
		 * @param fieldName  过滤的字段名
		 * @param fieldValue  对应字段的值
		 * @param ftype 过滤的方式
		 */
		void dataFilterWithConditionsEqual(FilterType ftype, FilterData... equalsField);
		
		/**.
		 * 判断数据是否重复
		 * @param type 字段
		 * @param id keyid
		 * @param fieldValue 需要新加入的值
		 */
		void filterFieldValueIsExist(FieldType type, Integer id, Object fieldValue);
		
		/**.
		 * 判断数据是否重复并且伴随字段相等的情况。
		 * @param type 字段
		 * @param id keyid
		 * @param fieldValue 需要新加入的值
		 */
		void filterFieldValueIsExistWithCondtionsEqual(Integer id, FilterData... equalsField);
}
