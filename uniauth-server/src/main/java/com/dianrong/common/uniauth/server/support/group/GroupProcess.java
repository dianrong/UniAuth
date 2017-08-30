package com.dianrong.common.uniauth.server.support.group;

import java.util.Collection;
import java.util.List;

public interface GroupProcess<T> {

  /**
   * 分组处理的实现操作.
   * @param groupList 传入的分好组的集合.
   */
  void process(List<T> groupList);

}
