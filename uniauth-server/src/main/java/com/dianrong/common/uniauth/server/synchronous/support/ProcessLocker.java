package com.dianrong.common.uniauth.server.synchronous.support;

import com.dianrong.common.uniauth.server.synchronous.exp.AcquireLockFailureException;

/**
 * 执行同步任务的锁获取器.
 */
public interface ProcessLocker {

  /**
   * 获取锁.
   * @throws AcquireLockFailureException 获取锁失败.
   */
  void lock() throws AcquireLockFailureException;

  /**
   * 尝试获取锁.
   * @return 返回True或False.
   */
  boolean tryLock();
}
