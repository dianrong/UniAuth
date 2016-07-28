package com.dianrong.common.uniauth.server.datafilter.impl;

import java.lang.reflect.Field;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import com.dianrong.common.uniauth.server.datafilter.DataFilter;
import com.dianrong.common.uniauth.server.datafilter.FieldType;
import com.dianrong.common.uniauth.server.datafilter.FilterData;
import com.dianrong.common.uniauth.server.datafilter.FilterType;

/**
 * . 数据过滤的模板方法，用于提供一些公用的属性以及方法
 * 
 * @author wanglin
 *
 */
public abstract class AbstractDataFilter implements DataFilter {
	
	/**.
	 *  日志对象
	 */
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * . 数据过滤的开关变量
	 */
	private boolean dataFilterSwitch = true;

	@Override
	public void dataFilter(FieldType type, Object fieldValue, FilterType ftype) {
		dataFilterWithConditionsEqual(ftype, new FilterData(type, fieldValue));
	}

	/**
	 * . 判断数据是否重复
	 * 
	 * @param type
	 *            字段
	 * @param id
	 *            keyid
	 * @param fieldValue
	 *            需要新加入的值
	 */
	@Override
	public void filterFieldValueIsExist(FieldType type, Integer id, Object fieldValue) {
		if (!isDataFilterSwitch()) {
			return;
		}
		// 判空处理
		if (fieldValue == null) {
			return;
		}
		doFilterFieldValueIsExist(type, id, fieldValue);
	}

	/**
	 * . 数据过滤并且伴随字段相等的情况。
	 * 
	 * @param ftype
	 *            过滤的方式
	 * @param equalsField
	 *            比较的字段以及值
	 */
	@Override
	public void dataFilterWithConditionsEqual(FilterType ftype, FilterData... equalsField) {
		if (!isDataFilterSwitch()) {
			return;
		}
		doDataFilterWithConditionsEqual(ftype, equalsField);
	}

	/**
	 * . 判断数据是否重复并且伴随字段相等的情况。
	 * 
	 * @param id
	 *            keyid
	 * @param equalsField
	 *            比较的字段以及值
	 */
	@Override
	public void filterFieldValueIsExistWithCondtionsEqual(Integer id, FilterData... equalsField) {
		if (!isDataFilterSwitch()) {
			return;
		}
		doFilterFieldValueIsExistWithConditionsEqual(id, equalsField);
	}

	/**
	 * . 数据过滤并且伴随字段相等的情况。
	 * 
	 * @param ftype
	 *            过滤的方式
	 * @param equalsField
	 *            需要比较的字段以及值
	 */
	protected abstract void doDataFilterWithConditionsEqual(FilterType ftype, FilterData... equalsField);

	/**
	 * . 判断数据是否重复并且伴随字段相等的情况。
	 * 
	 * @param id
	 *            keyid
	 * @param equalsField
	 *            需要比较的字段以及值
	 */
	protected abstract void doFilterFieldValueIsExistWithConditionsEqual(Integer id, FilterData... equalsField);

	/**
	 * . 判断数据是否重复
	 * 
	 * @param type
	 *            字段
	 * @param id
	 *            keyid
	 * @param fieldValue
	 *            需要新加入的值
	 */
	protected abstract void doFilterFieldValueIsExist(FieldType type, Integer id, Object fieldValue);

	public boolean isDataFilterSwitch() {
		return dataFilterSwitch;
	}

	public void setDataFilterSwitch(boolean dataFilterSwitch) {
		this.dataFilterSwitch = dataFilterSwitch;
	}

	/**
	 * . 从Role中获取数据
	 * 
	 * @param obj
	 *            Role
	 * @param type
	 *            结果
	 * @return 结果
	 */
	protected Object getObjectValue(Object obj, FieldType type) {
		if (obj == null) {
			return null;
		}
		try {
			String fieldName = FieldType.getFieldName(type);
			if (StringUtils.isEmpty(fieldName)) {
				return null;
			};
			
			fieldName = String.valueOf(fieldName.charAt(0)).toLowerCase() + fieldName.substring(1, fieldName.length());
			Field field = ReflectionUtils.findField(obj.getClass(), fieldName);
			if (field == null) {
				return null;
			}
			field.setAccessible(true);
			// 通过反射获取值
			return ReflectionUtils.getField(field, obj);
		} catch (Exception ex) {
			logger.warn("failed get object filed", ex);
		}
		return null;
	}
}
