package com.dianrong.common.uniauth.server.datafilter;

import java.util.Map;

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
		 * 过滤多个字段的接口
		 * @param filterMap map包括字段名以及对应的字段值
		 * @param ftype 过滤的方式
		 */
		void dataFilter(Map<FieldType, Object> filterMap, FilterType ftype);
		
		/**.
		 * 判断数据是否重复
		 * @param type 字段
		 * @param id keyid
		 * @param fieldValue 需要新加入的值
		 */
		void fileterFieldValueIsExsist(FieldType type, Integer id, Object fieldValue);
		
		/**.
		 * 数据过滤并且伴随字段相等的情况。
		 * @param fieldName  过滤的字段名
		 * @param fieldValue  对应字段的值
		 * @param ftype 过滤的方式
		 */
		void dataFilterWithCondtionsEqual(FilterType ftype, FilterData... equalsField);
		
		/**.
		 * 判断数据是否重复并且伴随字段相等的情况。
		 * @param type 字段
		 * @param id keyid
		 * @param fieldValue 需要新加入的值
		 */
		void filterFieldValueIsExsistWithCondtionsEqua(Integer id, FilterData... equalsField);
}
