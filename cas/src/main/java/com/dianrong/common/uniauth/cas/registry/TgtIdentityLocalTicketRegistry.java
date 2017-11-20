package com.dianrong.common.uniauth.cas.registry;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.jasig.cas.ticket.registry.AbstractTicketRegistry;
import org.springframework.util.CollectionUtils;

/**
 * 基于Redis的实现.
 */
@Slf4j
public class TgtIdentityLocalTicketRegistry extends AbstractTgtIdentityTicketRegistryDelegate {

  private final ConcurrentHashMap<Long, Set<String>> cache;

  public TgtIdentityLocalTicketRegistry(AbstractTicketRegistry ticketRegistry) {
    super(ticketRegistry);
    this.cache = new ConcurrentHashMap<>();
  }

  @Override
  public void deleteTgtId(Long userId) {
    this.cache.remove(userId);
  }

  @Override
  public void deleteTgtId(Long userId, String tgt) {
    Set<String> tgtSet = this.cache.get(userId);
    if (tgt == null) {
      return;
    }
    tgtSet.remove(tgt);
    if (tgtSet.isEmpty()) {
      synchronized (tgtSet) {
        tgtSet = this.cache.get(userId);
        if (tgtSet.isEmpty()) {
          this.cache.remove(userId);
        }
      }
    }
  }

  @Override
  public Set<String> getTgtId(Long userId) {
    Set<String> result = this.cache.get(userId);
    if (CollectionUtils.isEmpty(result)) {
      return Collections.emptySet();
    }
    return Collections.unmodifiableSet(result);
  }

  @Override
  public void setTgtId(Long userId, String tgtId) {
    Set<String> tgtSet = this.cache.get(userId);
    if (tgtSet == null) {
      tgtSet = new HashSet<>();
      this.cache.putIfAbsent(userId, tgtSet);
      tgtSet = this.cache.get(userId);
      tgtSet.add(tgtId);
      return;
    }
    synchronized (tgtSet) {
      tgtSet = this.cache.get(userId);
      if (tgtSet == null) {
        setTgtId(userId, tgtId);
        return;
      }
      tgtSet.add(tgtId);
    }
  }
}
