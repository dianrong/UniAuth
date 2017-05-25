package com.dianrong.common.techops.bean;

import java.util.List;

/**
 * Created by Arc on 2/3/2016.
 */
public class Node {

  private String id;
  private String label;
  private String code;
  private String type;
  private Boolean checked;
  private String description;
  private Boolean ownerMarkup;
  private Boolean isRootGrp;
  private List<Node> children;

  public String getId() {
    return id;
  }

  public Node setId(String id) {
    this.id = id;
    return this;
  }

  public String getLabel() {
    return label;
  }

  public Node setLabel(String label) {
    this.label = label;
    return this;
  }

  public String getType() {
    return type;
  }

  public Node setType(String type) {
    this.type = type;
    return this;
  }

  public List<Node> getChildren() {
    return children;
  }

  public Node setChildren(List<Node> children) {
    this.children = children;
    return this;
  }

  public String getCode() {
    return code;
  }

  public Node setCode(String code) {
    this.code = code;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public Node setDescription(String description) {
    this.description = description;
    return this;
  }

  public Boolean getChecked() {
    return checked;
  }

  public Node setChecked(Boolean checked) {
    this.checked = checked;
    return this;
  }

  public Boolean getOwnerMarkup() {
    return ownerMarkup;
  }

  public Node setOwnerMarkup(Boolean ownerMarkup) {
    this.ownerMarkup = ownerMarkup;
    return this;
  }

  public Boolean getIsRootGrp() {
    return isRootGrp;
  }

  public Node setIsRootGrp(Boolean isRootGrp) {
    this.isRootGrp = isRootGrp;
    return this;
  }
}
