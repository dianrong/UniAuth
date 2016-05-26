package com.dr.cas.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Pattern;

import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.dianrong.common.auth.server.service.SlActorService;
import com.dianrong.common.auth.server.service.SlBehaviorLogService;
import com.dianrong.common.auth.server.service.SlIpService;
import com.dianrong.common.uniauth.common.bean.dto.SlActorDto;
import com.dianrong.common.uniauth.common.bean.dto.SlActorEnum;
import com.dianrong.common.uniauth.common.bean.dto.SlBehaviorLogDto;
import com.dianrong.common.uniauth.common.bean.dto.SlBehaviorLogEnum;
import com.dianrong.common.uniauth.common.bean.dto.SlIpDto;
import com.dr.cas.authentication.principal.PasswordCredential;
import com.dr.cas.exception.AuthenticationFailureException.ACCOUNT_FAIL_TIME_10;
import com.dr.cas.exception.AuthenticationFailureException.ACCOUNT_FAIL_TIME_4;
import com.dr.cas.exception.AuthenticationFailureException.ACCOUNT_FAIL_TIME_5;
import com.dr.cas.exception.AuthenticationFailureException.ACCOUNT_FAIL_TIME_6;
import com.dr.cas.exception.AuthenticationFailureException.ACCOUNT_FAIL_TIME_7;
import com.dr.cas.exception.AuthenticationFailureException.ACCOUNT_FAIL_TIME_8;
import com.dr.cas.exception.AuthenticationFailureException.ACCOUNT_FAIL_TIME_9;
import com.dr.cas.exception.AuthenticationFailureException.ACCOUNT_LOCKED;
import com.dr.cas.exception.AuthenticationFailureException.INVALID_CREDENTIALS;
import com.dr.cas.util.CredentialsUtils;

/**.
 * 处理用户登陆的各种业务逻辑
 * @author wanglin
 */
public class PasswordLoginService  {

  private static Logger logger = LoggerFactory.getLogger(PasswordLoginService.class);

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
  @Qualifier("slBehaviorLogService")
  private SlBehaviorLogService slBehaviorLogService;
  
  @Autowired
  @Qualifier("slIpService")
  private SlIpService slIpService;
  
  /**.
   * 用户类型定义
   */
  private static SlActorEnum.Type[] userTypes = new SlActorEnum.Type[] { SlActorEnum.Type.USER,  SlActorEnum.Type.OPERATOR };

  /**.
   * 密码登陆验证处理逻辑
   * @param credential  包含account和password的model
   * @return  登陆成功返回的用户id
   * @throws LoginException 登陆抛出的异常
   */
  public Long authenticate(PasswordCredential credential) throws LoginException {
    long begin = System.currentTimeMillis();
    String username = credential.getUsername().trim().toLowerCase();
    String password = credential.getPassword();
    SlActorDto actor = null;
    boolean isSuccess = false;
    try {
      if (logger.isDebugEnabled()) {
        logger.debug("Login attempt for: " + username);
      }
      actor = findActor(credential);
      // actor is found, then validate password
      if(CredentialsUtils.passwordMatching(password, actor.getCredentialSalt(), actor.getCredential())) {
        // update salt
        updatePasswordSalt(actor, password);
        // reset login attempts
        credential.setAttempts(0);
      } else {
        // increment failure count
        credential.setAttempts(actor.getFailedAuthCnt() + 1);
      }

      // handle invalid failure
      switch (credential.getAttempts()) {
        // show warning after more than 3 tries e.g. ACCOUNT_FAIL_TIME_10
        case 0:
          break; // success
        case 1: case 2: case 3:
          throw new INVALID_CREDENTIALS("password mismatch " + username);
        case 4:
          throw new ACCOUNT_FAIL_TIME_4();
        case 5:
          throw new ACCOUNT_FAIL_TIME_5();
        case 6:
          throw new ACCOUNT_FAIL_TIME_6();
        case 7:
          throw new ACCOUNT_FAIL_TIME_7();
        case 8:
          throw new ACCOUNT_FAIL_TIME_8();
        case 9:
          throw new ACCOUNT_FAIL_TIME_9();
        case 10:
          throw new ACCOUNT_FAIL_TIME_10();
        default:
          // not reached
          throw new ACCOUNT_LOCKED();
      }

      Long realActorId = checkActor(actor);
      // check login location and record ip location
      if (!checkIP(actor, credential.getIPAddress())) {
        // TODO: IP validation failure, challenge user exception
      }
      isSuccess = true;
      return realActorId;
    } finally {
      Long actorId = credential.getActorId();
      // update attempts, only if attempts are reset or incremented
      if (actor != null && credential.getAttempts() != actor.getFailedAuthCnt()) {
        try {
          logger.info("updating login failure " + credential.getAttempts() + " " + username);
          slActorService.updateLoginFailureCnt(actor.getId(), credential.getAttempts());
        } catch (Exception ee) {
          ee.printStackTrace();
        }
      }
      // record login success/failure
      recordBehaviorLog4login(actorId, username, credential.getIp(), isSuccess);
      // don't check limit, if it's already over the limit
      if (!isSuccess && !credential.isOverLimit()) {
        credential.setOverLimit(isOverLimit4login(username, actorId, credential.getIp()));
      }
      if (logger.isDebugEnabled()) {
        logger.debug("login took " + (System.currentTimeMillis() - begin));
      }
    }
  }
  
