package com.dianrong.common.auth.server.data.mapperAdapter;

import java.util.List;

import javax.ws.rs.NotSupportedException;

import org.springframework.beans.factory.annotation.Autowired;

import com.dianrong.common.auth.server.data.entity.SlBehaviorLog;
import com.dianrong.common.auth.server.data.entity.SlBehaviorLogExample;
import com.dianrong.common.auth.server.data.mapper.SlBehaviorLogMapper;

public class SlBehaviorLogMapperAdapter extends BaseMapperAdapter{
	
	/**.
	 * 信息操作mapper
	 */
	@Autowired
	private SlBehaviorLogMapper slBehaviorLogMapper;
	
    public int insert(SlBehaviorLog record) {
    	if(isMysql()) {
    		return slBehaviorLogMapper.insert_mysql(record);
    	} 
    	if(isOracle()) {
    		return slBehaviorLogMapper.insert_oracle(record);
    	}
    	throw new NotSupportedException("check your config, spring config baseAdapterMapper's dialect. the service just support oracle and mysql");
    }

    /**.
     * 根据条件查询数据跳数
     * @param example 条件
     * @return 跳数
     */
    public  long selectInfoCount(SlBehaviorLogExample example) {
    	return  slBehaviorLogMapper.selectInfoCount(example);
    }
    
    /**.
     * 提供分页查询数据的功能
     * @param record condtion
     * @return 结果
     */
   public  List<SlBehaviorLog> selectInfoByPage(SlBehaviorLog record) {
	   if(isMysql()) {
   		return slBehaviorLogMapper.selectInfoByPage_MySql(record);
	   	} 
	   	if(isOracle()) {
	   		return slBehaviorLogMapper.selectInfoByPage_Oracle(record);
	   	}
	   	throw new NotSupportedException("check your config, spring config baseAdapterMapper's dialect. the service just support oracle and mysql");
    }
}