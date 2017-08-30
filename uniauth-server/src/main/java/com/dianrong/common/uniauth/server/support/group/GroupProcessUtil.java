package com.dianrong.common.uniauth.server.support.group;

import com.dianrong.common.uniauth.common.exp.UniauthCommonException;
import com.dianrong.common.uniauth.common.util.ObjectUtil;
import com.dianrong.common.uniauth.server.util.UniauthServerConstant;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;

/**
 * 分组操作.
 */
@Slf4j public final class GroupProcessUtil {

  /**
   * 分批处理集合.
   * <p>等同于groupProcess(contentCollection, UniauthServerConstant.BATCH_INSERT_NUM, groupProcess);</p>
   * @param contentCollection 集合列表.
   * @param groupProcess 每一批集合的处理实现.
   * @param <T> 集合的类型.
   */
  public static <T> void groupProcess(Collection<T> contentCollection,
      GroupProcess<T> groupProcess) {
    groupProcess(contentCollection, UniauthServerConstant.BATCH_INSERT_NUM, groupProcess);
  }

  /**
   * 分批处理集合.
   * @param contentCollection 集合列表.
   * @param collectionSize 每一批列表大小.
   * @param groupProcess 每一批集合的处理实现.
   * @param <T> 集合的类型.
   */
  public static <T> void groupProcess(Collection<T> contentCollection, int collectionSize,
      GroupProcess<T> groupProcess) {
    if (ObjectUtil.collectionIsEmptyOrNull(contentCollection)) {
      return;
    }

    if (collectionSize <= 0) {
      throw new UniauthCommonException("collection size must be a number that great than 0");
    }
    if (groupProcess == null) {
      throw new UniauthCommonException("groupProcess implement can not be null");
    }

    // 分组处理
    List<T> eachGroup = Lists.newArrayList();
    for (T t : contentCollection) {
      eachGroup.add(t);
      if (eachGroup.size() >= collectionSize) {
        groupProcess.process(eachGroup);
        eachGroup = Lists.newArrayList();
      }
    }
    if (!eachGroup.isEmpty()) {
      groupProcess.process(eachGroup);
    }
  }
}
