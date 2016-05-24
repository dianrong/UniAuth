package com.dianrong.common.auth.server.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dianrong.common.auth.server.data.entity.SlBehaviorLog;
import com.dianrong.common.auth.server.data.entity.SlBehaviorLogExample;
import com.dianrong.common.auth.server.data.entity.SlBehaviorLogExample.Criteria;
import com.dianrong.common.auth.server.data.mapperAdapter.SlBehaviorLogMapperAdapter;
import com.dianrong.common.uniauth.common.bean.dto.SlBehaviorLogDto;
import com.dianrong.common.uniauth.common.bean.dto.SlBehaviorLogEnum;
import com.dianrong.common.uniauth.common.util.StringUtil;

@Service
public class SlBehaviorLogService {
	/**.
	 * 日志处理对象
	 */
	  private final Logger logger = LoggerFactory.getLogger(getClass());
	
	/**.
	 * 信息操作mapper
	 */
	@Autowired
	private SlBehaviorLogMapperAdapter slBehaviorLogMapper;
	
	 /**
	   * log to sl$behavior_log table
	   * @param seqName
	   * @param fetchSize
	   * @return 
	   */
	  public void insertBehaviorLog(SlBehaviorLogDto log) {
		   try {
			  // 插入数据
			  SlBehaviorLog insertInfo = new SlBehaviorLog();
			  
			  //id 不设  采用oracle自增长的
			  insertInfo.setAid(log.getAid());
			  insertInfo.setType(log.getType());
			  insertInfo.setTarget(log.getTarget());
			  insertInfo.setIpAddr(log.getIpAddr());
			  insertInfo.setCreateD(new Date());
			  insertInfo.setResult(log.getResult());
			  
			  slBehaviorLogMapper.insert(insertInfo);
		   } catch (Exception e) {
			      logger.error("failed to add behavior log: ", e);
			      throw e;
		  }
	  }
	  
	  /**
	   * 根据手机号查询behaviorLog记录数
	   * */
	  public long countBehaviorLogsByCellphone(String cellphone, Short type, Date startDate) {
		   try {
			  // query coditions
			  SlBehaviorLogExample condtion = new SlBehaviorLogExample();
			  Criteria criteria = condtion.createCriteria();
			  if(!StringUtil.strIsNullOrEmpty(cellphone)) {
				  criteria.andTargetEqualTo(cellphone);
			  }
			  if(type != null) {
				  criteria.andTypeEqualTo(type);
			  }
			  if(startDate != null) {
				  criteria.andCreateDGreaterThanOrEqualTo(startDate);
			  }
			  return  slBehaviorLogMapper.selectInfoCount(condtion);
		   } catch (Exception e) {
			      logger.warn("fail to count behaviorlog " + e.getMessage());
			      throw e;
		   }
	  }
	  
	  /**
	   * 根据ip查询behaviorLog记录数
	   * */
	  public long countBehaviorLogsByIp(String ip, Short type, Date startDate) {
	    return countBehaviorLogsByIp(ip, type, startDate, null);
	  }

	  /**
	   * 根据ip查询behaviorLog记录数
	   * */
	  public long countBehaviorLogsByIp(String ip, Short type, Date startDate, Short result) {
		  try {
			  // query coditions
			  SlBehaviorLogExample condtion = new SlBehaviorLogExample();
			  Criteria criteria = condtion.createCriteria();
			  if(!StringUtil.strIsNullOrEmpty(ip)) {
				  criteria.andIpAddrEqualTo(ip);
			  }
			  if(type != null) {
				  criteria.andTypeEqualTo(type);
			  }
			  if(startDate != null) {
				  criteria.andCreateDGreaterThanOrEqualTo(startDate);
			  }
			  if(result != null) {
				  criteria.andResultEqualTo(result);
			  }
			  return  slBehaviorLogMapper.selectInfoCount(condtion);
		   } catch (Exception e) {
			      logger.warn("fail to count behaviorlog " + e.getMessage());
			      throw e;
		   }
	  }
	  
	  /**
	   * 根据用户ID查询behaviorLog记录
	   * */
	  public List<Short> queryBehaviorLogsByAid(Long aid) {
		  try {
			  // 查询条件
			  SlBehaviorLogEnum.Type type = SlBehaviorLogEnum.Type.LOGIN_USER_ID;
			  SlBehaviorLog record = new SlBehaviorLog();
			  record.setAid(new BigDecimal(aid));
			  record.setType(type.getValue());
			  record.setCreateD(type.getStartDate());
			  record.setPageOffSet(0);
			  record.setPageSize(type.getLimitInTimeUnit());
			  record.setOrderByClauseDesc("id");
			  List<SlBehaviorLog> resultInfo = slBehaviorLogMapper.selectInfoByPage(record);
			  if(resultInfo != null) {
				  List<Short> rlist = new ArrayList<Short>();
				  for(SlBehaviorLog tl : resultInfo) {
					  rlist.add(tl.getResult());
				  }
				  return rlist;
			  }
		  } catch (Exception e) {
		      logger.warn("fail to count behavior log by param "+ e.getMessage());
		      throw e;
		    }
		  return null;
	  }

	  /**
	   * 根据ip查询behaviorLog记录
	   * */
	  public List<Short> queryBehaviorLogsByIp(String ip) {
		  try {
			  // 查询条件
			  SlBehaviorLogEnum.Type type = SlBehaviorLogEnum.Type.LOGIN_IP;
			  SlBehaviorLog record = new SlBehaviorLog();
			  record.setIpAddr(ip);
			  record.setType(type.getValue());
			  record.setCreateD(type.getStartDate());
			  record.setPageOffSet(0);
			  record.setPageSize(type.getLimitInTimeUnit());
			  record.setOrderByClauseDesc("id");
			  List<SlBehaviorLog> resultInfo = slBehaviorLogMapper.selectInfoByPage(record);
			  if(resultInfo != null) {
				  List<Short> rlist = new ArrayList<Short>();
				  for(SlBehaviorLog tl : resultInfo) {
					  rlist.add(tl.getResult());
				  }
				  return rlist;
			  }
		  } catch (Exception e) {
		      logger.warn("fail to count behavior log by param "+ e.getMessage());
		      throw e;
		    }
		  return null;
	  }
}
