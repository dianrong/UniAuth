package com.dianrong.common.uniauth.server.datafilter.impl;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dianrong.common.uniauth.common.bean.InfoName;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.util.StringUtil;
import com.dianrong.common.uniauth.server.data.entity.Permission;
import com.dianrong.common.uniauth.server.data.entity.PermissionExample;
import com.dianrong.common.uniauth.server.data.mapper.PermissionMapper;
import com.dianrong.common.uniauth.server.datafilter.FieldType;
import com.dianrong.common.uniauth.server.datafilter.FilterData;
import com.dianrong.common.uniauth.server.datafilter.FilterType;
import com.dianrong.common.uniauth.server.exp.AppException;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.dianrong.common.uniauth.server.util.TypeParseUtil;
import com.dianrong.common.uniauth.server.util.UniBundle;

/**.
 * 权限的数据过滤处理实现.
 * @author wanglin
 */
@Service("permissionDataFilter")
public class PermissionDataFilter extends CurrentAbstractDataFilter {
	
	@Autowired
	private PermissionMapper permissionMapper;
	
	/**.
	 * 标示处理的表名
	 */
	private String processTableName = "权限数据";
	
	/**.
	 * 处理过滤status=0的情况
	 * @param filterMap 过滤条件字段
	 */
	@Override
	public void filterStatusEqual0(Map<FieldType, Object> filterMap){
		Set<Entry<FieldType, Object>> entrySet = filterMap.entrySet();
		//遍历
		for(Entry<FieldType, Object> kv : entrySet){
			if(kv.getKey() == FieldType.FIELD_TYPE_ID){
				int countById = permissionMapper.countPermissionByIdWithStatusEffective(TypeParseUtil.parseToLongFromObject(kv.getValue()));
				//有数据  就要报错
				if(countById > 0){
					throw new AppException(InfoName.INTERNAL_ERROR, UniBundle.getMsg("datafilter.data.exsit.error", processTableName, "id" , TypeParseUtil.parseToLongFromObject(kv.getValue())));
				}
			}
		}
	}
	
	/**.
	 * 处理过滤不能出现status=0的情况
	 * @param filterMap 入参数据
	 */
	@Override
	public void filterNoStatusEqual0(Map<FieldType, Object> filterMap){
		Set<Entry<FieldType, Object>> entrySet = filterMap.entrySet();
		//遍历
		for(Entry<FieldType, Object> kv : entrySet){
			if(kv.getKey() == FieldType.FIELD_TYPE_ID){
				int countById = permissionMapper.countPermissionByIdWithStatusEffective(TypeParseUtil.parseToLongFromObject(kv.getValue()));
				//有数据  就要报错
				if(countById <= 0){
					throw new AppException(InfoName.INTERNAL_ERROR, UniBundle.getMsg("datafilter.data.notexsit.error", processTableName, "id" , TypeParseUtil.parseToLongFromObject(kv.getValue())));
				}
				break;
			}
		}
	}
	
	@Override
	public void doDataFilterWithConditionsEqual(FilterType ftype, FilterData... equalsField) {
			switch(ftype){
				case FILTER_TYPE_EXSIT_DATA:
					if(dataWithCondtionsEqualExsit(equalsField)){
						throw new AppException(InfoName.INTERNAL_ERROR, UniBundle.getMsg("datafilter.data.mutilcondition.exsit.error", processTableName, getFieldTypeKeyAndValue(equalsField)));
					}
					break;
				case FILTER_TYPE_NO_DATA:
					if(!dataWithCondtionsEqualExsit(equalsField)){
						throw new AppException(InfoName.INTERNAL_ERROR, UniBundle.getMsg("datafilter.data.mutilcondition.notexsit.error", processTableName, getFieldTypeKeyAndValue(equalsField)));
					}
					break;
				default:
					break;
			}
	}
	
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
		doDataFilterWithConditionsEqual(FilterType.FILTER_TYPE_EXSIT_DATA, equalsField) ;
	}
	
	/**.
	 * 判断某几个字段是否同时存在.
	 */
	private boolean dataWithCondtionsEqualExsit(FilterData... equalsField){
		//判空处理
		if(equalsField == null || equalsField.length == 0) {
			return false;
		}
		//首先根据类型和值获取到对应的model数组
		PermissionExample condition = new PermissionExample();
		PermissionExample.Criteria criteria =  condition.createCriteria();
		//构造查询条件
		for(FilterData fd: equalsField){
			switch(fd.getType()) {
				case FIELD_TYPE_VALUE:
					criteria.andValueEqualTo(TypeParseUtil.parseToStringFromObject(fd.getValue()));
					break;
				case FIELD_TYPE_PERM_TYPE_ID:
					criteria.andPermTypeIdEqualTo(Integer.parseInt(TypeParseUtil.parseToLongFromObject(fd.getValue()).toString()));
					break;
				case FIELD_TYPE_DOMAIN_ID:
					criteria.andDomainIdEqualTo(Integer.parseInt(TypeParseUtil.parseToLongFromObject(fd.getValue()).toString()));
					break;
				default:
					break;
			}
		}
		criteria.andStatusEqualTo(AppConstants.ZERO_Byte);
		//查询
		List<Permission> permissiones = permissionMapper.selectByExample(condition);
		if(permissiones != null && !permissiones.isEmpty()){
			return true;
		}
		return false;
	}
	
	/**.
	 * 获取描述符
	 * @param equalsField equalsField处理的字段
	 * @return 描述符string
	 */
	private String getFieldTypeKeyAndValue(FilterData... equalsField){
		if(equalsField == null || equalsField.length == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		String filterKeyVal = "=";
		String filterEle = ";";
		for(FilterData fd: equalsField){
			if(sb.toString().length() > 0) {
				sb.append(filterEle);
			}
			sb.append(FieldType.getTypeDes(fd.getType()));
			sb.append(filterKeyVal);
			sb.append(StringUtil.getObjectStr(fd.getValue()));
		}
		return sb.toString();
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
}
