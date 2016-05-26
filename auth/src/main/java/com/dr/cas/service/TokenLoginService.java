package com.dr.cas.service;

import java.util.Calendar;
import java.util.regex.Pattern;

import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.dianrong.common.auth.server.service.SlActorService;
import com.dianrong.common.auth.server.service.SlThirdLoginService;
import com.dianrong.common.uniauth.common.bean.dto.SlActorDto;
import com.dianrong.common.uniauth.common.bean.dto.SlThirdLoginDto;
import com.dr.cas.authentication.principal.TokenCredential;
import com.dr.cas.exception.AuthenticationFailureException.INVALID_TOKEN;
import com.dr.cas.exception.AuthenticationFailureException.TOKEN_IS_EXPIRED;

/**.
 *  处理手势登陆
 * @author wanglin
 */
public class TokenLoginService  {

  private static Logger logger = LoggerFactory.getLogger(TokenLoginService.class);

  /**.
   * 电话号码的正则表达式
   */
  private static final String Chinesemobilephone = "^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$"; // 13XXXXXXXXX,14XXXXXXXXX,15XXXXXXXX,18XXXXXXXXX
  public static final Pattern ChinesemobilephoneRE = Pattern.compile(Chinesemobilephone);

  
  // 引入数据处理的所有的service
  @Autowired
  @Qualifier("slActorService")
  private SlActorService slActorService;
  
  @Autowired
  @Qualifier("slThirdLoginService")
  private SlThirdLoginService slThirdLoginService;

  /**.
   *  手势登陆处理
   * @param cred 包含手势密码的model
   * @return  登陆成功返回用户id
   * @throws LoginException  登陆抛出异常
   */
  public Long authenticate(TokenCredential cred) throws LoginException {
    long begin = System.currentTimeMillis();

   String accountName = cred.getUsername().trim().toLowerCase();
   String accessToken = cred.getAccessToken();
   try {
     if (logger.isDebugEnabled()) {
       logger.debug("Token login attempt for: " + accountName);
     }
     SlActorDto actor = validateToken(accountName, accessToken);
     // actor type and IPs are already checked by password login

     // successful login, store DB id in session
     return actor.getId();
   } finally {
     if (logger.isDebugEnabled()) {
       logger.debug("Token login done for " + accountName + " " + (System.currentTimeMillis() - begin));
     }
   }
 }

 
  /**
   * find actor by token
   * @param accountName
   * @param accessToken
   * @return
   * @throws LoginException
   */
  private SlActorDto validateToken(String accountName, String accessToken)
      throws LoginException {
	  SlActorDto actor = null;
	  SlThirdLoginDto tpLogin = slThirdLoginService.getThirdPartyLogin(accessToken, SlThirdLoginDto.SourceType.DIANRONG);
    if (tpLogin == null) {
      throw new INVALID_TOKEN();
    }

    if(tpLogin.isExpired()) {
      throw new TOKEN_IS_EXPIRED();
    }
      
    actor = slActorService.getUserByID(tpLogin.getBindAid() == null ? null : tpLogin.getBindAid().longValue());
    if (actor == null) {
      throw new INVALID_TOKEN();
    }
      
    // check actor account name
    if (isValidAccountName(accountName, actor)) {
      return actor;
    } else {
      // expire this token
      Calendar expiration=Calendar.getInstance();
      expiration.add(Calendar.DATE, -1);
      slThirdLoginService.expireAccessToken(accessToken, expiration.getTimeInMillis());
      throw new TOKEN_IS_EXPIRED();
    }
  }

  /**.
   * 验证用户信息是否合法
   * @param accountName
   * @param actor
   * @return
   */
  private boolean isValidAccountName(String accountName, SlActorDto actor) {
    if (accountName == null || accountName.isEmpty() || actor == null) {
      return false;
    }
    if (accountName.equalsIgnoreCase(actor.getName())) {
      return true;
    }
    if (accountName.equalsIgnoreCase(actor.getEmail())) {
      return true;
    }
    if (accountName.equals(actor.getCellphone())) {
      return true;
    }
    return false;
  }
}