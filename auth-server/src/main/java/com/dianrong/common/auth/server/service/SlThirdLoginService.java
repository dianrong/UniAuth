package com.dianrong.common.auth.server.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dianrong.common.auth.server.data.entity.SlThirdLogin;
import com.dianrong.common.auth.server.data.entity.SlThirdLoginExample;
import com.dianrong.common.auth.server.data.mapperAdapter.SlThirdLoginMapperAdapter;
import com.dianrong.common.auth.server.util.BeanConverter;
import com.dianrong.common.uniauth.common.bean.dto.SlThirdLoginDto;

/**.
 * 第三方登陆处理
 * @author wanglin
 *
 */
@Service
public class SlThirdLoginService {
	/**.
	 * 日志处理对象
	 */
	  private final Logger logger = LoggerFactory.getLogger(getClass());
	
	  /**.
		 * 数据处理 mapper
		 */
		@Autowired
		private SlThirdLoginMapperAdapter slThirdLoginMapper;
	  
	/**
	   * set expiration to yesterday and expires this token
	   * @param openUid openUid
	   * @param newTime  newTime
	   */
	  public void expireAccessToken(String openUid, long newTime) {
		  try {
			  SlThirdLogin record = new SlThirdLogin();
			  record.setExpiresIn(newTime+ "'");
			  record.setOpenUid(openUid);
			  record.setLoginType((short)SlThirdLoginDto.SourceType.DIANRONG.ordinal());
			  slThirdLoginMapper.updateByPrimaryKeySelective(record);
		    } catch (Exception e) {
		      logger.warn("failed to update token: " + e.getMessage());
		      throw e;
		    }
	  }
	  
	  /**
	   * upsert token a token into sl$third_login table
	   * @param token token
	   */
	  public void createToken(SlThirdLoginDto token) {
		  try {
			  // 先查询
			  SlThirdLoginExample query = new SlThirdLoginExample();
			  query.createCriteria().andOpenUidEqualTo(token.getOpenUid()).andLoginTypeEqualTo(token.getLoginType());
			  List<SlThirdLogin> queryInfo = slThirdLoginMapper.selectByExample(query);
			  // 存在则更新
			  if(queryInfo != null && !queryInfo.isEmpty()) {
				  SlThirdLogin update = new SlThirdLogin();
				  update.setExpiresIn(token.getExpiresIn() == null ? "" : token.getExpiresIn());
				  update.setOpenUid(token.getOpenUid());
				  update.setLoginType(token.getLoginType());
				  slThirdLoginMapper.updateByPrimaryKeySelective(update);
			  } else {
				  // 不存在则添加
				  SlThirdLogin insert = new SlThirdLogin();
				  insert.setBindAid(token.getBindAid());
				  insert.setOpenUid(token.getOpenUid());
				  insert.setLoginType(token.getLoginType());
				  insert.setNickName(token.getNickName());
				  insert.setAccessToken(token.getAccessToken());
				  insert.setExpiresIn(token.getExpiresIn());
				  slThirdLoginMapper.insert(insert);
			  }
		  } catch (Exception e) {
		      logger.warn("failed to save token: " + e.getMessage());
		      throw e;
		    }
	  }
	  
	  /**
	   * get token from sl$third_login table
	   */
	  public SlThirdLoginDto getThirdPartyLogin(String openUid, SlThirdLoginDto.SourceType type) {
		  try {
			  if(type == null) {
				  throw new NullPointerException("SourceType can not be null");
			  }
			  // 先查询
			  SlThirdLoginExample query = new SlThirdLoginExample();
			  query.createCriteria().andOpenUidEqualTo(openUid).andLoginTypeEqualTo((short)type.ordinal());
			  List<SlThirdLogin> queryInfo = slThirdLoginMapper.selectByExample(query);
			  
			  // 返回第一个
			  if(queryInfo != null && !queryInfo.isEmpty()) {
				  return BeanConverter.convert(queryInfo.get(0));
			  }
		  } catch (Exception e) {
		      logger.warn("failed to getThirdPartyLogin: " + e.getMessage());
		      throw e;
		    }
		  return null;
	  }
}
