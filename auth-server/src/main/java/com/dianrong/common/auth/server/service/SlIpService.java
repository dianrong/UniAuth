package com.dianrong.common.auth.server.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dianrong.common.auth.server.data.entity.SlIp;
import com.dianrong.common.auth.server.data.entity.SlIpExample;
import com.dianrong.common.auth.server.data.mapperAdapter.SlIpMapperAdapter;
import com.dianrong.common.auth.server.util.BeanConverter;
import com.dianrong.common.uniauth.common.bean.dto.SlIpDto;

@Service
public class SlIpService {
	/**.
	 * 日志处理对象
	 */
	  private final Logger logger = LoggerFactory.getLogger(getClass());
	
	/**.
	 * 数据处理 mapper
	 */
	@Autowired
	private SlIpMapperAdapter slIpMapper;
	
	/**.
	 * 用于获取对应的表名的服务
	 */
	@Autowired
	private SlSequenceService sequenceService;
	
	/**.
	 * 获取表名对应的seq_name
	 */
	private final String sq_name = "ip_seq";
	
	/**.
	 * 插入slip的数据
	 * @param address 
	 */
	  public void addIpAddress(SlIpDto address) {
	    try {
			  // 插入数据model
			  SlIp insertInfo = new SlIp();
			  insertInfo.setId(new BigDecimal(sequenceService.assignSequenceNumber(sq_name)));
			  insertInfo.setAid(address.getAid());
			  insertInfo.setIpAddr(address.getIpAddr());
			  insertInfo.setType(address.getType());
			  insertInfo.setStatus(address.getStatus());
			  insertInfo.setLatitude(address.getLatitude());
			  insertInfo.setLongitude(address.getLongitude());
			  insertInfo.setCountry(address.getCountry());
			  insertInfo.setRegion(address.getRegion());
			  insertInfo.setAreaCode(address.getAreaCode());
			  insertInfo.setCity(address.getCity());
			  insertInfo.setCreateD(new Date());
	    	  slIpMapper.insert(insertInfo);
	    } catch (Exception e) {
	      logger.warn("failed to add ip: " + e.getMessage());
	      throw e;
	    }
	  }
	  
	  
	  /**.
	   * 根据aid获取SlIp数据
	   * @param aid aid
	   * @return SlIp 列表
	   */
	  public List<SlIpDto> getIpAddresses(long aid) {
		  List<SlIpDto> result = new ArrayList<SlIpDto>();
		  try {
			  SlIpExample condtion = new SlIpExample();
			  condtion.createCriteria().andAidEqualTo(new BigDecimal(aid));
			  List<SlIp> infoes = slIpMapper.selectByExample(condtion);
			  if(infoes != null && !infoes.isEmpty()) {
				  for(SlIp tip : infoes) {
					  result.add(BeanConverter.convert(tip));
				  }
			  }
		  } catch (Exception e) {
		      logger.warn("fail to get Ip address: " + e.getMessage());
		      // ignore error
		    }
		  return result;
	  }
	  
	  /**.
	   * 根据aid获取SlIp数据中最近生成的一条
	   * @param aid aid
	   * @return 第一条数据
	   */
	  public SlIpDto getLastIpAddress(long aid) {
		  try {
			  SlIp condtion = new SlIp();
			  condtion.setAid(new BigDecimal(aid));
			  condtion.setPageOffSet(0);
			  condtion.setPageSize(1);
			  condtion.setDescClause("create_d");
			  List<SlIp> infoes = slIpMapper.selectForPage(condtion);
			  if(infoes != null && !infoes.isEmpty()) {
					 return BeanConverter.convert(infoes.get(0));
			  }
		  } catch (Exception e) {
		       logger.warn("fail to get Ip address: " + e.getMessage());
		      // ignore error
		    }
		  return null;
	  }
	  
	  
}
