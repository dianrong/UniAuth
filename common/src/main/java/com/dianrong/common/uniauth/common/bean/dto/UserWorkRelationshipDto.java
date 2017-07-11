package com.dianrong.common.uniauth.common.bean.dto;

import io.swagger.annotations.ApiModel;

import java.util.Date;

import lombok.ToString;

@ToString
@ApiModel("用户的工作上下级关系")
public class UserWorkRelationshipDto extends TenancyBaseDto {

  private static final long serialVersionUID = 2660609756037424927L;

  private Long id;

  private Long userId;

  private Long managerId;

  private Long supervisorId;

  private Byte type;

  private Date assignmentDate;

  private Date hireDate;

  private String businessUnitName;

  private String departmentName;

  private String legalEntityName;

  private String workPhone;

  private String workLocation;

  private String workAddress;

  private Date createDate;

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
