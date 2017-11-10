package com.dianrong.common.uniauth.cas.action;

import com.dianrong.common.uniauth.cas.helper.StaffNoPersistTagHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.webflow.execution.Event;

/**
 * 判断是否需要进行StaffNo存储提示.
 *
 * @author wanglin
 */
@Slf4j
public class StaffNoPersistDecisionAction {

  /**
   * 提示需要进行StaffNo填写.
   */
  private static final String TRUE = "true";

  /**
   * 不提示.
   */
  private static final String FALSE = "false";

  public Event decide() {
    if (!StaffNoPersistTagHolder.get()) {
      return new Event(this, FALSE);
    }
    return new Event(this, TRUE);
  }
}
