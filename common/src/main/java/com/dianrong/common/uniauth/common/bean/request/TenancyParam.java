package com.dianrong.common.uniauth.common.bean.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;

@ApiModel("租户接口请求参数model")
@ToString
public class TenancyParam extends PageParam {

  private static final long serialVersionUID = -1587818665974806460L;

  @ApiModelProperty("主键id")
  private Long id;

  @ApiModelProperty("租户编码")
  private String code;

  @ApiModelProperty("租户名称")
  private String name;

  @ApiModelProperty("联系人")
  private String contactName;

  @ApiModelProperty("联系电话")
  private String phone;

  @ApiModelProperty("信息描述")
  private String description;

  @ApiModelProperty("状态")
  private Byte status;

  @ApiModelProperty("管理员的邮箱")
  private String adminEmail;

  @ApiModelProperty("管理员的密码")
  private String adminPassword;

  public Long getId() {
    return id;
  }

  public String getCode() {
    return code;
  }

  public String getName() {
    return name;
  }

  public String getContactName() {
    return contactName;
  }

  public String getPhone() {
    return phone;
  }

  public String getDescription() {
    return description;
  }

  public Byte getStatus() {
    return status;
  }

  public TenancyParam setId(Long id) {
    this.id = id;
    return this;
  }

  public TenancyParam setCode(String code) {
    this.code = code;
    return this;
  }

  public TenancyParam setName(String name) {
    this.name = name;
    return this;
  }

  public TenancyParam setContactName(String contactName) {
    this.contactName = contactName;
    return this;
  }

  public TenancyParam setPhone(String phone) {
    this.phone = phone;
    return this;
  }

  public TenancyParam setDescription(String description) {
    this.description = description;
    return this;
  }

  public TenancyParam setStatus(Byte status) {
    this.status = status;
    return this;
  }

  public String getAdminEmail() {
    return adminEmail;
  }

  public TenancyParam setAdminEmail(String adminEmail) {
    this.adminEmail = adminEmail;
    return this;
  }

  public String getAdminPassword() {
    return adminPassword;
  }

  public TenancyParam setAdminPassword(String adminPassword) {
    this.adminPassword = adminPassword;
    return this;
  }
}
