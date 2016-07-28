package com.dianrong.common.uniauth.server.datafilter.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.server.data.entity.Grp;
import com.dianrong.common.uniauth.server.data.entity.GrpExample;
import com.dianrong.common.uniauth.server.data.mapper.GrpMapper;
import com.dianrong.common.uniauth.server.datafilter.FieldType;
import com.dianrong.common.uniauth.server.datafilter.FilterData;
import com.dianrong.common.uniauth.server.datafilter.FilterType;
import com.dianrong.common.uniauth.server.util.TypeParseUtil;
import com.dianrong.common.uniauth.server.util.UniBundle;

/**.
 * 组的数据过滤处理实现.
 * @author wanglin
 */
@Service("groupDataFilter")
public class GroupDataFilter extends CurrentAbstractDataFilter {
	
	@Autowired
    private GrpMapper grpMapper;

	@Override
	protected void doFilterFieldValueIsExist(FieldType type, Integer id, Object fieldValue) {
		switch(type){
			case FIELD_TYPE_CODE:
					String newCode = TypeParseUtil.parseToStringFromObject(fieldValue);
					Grp grpInfo = grpMapper.selectByIdWithStatusEffective(id);
					if(grpInfo != null){
						//如果数据信息没有改变  则不管
						if(newCode.equals(grpInfo.getCode())){
							break;
						}
					} 
					//查看是否存在其他的记录是该code
					this.dataFilter(FieldType.FIELD_TYPE_CODE, newCode, FilterType.FILTER_TYPE_EXSIT_DATA);
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
        GrpExample condition = new GrpExample();
        GrpExample.Criteria criteria =  condition.createCriteria();
        
        criteria.andStatusEqualTo(AppConstants.ZERO_Byte);
        
        //构造查询条件
        for(FilterData fd: equalsField){
            switch(fd.getType()) {
            case FIELD_TYPE_ID:
            	criteria.andIdEqualTo(TypeParseUtil.parseToIntegerFromObject(fd.getValue()));
				break;
			case FIELD_TYPE_CODE:
				criteria.andCodeEqualTo(TypeParseUtil.parseToStringFromObject(fd.getValue()));
				break;
                default:
                    break;
            }
        }
        //查询
        int count = grpMapper.countByExample(condition);
        if(count > 0){
            return true;
        }
        return false;
	}

	@Override
	protected String getProcessTableName() {
		return UniBundle.getMsg("data.filter.table.name.group");
	}
}
