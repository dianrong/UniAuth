package com.dianrong.common.uniauth.common.bean.request;

import java.util.List;

public class DomainParam extends PageParam {

  private static final long serialVersionUID = -1587818665974806460L;
  private List<Integer> domainIds;
  private Integer id;
  // generally, code can't be changed!
  private String code;
  private String displayName;
  private String description;
  private Byte status;

  private List<String> domainCodeList;
  private List<Long> includeTenancyIds;

  public List<String> getDomainCodeList() {
    return domainCodeList;
  }

  public DomainParam setDomainCodeList(List<String> domainCodeList) {
    this.domainCodeList = domainCodeList;
    return this;
  }

  public Integer getId() {
    return id;
  }

  public DomainParam setId(Integer id) {
    this.id = id;
    return this;
  }

  public String getCode() {
    return code;
  }

  public DomainParam setCode(String code) {
    this.code = code;
    return this;
  }

  public String getDisplayName() {
    return displayName;
  }

  public DomainParam setDisplayName(String displayName) {
    this.displayName = displayName;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public DomainParam setDescription(String description) {
    this.description = description;
    return this;
  }

  public Byte getStatus() {
    return status;
  }

  public DomainParam setStatus(Byte status) {
    this.status = status;
    return this;
  }

  public List<Integer> getDomainIds() {
    return domainIds;
  }

  public DomainParam setDomainIds(List<Integer> domainIds) {
    this.domainIds = domainIds;
    return this;
  }

  public List<Long> getIncludeTenancyIds() {
    return includeTenancyIds;
  }

  public DomainParam setIncludeTenancyIds(List<Long> includeTenancyIds) {
    this.includeTenancyIds = includeTenancyIds;
    return this;
  }

  @Override
  public String toString() {
    return "DomainParam [domainIds=" + domainIds + ", id=" + id + ", code=" + code
        + ", displayName=" + displayName + ", description=" + description + ", status=" + status
        + ", domainCodeList=" + domainCodeList + ", includeTenancyIds=" + includeTenancyIds + "]";
  }
}
