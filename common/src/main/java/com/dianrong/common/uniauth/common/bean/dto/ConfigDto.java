package com.dianrong.common.uniauth.common.bean.dto;

import java.io.Serializable;

import lombok.ToString;

/**
 * Created by Arc on 25/3/2016.
 */
@ToString
public class ConfigDto implements Serializable {

  private static final long serialVersionUID = -5735868920727388059L;
  private Integer id;
  private String cfgKey;
  private Integer cfgTypeId;
  private String cfgType;
  private String value;
  private byte[] file;

  public Integer getCfgTypeId() {
    return cfgTypeId;
  }

  public ConfigDto setCfgTypeId(Integer cfgTypeId) {
    this.cfgTypeId = cfgTypeId;
    return this;
  }

  public String getCfgType() {
    return cfgType;
  }

  public ConfigDto setCfgType(String cfgType) {
    this.cfgType = cfgType;
    return this;
  }

  public Integer getId() {
    return id;
  }

  public ConfigDto setId(Integer id) {
    this.id = id;
    return this;
  }

  public String getCfgKey() {
    return cfgKey;
  }

  public ConfigDto setCfgKey(String cfgKey) {
    this.cfgKey = cfgKey;
    return this;
  }

  public String getValue() {
    return value;
  }

  public ConfigDto setValue(String value) {
    this.value = value;
    return this;
  }

  public byte[] getFile() {
    return file;
  }

  public ConfigDto setFile(byte[] file) {
    this.file = file;
    return this;
  }
}
