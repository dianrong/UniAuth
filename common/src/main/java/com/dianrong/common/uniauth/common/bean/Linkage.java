package com.dianrong.common.uniauth.common.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

/**
 * Created by Arc on 18/3/2016.
 */
@ApiModel("类型map的数据结构")
public class Linkage<E1 extends Serializable, E2 extends Serializable> implements Serializable {

  private static final long serialVersionUID = -1461492060919842540L;

  @ApiModelProperty("关联的key")
  private E1 entry1;

  @ApiModelProperty("关联的value")
  private E2 entry2;

  public E1 getEntry1() {
    return entry1;
  }

  public Linkage<E1, E2> setEntry1(E1 entry1) {
    this.entry1 = entry1;
    return this;
  }

  public E2 getEntry2() {
    return entry2;
  }

  public Linkage<E1, E2> setEntry2(E2 entry2) {
    this.entry2 = entry2;
    return this;
  }

}
