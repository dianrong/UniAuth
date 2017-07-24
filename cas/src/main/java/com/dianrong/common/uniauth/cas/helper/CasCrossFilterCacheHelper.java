package com.dianrong.common.uniauth.cas.helper;

import com.dianrong.common.uniauth.cas.helper.thread.RefreshCasCrossFilterCacheRunnable;
import com.dianrong.common.uniauth.cas.helper.thread.SingleScheduledThreadPool;
import com.dianrong.common.uniauth.cas.model.CasCrossFilterCacheModel;
import com.dianrong.common.uniauth.cas.service.CfgService;
import com.dianrong.common.uniauth.common.bean.dto.ConfigDto;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.util.StringUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.annotation.PostConstruct;
import javax.servlet.FilterConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 用于辅助CAS的Cross filter获取缓存数据<br>.
 * PS: 目前只缓存Orgin数据.
 *
 * @author wanglin
 */
@Slf4j
public final class CasCrossFilterCacheHelper {

  /**
   * 调用远程服务的service.
   */
  @Autowired
  private CfgService cfgService;

  /**
   * 缓存对象.
   */
  private volatile CasCrossFilterCacheModel cacheModel;

  /**
   * 缓存Orgin的正则表达式.
   */
  private volatile Set<Pattern> patternCache;


  /**
   * Origins from {@link FilterConfig}.
   */
  private String defualtCrosAllowedOrigins;

  public String getDefualtCrosAllowedOrigins() {
    return defualtCrosAllowedOrigins;
  }

  public void setDefualtCrosAllowedOrigins(String defualtCrosAllowedOrigins) {
    this.defualtCrosAllowedOrigins = defualtCrosAllowedOrigins;
  }

  /**
   * 获取Origin列表的接口方法.
   */
  public Set<String> getOriginCacheSet() {
    if (this.cacheModel == null) {
      throw new RuntimeException("CasCrossFilterCacheHelper init failed!!");
    }
    return this.cacheModel.getOrginRegular();
  }

  /**
   * 获取Origin列表的接口方法.
   */
  public Set<Pattern> getOriginRegularCacheSet() {
    return this.patternCache;
  }

  /**
   * 设置缓存的初始值.
   */
  @PostConstruct
  private void init() {
    // 直接刷新缓存
    refreshCache();
    // 开启异步刷新线程
    SingleScheduledThreadPool.INSTANCE
        .loadScheduledTask(new RefreshCasCrossFilterCacheRunnable(this),
            AppConstants.CAS_CFG_CACHE_REFRESH_PERIOD_MILLES,
            AppConstants.CAS_CFG_CACHE_REFRESH_PERIOD_MILLES);
  }

  /**
   * 刷新缓存.
   */
  public void refreshCache() {
    try {
      List<ConfigDto> cfges = cfgService
          .queryConfigDtoByLikeCfgKeys(AppConstants.CAS_CFG_KEY_CROSS_FILTER_ORIGIN_PREFIX);
      if (cfges != null && !cfges.isEmpty()) {
        // 刷新缓存
        List<String> regulars = new ArrayList<String>();
        for (ConfigDto cto : cfges) {
          if (AppConstants.CAS_CFG_TYPE_TEXT.equalsIgnoreCase(cto.getCfgType()) && !StringUtil
              .strIsNullOrEmpty(cto.getValue())) {
            regulars.add(cto.getValue());
          }
        }
        this.cacheModel = new CasCrossFilterCacheModel(regulars);
      }
    } catch (Exception ex) {
      log.warn("CasCrossFilterCacheHelper refresh cache exception", ex);
    }
    if (this.cacheModel == null) {
      if (!StringUtil.strIsNullOrEmpty(this.defualtCrosAllowedOrigins)) {
        // 使用默认值
        this.cacheModel = new CasCrossFilterCacheModel(this.defualtCrosAllowedOrigins);
      } else {
        // 使用默认值
        this.cacheModel = new CasCrossFilterCacheModel();
      }
    }

    // refresh regular cache
    Set<Pattern> tempPattern = new HashSet<Pattern>();
    for (String torigin : this.cacheModel.getOrginRegular()) {
      try {
        Pattern tp = Pattern.compile(torigin);
        tempPattern.add(tp);
      } catch (PatternSyntaxException pse) {
        log.error("invalid regular pattern : " + torigin);
      }
    }
    this.patternCache = Collections.unmodifiableSet(tempPattern);
  }
}
