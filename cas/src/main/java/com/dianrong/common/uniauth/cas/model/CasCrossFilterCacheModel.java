package com.dianrong.common.uniauth.cas.model;

import com.dianrong.common.uniauth.common.util.StringUtil;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 用于缓存Cross filter的缓存model.
 *
 * @author wanglin
 */
public final class CasCrossFilterCacheModel implements Serializable {

  private static final long serialVersionUID = -5705975046679932692L;

  /**
   * . 存储crossFilter的cache的正则表达式字符串列表
   */
  private final Set<String> orginRegular;

  /**
   * . 默认的regular字符串分隔符
   */
  private static final String DEFAULTSPILTSTR = ";";

  /**
   * . 构造函数
   */
  public CasCrossFilterCacheModel() {
    this("");
  }

  /**
   * . 构造函数
   *
   * @param orginRegularStr regular字符串
   */
  public CasCrossFilterCacheModel(String orginRegularStr) {
    this(Arrays.asList(orginRegularStr));
  }

  /**
   * . 构造函数
   *
   * @param orginRegularList regular列表
   */
  public CasCrossFilterCacheModel(List<String> orginRegularList) {
    this(orginRegularList, DEFAULTSPILTSTR);
  }

  /**
   * . 构造函数
   *
   * @param orginRegularStr regular字符串
   * @param splitStr 分隔符
   */
  public CasCrossFilterCacheModel(String orginRegularStr, String splitStr) {
    this(Arrays.asList(orginRegularStr), DEFAULTSPILTSTR);
  }

  /**
   * . 构造函数
   *
   * @param orginRegularList regular列表
   * @param splitStr 分隔符
   */
  public CasCrossFilterCacheModel(List<String> orginRegularList, String splitStr) {
    if (StringUtil.strIsNullOrEmpty(splitStr)) {
      splitStr = DEFAULTSPILTSTR;
    }

    Set<String> tempSet = new HashSet<String>();
    // init set
    for (String regularStr : orginRegularList) {
      if (!StringUtil.strIsNullOrEmpty(regularStr)) {
        String[] regulars = regularStr.split(splitStr.trim());
        for (int i = 0, len = regulars.length; i < len; i++) {
          if (regulars[i].trim().length() > 0) {
            tempSet.add(regulars[i].trim());
          }
        }
      }
    }

    // 不能改变的
    this.orginRegular = Collections.unmodifiableSet(tempSet);
  }

  public Set<String> getOrginRegular() {
    return orginRegular;
  }
}