  /**.
   * 根据账号查找用户信息
   * @param cred authtication
   * @return用户信息
   * @throws LoginException
   */
  private SlActorDto findActor(PasswordCredential cred)
      throws LoginException {
    String username = cred.getUsername();
    SlActorDto actor = null;
    boolean loginWithCellphone = false;
    // try email first
    if (username.contains("@")) {
      actor = slActorService.getUserByEmail(username);
    }
    // try username, if doesn't look like a cellphone
    else if (!isCellphone(username)) {
      // not cellphone, must be usrname
      actor = slActorService.getUserByName(username);
    }
    // try cellphone
    else {
      actor = slActorService.getUserByCellphone(username);
      loginWithCellphone = true;
    }

    // not found, show captcha
    if (actor == null) {
      // set over limit without checking db
      cred.setOverLimit(true);
      logger.debug("actor not found: {}", username);
      // actor not found
      throw new INVALID_CREDENTIALS("user not found " + username);
    }
    
    // actor found
    // assert actor is not null
    cred.setActorId(actor.getId());

    // actor is locked out
    if (actor.getFailedAuthCnt() >= 10) {
      logger.info(username + " locked after 10 attempts");
      throw new ACCOUNT_LOCKED();
    }
 
    // user can only login with cellphone if the cellphone is verified
    if (loginWithCellphone && !actor.isCellphoneVerified()) {
      logger.debug("cellphone not verified: {}", username);
      throw new INVALID_CREDENTIALS(
          "cellphone not verified " + username);
    }

    // check if over limit
    if (isOverLimit4login(username, actor.getId(), cred.getIp())) {
      cred.setOverLimit(true);
      logger.debug("actor over limit: {}", username);
    }
     return actor;
  }

  /**.
   * 更新用户密码的秘钥
   * @param actor
   * @param password
   */
  private void updatePasswordSalt(SlActorDto actor, String password) {
    try {
      // add salt to password
      if (actor.getCredentialSalt() == null) {
        byte[] salt = CredentialsUtils.createSaltByte();
        byte[] credential = CredentialsUtils.digest(password, salt);
        logger.info("Trying to generate credential salt for user " + actor.getId());
      
        // 更新用户的秘钥信息
        slActorService.updateCredential(actor.getId(), credential, salt);
      }
    } catch (Exception ex) {
      // allow update salt failure, will be updated next time
      logger.warn("error updating salt " + actor.getId());
    }
  }
  
 
 /**.
  * 判断登陆的凭证是否是电话号码
  * @param username  登陆凭证
  * @return true：是电话号码 ， false：不是电话号码
  */
  private boolean isCellphone(String username) {
    if (username == null) {
      return false;
    }
    return ChinesemobilephoneRE.matcher(username).matches();
  }

  /**.
   * 判断登陆人的身份
   * @param actor
   * @return
   * @throws LoginException
   */
  private Long checkActor(SlActorDto actor) throws LoginException {
    // sub account is not allowed to login
    if (SlActorEnum.SubType.SUB_ACCOUNT.equals(actor.getSubtype())) {
      throw new INVALID_CREDENTIALS("sub user is not allowed to login! id=" + actor.getId());
    }

    // checking user status
    // Canceled ---
    if (actor.getuStatus() == SlActorEnum.Status.CANCELED
        || actor.getuStatus() == SlActorEnum.Status.CANCEL_PENDING) {
      logger.info("invalid user status " + actor.getuStatus() + " : " + actor.getName());
      throw new INVALID_CREDENTIALS(
          "user cancelled " + actor.getName());
    }

    // checking user tye
    SlActorEnum.Type type = actor.getType();

    // admin is allowed as impersonator only
    if (type == SlActorEnum.Type.ADMIN && actor.getSsn() == null) {
      throw new INVALID_CREDENTIALS(
          "Amdin must be impersonator: " + actor.getName());
    }
    if (!type.in(userTypes)) {
      throw new INVALID_CREDENTIALS(
          "invalid user type " + type + " : " + actor.getName());
    }
    
    // If actor is Impersonator, create session based on impersonated actor
    Long impersonatorAid = null;
    if(actor.isFlagSet(SlActorEnum.Flag.Impersonator) && type.equals(SlActorEnum.Type.ADMIN)){
      if(actor.getSsnShort() != null){
        try {
          impersonatorAid = actor.getId();
          actor = slActorService.getUserByID(Long.valueOf(actor.getSsn()));
        } catch (Exception e) {
          logger.error("error getting impersonator " + actor.getSsn());
        }
        if(actor == null){
          throw new INVALID_CREDENTIALS(
              "invalid impersonator " + impersonatorAid);
        }
      }
      
      //If actor just have limit privilege 
      if(actor.getSubtype() == SlActorEnum.SubType.IMPERSONATE_LIMIT){
        if(actor.getUserIntent() != SlActorEnum.LCUserIntent.BORROWER){
          throw new INVALID_CREDENTIALS(
              "can only login as borrower "  + actor.getName());
        }
        if (actor.getbStatus() == SlActorEnum.Status.CONFIRMED) {
          throw new INVALID_CREDENTIALS(
              "You can't login as a comfirmed borrower! " + actor.getName());
        }
      }
      //End of checking limit privileges
    }
    return actor.getId();
  }

