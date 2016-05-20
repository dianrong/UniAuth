package com.dianrong.common.uniauth.server.datafilter.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dianrong.common.uniauth.server.data.entity.UserExtend;
import com.dianrong.common.uniauth.server.data.entity.UserExtendExample;
import com.dianrong.common.uniauth.server.data.mapper.UserExtendMapper;
import com.dianrong.common.uniauth.server.datafilter.FieldType;
import com.dianrong.common.uniauth.server.datafilter.FilterData;
import com.dianrong.common.uniauth.server.datafilter.FilterType;
import com.dianrong.common.uniauth.server.util.TypeParseUtil;

/**.
 * 用户扩展字段数据过滤处理
 * @author wanglin
 */
@Service("userExtendDataFilter")
public class UserExtendDataFilter extends CurrentAbstractDataFilter{
	/**.
	 * dao
	 */
	@Autowired
	 private UserExtendMapper userExtendMapper;
	
	@Override
	protected void doFilterFieldValueIsExist(FieldType type, Integer id, Object fieldValue) {
		switch(type){
		case FIELD_TYPE_CODE:
			String newCode = TypeParseUtil.parseToStringFromObject(fieldValue);
			UserExtendExample condtion = new UserExtendExample();
			condtion.createCriteria().andIdEqualTo(TypeParseUtil.parseToLongFromObject(id));
			List<UserExtend> infoes =  userExtendMapper.selectByExample(condtion);
			if(infoes != null && infoes.size() > 0){
				//如果数据信息没有改变  则不管
				if(newCode.equals(infoes.get(0).getCode())){
					break;
				}
			}
			//查看是否存在其他的记录是该信息
			this.dataFilter(type, newCode, FilterType.FILTER_TYPE_EXSIT_DATA);
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
        UserExtendExample condition = new UserExtendExample();
        UserExtendExample.Criteria criteria =  condition.createCriteria();
        //构造查询条件
        for(FilterData fd: equalsField){
            switch(fd.getType()) {
            case FIELD_TYPE_CODE:
            	criteria.andCodeEqualTo(TypeParseUtil.parseToStringFromObject(fd.getValue()));
            	break;
                default:
                    break;
            }
        }
        //查询
        int count = userExtendMapper.countByExample(condition);
        if(count > 0){
            return true;
        }
        return false;
	}

	@Override
	protected String getProcessTableName() {
		return "用户扩展数据";
	}
}
