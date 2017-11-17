package com.dianrong.common.uniauth.cas.registry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.jasig.cas.ticket.registry.AbstractTicketRegistry;

/**
 * 基于Redis的实现.
 */
@Slf4j
public class TgtIdentityLocalTicketRegistry extends AbstractTgtIdentityTicketRegistryDelegate {

  private final Map<Long, String> cache;

  public TgtIdentityLocalTicketRegistry(AbstractTicketRegistry ticketRegistry) {
    super(ticketRegistry);
    this.cache = new ConcurrentHashMap<>();
  }

  @Override
  public void deleteTgtId(Long userId) {
    this.cache.remove(userId);
  }

  @Override
  public String getTgtId(Long userId) {
    return this.cache.get(userId);
  }

  @Override
  public void setTgtId(Long userId, String tgtId) {
    this.cache.put(userId, tgtId);
  }
}
