package org.springframework.security.web.access.sessionmanage;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.web.session.InvalidSessionStrategy;

/**
 * 组合InvalidSessionStrategy.
 *
 * @author wanglin
 */
public final class CompositeInvalidSessionStrategy implements InvalidSessionStrategy {

  private List<InvalidSessionStrategy> invalidSessionStrategies;

  /**
   * 获取非法Session的处理策略实现.
   */
  public List<InvalidSessionStrategy> getInvalidSessionStrategies() {
    if (invalidSessionStrategies == null) {
      return Lists.newArrayList();
    }
    return Collections.unmodifiableList(invalidSessionStrategies);
  }

  public void setInvalidSessionStrategies(List<InvalidSessionStrategy> invalidSessionStrategies) {
    this.invalidSessionStrategies = new ArrayList<>(invalidSessionStrategies);
  }

  @Override
  public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    if (this.invalidSessionStrategies != null && !this.invalidSessionStrategies.isEmpty()) {
      for (InvalidSessionStrategy invalidSessionStrategy : this.invalidSessionStrategies) {
        invalidSessionStrategy.onInvalidSessionDetected(request, response);
      }
    }
  }
}
