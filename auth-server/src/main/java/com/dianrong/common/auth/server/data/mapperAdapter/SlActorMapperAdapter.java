package com.dianrong.common.auth.server.data.mapperAdapter;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dianrong.common.auth.server.data.entity.SlActor;
import com.dianrong.common.auth.server.data.entity.SlActorExample;
import com.dianrong.common.auth.server.data.entity.SlActorWithBLOBs;
import com.dianrong.common.auth.server.data.mapper.SlActorMapper;

public class SlActorMapperAdapter extends BaseMapperAdapter{
	
	/**.
	 * 用户信息操作mapper
	 */
	@Autowired
	private SlActorMapper slActorMapper;
	
    public int countByExample(SlActorExample example){
    	return slActorMapper.countByExample(example);
    }

    public int deleteByExample(SlActorExample example){
    	return slActorMapper.deleteByExample(example);
    }

   public int deleteByPrimaryKey(BigDecimal id){
	   return slActorMapper.deleteByPrimaryKey(id);
   }

    public int insert(SlActorWithBLOBs record) {
    	return slActorMapper.insert(record);
    }

    public int insertSelective(SlActorWithBLOBs record) {
    	return slActorMapper.insertSelective(record);
    }

    public List<SlActorWithBLOBs> selectByExampleWithBLOBs(SlActorExample example){
    	return this.slActorMapper.selectByExampleWithBLOBs(example);
    }

    public List<SlActor> selectByExample(SlActorExample example) {
    	return slActorMapper.selectByExample(example);
    }

    public SlActorWithBLOBs selectByPrimaryKey(BigDecimal id){
    	return slActorMapper.selectByPrimaryKey(id);
    }

    public int updateByExampleSelective(SlActorWithBLOBs record, SlActorExample example) {
    	return slActorMapper.updateByExampleSelective(record, example);
    }

   public int updateByExampleWithBLOBs(SlActorWithBLOBs record, SlActorExample example) {
	   return slActorMapper.updateByExampleWithBLOBs(record, example);
   }

   public int updateByExample(SlActor record, SlActorExample example) {
	   return slActorMapper.updateByExample(record, example);
   }

    public int updateByPrimaryKeySelective(SlActorWithBLOBs record) {
    	return slActorMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKeyWithBLOBs(SlActorWithBLOBs record){
    	return slActorMapper.updateByPrimaryKeyWithBLOBs(record);
    }

    public int updateByPrimaryKey(SlActor record){
    	return slActorMapper.updateByPrimaryKey(record);
    }
}