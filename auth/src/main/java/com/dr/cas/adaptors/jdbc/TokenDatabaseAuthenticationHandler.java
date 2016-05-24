package com.dr.cas.adaptors.jdbc;

import java.security.GeneralSecurityException;

import org.jasig.cas.authentication.Credential;
import org.jasig.cas.authentication.HandlerResult;
import org.jasig.cas.authentication.PreventedException;
import org.jasig.cas.authentication.UsernamePasswordCredential;
import org.jasig.cas.authentication.handler.support.AbstractUsernamePasswordAuthenticationHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dr.cas.authentication.principal.TokenCredential;
import com.dr.cas.service.TokenLoginService;

public class TokenDatabaseAuthenticationHandler extends AbstractUsernamePasswordAuthenticationHandler {

  private static Logger logger = LoggerFactory
      .getLogger(PasswordDatabaseAuthenticationHandler.class);

  private TokenLoginService loginService;
  
  /** {@inheritDoc} */
  @Override
  protected final HandlerResult authenticateUsernamePasswordInternal(
      final UsernamePasswordCredential credential)
      throws GeneralSecurityException, PreventedException {

    try {
      TokenCredential cred = (TokenCredential)credential;
      Long id = loginService.authenticate(cred);
    
      // get canonical user ID as username
      String strId = String.valueOf(id);
      cred.setUsername(strId);
      return createHandlerResult(credential, this.principalFactory.createPrincipal(strId), null);
    } catch (GeneralSecurityException e) {
      logger.info("invalid login: " + e.getMessage());
      throw e;
    } catch (Exception e) {
      logger.error("Error login", e);
      e.printStackTrace();
      throw new PreventedException(e.getMessage(), e);
    }
  }

  public void setLoginService(TokenLoginService loginService) {
    this.loginService = loginService;
  }
  
  
  @Override
  public boolean supports(final Credential credential) {
      return credential instanceof TokenCredential;
  }
}
