package com.dianrong.common.uniauth.client.custom.jwt;

import com.dianrong.common.uniauth.common.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * JWT操作中的一些公用操作.
 * 
 * @author wanglin
 *
 */
@Slf4j
public final class JWTWebScopeUtil {

  /**
   * 一个标识TAG,用于在Session中标识验证通过的用户的信息.
   */
  public static final String JWT_AUTHENTICATED_TAG = "_UNIAUTH_JWT_AUTHTICATED_USER_INFO_";

  public static void refreshJWTUserInfoTag(JWTUserTagInfo tagInfo, HttpServletRequest request) {
    Assert.notNull(tagInfo);
    refreshJWTUserInfoTag(tagInfo.getIdentity(), tagInfo.getTenancyId(), request);
  }

  public static void refreshJWTUserInfoTag(String identity, Long tenancyId,
      HttpServletRequest request) {
    JWTUserTagInfo tag = new JWTUserTagInfo();
    tag.setIdentity(identity);
    tag.setTenancyId(tenancyId);
    request.getSession().setAttribute(JWT_AUTHENTICATED_TAG, tag);
  }

  /**
   * 清除登陆成功标识信息.
   */
  public static void removeJWTUserInfoTag(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    if (session != null) {
      session.removeAttribute(JWT_AUTHENTICATED_TAG);
    }
  }

  /**
   * 获取指定请求中的JWT用户标识信息.
   */
  public static JWTUserTagInfo getJWTUserTagInfo(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    if (session != null) {
      Object info = session.getAttribute(JWT_AUTHENTICATED_TAG);
      if (info instanceof JWTUserTagInfo) {
        return (JWTUserTagInfo) info;
      }
      if (info != null) {
        log.warn("Current request's session, the tag info is not type: JWTUserTagInfo");
      }
    } else {
      log.debug("Current request do not create a session!");
    }
    return null;
  }

  /**
   * 判断标识信息与传入的信息是否一致.
   */
  public static boolean isTagInfoEquals(String identity, Long tenancyId,
      HttpServletRequest request) {
    JWTUserTagInfo tagInfo = getJWTUserTagInfo(request);
    if (tagInfo == null) {
      return false;
    }
    return tagInfo.isEquals(identity, tenancyId);
  }

  @ToString
  @Getter
  @Setter
  public static final class JWTUserTagInfo {
    private String identity;

    private Long tenancyId;

    public boolean isEquals(String identity, Long tenancyId) {
      return objectEquals(this.identity, identity) && objectEquals(this.tenancyId, tenancyId);
    }

    private boolean objectEquals(Object o1, Object o2) {
      if (o1 == null && o2 == null) {
        return true;
      }
      if (o1 != null && o2 != null) {
        return o1.equals(o2);
      }
      return false;
    }
  }
}
