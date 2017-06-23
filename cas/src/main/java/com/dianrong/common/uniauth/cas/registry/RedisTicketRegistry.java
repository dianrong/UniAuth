package com.dianrong.common.uniauth.cas.registry;

import com.dianrong.common.uniauth.common.util.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.extern.slf4j.Slf4j;

import org.jasig.cas.authentication.principal.Service;
import org.jasig.cas.ticket.ServiceTicket;
import org.jasig.cas.ticket.Ticket;
import org.jasig.cas.ticket.TicketGrantingTicket;
import org.jasig.cas.ticket.registry.AbstractDistributedTicketRegistry;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;

@Slf4j
public class RedisTicketRegistry extends AbstractDistributedTicketRegistry {

  @NotNull
  private final RedisTemplate<String, Object> redisTemplate;

  /**
   * TGT cache entry timeout in seconds.
   */
  @Min(0)
  private final int tgtTimeout;

  /**
   * ST cache entry timeout in seconds.
   */
  @Min(0)
  private final int stTimeout;

  /**
   * Ticket的key的前缀字符,防止在redis中与其他key冲突.
   */
  private String ticketPrefix = "uniauth";

  /**
   * 构造一个RedisTicketRegistry.
   */
  public RedisTicketRegistry(RedisTemplate<String, Object> redisTemplate, int tgtTimeout,
      int stTimeout) {
    this.redisTemplate = redisTemplate;
    this.tgtTimeout = tgtTimeout;
    this.stTimeout = stTimeout;
  }

  @Override
  public void addTicket(Ticket ticket) {
    log.debug("Adding ticket {}", ticket);
    try {
      redisTemplate.opsForValue()
          .set(getTicketKey(ticket.getId()), ticket, getTimeout(ticket), TimeUnit.SECONDS);
    } catch (final Exception e) {
      log.error("Failed adding {}", ticket, e);
      throw e;
    }
  }

  @Override
  public Ticket getTicket(String ticketId) {
    try {
      final Ticket t = (Ticket) this.redisTemplate.opsForValue().get(getTicketKey(ticketId));
      if (t != null) {
        return getProxiedTicketInstance(t);
      }
    } catch (final Exception e) {
      log.error("Failed fetching {} ", ticketId, e);
      throw e;
    }
    return null;
  }

  @Override
  public boolean deleteTicket(String ticketId) {
    log.debug("Deleting ticket {}", ticketId);
    try {
      Ticket t = getTicket(ticketId);
      if (t instanceof TicketGrantingTicket) {
        deleteChildren((TicketGrantingTicket) t);
      }
      this.redisTemplate.delete(getTicketKey(ticketId));
      return true;
    } catch (final Exception e) {
      log.error("Failed deleting {}", ticketId, e);
      throw e;
    }
  }

  /**
   * Delete TGT's service tickets.
   *
   * @param ticket the ticket
   */
  private void deleteChildren(final TicketGrantingTicket ticket) {
    // delete service tickets
    final Map<String, Service> services = ticket.getServices();
    if (services != null && !services.isEmpty()) {
      for (final Map.Entry<String, Service> entry : services.entrySet()) {
        if (this.deleteServiceTicket(entry.getKey())) {
          log.trace("Removed service ticket [{}]", entry.getKey());
        } else {
          log.trace("Unable to remove service ticket [{}]", entry.getKey());
        }
      }
    }
  }

  /**
   * . 指定删除service ticket
   */
  private boolean deleteServiceTicket(String ticketId) {
    if (ticketId == null) {
      return false;
    }
    log.debug("Deleting ticket {}", ticketId);
    try {
      this.redisTemplate.delete(getTicketKey(ticketId));
      return true;
    } catch (final Exception e) {
      log.error("Failed deleting {}", ticketId, e);
      throw e;
    }
  }

  @Override
  public Collection<Ticket> getTickets() {
    // throw new UnsupportedOperationException("GetTickets not supported.");
    return new ArrayList<Ticket>();
  }

  @Override
  protected void updateTicket(Ticket ticket) {
    log.debug("Updating ticket {}", ticket);
    try {
      this.redisTemplate.delete(getTicketKey(ticket.getId()));
      redisTemplate.opsForValue()
          .set(getTicketKey(ticket.getId()), ticket, getTimeout(ticket), TimeUnit.SECONDS);
    } catch (final Exception e) {
      log.error("Failed updating {}", ticket, e);
      throw e;
    }
  }

  @Override
  protected boolean needsCallback() {
    return true;
  }

  private int getTimeout(final Ticket t) {
    if (t instanceof TicketGrantingTicket) {
      return this.tgtTimeout;
    } else if (t instanceof ServiceTicket) {
      return this.stTimeout;
    }
    throw new IllegalArgumentException("Invalid ticket type");
  }

  /**
   * 获取ticket的key.
   *
   * @param ticketId 不能为空
   * @return 生成的key
   */
  protected String getTicketKey(String ticketId) {
    Assert.notNull(ticketId);
    return this.ticketPrefix + ":" + ticketId.trim();
  }

  public String getTicketPrefix() {
    return ticketPrefix;
  }

  /**
   * 设置Ticket Key的前缀.
   */
  public void setTicketPrefix(String ticketPrefix) {
    if (!StringUtils.hasText(ticketPrefix)) {
      log.warn("can not set ticketPrefix with empty string.");
      return;
    }
    log.info("set new ticketPrefix {}", ticketPrefix);
    this.ticketPrefix = ticketPrefix;
  }
}
