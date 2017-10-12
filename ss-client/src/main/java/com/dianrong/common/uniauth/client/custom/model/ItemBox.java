package com.dianrong.common.uniauth.client.custom.model;

/**
 * 对于有内部对象这种数据结构的封装.
 */
public abstract class ItemBox<T> {

  private final T item;

  public ItemBox(T item) {
    this.item = item;
  }

  public T getItem() {
    return item;
  }

  /**
   * 对于ItemBox类型对象,一直获取到底.
   */
  public static <T> T getItem(ItemBox itemBox, Class<T> clz) {
    if (itemBox == null) {
      return null;
    }
    Object obj = itemBox.getItem();
    T result = null;
    if (obj instanceof ItemBox) {
      result = getItem((ItemBox) obj, clz);
    }
    if (result != null) {
      return result;
    }
    if (obj == null) {
      return null;
    }
    if (clz.isAssignableFrom(obj.getClass())) {
      return (T) obj;
    }
    return null;
  }
}
