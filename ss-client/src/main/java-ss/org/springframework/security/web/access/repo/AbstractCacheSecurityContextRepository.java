package org.springframework.security.web.access.repo;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpRequestResponseHolder;

import com.dianrong.common.uniauth.common.cache.UniauthCache;
import com.dianrong.common.uniauth.common.cache.UniauthCacheManager;
import com.dianrong.common.uniauth.common.util.Assert;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractCacheSecurityContextRepository
    implements UniauthSecurityContextRepository {

  protected final UniauthCache contextCache;

  private long cacheMinutes = 10;

  public AbstractCacheSecurityContextRepository(UniauthCacheManager uniauthCacheManager) {
    Assert.notNull(uniauthCacheManager, "UniauthCacheManager must not be null");
    this.contextCache = uniauthCacheManager.getCache(getCacheName());
  }

  @Override
  public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
    HttpServletRequest request = requestResponseHolder.getRequest();
    String key = getCacheKey(request);
    if (key == null) {
      log.warn(getCacheName() + ": the cache key is null");
      return null;
    }
    Object contextFromCache = contextCache.get(key);
    if (contextFromCache instanceof SecurityContext) {
      return (SecurityContext) contextFromCache;
    }
    if (contextFromCache != null) {
      log.debug("Obtained a valid SecurityContext from " + key + ": '" + contextFromCache + "'");
    }
    return generateNewContext();
  }

  @Override
  public void saveContext(SecurityContext context, HttpServletRequest request,
      HttpServletResponse response) {
    if (context == null) {
      return;
    }
    String key = getCacheKey(request);
    if (key == null) {
      log.warn(getCacheName() + ": the cache key is null");
      return;
    }
    if (!containsContext(request)) {
      contextCache.put(key, context, this.cacheMinutes, TimeUnit.MINUTES);
    }
  }

  @Override
  public boolean containsContext(HttpServletRequest request) {
    String key = getCacheKey(request);
    if (key == null) {
      log.warn(getCacheName() + ": the cache key is null");
      return false;
    }
    return contextCache.get(key) != null;
  }

  /**
   * 创建新的SecurityContext.
   */
  protected SecurityContext generateNewContext() {
    return SecurityContextHolder.createEmptyContext();
  }

  public long getCacheMinutes() {
    return cacheMinutes;
  }

  public void setCacheMinutes(long cacheMinutes) {
    this.cacheMinutes = cacheMinutes;
  }

  /**
   * 返回缓存的名称,不能为空.
   */
  protected abstract String getCacheName();

  protected abstract String getCacheKey(HttpServletRequest request);
}
