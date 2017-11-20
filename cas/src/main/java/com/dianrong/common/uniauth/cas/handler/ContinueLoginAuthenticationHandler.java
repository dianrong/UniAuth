package com.dianrong.common.uniauth.cas.handler;

import com.dianrong.common.uniauth.cas.model.ExtendLoginCredential;
import com.dianrong.common.uniauth.cas.service.UserLoginService;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.enm.CasProtocol;
import com.dianrong.common.uniauth.common.util.StringUtil;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;
import org.jasig.cas.authentication.Credential;
import org.jasig.cas.authentication.HandlerResult;
import org.jasig.cas.authentication.PreventedException;
import org.jasig.cas.authentication.handler.support.AbstractPreAndPostProcessingAuthenticationHandler;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 延续登录的处理Handler
 */
public class ContinueLoginAuthenticationHandler extends
    AbstractPreAndPostProcessingAuthenticationHandler {

  @Autowired
  private UserLoginService userLoginService;

  @Override
  protected HandlerResult doAuthentication(Credential credential)
      throws GeneralSecurityException, PreventedException {
    ExtendLoginCredential extendLoginCredential = (ExtendLoginCredential) credential;
    // 登陆操作
    UserDto userInfo = userLoginService.login(extendLoginCredential.getUserId());
    Map<String, Object> attributes = new HashMap<>(4);
    attributes.put(CasProtocol.DianRongCas.getTenancyIdName(),
        StringUtil.translateIntegerToLong(userInfo.getTenancyId()));
    attributes.put(CasProtocol.DianRongCas.getUserIdName(), userInfo.getId());
    attributes.put(CasProtocol.DianRongCas.getLoginAccountName(), extendLoginCredential.getUserId().toString());
    attributes.put(CasProtocol.DianRongCas.getContinueLoginTimeName(),
        extendLoginCredential.loginAgainExtendLoginTimes());
    return createHandlerResult(credential,
        this.principalFactory
            .createPrincipal(userInfo.getAccount(), attributes), null);
  }

  @Override
  public boolean supports(final Credential credential) {
    return credential instanceof ExtendLoginCredential;
  }
}
