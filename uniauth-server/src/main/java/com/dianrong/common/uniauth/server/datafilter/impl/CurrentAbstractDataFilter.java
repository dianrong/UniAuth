package com.dianrong.common.uniauth.server.datafilter.impl;

import com.dianrong.common.uniauth.common.bean.InfoName;
import com.dianrong.common.uniauth.common.util.StringUtil;
import com.dianrong.common.uniauth.server.datafilter.FieldType;
import com.dianrong.common.uniauth.server.datafilter.FilterData;
import com.dianrong.common.uniauth.server.datafilter.FilterType;
import com.dianrong.common.uniauth.server.exp.AppException;
import com.dianrong.common.uniauth.server.util.ObjectUtil;
import com.dianrong.common.uniauth.server.util.UniBundle;

/**
 * . 目前阶段需要处理的一个比较固定的流程
 * 
 * @author wanglin
 *
 */
public abstract class CurrentAbstractDataFilter extends AbstractDataFilter {
	// 默认实现都放这里 需要的自己重写
	@Override
	protected void doFilterFieldValueIsExist(FieldType type, Integer id, Object fieldValue) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void doFilterFieldValueIsExistWithConditionsEqual(Integer id, FilterData... equalsField) {
		// 不处理
		if (equalsField == null || equalsField.length == 0) {
			return;
		}
		Object record = getRecordByPrimaryKey(id);
		if (record != null) {
			// 默认是全部相等的
			boolean isEqual = true;
			// 如果数据信息没有改变 则不管
			for (FilterData fd : equalsField) {
				Object v1 = getObjectValue(record, fd.getType());
				Object v2 = fd.getValue();
				if (!ObjectUtil.objectEqual(v1, v2)) {
					isEqual = false;
					break;
				}
			}
			if (isEqual) {
				return;
			}
		}
		// 查看是否存在其他的记录是该信息
		this.dataFilterWithConditionsEqual(FilterType.FILTER_TYPE_EXSIT_DATA, equalsField);
	}

	@Override
	protected void doDataFilterWithConditionsEqual(FilterType ftype, FilterData... equalsField) {
		if (ftype == null) {
			throw new NullPointerException();
		}
		switch (ftype) {
		case FILTER_TYPE_EXSIT_DATA:
			if (dataWithConditionsEqualExist(equalsField)) {
				throw new AppException(InfoName.INTERNAL_ERROR,
						UniBundle.getMsg("datafilter.data.mutilcondition.exsit.error", getProcessTableName(),
								getFieldTypeKeyAndValue(equalsField)));
			}
			break;
		case FILTER_TYPE_NO_DATA:
			if (!dataWithConditionsEqualExist(equalsField)) {
				throw new AppException(InfoName.INTERNAL_ERROR,
						UniBundle.getMsg("datafilter.data.mutilcondition.notexsit.error", getProcessTableName(),
								getFieldTypeKeyAndValue(equalsField)));
			}
			break;
		default:
			break;
		}
	}

	/**
	 * . 获取描述符
	 * 
	 * @param equalsField
	 *            equalsField处理的字段
	 * @return 描述符string
	 */
	private String getFieldTypeKeyAndValue(FilterData... equalsField) {
		if (equalsField == null || equalsField.length == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		String filterKeyVal = "=";
		String filterEle = ";";
		for (FilterData fd : equalsField) {
			if (sb.toString().length() > 0) {
				sb.append(filterEle);
			}
			sb.append(FieldType.getTypeDes(fd.getType()));
			sb.append(filterKeyVal);
			sb.append(StringUtil.getObjectStr(fd.getValue()));
		}
		return sb.toString();
	}

	/**
	 * . 处理查询数据中是否存在对应的字段相等的情况
	 * 
	 * @param equalsField
	 *            字段列表
	 * @return 结果
	 */
	protected abstract boolean dataWithConditionsEqualExist(FilterData... equalsField);

	/**
	 * . 获取描述的表的名字
	 * 
	 * @return 表名
	 */
	protected abstract String getProcessTableName();

	/**
	 * . 根据主键id获取对象 空实现一个
	 * 
	 * @param id
	 *            primary key
	 * @return record
	 */
	protected Object getRecordByPrimaryKey(Integer id) {
		throw new UnsupportedOperationException();
	};
}
