package com.dianrong.common.uniauth.cas.registry.support;

import com.dianrong.common.uniauth.common.cons.AppConstants;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import org.apache.commons.lang3.math.NumberUtils;
import org.jasig.cas.ticket.TicketState;
import org.jasig.cas.ticket.support.AbstractCasExpirationPolicy;
import org.springframework.util.Assert;

public class CasMultiTimeUseOrTimeoutExpirationPolicy extends AbstractCasExpirationPolicy {

  /**
   * Serialization support.
   */
  private static final long serialVersionUID = -5704993954986738308L;

  /**
   * The time to kill in milliseconds.
   */
  private final long timeToKillInMilliSeconds;

  /**
   * The maximum number of uses before expiration.
   */
  private final int numberOfUses;

  @Resource(name = "uniauthConfig")
  private Map<String, String> allZkNodeMap;

  /**
   * No-arg constructor for serialization support.
   */
  public CasMultiTimeUseOrTimeoutExpirationPolicy() {
    this.timeToKillInMilliSeconds = 0;
    this.numberOfUses = 0;
  }

  /**
   * Instantiates a new multi time use or timeout expiration policy.
   *
   * @param numberOfUses the number of uses
   * @param timeToKillInMilliSeconds the time to kill in milli seconds
   */
  public CasMultiTimeUseOrTimeoutExpirationPolicy(final int numberOfUses,
      final long timeToKillInMilliSeconds) {
    this.timeToKillInMilliSeconds = timeToKillInMilliSeconds;
    this.numberOfUses = numberOfUses;
    Assert.isTrue(this.numberOfUses > 0, "numberOfUsers must be greater than 0.");
    Assert.isTrue(this.timeToKillInMilliSeconds > 0,
        "timeToKillInMilliseconds must be greater than 0.");

  }

  /**
   * Instantiates a new multi time use or timeout expiration policy.
   *
   * @param numberOfUses the number of uses
   * @param timeToKill the time to kill
   * @param timeUnit the time unit
   */
  public CasMultiTimeUseOrTimeoutExpirationPolicy(final int numberOfUses, final long timeToKill,
      final TimeUnit timeUnit) {
    this(numberOfUses, timeUnit.toMillis(timeToKill));
  }

  @Override
  public boolean isExpired(final TicketState ticketState) {
    return (ticketState == null) || (ticketState.getCountOfUses() >= this.getStUsedTimes())
        || (System.currentTimeMillis() - ticketState.getLastTimeUsed()
        >= this.timeToKillInMilliSeconds);
  }

  /**
   * . 获取st使用的次数
   */
  private int getStUsedTimes() {
    int useTimes = NumberUtils
        .toInt(allZkNodeMap.get(AppConstants.ZK_NODE_NAME_ST_USED_TIMES), this.numberOfUses);
    return useTimes <= 0 ? this.numberOfUses : useTimes;
  }
}
