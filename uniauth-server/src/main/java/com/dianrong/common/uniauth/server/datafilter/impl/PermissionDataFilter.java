package com.dianrong.common.uniauth.server.datafilter.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.server.data.entity.Permission;
import com.dianrong.common.uniauth.server.data.entity.PermissionExample;
import com.dianrong.common.uniauth.server.data.mapper.PermissionMapper;
import com.dianrong.common.uniauth.server.datafilter.FieldType;
import com.dianrong.common.uniauth.server.datafilter.FilterData;
import com.dianrong.common.uniauth.server.datafilter.FilterType;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.dianrong.common.uniauth.server.util.TypeParseUtil;

/**.
 * 权限的数据过滤处理实现.
 * @author wanglin
 */
@Service("permissionDataFilter")
public class PermissionDataFilter extends CurrentAbstractDataFilter {
	
	@Autowired
	private PermissionMapper permissionMapper;
	
	@Override
	public void doFilterFieldValueIsExistWithConditionsEqual(Integer id, FilterData... equalsField) {
		CheckEmpty.checkEmpty(id, "permission的ID");
		//不处理
		if(equalsField ==  null || equalsField.length == 0) {
			return;
		}
		Permission permission = permissionMapper.selectByIdWithStatusEffective(id);
		if(permission != null){
			// 默认是全部相等的
			boolean isEqual = true;
			// 如果数据信息没有改变  则不管
			for(FilterData fd: equalsField){
				Object  v1 =  getObjectValue(permission, fd.getType());
				Object v2 = fd.getValue();
				if(v1 == null && v2== null){
					continue;
				}
				// 其中某个字段为空了 都不能认为是相等的
				if(v1== null || v2 == null){
					isEqual = false;
					break;
				}
				if(!v1.equals(v2)){
					isEqual = false;
					break;
				}
			}
			if(isEqual){
				return;
			}
		}
		//查看是否存在其他的记录
		doDataFilterWithConditionsEqual(FilterType.FILTER_TYPE_EXSIT_DATA,  equalsField) ;
	}
	
	/**.
	 * 从permission中获取数据
	 * @param obj permission
	 * @param type 结果
	 * @return 结果
	 */
	private Object getObjectValue(Permission obj, FieldType type){
		if(obj == null){
			return null;
		}
		//根据自身情况来实现
		switch(type){
		case FIELD_TYPE_ID:
			return obj.getId();
		case  FIELD_TYPE_VALUE:
			return obj.getValue();
		case  FIELD_TYPE_PERM_TYPE_ID:
			return obj.getPermTypeId();
		case  FIELD_TYPE_DOMAIN_ID:
			return obj.getDomainId();
			default:
				break;
		}
		return null;
	}

	@Override
	protected boolean dataWithConditionsEqualExist(FilterData... equalsField) {
		//判空处理
				if(equalsField == null || equalsField.length == 0) {
					return false;
				}
				//首先根据类型和值获取到对应的model数组
				PermissionExample condition = new PermissionExample();
				PermissionExample.Criteria criteria =  condition.createCriteria();
				
				criteria.andStatusEqualTo(AppConstants.ZERO_Byte);
				//构造查询条件
				for(FilterData fd: equalsField){
					switch(fd.getType()) {
						case FIELD_TYPE_ID:
							criteria.andIdEqualTo(TypeParseUtil.parseToIntegerFromObject(fd.getValue()));
							break;
						case FIELD_TYPE_VALUE:
							criteria.andValueEqualTo(TypeParseUtil.parseToStringFromObject(fd.getValue()));
							break;
						case FIELD_TYPE_PERM_TYPE_ID:
							criteria.andPermTypeIdEqualTo(TypeParseUtil.parseToIntegerFromObject(fd.getValue()));
							break;
						case FIELD_TYPE_DOMAIN_ID:
							criteria.andDomainIdEqualTo(TypeParseUtil.parseToIntegerFromObject(fd.getValue()));
							break;
						default:
							break;
					}
				}
				//查询
				List<Permission> permissiones = permissionMapper.selectByExample(condition);
				if(permissiones != null && !permissiones.isEmpty()){
					return true;
				}
				return false;
	}

	@Override
	protected String getProcessTableName() {
		return "权限数据";
	}
}
