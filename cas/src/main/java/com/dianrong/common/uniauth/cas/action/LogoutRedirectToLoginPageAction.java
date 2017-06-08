package com.dianrong.common.uniauth.cas.action;

import org.jasig.cas.web.flow.LogoutAction;
import org.springframework.util.Assert;
import org.springframework.webflow.action.AbstractAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

/**
 * Redirect to login page.
 *
 * @author wanglin
 */
public final class LogoutRedirectToLoginPageAction extends AbstractAction {

  public static final String REDIRECT_URL_KEY = "logoutRedirectUrl";
  public static final String LOGIN_PAGE_URL = "/login";

  private final LogoutAction originalAction;


  public LogoutRedirectToLoginPageAction(LogoutAction originalAction) {
    Assert.notNull(originalAction);
    this.originalAction = originalAction;
  }

  @Override
  protected Event doExecute(RequestContext context) throws Exception {
    Event result = originalAction.execute(context);
    if (result != null) {
      if (result.getId().equals(LogoutAction.FINISH_EVENT)) {
        if (context.getFlowScope().get(REDIRECT_URL_KEY) == null) {
          context.getFlowScope().put(REDIRECT_URL_KEY, LOGIN_PAGE_URL);
        }
      }
    }
    return result;
  }
}
