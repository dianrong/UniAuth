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
import com.dianrong.common.uniauth.server.datafilter.FilterType;
import com.dianrong.common.uniauth.server.exp.AppException;
import com.dianrong.common.uniauth.server.util.TypeParseUtil;
import com.dianrong.common.uniauth.server.util.UniBundle;

/**.
 * 权限的数据过滤处理实现.
 * @author wanglin
 */
@Service("permissionDataFilter")
public class PermissionDataFilter extends CurrentAbastracDataFIleter{
	
	@Autowired
	private PermissionMapper permissionMapper;
	
	/**.
	 * 标示处理的表名
	 */
	private String processTalbeName = "权限数据";
	
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
				int countById = permissionMapper.countPermissionByIdWithStatusEffective(TypeParseUtil.paraseToLongFromObject(kv.getValue()));
				//有数据  就要报错
				if(countById > 0){
					throw new AppException(InfoName.INTERNAL_ERROR, UniBundle.getMsg("datafilter.data.exsit.error", processTalbeName , "id" , TypeParseUtil.paraseToLongFromObject(kv.getValue())));
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
				int countById = permissionMapper.countPermissionByIdWithStatusEffective(TypeParseUtil.paraseToLongFromObject(kv.getValue()));
				//有数据  就要报错
				if(countById <= 0){
					throw new AppException(InfoName.INTERNAL_ERROR, UniBundle.getMsg("datafilter.data.notexsit.error", processTalbeName , "id" , TypeParseUtil.paraseToLongFromObject(kv.getValue())));
				}
				break;
			}
		}
	}
	
	@Override
	public void doDataFilterWithCondtionsEqual(FieldType type, Object fieldValue, FilterType ftype, FieldType... equalFields) {
			switch(ftype){
				case FILTER_TYPE_EXSIT_DATA:
					if(dataWithCondtionsEqualExsit(type, fieldValue, equalFields)){
						throw new AppException(InfoName.INTERNAL_ERROR, UniBundle.getMsg("datafilter.data.mutilcondition.exsit.error", processTalbeName, getFieldTypeKeyAndValue(type, fieldValue, equalFields)));
					}
					break;
				case FILTER_TYPE_NO_DATA:
					if(!dataWithCondtionsEqualExsit(type, fieldValue, equalFields)){
						throw new AppException(InfoName.INTERNAL_ERROR, UniBundle.getMsg("datafilter.data.mutilcondition.notexsit.error", processTalbeName , getFieldTypeKeyAndValue(type, fieldValue, equalFields)));	
					}
					break;
				default:
					break;
			}
	}
	
	@Override
	public void doFilterFieldValueIsExsistWithCondtionsEqua(FieldType type, Integer id, Object fieldValue, FieldType... equalFields) {
		switch(type){
		case FIELD_TYPE_VALUE:
			String newPermissionValue = TypeParseUtil.paraseToStringFromObject(fieldValue);
			Permission permission = permissionMapper.selectByIdWithStatusEffective(id);
			if(permission != null){
				//如果数据信息没有改变  则不管
				if(newPermissionValue.equals(permission.getValue())){
					break;
				}
			}
			//查看是否存在其他的记录是该信息
			doDataFilterWithCondtionsEqual(type, fieldValue, FilterType.FILTER_TYPE_EXSIT_DATA, equalFields);
			break;
		default:
			break;
		}
	}
	
	/**.
	 * 判断某几个字段是否同时存在.
	 */
	private boolean dataWithCondtionsEqualExsit(FieldType type, Object fieldValue ,FieldType... equalFields){
		// 默认值
		boolean result = false;
		switch(type){
		case FIELD_TYPE_VALUE:
			//首先根据类型和值获取到对应的model数组
			PermissionExample condition = new PermissionExample();
			condition.createCriteria().andValueEqualTo(TypeParseUtil.paraseToStringFromObject(fieldValue)).andStatusEqualTo(AppConstants.ZERO_Byte);
			List<Permission> permissiones = permissionMapper.selectByExample(condition);
			if(permissiones == null || permissiones.isEmpty()){
				break;
			}
			for(int i = 0 ; i < permissiones.size() ; i++){
				for(int j = i+1; j < permissiones.size() ; j++){
					Permission outp = permissiones.get(i);
					Permission innerp = permissiones.get(j);
					//存在一个则返回
					if(judgeTwoPermissonByFieldes(outp, innerp, equalFields)){
						result = true;
						break;
					}
				}
			}
			break;
			default:
				break;
		}
		return result;
	}
	
	/**.
	 * 获取描述符
	 * @param type 类型
	 * @param fieldValue 类型值
 	 * @param p 对象
	 * @param fields 对象中的字段
	 * @return 描述符string
	 */
	private String getFieldTypeKeyAndValue(FieldType type, Object fieldValue, FieldType... fields){
		StringBuilder sb = new StringBuilder();
		String filterKeyVal = ":";
		String filterEle = ",";
		if(type != null){
			sb.append(FieldType.getTypeDes(type));
			sb.append(filterKeyVal);
			sb.append(StringUtil.getObjectStr(fieldValue));
			sb.append(filterEle);
		}
		for(FieldType tf : fields){
			sb.append(FieldType.getTypeDes(tf));
			sb.append(filterKeyVal);
		}
		return sb.toString();
	}
	
	/**.
	 * 比较两个Permission在某些字段上是否一致
	 * @param p1 p1
	 * @param p2 p2
	 * @param equalFields 需要比较的字段
	 * @return 结果
	 */
	private boolean judgeTwoPermissonByFieldes(Permission p1, Permission p2 , FieldType... equalFields){
		if(p1 == null || p2 == null){
			return false;
		}
		for(FieldType tft: equalFields){
			Object  v1 =  getObjectValue(p1, tft);
			Object  v2 =  getObjectValue(p2, tft);
			//其中某个字段为空了 都不能认为是相等的
			if(StringUtil.strIsNullOrEmpty(StringUtil.getObjectStr(v1)) || StringUtil.strIsNullOrEmpty(StringUtil.getObjectStr(v2))){
				return false;
			}
			if(!v1.equals(v2)){
				return false;
			}
		}
		return true;
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
