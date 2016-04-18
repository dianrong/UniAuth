package com.dianrong.common.uniauth.server.datafilter.impl;

import java.util.HashMap;
import java.util.Map;

import com.dianrong.common.uniauth.server.datafilter.DataFilter;
import com.dianrong.common.uniauth.server.datafilter.FieldType;
import com.dianrong.common.uniauth.server.datafilter.FilterData;
import com.dianrong.common.uniauth.server.datafilter.FilterType;

/**.
 * 数据过滤的模板方法，用于提供一些公用的属性以及方法
 * @author wanglin
 *
 */
public abstract class AbstractDataFilter  implements DataFilter{
	
	/**.
	 * 数据过滤的开关变量
	 */
	private boolean dataFilterSwitch = true;
	
	/**.
	 * 过滤数据的接口.
	 * @param FieldType  过滤的字段名
	 * @param fieldValue  对应字段的值
	 * @param ftype 过滤的方式
	 */
	@Override
	public void dataFilter(FieldType FieldType, Object fieldValue, FilterType ftype){
		if(!isDataFilterSwitch()){
			return;
		}
		Map<FieldType, Object>  filterMap = new HashMap<FieldType, Object>();
		filterMap.put(FieldType, fieldValue);
		doDataFilter(filterMap, ftype );
	}
	
	/**.
	 * 过滤多个字段的接口
	 * @param filterMap map包括字段名以及对应的字段值
	 * @param ftype 过滤的方式
	 */
	@Override
	public void dataFilter(Map<FieldType, Object> filterMap, FilterType ftype){
		if(!isDataFilterSwitch()){
			return;
		}
		//判空处理
		if(filterMap == null || filterMap.isEmpty()){
			return;
		}
		doDataFilter(filterMap, ftype);
	}
	
	/**.
	 * 判断数据是否重复
	 * @param type 字段
	 * @param id keyid
	 * @param fieldValue 需要新加入的值
	 */
	@Override
	public void filterFieldValueIsExist(FieldType type, Integer id, Object fieldValue){
		if(!isDataFilterSwitch()){
			return;
		}
		//判空处理
		if(fieldValue == null){
			return;
		}
		doFilterFieldValueIsExist(type, id, fieldValue);
	}
	
	/**.
	 * 数据过滤并且伴随字段相等的情况。
	 * @param ftype 过滤的方式
	 * @param equalsField 比较的字段以及值
	 */
	@Override
	public void dataFilterWithConditionsEqual(FilterType ftype, FilterData... equalsField){
		if(!isDataFilterSwitch()){
			return;
		}
		doDataFilterWithConditionsEqual(ftype, equalsField);
	}
	
	/**.
	 * 判断数据是否重复并且伴随字段相等的情况。
	 * @param id keyid
	 * @param equalsField 比较的字段以及值
	 */
	@Override
	public void filterFieldValueIsExistWithCondtionsEqual(Integer id, FilterData... equalsField){
		if(!isDataFilterSwitch()){
			return;
		}
		doFilterFieldValueIsExistWithConditionsEqual(id, equalsField);
	}
	
	
	/**.
	 * 数据过滤并且伴随字段相等的情况。
	 * @param ftype 过滤的方式
	 * @param  equalsField 需要比较的字段以及值
	 */
	protected abstract void doDataFilterWithConditionsEqual(FilterType ftype, FilterData... equalsField);
	
	/**.
	 * 判断数据是否重复并且伴随字段相等的情况。
	 * @param id keyid
	 * @param equalsField 需要比较的字段以及值
	 */
	protected abstract void doFilterFieldValueIsExistWithConditionsEqual(Integer id, FilterData... equalsField);
	
	/**.
	 * 判断数据是否重复
	 * @param type 字段
	 * @param id keyid
	 * @param fieldValue 需要新加入的值
	 */
	protected abstract void doFilterFieldValueIsExist(FieldType type, Integer id, Object fieldValue);
	
	/**.
	 * 过滤多个字段的接口（目前该模型就支持这两种）
	 * @param filterMap map包括字段名以及对应的字段值
	 * @param ftype 过滤的方式
	 */
	protected abstract void doDataFilter(Map<FieldType, Object> filterMap, FilterType ftype);
	
	public boolean isDataFilterSwitch() {
		return dataFilterSwitch;
	}

	public void setDataFilterSwitch(boolean dataFilterSwitch) {
		this.dataFilterSwitch = dataFilterSwitch;
	}
}
