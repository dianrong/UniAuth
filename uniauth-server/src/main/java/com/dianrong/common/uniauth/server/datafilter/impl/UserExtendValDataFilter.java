package com.dianrong.common.uniauth.server.datafilter.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dianrong.common.uniauth.server.data.entity.UserExtendVal;
import com.dianrong.common.uniauth.server.data.entity.UserExtendValExample;
import com.dianrong.common.uniauth.server.data.mapper.UserExtendValMapper;
import com.dianrong.common.uniauth.server.datafilter.FilterData;
import com.dianrong.common.uniauth.server.datafilter.FilterType;
import com.dianrong.common.uniauth.server.util.TypeParseUtil;

/**.
 * 用户扩展值数据过滤
 * @author wanglin
 */
@Service("userExtendValDataFilter")
public class UserExtendValDataFilter extends CurrentAbstractDataFilter{
	/**.
	 * dao
	 */
	@Autowired
	 private UserExtendValMapper userExtendValMapper;
	
	@Override
	protected void doFilterFieldValueIsExistWithConditionsEqual(Integer id, FilterData... equalsField) {
		UserExtendValExample condtion = new UserExtendValExample();
		condtion.createCriteria().andIdEqualTo(TypeParseUtil.parseToLongFromObject(id));
		List<UserExtendVal> infoes =  userExtendValMapper.selectByExample(condtion);
		// 如果数据信息没有改变  则不管
		boolean allEqual = false;
		if(infoes != null && infoes.size() > 0){
			UserExtendVal tinfo = infoes.get(0);
			// 如果数据信息没有改变  则不管
			allEqual = true;
			for(FilterData filterData: equalsField) {
				switch(filterData.getType()){
					case FIELD_TYPE_USER_ID:
						Long n_userId = TypeParseUtil.parseToLongFromObject(filterData.getValue());
						Long o_userId = tinfo.getUserId();
						if(!o_userId.equals(n_userId)) {
							allEqual = false;
						} 
						break;
					case FIELD_TYPE_EXTEND_ID:
						Long n_extendId = TypeParseUtil.parseToLongFromObject(filterData.getValue());
						Long o_extendId = tinfo.getExtendId();
						if(!o_extendId.equals(n_extendId)) {
							allEqual = false;
						} 
						break;
					default:
						break;
					}
			}
		}
		if(!allEqual) {
			//查看是否存在其他的记录是该信息
			this.dataFilterWithConditionsEqual(FilterType.FILTER_TYPE_EXSIT_DATA, equalsField );
		}
	}
	
	@Override
	protected boolean dataWithConditionsEqualExist(FilterData... equalsField) {
		 //判空处理
        if(equalsField == null || equalsField.length == 0) {
            return false;
        }
        //首先根据类型和值获取到对应的model数组
        UserExtendValExample condition = new UserExtendValExample();
        UserExtendValExample.Criteria criteria =  condition.createCriteria();
        //构造查询条件
        for(FilterData fd: equalsField){
            switch(fd.getType()) {
            case FIELD_TYPE_USER_ID:
            	criteria.andUserIdEqualTo(TypeParseUtil.parseToLongFromObject(fd.getValue()));
            	break;
            case FIELD_TYPE_EXTEND_ID:
            	criteria.andExtendIdEqualTo(TypeParseUtil.parseToLongFromObject(fd.getValue()));
            	break;
                default:
                    break;
            }
        }
        //查询
        int count = userExtendValMapper.countByExample(condition);
        if(count > 0){
            return true;
        }
        return false;
	}

	@Override
	protected String getProcessTableName() {
		return "用户扩展值数据";
	}
}
