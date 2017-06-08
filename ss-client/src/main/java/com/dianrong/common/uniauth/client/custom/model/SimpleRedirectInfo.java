package com.dianrong.common.uniauth.client.custom.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Uniauth自定义的ajax请求跳转格式实现.
 *
 * @author wanglin
 */
public class SimpleRedirectInfo {

  private List<Item> info;

  public List<Item> getInfo() {
    return info;
  }

  public SimpleRedirectInfo(String name, String msg) {
    this.info = new ArrayList<Item>();
    this.info.add(new Item(name, msg));
  }

  /**
   * 构造函数.
   */
  public SimpleRedirectInfo(Map<String, String> infoes) {
    this.info = new ArrayList<Item>();
    if (infoes != null && !infoes.isEmpty()) {
      Set<Entry<String, String>> entrySet = infoes.entrySet();
      for (Entry<String, String> entry : entrySet) {
        this.info.add(new Item(entry.getKey(), entry.getValue()));
      }
    }
  }

  public class Item {

    private final String name;
    private final String msg;

    public Item(String name, String msg) {
      this.name = name;
      this.msg = msg;
    }

    public String getName() {
      return name;
    }

    public String getMsg() {
      return msg;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((msg == null) ? 0 : msg.hashCode());
      result = prime * result + ((name == null) ? 0 : name.hashCode());
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      Item other = (Item) obj;
      if (msg == null) {
        if (other.msg != null) {
          return false;
        }
      } else if (!msg.equals(other.msg)) {
        return false;
      }
      if (name == null) {
        if (other.name != null) {
          return false;
        }
      } else if (!name.equals(other.name)) {
        return false;
      }
      return true;
    }
  }
}
