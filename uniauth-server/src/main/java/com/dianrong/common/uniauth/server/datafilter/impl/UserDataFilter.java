package com.dianrong.common.uniauth.server.datafilter.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.server.data.entity.User;
import com.dianrong.common.uniauth.server.data.entity.UserExample;
import com.dianrong.common.uniauth.server.data.mapper.UserMapper;
import com.dianrong.common.uniauth.server.datafilter.FieldType;
import com.dianrong.common.uniauth.server.datafilter.FilterData;
import com.dianrong.common.uniauth.server.datafilter.FilterType;
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
				this.dataFilter(FieldType.FIELD_TYPE_EMAIL, newEmail, FilterType.FILTER_TYPE_EXSIT_DATA);
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
				this.dataFilter(FieldType.FIELD_TYPE_PHONE, newPhone, FilterType.FILTER_TYPE_EXSIT_DATA);
				break;
			default:
				break;
			}
	}

	@Override
	protected boolean dataWithConditionsEqualExist(FilterData... equalsField) {
		 //判空处理
        if(equalsField == null || equalsField.length == 0) {
            return false;
        }
        //首先根据类型和值获取到对应的model数组
        UserExample condition = new UserExample();
        UserExample.Criteria criteria =  condition.createCriteria();
        
        criteria.andStatusEqualTo(AppConstants.ZERO_Byte);
        //构造查询条件
        for(FilterData fd: equalsField){
            switch(fd.getType()) {
            case FIELD_TYPE_ID:
            	criteria.andIdEqualTo(TypeParseUtil.parseToLongFromObject(fd.getValue()));
            	break;
			case FIELD_TYPE_EMAIL:
				criteria.andEmailEqualTo(TypeParseUtil.parseToStringFromObject(fd.getValue()));
				break;
			case FIELD_TYPE_PHONE:
				criteria.andPhoneEqualTo(TypeParseUtil.parseToStringFromObject(fd.getValue()));
                    break;
                default:
                    break;
            }
        }
        //查询
        int count = userMapper.countByExample(condition);
        if(count > 0){
            return true;
        }
        return false;
	}

	@Override
	protected String getProcessTableName() {
		return UniBundle.getMsg("data.filter.table.name.user");
	}
}
