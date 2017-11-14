package com.dianrong.common.uniauth.cas.handler;

import com.dianrong.common.uniauth.cas.helper.StaffNoPersistTagHolder;
import com.dianrong.common.uniauth.cas.model.CasUsernamePasswordCredential;
import com.dianrong.common.uniauth.cas.service.UserLoginService;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.enm.CasProtocol;
import com.dianrong.common.uniauth.common.util.StringUtil;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.shiro.util.StringUtils;
import org.jasig.cas.authentication.Credential;
import org.jasig.cas.authentication.HandlerResult;
import org.jasig.cas.authentication.PreventedException;
import org.jasig.cas.authentication.UsernamePasswordCredential;
import org.jasig.cas.authentication.handler.support.AbstractUsernamePasswordAuthenticationHandler;
import org.springframework.beans.factory.annotation.Autowired;

public class UniauthAuthenticationHandler extends AbstractUsernamePasswordAuthenticationHandler {

  @Autowired
  private UserLoginService userLoginService;

  /**
   * Zk配置属性..
   */
  @Resource(name = "uniauthConfig")
  private Map<String, String> uniauthConfig;

  @Override
  protected HandlerResult authenticateUsernamePasswordInternal(
      UsernamePasswordCredential credential) throws GeneralSecurityException, PreventedException {
    CasUsernamePasswordCredential casUserNameCredential =
        (CasUsernamePasswordCredential) credential;
    String userName = StringUtil.trimCompatibleNull(casUserNameCredential.getUsername());
    String password = casUserNameCredential.getPassword();
    String tenancyCode = StringUtil.trimCompatibleNull(casUserNameCredential.getTenancyCode());

    // 登陆操作
    UserDto userInfo = userLoginService.login(userName, password, tenancyCode);
    if (checkStaffNo()) {
      String staffNo = userInfo.getStaffNo();
      if (!StringUtils.hasText(staffNo)) {
        // 未设置员工号
        StaffNoPersistTagHolder.persist();
      }
    }
    Map<String, Object> attributes = new HashMap<String, Object>(3);
    attributes.put(CasProtocol.DianRongCas.getTenancyIdName(),
        StringUtil.translateIntegerToLong(userInfo.getTenancyId()));
    attributes.put(CasProtocol.DianRongCas.getUserIdName(), userInfo.getId());
    attributes.put(CasProtocol.DianRongCas.getLoginAccountName(), userInfo.getAccount());
    return createHandlerResult(credential,
        this.principalFactory.createPrincipal(userName, attributes), null);
  }

  @Override
  public boolean supports(final Credential credential) {
    return credential instanceof CasUsernamePasswordCredential;
  }

  /**
   * 根据配置决定是否Check用户的员工标号.
   */
  private boolean checkStaffNo() {
    return Boolean.TRUE.toString().equalsIgnoreCase(uniauthConfig.get("cas.check.staff.no"));
  }
}
