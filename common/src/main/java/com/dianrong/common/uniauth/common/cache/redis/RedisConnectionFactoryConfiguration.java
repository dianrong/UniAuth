package com.dianrong.common.uniauth.common.cache.redis;

import com.dianrong.common.uniauth.common.util.StringUtil;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Set;

/**
 * Redis ConnectionFactory的配置参数.
 */
@Slf4j @ToString public class RedisConnectionFactoryConfiguration {

  /**
   * 配置的默认配置.如果没有主动配置属性,则使用defaultConfiguration的配置信息作为配置.
   */
  private RedisConnectionFactoryConfiguration defaultConfiguration;

  /**
   * 选取redis的类型,默认值为SINGLE.
   */
  private ConfigTagItem<RedisType> type =
      new ConfigTagItem<>(RedisType.SINGLE, new ValueQuery<RedisType>() {
        @Override public RedisType get(RedisConnectionFactoryConfiguration defaultConfiguration) {
          return defaultConfiguration.getType();
        }
      });

  /**
   * 选取redis的database,默认值为0.
   */
  private ConfigTagItem<Integer> database = new ConfigTagItem<>(0, new ValueQuery<Integer>() {
    @Override public Integer get(RedisConnectionFactoryConfiguration defaultConfiguration) {
      return defaultConfiguration.getDatabase();
    }
  });

  /**
   * 连接redis的密码
   */
  private ConfigTagItem<String> password = new ConfigTagItem<>(new ValueQuery<String>() {
    @Override public String get(RedisConnectionFactoryConfiguration defaultConfiguration) {
      return defaultConfiguration.getPassword();
    }
  });

  /**
   * 连接的超时时间(毫秒数).
   */
  private ConfigTagItem<Integer> timeout = new ConfigTagItem<>(3000, new ValueQuery<Integer>() {
    @Override public Integer get(RedisConnectionFactoryConfiguration defaultConfiguration) {
      return defaultConfiguration.getTimeout();
    }
  });

  // Single configuration
  /**
   * Redis服务器Host.
   */
  private ConfigTagItem<String> host = new ConfigTagItem<>("localhost", new ValueQuery<String>() {
    @Override public String get(RedisConnectionFactoryConfiguration defaultConfiguration) {
      return defaultConfiguration.getHost();
    }
  });

  /**
   * Redis服务器端口.
   */
  private ConfigTagItem<Integer> port = new ConfigTagItem<>(6379, new ValueQuery<Integer>() {
    @Override public Integer get(RedisConnectionFactoryConfiguration defaultConfiguration) {
      return defaultConfiguration.getPort();
    }
  });

  // Sentinel configuration
  /**
   * Master节点.
   */
  private ConfigTagItem<String> master = new ConfigTagItem<>("mymaster", new ValueQuery<String>() {
    @Override public String get(RedisConnectionFactoryConfiguration defaultConfiguration) {
      return defaultConfiguration.getMaster();
    }
  });
  /**
   * 哨兵节点集合.
   */
  private ConfigTagItem<Set<String>> sentinels = new ConfigTagItem<>(new ValueQuery<Set<String>>() {
    @Override public Set<String> get(RedisConnectionFactoryConfiguration defaultConfiguration) {
      return defaultConfiguration.getSentinels();
    }
  });

  // Cluster configuration
  /**
   * 集群节点集合.
   */
  private ConfigTagItem<Set<String>> clusters = new ConfigTagItem<>(new ValueQuery<Set<String>>() {
    @Override public Set<String> get(RedisConnectionFactoryConfiguration defaultConfiguration) {
      return defaultConfiguration.getClusters();
    }
  });

  /**
   * 集群失败之后的重定向次数.
   */
  private ConfigTagItem<Integer> maxRedirects = new ConfigTagItem<>(5, new ValueQuery<Integer>() {
    @Override public Integer get(RedisConnectionFactoryConfiguration defaultConfiguration) {
      return defaultConfiguration.getMaxRedirects();
    }
  });

