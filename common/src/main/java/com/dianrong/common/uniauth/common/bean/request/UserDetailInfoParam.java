package com.dianrong.common.uniauth.common.bean.request;

import java.util.Date;

import lombok.ToString;

/**
 * 用户属性集合请求参数.
 */
@ToString
public class UserDetailInfoParam extends Operator {
  private static final long serialVersionUID = -1274068077790724316L;
  
  private Long id;

  private Long userId;

  private String firstName;

  private String lastName;

  private String displayName;

  private String nickName;

  private String identityNo;

  private String motto;

  private String image;

  private String ssn;

  private String weibo;

  private String wechatNo;

  private String address;

  private Date birthday;

  private String gender;

  private String position;

  private String department;

  private String title;

  private Long aid;

  private Date lastPositionModifyDate;

  private Date entryDate;

  private Date leaveDate;

  private String remark;

  public Long getId() {
    return id;
  }

  public UserDetailInfoParam setId(Long id) {
    this.id = id;
    return this;
  }

  public Long getUserId() {
    return userId;
  }

  public UserDetailInfoParam setUserId(Long userId) {
    this.userId = userId;
    return this;
  }

  public String getFirstName() {
    return firstName;
  }

  public UserDetailInfoParam setFirstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  public String getLastName() {
    return lastName;
  }

  public UserDetailInfoParam setLastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  public String getDisplayName() {
    return displayName;
  }

  public UserDetailInfoParam setDisplayName(String displayName) {
    this.displayName = displayName;
    return this;
  }

  public String getNickName() {
    return nickName;
  }

  public UserDetailInfoParam setNickName(String nickName) {
    this.nickName = nickName;
    return this;
  }

  public String getIdentityNo() {
    return identityNo;
  }

  public UserDetailInfoParam setIdentityNo(String identityNo) {
    this.identityNo = identityNo;
    return this;
  }

  public String getMotto() {
    return motto;
  }

  public UserDetailInfoParam setMotto(String motto) {
    this.motto = motto;
    return this;
  }

  public String getImage() {
    return image;
  }

  public UserDetailInfoParam setImage(String image) {
    this.image = image;
    return this;
  }

  public String getSsn() {
    return ssn;
  }

  public UserDetailInfoParam setSsn(String ssn) {
    this.ssn = ssn;
    return this;
  }

  public String getWeibo() {
    return weibo;
  }

  public UserDetailInfoParam setWeibo(String weibo) {
    this.weibo = weibo;
    return this;
  }

  public String getWechatNo() {
    return wechatNo;
  }

  public UserDetailInfoParam setWechatNo(String wechatNo) {
    this.wechatNo = wechatNo;
    return this;
  }

  public String getAddress() {
    return address;
  }

  public UserDetailInfoParam setAddress(String address) {
    this.address = address;
    return this;
  }

  public Date getBirthday() {
    return birthday;
  }

  public UserDetailInfoParam setBirthday(Date birthday) {
    this.birthday = birthday;
    return this;
  }

  public String getGender() {
    return gender;
  }

  public UserDetailInfoParam setGender(String gender) {
    this.gender = gender;
    return this;
  }

  public String getPosition() {
    return position;
  }

  public UserDetailInfoParam setPosition(String position) {
    this.position = position;
    return this;
  }

  public String getDepartment() {
    return department;
  }

  public UserDetailInfoParam setDepartment(String department) {
    this.department = department;
    return this;
  }

  public String getTitle() {
    return title;
  }

  public UserDetailInfoParam setTitle(String title) {
    this.title = title;
    return this;
  }

  public Long getAid() {
    return aid;
  }

  public UserDetailInfoParam setAid(Long aid) {
    this.aid = aid;
    return this;
  }

  public Date getLastPositionModifyDate() {
    return lastPositionModifyDate;
  }

  public UserDetailInfoParam setLastPositionModifyDate(Date lastPositionModifyDate) {
    this.lastPositionModifyDate = lastPositionModifyDate;
    return this;
  }

  public Date getEntryDate() {
    return entryDate;
  }

  public UserDetailInfoParam setEntryDate(Date entryDate) {
    this.entryDate = entryDate;
    return this;
  }

  public Date getLeaveDate() {
    return leaveDate;
  }

  public UserDetailInfoParam setLeaveDate(Date leaveDate) {
    this.leaveDate = leaveDate;
    return this;
  }

  public String getRemark() {
    return remark;
  }

  public UserDetailInfoParam setRemark(String remark) {
    this.remark = remark;
    return this;
  }
}
