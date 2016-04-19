package com.dianrong.common.uniauth.server.datafilter.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.server.data.entity.RoleExample;
import com.dianrong.common.uniauth.server.data.mapper.RoleMapper;
import com.dianrong.common.uniauth.server.datafilter.FilterData;
import com.dianrong.common.uniauth.server.util.TypeParseUtil;

/**.
 * 角色的数据过滤处理实现.
 * @author wanglin
 */
@Service("roleDataFilter")
public class RoleDataFilter extends CurrentAbstractDataFilter {
	
	@Autowired
    private RoleMapper roleMapper;
	
	@Override
	protected boolean dataWithConditionsEqualExist(FilterData... equalsField) {
		 //判空处理
        if(equalsField == null || equalsField.length == 0) {
            return false;
        }
        //首先根据类型和值获取到对应的model数组
        RoleExample condition = new RoleExample();
        RoleExample.Criteria criteria =  condition.createCriteria();
        
        criteria.andStatusEqualTo(AppConstants.ZERO_Byte);
        //构造查询条件
        for(FilterData fd: equalsField){
            switch(fd.getType()) {
                case FIELD_TYPE_ID:
                    criteria.andIdEqualTo(TypeParseUtil.parseToIntegerFromObject(fd.getValue()));
                    break;
                default:
                    break;
            }
        }
        //查询
        int count = roleMapper.countByExample(condition);
        if(count > 0){
            return true;
        }
        return false;
	}

	@Override
	protected String getProcessTableName() {
		return "角色";
	}
}
