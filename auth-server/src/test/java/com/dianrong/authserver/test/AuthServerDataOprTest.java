package com.dianrong.authserver.test;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dianrong.common.auth.server.service.SlActorService;
import com.dianrong.common.auth.server.service.SlActorVirtualLoanService;
import com.dianrong.common.auth.server.service.SlBehaviorLogService;
import com.dianrong.common.auth.server.service.SlIpService;
import com.dianrong.common.auth.server.service.SlSequenceService;
import com.dianrong.common.auth.server.service.SlThirdLoginService;
import com.dianrong.common.uniauth.common.bean.dto.SlActorDto;
import com.dianrong.common.uniauth.common.bean.dto.SlBehaviorLogDto;
import com.dianrong.common.uniauth.common.bean.dto.SlIpDto;
import com.dianrong.common.uniauth.common.bean.dto.SlThirdLoginDto;
import com.google.gson.Gson;

/**.
 * 测试auth-server的数据库api
 * @author wanglin
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring-configuration/*.xml"})
public class AuthServerDataOprTest {
	
	private Gson gson = new Gson();  
	
	/**.
	 * 用户信息操作
	 */
	@Autowired
	private SlActorService actorService;
	
	@Test
	public void testActorGetUserByName(){
		SlActorDto userByName = actorService.getUserByName("xiaozhao");
		System.out.println(gson.toJson(userByName));
	}
	
	@Test
	public void testActorgetUserByCellphone(){
		SlActorDto userByName = actorService.getUserByCellphone("15026682113");
		System.out.println(gson.toJson(userByName));
	}
	
	@Test
	public void testActorgetUserByEmail(){
		SlActorDto userByName = actorService.getUserByEmail("xiaozhao@dianrong.com");
		System.out.println(gson.toJson(userByName));
	}
	
	@Test
	public void testActogetUserByID(){
		SlActorDto userByName = actorService.getUserByID(11105823L);
		System.out.println(gson.toJson(userByName));
	}
	
	/**.
	 * xiaomi
	 */
	@Autowired
	private SlActorVirtualLoanService actorVirtualLoanService;
	
	@Test
	public void  testActorVLService(){
		System.out.println(actorVirtualLoanService.isXiaoming(10914029L));
	}
	
	@Autowired
	private SlBehaviorLogService behaviorLogService;
	
	@Test
	public void testSLBehaviroLogcountBehaviorLogsByCellphone(){
		System.out.println(behaviorLogService.countBehaviorLogsByCellphone("15982871936", (short)1, new Date()));
	}
	
	@Test
	public void testSLBehavirocountBehaviorLogsByIp(){
		System.out.println(behaviorLogService.countBehaviorLogsByIp("192.168.1.11", (short)1, new Date()));
	}
	
	@Test
	public void testSLBehavirocountBehaviorLogsByIpResult(){
		System.out.println(behaviorLogService.countBehaviorLogsByIp("192.168.1.11", (short)1, new Date(), (short)123));
	}
	
	@Test
	public void testSLBehavirqueryBehaviorLogsByAid(){
		System.out.println(gson.toJson(behaviorLogService.queryBehaviorLogsByAid(11183423L)));
	}
	
	@Test
	public void testSLBehavirqueryBehaviorLogsByIp(){
		System.out.println(gson.toJson(behaviorLogService.queryBehaviorLogsByIp("chengdu")));
	}
	
	@Test
	public void testSLBehavirinsertBehaviorLog(){
		SlBehaviorLogDto log = new SlBehaviorLogDto();
		log.setAid(new BigDecimal(11183423L));
		log.setIpAddr("chengdu");
		log.setResult((short)1);
		log.setTarget("target");
		log.setType((short)2);
		behaviorLogService.insertBehaviorLog(log);
	}
	
	@Autowired
	private SlIpService ipService;
	
	@Test
	public void testSLIpaddIpAddress(){
		SlIpDto dto = new SlIpDto();
		dto.setAid(new BigDecimal(11183423L));
		dto.setAreaCode("chengdu");
		dto.setCity("chengdu");
		dto.setCountry("china");
		dto.setIpAddr("chengdu");
		dto.setLatitude(new BigDecimal("12"));
		dto.setLongitude(new BigDecimal("12"));
		dto.setType((short)2);
		dto.setStatus((short)2);
		dto.setRegion("chengdu");
		ipService.addIpAddress(dto);
	}
	
	@Test
	public void testSLIpgetIpAddresses(){
		System.out.println(gson.toJson(ipService.getIpAddresses(11183423L)));
	}
	
	@Test
	public void testSLIpgetLastIpAddress(){
		System.out.println(gson.toJson(ipService.getLastIpAddress(11183423L)));
	}
	
	@Autowired
	private SlSequenceService sequenceService;
	
	@Autowired
	private SlThirdLoginService thirdLoginService;
	
	@Test
	public void testThirdLoginCreateToken(){
		SlThirdLoginDto token = new SlThirdLoginDto();
		token.setAccessToken("wwf");
		token.setBindAid(new BigDecimal(11183423L));
		token.setExpiresIn("512");
		token.setLoginType((short)SlThirdLoginDto.SourceType.DIANRONG.ordinal());
		token.setNickName("wwf");
		token.setOpenUid("123456");
		thirdLoginService.createToken(token);
	}
	
	@Test
	public void testThirdLoginexpireAccessToken(){
		thirdLoginService.expireAccessToken("123456", 12345l);
	}
	
	@Test
	public void testThirdLogingetThirdPartyLogin(){
		System.out.println(gson.toJson(thirdLoginService.getThirdPartyLogin("123456", SlThirdLoginDto.SourceType.DIANRONG)));
	}
}