  /**.
   * 判断用户登陆ip的相关信息
   * @param actor
   * @param newIP
   * @return
   */
  private boolean checkIP(SlActorDto actor, SlIpDto newIP) {
    if (newIP == null) {
      return true;
    }
    // get last IP
    SlIpDto lastIp = null;
    try {
      // get last IP and compare with new IP
      lastIp = slIpService.getLastIpAddress(actor.getId());
      if (!newIP.sameLocationAs(lastIp)) {
        return false;
      }
      // same IP region or new IP, save it
      newIP.setAid(actor.getId() == null ? null : new BigDecimal(actor.getId()));
      slIpService.addIpAddress(newIP);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return true;
  }

  /**.
   * 登陆行为的一些校验  比如错误次数，同一个ip多次登陆等
   * @param identity
   * @param actorId
   * @param ip
   * @return
   */
  private boolean isOverLimit4login(String identity, Long actorId, String ip) {
    // actor not found
    if (actorId == null) {
      logger.warn("identity[{}] not found!", identity);
      return true;
    }
    
    try {
      if (actorId !=  null) {
        // query 
        List<Short> list = slBehaviorLogService.queryBehaviorLogsByAid(actorId);
        logger.info("login attempts from id " + actorId + " is " + list);

        if (isOverLimit(list)) {
          logger.info("login error times is over limit , identity is " + identity);
          return true;
        }
      }

      // 同一个ip在timeunit中密码累计输错的次数
      if (ip != null && !ip.trim().isEmpty()) {
    	  
    	  // 判断用户登陆的次数
    	  SlBehaviorLogEnum.Type type = SlBehaviorLogEnum.Type.LOGIN_IP;
    	  
    	  // 获取单位时间某个ip的登陆次数
         long count = slBehaviorLogService.countBehaviorLogsByIp(ip, type.getValue(), type.getStartDate());
        logger.info("login attempts from ip " + ip + " is " + count);
        if (type.judgeIsOverLimit(count)) {
          logger.info("login error times is over limit , ip is " + ip);
          return true;
        }
      }
    } catch (Exception e) {
      logger.error("error query behavior log", e);
    }
    return false;
  }

  /**
   * 计算list中的连续错误条数，计算规则为：<br/>
   * 遍历list，result !=succ 时，errorCount++，直至碰到第一个succ
   * */
  private boolean isOverLimit(List<Short> list) {
    if (list == null || list.isEmpty()) {
      return false;
    }
    for (Short i : list) {
      if (i.intValue() == SlBehaviorLogEnum.Result.SUCC.getValue()) {
        return false;
      }
    }
    return true;
  }

  /**.
   * 记录登陆的日志信息
   * @param aid
   * @param identity 
   * @param ip
   * @param isSucc
   */
  public void recordBehaviorLog4login(Long aid, String identity, String ip, boolean isSucc) {
    try {
    	SlBehaviorLogDto log = new SlBehaviorLogDto();
      log.setCreateD(new java.sql.Timestamp(System.currentTimeMillis()));
      log.setAid(aid == null ? null : new BigDecimal(aid));
      log.setTarget(identity);
      log.setIpAddr(ip);
      log.setResult(isSucc ? SlBehaviorLogEnum.Result.SUCC.getValue() : SlBehaviorLogEnum.Result.FAIL.getValue());
      log.setType(SlBehaviorLogEnum.Type.LOGIN_USER_ID.getValue());
      slBehaviorLogService.insertBehaviorLog(log);
    } catch (Exception e) {
      logger.error(String.format(
          "recordBehaviorLog4login occur error , aid %s , identity %s , ip %s , isSucc %s",
          String.valueOf(aid), identity, ip, String.valueOf(isSucc)), e);
    }
  }
}