  public void setType(String type) {
    if (type == null) {
      return;
    }
    this.type.setContent(RedisType.toType(type));
  }

  public void setDatabase(String database) {
    if (database == null) {
      return;
    }
    Integer tempDatabase = StringUtil.tryToTranslateStrToInt(database);
    Assert.isTrue(tempDatabase == null || tempDatabase >= 0,
        "parameter database must be equal or greater than 0");
    this.database.setContent(tempDatabase);
  }

  public void setTimeout(String timeout) {
    if (timeout == null) {
      return;
    }
    Integer tempTimeout = StringUtil.tryToTranslateStrToInt(timeout);
    Assert.isTrue(tempTimeout == null || tempTimeout >= 0,
        "parameter timeout must be equal or greater than 0");
    this.timeout.setContent(tempTimeout);
  }

  public void setHost(String host) {
    this.host.setContent(host);
  }

  public void setPassword(String password) {
    this.password.setContent(password);
  }

  public void setPort(String port) {
    if (port == null) {
      return;
    }
    Integer tempPort = StringUtil.tryToTranslateStrToInt(port);
    Assert.isTrue(tempPort == null || (tempPort > 0 && tempPort < 65535),
        "parameter timeout must be greater than 0 and less than 65535");
    this.port.setContent(tempPort);
  }

  public void setMaster(String master) {
    this.master.setContent(master);
  }

  public void setSentinels(String sentinels) {
    if (sentinels == null) {
      return;
    }
    this.sentinels.setContent(StringUtils.commaDelimitedListToSet(sentinels.trim()));
  }


  public void setClusters(String clusters) {
    if (clusters == null) {
      return;
    }
    this.clusters.setContent(StringUtils.commaDelimitedListToSet(clusters.trim()));
  }

  public void setMaxRedirects(String maxRedirects) {
    if (maxRedirects == null) {
      return;
    }
    Integer tempMaxRedirects = StringUtil.tryToTranslateStrToInt(maxRedirects);
    Assert.isTrue(tempMaxRedirects == null || (tempMaxRedirects >= 0),
        "parameter timeout must be equal or greater than 0");
    this.maxRedirects.setContent(tempMaxRedirects);
  }

  public RedisType getType() {
    return getValue(type);
  }

  public int getDatabase() {
    return getValue(database);
  }

  public int getTimeout() {
    return getValue(timeout);
  }

  public String getPassword() {
    return getValue(password);
  }

  public String getHost() {
    return getValue(host);
  }

  public int getPort() {
    return getValue(port);
  }

  public String getMaster() {
    return getValue(master);
  }

  public Set<String> getSentinels() {
    return getValue(sentinels);
  }

  public Set<String> getClusters() {
    return getValue(clusters);
  }

  public int getMaxRedirects() {
    return getValue(maxRedirects);
  }

  /**
   * 如果item是主动配置过的,则直接返回其值.
   * 如果没有主动配置过的,则查看是否有配置默认的配置对象,
   * 返回默认配置的信息.
   */
  private <E> E getValue(ConfigTagItem<E> item) {
    if (item.configTag) {
      return item.content;
    }
    if (this.defaultConfiguration != null) {
      return item.query.get(this.defaultConfiguration);
    }
    return item.content;
  }

  private interface ValueQuery<E> {
    E get(RedisConnectionFactoryConfiguration defaultConfiguration);
  }

  public void setDefaultConfiguration(RedisConnectionFactoryConfiguration defaultConfiguration) {
    this.defaultConfiguration = defaultConfiguration;
  }

  /**
   * 用于标识某个属性是否配置过.
   */
  private static final class ConfigTagItem<T> {
    /**
     * 是否配置过.
     */
    private boolean configTag = false;

    private T content;

    private ValueQuery<T> query;

    public ConfigTagItem(ValueQuery<T> query) {
      this(null, query);
    }

    public ConfigTagItem(T content, ValueQuery<T> query) {
      this.content = content;
      this.query = query;
    }

    public void setContent(T content) {
      this.configTag = true;
      this.content = content;
    }
  }
}
