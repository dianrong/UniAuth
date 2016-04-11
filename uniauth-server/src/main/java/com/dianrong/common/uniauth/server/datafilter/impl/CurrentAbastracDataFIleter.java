package com.dianrong.common.uniauth.server.datafilter.impl;

import java.util.Map;

import com.dianrong.common.uniauth.server.datafilter.FieldType;
import com.dianrong.common.uniauth.server.datafilter.FilterData;
import com.dianrong.common.uniauth.server.datafilter.FilterType;

/**.
 * 目前阶段需要处理的一个比较固定的流程
 * @author wanglin
 *
 */
public abstract class CurrentAbastracDataFIleter extends AbstractDataFilter{
	/**.
	 * 过滤多个字段的接口（目前该模型就支持这两种）
	 * @param filterMap map包括字段名以及对应的字段值
	 * @param ftype 过滤的方式
	 */
	@Override
	public void doDataFilter(Map<FieldType, Object> filterMap, FilterType ftype){
		switch(ftype){
			case FILTER_TYPE_EXSIT_DATA:
				filterStatusEqual0(filterMap);
				break;
			case FILTER_TYPE_NO_DATA:
				filterNoStatusEqual0(filterMap);
				break;
			default:
				break;
		}
	}
	
	//以下提供的是一些默认实现
	
	/**.
	 * 处理过滤status=0的情况
	 * @param filterMap 过滤条件字段
	 */
	protected abstract void filterStatusEqual0(Map<FieldType, Object> filterMap);
	
	/**.
	 * 处理过滤status!=0的情况
	 * @param filterMap 过滤条件字段
	 */
	protected abstract void filterNoStatusEqual0(Map<FieldType, Object> filterMap);
	

	//默认实现都放这里 需要的自己重写
	@Override
	protected void doFileterFieldValueIsExsist(FieldType type, Integer id, Object fieldValue) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	protected void doDataFilterWithCondtionsEqual(FilterType ftype, FilterData... equalsField) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void doFilterFieldValueIsExsistWithCondtionsEqua(Integer id, FilterData... equalsField) {
		throw new UnsupportedOperationException();
	}
}
