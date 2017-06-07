package com.dianrong.common.uniauth.server.support.apicontrl;

import com.dianrong.common.uniauth.common.bean.dto.ApiPermissionDto;
import com.dianrong.common.uniauth.server.service.ApiPermissionService;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * 用于辅助 ApiControlFilter 完成权限控制的功能 主要是做一些缓存处理.
 *
 * @author wanglin
 */
@Component
public final class ServerPermissionCacher {

  /**
   * 更新公共权限信息的时间间隔(分钟).
   */
  public static final Long REFRESH_PERMISSION_MINUTE_PERIOD = 60L;

  // 缓存一些正则表达式
  private final Map<String, Pattern> patternsCache = Maps.newConcurrentMap();

  // 缓存公共的权限信息
  private Set<ApiCtlPermissionItem> publicPermissions = Sets.newHashSet();

  // 定时刷新缓存的执行器
  private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

  @Autowired
  private ApiPermissionService apiPermissionService;

  // 定时更新权限信息的任务
  private Runnable refreshPermissionTask = new Runnable() {
    @Override
    public void run() {
      List<ApiPermissionDto> permissionDtos = apiPermissionService.searchAllPublicPermissions();
      Set<ApiCtlPermissionItem> permissions = Sets.newHashSet();
      for (ApiPermissionDto p : permissionDtos) {
        ApiCtlPermissionItem item = new ApiCtlPermissionItem();
        item.setMethod(p.getMethod()).setUri(p.getUri());
        permissions.add(item);
      }
      publicPermissions = permissions;
    }
  };

  public ServerPermissionCacher() {
    super();
  }

  /**
   * 启动缓存刷新任务.
   */
  @PostConstruct
  public void setUpRefreshTask() {
    // 初始化cache的时候 同步刷新一次缓存
    // 如果通过异步的方式刷新缓存,有可能cache在使用的时候,还没有刷新缓存,导致从缓存中拿到数据不正确
    // 典型的就是,Uniauth-server在重启的时候,访问API会提示没有访问权限.
    refreshPermissionTask.run();
    // 添加定时执行任务
    executor.scheduleAtFixedRate(refreshPermissionTask, REFRESH_PERMISSION_MINUTE_PERIOD,
        REFRESH_PERMISSION_MINUTE_PERIOD, TimeUnit.MINUTES);
  }

  /**
   * Get pattern from cache. if no cache, create a new one and cache it
   *
   * @param patternStr patternStr can not null
   * @return Pattern
   * @throws IllegalArgumentException if patternStr is null
   * @throws PatternSyntaxException if patternStr is a invalid pattern String
   */
  public Pattern getPattern(String patternStr) {
    Assert.notNull(patternStr);
    Pattern pattern = patternsCache.get(patternStr);
    if (pattern == null) {
      pattern = Pattern.compile(patternStr);
      // cache
      patternsCache.put(patternStr, pattern);
    }
    return patternsCache.get(patternStr);
  }

  /**
   * Return public permissions.
   *
   * @return Set of apiCtlPermissionItem
   */
  public Set<ApiCtlPermissionItem> getPublicPermissions() {
    return Collections.unmodifiableSet(this.publicPermissions);
  }
}
