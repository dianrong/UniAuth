package com.dianrong.common.uniauth.server.exp;

import com.dianrong.common.uniauth.common.bean.InfoName;
import com.dianrong.common.uniauth.server.support.tree.TreeType;

/**
 * 操作的树结构不匹配的异常.
 * 
 * @author wanglin
 */
public class TreeTypeMisMatchException extends AppException {
  private static final long serialVersionUID = -8540649906647227589L;

  private Integer grpId;

  private TreeType type;

  public TreeTypeMisMatchException(InfoName infoName) {
    this(infoName, null);
  }

  public TreeTypeMisMatchException(InfoName infoName, String msg) {
    this(infoName, msg, null);
  }

  public TreeTypeMisMatchException(InfoName infoName, String msg, Throwable t) {
    super(infoName, msg, t);
  }

  public Integer getGrpId() {
    return grpId;
  }

  public void setGrpId(Integer grpId) {
    this.grpId = grpId;
  }

  public TreeType getType() {
    return type;
  }

  public void setType(TreeType type) {
    this.type = type;
  }
}
