package com.dianrong.common.uniauth.cas.action;

import org.jasig.cas.web.flow.LogoutAction;
import org.springframework.util.Assert;
import org.springframework.webflow.action.AbstractAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

/**
 * redirect to login page
 * @author wanglin
 */
public final class LogoutFinishEventDecorateAction extends AbstractAction {
    private final LogoutAction originalAction;

    public LogoutFinishEventDecorateAction(LogoutAction originalAction) {
        Assert.notNull(originalAction);
        this.originalAction = originalAction;
    }

    @Override
    protected Event doExecute(RequestContext context) throws Exception {
        Event result = originalAction.execute(context);
        if (result != null) {
            if (result.getId().equals(LogoutAction.FINISH_EVENT)) {
                context.getFlowScope().put("logoutRedirectUrl", "/login");
            }
        }
        return result;
    }

}
