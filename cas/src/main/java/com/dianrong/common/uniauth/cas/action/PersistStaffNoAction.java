package com.dianrong.common.uniauth.cas.action;

import com.dianrong.common.uniauth.cas.helper.StaffNoPersistTagHolder;
import com.dianrong.common.uniauth.cas.model.UserAttribute;
import com.dianrong.common.uniauth.cas.util.CasConstants;
import com.dianrong.common.uniauth.cas.util.WebScopeUtil;
import com.dianrong.common.uniauth.common.util.ZkNodeUtils;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.jasig.cas.authentication.principal.Service;
import org.jasig.cas.web.support.WebUtils;
import org.springframework.util.StringUtils;
import org.springframework.webflow.action.AbstractAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

/**
 * 存储用户的员工号的Action.
 *
 * @author wanglin
 */
@Slf4j
public class PersistStaffNoAction {

  protected Event persist(final UserAttribute userAttribute) throws Exception {
    Long userId = StaffNoPersistTagHolder.get();
    if (userId == null) {
      return new Event(this,"success");
    }
    if (StringUtils.hasText(userAttribute.getStaffNo())) {
      // persist

    }
    return new Event(this,"success");
  }
}
