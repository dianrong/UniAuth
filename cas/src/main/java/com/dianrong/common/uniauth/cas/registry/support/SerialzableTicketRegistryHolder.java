package com.dianrong.common.uniauth.cas.registry.support;

import java.io.Serializable;

import org.jasig.cas.ticket.Ticket;

/**
 * . 需要序列化存储ticket的registry的辅助holder
 *
 * @author wanglin
 */
public interface SerialzableTicketRegistryHolder extends Serializable {

  /**
   * . 序列化之前处理
   */
  void beforeSerializable(Ticket ticket);

  /**
   * . 序列化之后处理
   */
  void afterSerializable(Ticket ticket);

  /**
   * . 反序列化之后处理
   */
  void afterDserializable(Ticket ticket);
}
