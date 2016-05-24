package com.dr.cas.adaptors.jdbc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;

import org.jasig.cas.ticket.Ticket;
import org.jasig.cas.ticket.TicketGrantingTicket;
import org.jasig.cas.ticket.registry.AbstractDistributedTicketRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisTicketRegistry extends AbstractDistributedTicketRegistry implements
    InitializingBean {
  
  private static Logger logger = LoggerFactory.getLogger(RedisTicketRegistry.class);

  private int database;
  private String host;
  private int port;
  private int timeout = 6000; // default to 6 seconds
  private String password;

  private int stTime; // ST max idle time
  private int tgtTime; // TGT max idle time

  private JedisPool cachePool;
  
  public void setDatabase(int database) {
    this.database = database;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public void setTimeout(int timeout) {
    this.timeout = timeout;
  }
  
  public void setPassword(String password) {
    this.password = password;
  }

  public void setStTime(int stTime) {
    this.stTime = stTime;
  }

  public void setTgtTime(int tgtTime) {
    this.tgtTime = tgtTime;
  }
  

  @Override
  public void afterPropertiesSet() throws Exception {
    JedisPoolConfig config = new JedisPoolConfig();
    config.setMaxTotal(64);
    config.setMaxIdle(32);
    config.setTestOnBorrow(true);
    cachePool = new JedisPool(config, host, port, timeout, password, database);


    logger.info("Redis initialized " + host);
  }

  @Override
  public void addTicket(Ticket ticket) {
    
    Jedis jedis = null;
    try {
      jedis = cachePool.getResource();
      int seconds = 0;
      String key = ticket.getId();

      if (ticket instanceof TicketGrantingTicket) {
        seconds = tgtTime / 1000;
      } else {
        seconds = stTime / 1000;
      }


      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(bos);
      oos.writeObject(ticket);
      oos.flush();

      jedis.set(key.getBytes(), bos.toByteArray());
      jedis.expire(key.getBytes(), seconds);

    } catch (Exception e) {
      logger.info("error adding ticket to redis " + e.getMessage());
    } finally {
      try {
        if (jedis != null) jedis.close();
      } catch (Exception e) {}
    }

  }

  @Override
  public boolean deleteTicket(String ticketId) {
    // TODO Auto-generated method stub
    if (ticketId == null) {
      return false;
    }

    Jedis jedis = null;
    try {
      jedis = cachePool.getResource();

      jedis.del(ticketId.getBytes());

    } catch (Exception e) {
      logger.info("error delete ticket from redis " + e.getMessage());
    } finally {
      try {
        if (jedis != null) jedis.close();
      } catch (Exception e) {}
    }

    return true;
  }

  @Override
  public Ticket getTicket(String ticketId) {
    // TODO Auto-generated method stub
    return getProxiedTicketInstance(getRawTicket(ticketId));
  }

  @Override
  public Collection<Ticket> getTickets() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("GetTickets not supported.");
  }

  @Override
  protected boolean needsCallback() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  protected void updateTicket(Ticket ticket) {
    // TODO Auto-generated method stub
    addTicket(ticket);
  }

  private Ticket getRawTicket(String ticketId) {

    if (null == ticketId) {
      return null;
    }

    Ticket ticket = null;
    Jedis jedis = null;
    try {
      jedis = cachePool.getResource();


      byte[] bytes = jedis.get(ticketId.getBytes());
      if (bytes != null && bytes.length > 0) {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        ticket = (Ticket) ois.readObject();
      }
    } catch (Exception e) {
      logger.info("error get ticket from redis " + e.getMessage());
    } finally {
      try {
        if (jedis != null) jedis.close();
      } catch (Exception e) {}
    }

    return ticket;
  }
  
  
  public static void main(String[] args) {
    
    JedisPoolConfig config = new JedisPoolConfig();
    JedisPool pool = new JedisPool(config, "localhost", 6378, 2000, null, 0);
    Jedis jedis = null;
    Jedis jedis2 = null;
    try {
      jedis = pool.getResource();
      jedis2 = pool.getResource();
      System.out.println("active: " + pool.getNumActive());
      System.out.println("idel: " + pool.getNumIdle());
      jedis.set("foo", "bar");
      String foobar = jedis2.get("foo");
      System.out.println("returned foo: " + foobar);
    } finally {
      if (jedis != null) {
        jedis.close();
        jedis2.close();
      }
    }
    System.out.println("active: " + pool.getNumActive());
    System.out.println("idel: " + pool.getNumIdle());
    pool.destroy();
    System.out.println("active: " + pool.getNumActive());
    System.out.println("idel: " + pool.getNumIdle());
  }
}
