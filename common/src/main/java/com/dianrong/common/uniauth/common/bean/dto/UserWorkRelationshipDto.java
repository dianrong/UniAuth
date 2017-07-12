package com.dianrong.common.uniauth.common.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

import lombok.ToString;

@ToString
@ApiModel("汇报关系信息")
public class UserWorkRelationshipDto extends TenancyBaseDto {

  private static final long serialVersionUID = 2660609756037424927L;

  @ApiModelProperty("主键id")
  private Long id;

  @ApiModelProperty("关联的用户id")
  private Long userId;

  @ApiModelProperty("经理的id")
  private Long managerId;

  @ApiModelProperty("监管员的id")
  private Long supervisorId;

  @ApiModelProperty("类型")
  private Byte type;

  @ApiModelProperty("到该岗位日期")
  private Date assignmentDate;

  @ApiModelProperty("被聘用日期")
  private Date hireDate;

  @ApiModelProperty("所属业务部门名称")
  private String businessUnitName;

  @ApiModelProperty("所属部门名称")
  private String departmentName;

  @ApiModelProperty("法定实体名称")
  private String legalEntityName;

  @ApiModelProperty("工作电话")
  private String workPhone;

  @ApiModelProperty("工作位置")
  private String workLocation;

  @ApiModelProperty("工作地址")
  private String workAddress;

  @ApiModelProperty("记录创建时间")
  private Date createDate;

  @ApiModelProperty("记录最新更新时间")
  private Date lastUpdate;

  public Long getId() {
    return id;
  }

  public UserWorkRelationshipDto setId(Long id) {
    this.id = id;
    return this;
  }

  public Long getUserId() {
    return userId;
  }

  public UserWorkRelationshipDto setUserId(Long userId) {
    this.userId = userId;
    return this;
  }

  public Long getManagerId() {
    return managerId;
  }

  public UserWorkRelationshipDto setManagerId(Long managerId) {
    this.managerId = managerId;
    return this;
  }

  public Long getSupervisorId() {
    return supervisorId;
  }

  public UserWorkRelationshipDto setSupervisorId(Long supervisorId) {
    this.supervisorId = supervisorId;
    return this;
  }

  public Byte getType() {
    return type;
  }

  public UserWorkRelationshipDto setType(Byte type) {
    this.type = type;
    return this;
  }

  public Date getAssignmentDate() {
    return assignmentDate;
  }

  public UserWorkRelationshipDto setAssignmentDate(Date assignmentDate) {
    this.assignmentDate = assignmentDate;
    return this;
  }

  public Date getHireDate() {
    return hireDate;
  }

  public UserWorkRelationshipDto setHireDate(Date hireDate) {
    this.hireDate = hireDate;
    return this;
  }

  public String getBusinessUnitName() {
    return businessUnitName;
  }

  public UserWorkRelationshipDto setBusinessUnitName(String businessUnitName) {
    this.businessUnitName = businessUnitName;
    return this;
  }

  public String getDepartmentName() {
    return departmentName;
  }

  public UserWorkRelationshipDto setDepartmentName(String departmentName) {
    this.departmentName = departmentName;
    return this;
  }

  public String getLegalEntityName() {
    return legalEntityName;
  }

  public UserWorkRelationshipDto setLegalEntityName(String legalEntityName) {
    this.legalEntityName = legalEntityName;
    return this;
  }

  public String getWorkPhone() {
    return workPhone;
  }

  public UserWorkRelationshipDto setWorkPhone(String workPhone) {
    this.workPhone = workPhone;
    return this;
  }

  public String getWorkLocation() {
    return workLocation;
  }

  public UserWorkRelationshipDto setWorkLocation(String workLocation) {
    this.workLocation = workLocation;
    return this;
  }

  public String getWorkAddress() {
    return workAddress;
  }

  public UserWorkRelationshipDto setWorkAddress(String workAddress) {
    this.workAddress = workAddress;
    return this;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public UserWorkRelationshipDto setCreateDate(Date createDate) {
    this.createDate = createDate;
    return this;
  }

  public Date getLastUpdate() {
    return lastUpdate;
  }

  public UserWorkRelationshipDto setLastUpdate(Date lastUpdate) {
    this.lastUpdate = lastUpdate;
    return this;
  }
}
