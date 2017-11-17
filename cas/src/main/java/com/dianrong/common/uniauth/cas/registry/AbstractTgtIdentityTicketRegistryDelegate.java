package com.dianrong.common.uniauth.cas.registry;

import com.dianrong.common.uniauth.common.enm.CasProtocol;
import com.dianrong.common.uniauth.common.util.Assert;
import java.util.Collection;
import lombok.extern.slf4j.Slf4j;
import org.jasig.cas.authentication.Authentication;
import org.jasig.cas.authentication.principal.Principal;
import org.jasig.cas.ticket.Ticket;
import org.jasig.cas.ticket.TicketGrantingTicket;
import org.jasig.cas.ticket.registry.AbstractTicketRegistry;
import org.jasig.cas.ticket.registry.TicketRegistry;

/**
 * 增强TicketRegistry功能,添加可以通过用户id管理tgt的功能.
 */
@Slf4j
public abstract class AbstractTgtIdentityTicketRegistryDelegate implements
    TgtIdentityTicketRegistry {

  private final AbstractTicketRegistry originalTicketRegistry;

  public AbstractTgtIdentityTicketRegistryDelegate(AbstractTicketRegistry originalTicketRegistry) {
    Assert.notNull(originalTicketRegistry, "originalTicketRegistry can not be null.");
    this.originalTicketRegistry = originalTicketRegistry;
  }

  @Override
  public void addTicket(Ticket ticket) {
    originalTicketRegistry.addTicket(ticket);
    // 针对TicketGrantingTicket做增强处理
    if (ticket instanceof TicketGrantingTicket) {
      Long userId = queryUserIdFromTgtTicket((TicketGrantingTicket) ticket);
      String tgtId = ticket.getId();
      if (userId != null) {
        log.debug("Cache user tgt cache.{}:{}", userId, tgtId);
        // Cache userId with tgtId.
        setTgtId(userId, tgtId);
      }
    }
  }

  @Override
  public <T extends Ticket> T getTicket(String ticketId, Class<? extends Ticket> clazz) {
    return originalTicketRegistry.getTicket(ticketId, clazz);
  }

  @Override
  public Ticket getTicket(String ticketId) {
    return originalTicketRegistry.getTicket(ticketId);
  }

  @Override
  public boolean deleteTicket(String ticketId) {
    Ticket t = this.getTicket(ticketId);
    boolean result = originalTicketRegistry.deleteTicket(ticketId);
    if (result && (t instanceof TicketGrantingTicket)) {
      Long userId = queryUserIdFromTgtTicket((TicketGrantingTicket) t);
      if (userId != null) {
        log.debug("Delete user tgt cache:{}", userId);
        // Cache userId with tgtId.
        deleteTgtId(userId);
      }
    }
    return result;
  }

  @Override
  public Collection<Ticket> getTickets() {
    return originalTicketRegistry.getTickets();
  }

  public int sessionCount() {
    return originalTicketRegistry.sessionCount();
  }

  public int serviceTicketCount() {
    return originalTicketRegistry.serviceTicketCount();
  }

  public TicketRegistry getOriginalTicketRegistry() {
    return originalTicketRegistry;
  }

  /**
   * 删除UserId关联的Tgt.
   */
  public void removeTgtByUserId(Long userId) {
    String tgt = queryTgtByUserId(userId);
    if (tgt != null) {
      this.deleteTicket(tgt);
    } else {
      log.debug("The Tgt associated with the user:{} does not exist", userId);
    }
  }

  /**
   * 根据UserId获取关联的Tgt.
   */
  public String queryTgtByUserId(Long userId) {
    return getTgtId(userId);
  }


  /**
   * 根据缓存Id获取tgtId.
   */
  public abstract void deleteTgtId(Long userId);

  /**
   * 删除tgtId.
   */
  public abstract String getTgtId(Long userId);

  /**
   * 缓存tgtId.
   */
  public abstract void setTgtId(Long userId, String tgtId);

  /**
   * 从TicketGrantingTicket中获取关联的用户的id.
   */
  private Long queryUserIdFromTgtTicket(TicketGrantingTicket ticketGrantingTicket) {
    Authentication authentication = ticketGrantingTicket.getAuthentication();
    if (authentication != null) {
      Principal principal = authentication.getPrincipal();
      if (principal != null) {
        return (Long) principal.getAttributes().get(CasProtocol.DianRongCas.getUserIdName());
      }
    }
    return null;
  }
}
