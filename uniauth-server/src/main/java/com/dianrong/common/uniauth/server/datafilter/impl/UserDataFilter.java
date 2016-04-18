package com.dianrong.common.uniauth.server.datafilter.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dianrong.common.uniauth.common.bean.InfoName;
import com.dianrong.common.uniauth.server.data.entity.User;
import com.dianrong.common.uniauth.server.data.mapper.UserMapper;
import com.dianrong.common.uniauth.server.datafilter.FieldType;
import com.dianrong.common.uniauth.server.exp.AppException;
import com.dianrong.common.uniauth.server.util.TypeParseUtil;
import com.dianrong.common.uniauth.server.util.UniBundle;

/**.
 * 用户的数据过滤处理实现.
 * @author wanglin
 */
@Service("userDataFilter")
public class UserDataFilter extends CurrentAbstractDataFilter {
	
	 @Autowired
	 private UserMapper userMapper;
	
	/**.
	 * 标示处理的表名
	 */
	private String processTableName = "用户";
	
	/**.
	 * 处理过滤status=0的情况
	 * @param filterMap 过滤条件字段
	 */
	@Override
	public void filterStatusEqual0(Map<FieldType, Object> filterMap){
		Set<Entry<FieldType, Object>> entrySet = filterMap.entrySet();
		//遍历
		for(Entry<FieldType, Object> kv : entrySet){
			switch(kv.getKey()){
				case FIELD_TYPE_ID:
					int countById = userMapper.countUserByIdWithStatusEffective(TypeParseUtil.parseToLongFromObject(kv.getValue()));
					//有数据  就要报错
					if(countById > 0){
						throw new AppException(InfoName.INTERNAL_ERROR, UniBundle.getMsg("datafilter.data.exsit.error", processTableName, "id" , TypeParseUtil.parseToLongFromObject(kv.getValue())));
					}
				case FIELD_TYPE_EMAIL:
					int countByEmail = userMapper.countUserByEmailWithStatusEffective(TypeParseUtil.parseToStringFromObject(kv.getValue()));
					if(countByEmail > 0){
						throw new AppException(InfoName.INTERNAL_ERROR, UniBundle.getMsg("datafilter.data.exsit.error", processTableName, "email" , TypeParseUtil.parseToStringFromObject(kv.getValue())));
					}
					break;
				case FIELD_TYPE_PHONE:
					int countByPhone = userMapper.countUserByPhoneWithStatusEffective(TypeParseUtil.parseToStringFromObject(kv.getValue()));
					if(countByPhone > 0){
						throw new AppException(InfoName.INTERNAL_ERROR, UniBundle.getMsg("datafilter.data.exsit.error", processTableName, "phone" , TypeParseUtil.parseToStringFromObject(kv.getValue())));
					}
					break;
				default:
					break;
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
			switch(kv.getKey()){
				case FIELD_TYPE_ID:
					int countById = userMapper.countUserByIdWithStatusEffective(TypeParseUtil.parseToLongFromObject(kv.getValue()));
					//有数据  就要报错
					if(countById <= 0){
						throw new AppException(InfoName.INTERNAL_ERROR, UniBundle.getMsg("datafilter.data.notexsit.error", processTableName, "id" , TypeParseUtil.parseToLongFromObject(kv.getValue())));
					}
				case FIELD_TYPE_EMAIL:
					int countByEmail = userMapper.countUserByEmailWithStatusEffective(TypeParseUtil.parseToStringFromObject(kv.getValue()));
					if(countByEmail <= 0){
						throw new AppException(InfoName.INTERNAL_ERROR, UniBundle.getMsg("datafilter.data.notexsit.error", processTableName, "email" , TypeParseUtil.parseToStringFromObject(kv.getValue())));
					}
					break;
				case FIELD_TYPE_PHONE:
					int countByPhone = userMapper.countUserByPhoneWithStatusEffective(TypeParseUtil.parseToStringFromObject(kv.getValue()));
					if(countByPhone <= 0){
						throw new AppException(InfoName.INTERNAL_ERROR, UniBundle.getMsg("datafilter.data.notexsit.error", processTableName, "phone" , TypeParseUtil.parseToStringFromObject(kv.getValue())));
					}
					break;
				default:
					break;
			}
		}
	}

	/**.
	 * 判断数据是否已经重复了
	 */
	@Override
	public void doFilterFieldValueIsExist(FieldType type, Integer id, Object fieldValue){
		switch(type){
			case FIELD_TYPE_EMAIL:
				String newEmail = TypeParseUtil.parseToStringFromObject(fieldValue);
				User userInfo = userMapper.selectByIdWithStatusEffective(id);
				if(userInfo != null){
					//如果数据信息没有改变  则不管
					if(newEmail.equals(userInfo.getEmail())){
						break;
					}
				}
				//查看是否存在其他的记录是该信息
				Map<FieldType, Object> tmap = new HashMap<FieldType, Object>();
				tmap.put(FieldType.FIELD_TYPE_EMAIL, newEmail);
				//进行判断
				this.filterStatusEqual0(tmap);
				break;
			case FIELD_TYPE_PHONE:
				String newPhone = TypeParseUtil.parseToStringFromObject(fieldValue);
				userInfo = userMapper.selectByIdWithStatusEffective(id);
				if(userInfo != null){
					//如果数据信息没有改变  则不管
					if(newPhone.equals(userInfo.getPhone())){
						break;
					}
				}
				//查看是否存在其他的记录是该信息
				tmap = new HashMap<FieldType, Object>();
				tmap.put(FieldType.FIELD_TYPE_PHONE, newPhone);
				//进行判断
				this.filterStatusEqual0(tmap);
				break;
			default:
				break;
			}
	}
}
