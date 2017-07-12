package com.dianrong.common.uniauth.server.service.support;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 根据存在的list和最终list获取需要处理的list. T 处理的对象类型. E 对象以某种类型为标识.
 */
public abstract class ProcessListQuery<T, E> {
  /**
   * 根据传入的已存在的列表和最终的列表计算出需要的列表.
   * 
   * @param exsistList 已存在的列表.
   * @param destList 最终需要更新成的列表集合.
   * @param insertList true or false. 是获取插入的列表还是删除的列表.
   * @return 得到的结果.
   */
  public List<T> getProcessList(final List<T> exsistList, 
      final List<T> destList, boolean insertList) {
    List<T> innerExistList = exsistList == null ? new ArrayList<T>() : exsistList;
    List<T> innerDestList = destList == null ? new ArrayList<T>() : destList;

    List<T> listToMap;
    List<T> listToList;
    if (insertList) {
      listToMap = innerExistList;
      listToList = innerDestList;
    } else {
      listToList = innerExistList;
      listToMap = innerDestList;
    }
    Map<E, T> map = Maps.newHashMap();
    for (T t : listToMap) {
      map.put(getId(t), t);
    }
    List<T> result = Lists.newArrayList();
    for (T t : listToList) {
      E id = getId(t);
      if (map.get(id) == null) {
        result.add(t);
      }
    }
    return result;
  }

  /**
   * 获取对象的唯一标识.
   */
  public abstract E getId(T o);
}