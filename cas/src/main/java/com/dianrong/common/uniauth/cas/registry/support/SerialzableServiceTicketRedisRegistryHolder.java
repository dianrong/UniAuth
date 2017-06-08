package com.dianrong.common.uniauth.cas.registry.support;

import java.lang.reflect.Field;
import lombok.extern.slf4j.Slf4j;
import org.jasig.cas.ticket.ServiceTicket;
import org.jasig.cas.ticket.Ticket;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

/**
 * . 专门用于处理RedisTicketRegistry
 *
 * @author wanglin
 */
@Slf4j
public final class SerialzableServiceTicketRedisRegistryHolder implements
    SerialzableTicketRegistryHolder {

  private static final long serialVersionUID = -178234378526374281L;
  /**
   * . service ticket的policy对象
   */
  private CasMultiTimeUseOrTimeoutExpirationPolicy serviceTicketExpirationPolicy;


  public CasMultiTimeUseOrTimeoutExpirationPolicy getServiceTicketExpirationPolicy() {
    return serviceTicketExpirationPolicy;
  }

  public void setServiceTicketExpirationPolicy(
      CasMultiTimeUseOrTimeoutExpirationPolicy serviceTicketExpirationPolicy) {
    this.serviceTicketExpirationPolicy = serviceTicketExpirationPolicy;
  }

  /**
   * . 序列化之前
   */
  @Override
  public void beforeSerializable(Ticket ticket) {
    // 目前只处理service ticket
    if (!(ticket instanceof ServiceTicket)) {
      return;
    }
    setField("expirationPolicy", ticket, null);
  }

  @Override
  public void afterSerializable(Ticket ticket) {
    // 目前只处理service ticket
    if (!(ticket instanceof ServiceTicket)) {
      return;
    }
    setField("expirationPolicy", ticket, serviceTicketExpirationPolicy);
  }

  @Override
  public void afterDserializable(Ticket ticket) {
    // 目前只处理service ticket
    if (!(ticket instanceof ServiceTicket)) {
      return;
    }
    setField("expirationPolicy", ticket, serviceTicketExpirationPolicy);
  }

  /**
   * . 通过反设置某个字段的值
   */
  private void setField(String fieldName, Object target, Object value) {
    Assert.notNull(fieldName, "fieldName can not be null");
    Assert.notNull(target, "target can not be null");
    Field field = ReflectionUtils.findField(target.getClass(), fieldName);
    if (field == null) {
      log.warn(String
          .format("can not find field %s in class %s ", fieldName, target.getClass().getName()));
      return;
    }
    boolean accessible = field.isAccessible();
    try {
      field.setAccessible(true);
      ReflectionUtils.setField(field, target, value);
    } catch (IllegalStateException ex) {
      log.warn(
          String.format("set field %s in class %s failed", fieldName, target.getClass().getName()),
          ex);
    } finally {
      field.setAccessible(accessible);
    }
  }

}
