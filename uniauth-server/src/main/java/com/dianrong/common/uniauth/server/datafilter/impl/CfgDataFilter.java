package com.dianrong.common.uniauth.server.datafilter.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dianrong.common.uniauth.server.data.entity.Cfg;
import com.dianrong.common.uniauth.server.data.entity.CfgExample;
import com.dianrong.common.uniauth.server.data.mapper.CfgMapper;
import com.dianrong.common.uniauth.server.datafilter.FieldType;
import com.dianrong.common.uniauth.server.datafilter.FilterData;
import com.dianrong.common.uniauth.server.datafilter.FilterType;
import com.dianrong.common.uniauth.server.util.TypeParseUtil;

/**.
 * 配置的数据过滤处理实现.
 * @author wanglin
 */
@Service("cfgDataFilter")
public class CfgDataFilter extends CurrentAbstractDataFilter {
	
	@Autowired
    private CfgMapper cfgMapper;
	
	/**.
	 * 判断数据是否已经重复了
	 */
	@Override
	public void doFilterFieldValueIsExist(FieldType type, Integer id, Object fieldValue){
		switch(type){
			case FIELD_TYPE_CFG_KEY:
				String newCfgKey = TypeParseUtil.parseToStringFromObject(fieldValue);
				Cfg cfgInfo = cfgMapper.selectByIdWithStatusEffective(id);
				if(cfgInfo != null){
					//如果数据信息没有改变  则不管
					if(newCfgKey.equals(cfgInfo.getCfgKey())){
						break;
					}
				}
				//查看是否存在其他的记录是该信息
				this.dataFilter(FieldType.FIELD_TYPE_CFG_KEY, newCfgKey, FilterType.FILTER_TYPE_EXSIT_DATA);
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
        CfgExample condition = new CfgExample();
        CfgExample.Criteria criteria =  condition.createCriteria();
        
        //构造查询条件
        for(FilterData fd: equalsField){
            switch(fd.getType()) {
                case FIELD_TYPE_CFG_KEY:
                    criteria.andCfgKeyEqualTo(TypeParseUtil.parseToStringFromObject(fd.getValue()));
                    break;
                default:
                    break;
            }
        }
        //查询
        int count = cfgMapper.countByExample(condition);
        if(count > 0){
            return true;
        }
        return false;
	}

	@Override
	protected String getProcessTableName() {
		return "配置数据";
	}
}
