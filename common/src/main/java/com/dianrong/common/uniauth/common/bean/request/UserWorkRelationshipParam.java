package com.dianrong.common.uniauth.common.bean.request;

import java.util.Date;

public class UserWorkRelationshipParam extends Operator {

  private static final long serialVersionUID = -6348944638499690435L;

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

  public Long getId() {
    return id;
  }

  public UserWorkRelationshipParam setId(Long id) {
    this.id = id;
    return this;
  }

  public Long getUserId() {
    return userId;
  }

  public UserWorkRelationshipParam setUserId(Long userId) {
    this.userId = userId;
    return this;
  }

  public Long getManagerId() {
    return managerId;
  }

  public UserWorkRelationshipParam setManagerId(Long managerId) {
    this.managerId = managerId;
    return this;
  }

  public Long getSupervisorId() {
    return supervisorId;
  }

  public UserWorkRelationshipParam setSupervisorId(Long supervisorId) {
    this.supervisorId = supervisorId;
    return this;
  }

  public Byte getType() {
    return type;
  }

  public UserWorkRelationshipParam setType(Byte type) {
    this.type = type;
    return this;
  }

  public Date getAssignmentDate() {
    return assignmentDate;
  }

  public UserWorkRelationshipParam setAssignmentDate(Date assignmentDate) {
    this.assignmentDate = assignmentDate;
    return this;
  }

  public Date getHireDate() {
    return hireDate;
  }

  public UserWorkRelationshipParam setHireDate(Date hireDate) {
    this.hireDate = hireDate;
    return this;
  }

  public String getBusinessUnitName() {
    return businessUnitName;
  }

  public UserWorkRelationshipParam setBusinessUnitName(String businessUnitName) {
    this.businessUnitName = businessUnitName;
    return this;
  }

  public String getDepartmentName() {
    return departmentName;
  }

  public UserWorkRelationshipParam setDepartmentName(String departmentName) {
    this.departmentName = departmentName;
    return this;
  }

  public String getLegalEntityName() {
    return legalEntityName;
  }

  public UserWorkRelationshipParam setLegalEntityName(String legalEntityName) {
    this.legalEntityName = legalEntityName;
    return this;
  }

  public String getWorkPhone() {
    return workPhone;
  }

  public UserWorkRelationshipParam setWorkPhone(String workPhone) {
    this.workPhone = workPhone;
    return this;
  }

  public String getWorkLocation() {
    return workLocation;
  }

  public UserWorkRelationshipParam setWorkLocation(String workLocation) {
    this.workLocation = workLocation;
    return this;
  }

  public String getWorkAddress() {
    return workAddress;
  }

  public UserWorkRelationshipParam setWorkAddress(String workAddress) {
    this.workAddress = workAddress;
    return this;
  }
}
