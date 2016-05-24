package com.dianrong.common.auth.server.service;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dianrong.common.auth.server.data.entity.SlActor;
import com.dianrong.common.auth.server.data.entity.SlActorExample;
import com.dianrong.common.auth.server.data.entity.SlActorExample.Criteria;
import com.dianrong.common.auth.server.data.entity.SlActorWithBLOBs;
import com.dianrong.common.auth.server.data.mapperAdapter.SlActorMapperAdapter;
import com.dianrong.common.auth.server.util.BeanConverter;
import com.dianrong.common.uniauth.common.bean.dto.SlActorDto;
import com.dianrong.common.uniauth.common.bean.dto.SlActorEnum;
import com.dianrong.common.uniauth.common.util.StringUtil;

/**.
 * operate slActor data
 * @author wanglin
 */
@Service
public class SlActorService {
	/**.
	 * 日志处理对象
	 */
	  private final Logger logger = LoggerFactory.getLogger(getClass());
	
	  /**.
		 * 用户信息操作mapper
		 */
		@Autowired
		private SlActorMapperAdapter slActorMapper;
	  
	/**.
	 * 辅助查询的actorType类型
	 */
	private static SlActorEnum.Type[] types = new SlActorEnum.Type[] { SlActorEnum.Type.USER,  SlActorEnum.Type.OPERATOR, SlActorEnum.Type.ADMIN };
	/**.
	 * query userinfo by userName
	 * 返回的userInfo必须是指定type类型的
	 * @param name userName
	 * @return userInfoModel
	 */
	public SlActorDto getUserByName(String name){
		if(StringUtil.strIsNullOrEmpty(name)) {
			throw new NullPointerException("name can not be null");
		}
		// 构造查询条件
		SlActorExample conditions = new SlActorExample();
		Criteria criteria = conditions.createCriteria();
		criteria.andNameEqualTo(name);
	    try {
	    	List<SlActorWithBLOBs> actors = slActorMapper.selectByExampleWithBLOBs(conditions);
	    	// return actor of type USER, OPERATOR or ADMIN, which ever found first
	    	// sort in memory to reduce DB load
	    	if(actors != null && !actors.isEmpty()) {
	    		//filter
	    		 for (int i = 0; i < types.length; i++) {
	    		        for (int j = 0; j < actors.size(); j++) {
	    		        	SlActorWithBLOBs actor = actors.get(i);
	    		          if (SlActorEnum.Type.fromShort(actor.getType()) == types[i]) {
	    		            return BeanConverter.convert(actor);
	    		          }
	    		       }
	    		 	}
	    		} 
	    	}catch(Exception e) {
	          logger.warn("user not found {} : {}", name, e.getMessage());
	          throw e;
	        }
	        return null;
	}
	
	  /**
	   * email should be unique
	   * @param email
	   * @return actor
	   */
	  public SlActorDto getUserByEmail(String email) {
		  	if(StringUtil.strIsNullOrEmpty(email)) {
		  			throw new NullPointerException("email can not be null");
			}
		  
		  	// 构造查询条件
			SlActorExample conditions = new SlActorExample();
			Criteria criteria = conditions.createCriteria();
			criteria.andEmailEqualTo(email);
		    try {
		    	List<SlActorWithBLOBs> actors = slActorMapper.selectByExampleWithBLOBs(conditions);
		    	if(actors == null || actors.isEmpty()) {
		    		return null;
		    	}
		    	return BeanConverter.convert(actors.get(0));
		    }catch(Exception e) {
		    	logger.warn("user email not found {} : {}", email, e.getMessage());
		         throw e;
		     }
	  }
	  
	  /**
	   * cell phone should be unique
	   * 
	   * @param cellphone
	   * @return actor
	   */
	  public SlActorDto getUserByCellphone(String cellphone) {
		  if(StringUtil.strIsNullOrEmpty(cellphone)) {
	  			throw new NullPointerException("cellphone can not be null");
		}
	  	// 构造查询条件
		SlActorExample conditions = new SlActorExample();
		Criteria criteria = conditions.createCriteria();
		criteria.andCellphoneEqualTo(cellphone);
	    try {
	    	List<SlActorWithBLOBs> actors = slActorMapper.selectByExampleWithBLOBs(conditions);
	    	if(actors == null || actors.isEmpty()) {
	    		return null;
	    	}
	    	return BeanConverter.convert(actors.get(0));
	    }catch(Exception e) {
	    	logger.warn("user cellphone not found {} : {}", cellphone, e.getMessage());
	         throw e;
	     }
	  }
	  
	  /**
	   * get uesr by ID
	   * @param id
	   * @return actor
	   */
	  public SlActorDto getUserByID(Long id) {
		  if( id == null) {
			  return null;
		  }
		  	// 构造查询条件
			SlActorExample conditions = new SlActorExample();
			Criteria criteria = conditions.createCriteria();
			criteria.andIdEqualTo(new BigDecimal(id));
		    try {
		    	List<SlActorWithBLOBs> actors = slActorMapper.selectByExampleWithBLOBs(conditions);
		    	if(actors == null || actors.isEmpty()) {
		    		return null;
		    	}
		    	return BeanConverter.convert(actors.get(0));
		    }catch(Exception e) {
		    	logger.warn("user id not found {} : {}", id, e.getMessage());
		         throw e;
		     }
	  }
	  
	  /**.
	   * update actor's  FAILED_AUTH_CNT by id 
	   * @param id userId
	   * @param failureCnt newFailureCnt
	   * @return result
	   */
	  public int updateLoginFailureCnt(Long id, int failureCnt) {
		  if(id == null) {
	  			throw new NullPointerException("id can not be null");
		  }
		  
		  //更新model
		  SlActor updateCondtion = new SlActor();
		  updateCondtion.setId(new BigDecimal(id));
		  updateCondtion.setFailedAuthCnt((short)failureCnt);
	      try {
	    	  return slActorMapper.updateByPrimaryKey(updateCondtion);
	      }catch(Exception e) {
	    	  logger.warn("failed to update failure cnt " + e.getMessage());
		      return 0; // ignore error
	     }
	  }
	  
	  /**
	   * set salted credentials
	   * 
	   * @param accessToken
	   */
	  public void updateCredential(long id, byte[] credential, byte[] credentialSalt) {
		  if(credential == null || credentialSalt == null) {
	  			throw new NullPointerException("credential can not be null");
		}
		  //更新model
		  SlActorWithBLOBs updateCondtion = new SlActorWithBLOBs();
		  updateCondtion.setId(new BigDecimal(id));
		  updateCondtion.setCredential(credential);
		  updateCondtion.setCredentialSalt(credentialSalt);
	      try {
	    	  slActorMapper.updateByPrimaryKeyWithBLOBs(updateCondtion);
	      }catch(Exception e) {
	    	  logger.warn("failed to update token " + e.getMessage());
		      throw e;
	     }
	  }
}
