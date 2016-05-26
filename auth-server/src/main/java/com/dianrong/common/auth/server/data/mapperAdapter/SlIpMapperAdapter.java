package com.dianrong.common.auth.server.data.mapperAdapter;

import java.util.List;

import javax.ws.rs.NotSupportedException;

import org.springframework.beans.factory.annotation.Autowired;

import com.dianrong.common.auth.server.data.entity.SlIp;
import com.dianrong.common.auth.server.data.entity.SlIpExample;
import com.dianrong.common.auth.server.data.mapper.SlIpMapper;

public class SlIpMapperAdapter extends BaseMapperAdapter{
    
	/**.
	 * 数据处理 mapper
	 */
	@Autowired
	private SlIpMapper slIpMapper;
	
   public int insert(SlIp record) {
	   return slIpMapper.insert(record);
   }

    public List<SlIp> selectByExample(SlIpExample example) {
    	return slIpMapper.selectByExample(example);
    }

    /**.
	  * 分页查询 oracle的版本 注意后期需要修改成mysql的版本 
	  * @param record record
	  * @return 结果
	  */
    public List<SlIp> selectForPage(SlIp record) {
    	  if(isMysql()) {
	   		return slIpMapper.selectForPage_MySql(record);
		   	} 
		   	if(isOracle()) {
		   		return slIpMapper.selectForPage_Oracle(record);
		   	}
		   	throw new NotSupportedException("check your config, spring config baseAdapterMapper's dialect. the service just support oracle and mysql");
    }
}