package com.dianrong.common.auth.server.data.mapperAdapter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dianrong.common.auth.server.data.entity.SlThirdLogin;
import com.dianrong.common.auth.server.data.entity.SlThirdLoginExample;
import com.dianrong.common.auth.server.data.mapper.SlThirdLoginMapper;

public class SlThirdLoginMapperAdapter extends BaseMapperAdapter{
    
	/**.
	 * 数据处理 mapper
	 */
	@Autowired
	private SlThirdLoginMapper slThirdLoginMapper;
	
   public  int insert(SlThirdLogin record) {
    	return this.slThirdLoginMapper.insert(record);
    }

    public List<SlThirdLogin> selectByExample(SlThirdLoginExample example) {
    	return slThirdLoginMapper.selectByExample(example);
    }

    public int updateByPrimaryKeySelective(SlThirdLogin record) {
    	return slThirdLoginMapper.updateByPrimaryKeySelective(record);
    }
}