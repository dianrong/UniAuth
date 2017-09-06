package com.dianrong.common.uniauth.server.support.tree;

import com.dianrong.common.uniauth.common.exp.UniauthCommonException;
import com.dianrong.common.uniauth.common.util.Assert;

/**
 * 存放当前请求的TreeType信息.
 */
public final class TreeTypeHolder {

  /**
   * 存放树类型信息.
   */
  private static final ThreadLocal<TreeType> TREE_TYPES = new ThreadLocal<>();

  public static void set(TreeType type) {
    Assert.notNull(type);
    TREE_TYPES.set(type);
  }

  public static TreeType get() {
    return TREE_TYPES.get();
  }

  /**
   * 如果当前线程没有设置树类型信息,则抛出异常.
   * @throws UniauthCommonException 当前线程中没有树类型信息.
   */
  public static TreeType getWithCheck() {
    TreeType type = TREE_TYPES.get();
    if (type == null) {
      throw new UniauthCommonException("Current thread is not set TreeType info!");
    }
    return type;
  }

  public static void clear() {
    TREE_TYPES.remove();
  }
}
