package com.dianrong.common.uniauth.common.cons;

/**
 * Uniauth版本定义.
 *
 * @author wanglin
 */
public class UniauthVersion {

  /**
   * 获取Uniauth的版本信息.
   */
  public static String getVersion() {
    Package pkg = UniauthVersion.class.getPackage();
    return (pkg != null ? pkg.getImplementationVersion() : null);
  }
}
