package com.dianrong.common.uniauth.common.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

import lombok.ToString;

/**
 * 区别于UserDetailDto,该DTO是用户的一组属性的集合.
 */
@ToString
@ApiModel("用户详细属性信息")
public class UserDetailInfoDto extends TenancyBaseDto {

  private static final long serialVersionUID = 7768297018223437914L;

  @ApiModelProperty("主键id")
  private Long id;

  @ApiModelProperty("关联的UserId")
  private Long userId;

  @ApiModelProperty("名")
  private String firstName;

  @ApiModelProperty("姓")
  private String lastName;

  @ApiModelProperty("显示名称")
  private String displayName;

  @ApiModelProperty("别名")
  private String nickName;

  @ApiModelProperty("身份证号")
  private String identityNo;

  @ApiModelProperty("座右铭")
  private String motto;

  @ApiModelProperty("头像存储的地址")
  private String image;

  @ApiModelProperty("ssn号")
  private String ssn;

  @ApiModelProperty("微博号")
  private String weibo;

  @ApiModelProperty("微信号")
  private String wechatNo;

  @ApiModelProperty("地址")
  private String address;

  @ApiModelProperty("出生日期")
  private Date birthday;

  @ApiModelProperty("性别")
  private String gender;

  @ApiModelProperty("职位")
  private String position;

  @ApiModelProperty("所在部门")
  private String department;

  @ApiModelProperty("头衔")
  private String title;

  @ApiModelProperty("关联Actor表中的Id")
  private Long aid;

  @ApiModelProperty("最近一次职位调整时间")
  private Date lastPositionModifyDate;

  @ApiModelProperty("入职日期")
  private Date entryDate;

  @ApiModelProperty("离职日期")
  private Date leaveDate;

  @ApiModelProperty("备注")
  private String remark;

  @ApiModelProperty("记录创建日期")
  private Date createDate;

  @ApiModelProperty("记录最新更新时间")
  private Date lastUpdate;

  public Long getId() {
    return id;
  }

  public UserDetailInfoDto setId(Long id) {
    this.id = id;
    return this;
  }

  public Long getUserId() {
    return userId;
  }

  public UserDetailInfoDto setUserId(Long userId) {
    this.userId = userId;
    return this;
  }

  public String getFirstName() {
    return firstName;
  }

  public UserDetailInfoDto setFirstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  public String getLastName() {
    return lastName;
  }

  public UserDetailInfoDto setLastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  public String getDisplayName() {
    return displayName;
  }

  public UserDetailInfoDto setDisplayName(String displayName) {
    this.displayName = displayName;
    return this;
  }

  public String getNickName() {
    return nickName;
  }

  public UserDetailInfoDto setNickName(String nickName) {
    this.nickName = nickName;
    return this;
  }

  public String getIdentityNo() {
    return identityNo;
  }

  public UserDetailInfoDto setIdentityNo(String identityNo) {
    this.identityNo = identityNo;
    return this;
  }

  public String getMotto() {
    return motto;
  }

  public UserDetailInfoDto setMotto(String motto) {
    this.motto = motto;
    return this;
  }

  public String getImage() {
    return image;
  }

  public UserDetailInfoDto setImage(String image) {
    this.image = image;
    return this;
  }

  public String getSsn() {
    return ssn;
  }

  public UserDetailInfoDto setSsn(String ssn) {
    this.ssn = ssn;
    return this;
  }

  public String getWeibo() {
    return weibo;
  }

  public UserDetailInfoDto setWeibo(String weibo) {
    this.weibo = weibo;
    return this;
  }

  public String getWechatNo() {
    return wechatNo;
  }

  public UserDetailInfoDto setWechatNo(String wechatNo) {
    this.wechatNo = wechatNo;
    return this;
  }

  public String getAddress() {
    return address;
  }

  public UserDetailInfoDto setAddress(String address) {
    this.address = address;
    return this;
  }

  public Date getBirthday() {
    return birthday;
  }

  public UserDetailInfoDto setBirthday(Date birthday) {
    this.birthday = birthday;
    return this;
  }

  public String getGender() {
    return gender;
  }

  public UserDetailInfoDto setGender(String gender) {
    this.gender = gender;
    return this;
  }

  public String getPosition() {
    return position;
  }

  public UserDetailInfoDto setPosition(String position) {
    this.position = position;
    return this;
  }

  public String getDepartment() {
    return department;
  }

  public UserDetailInfoDto setDepartment(String department) {
    this.department = department;
    return this;
  }

  public String getTitle() {
    return title;
  }

  public UserDetailInfoDto setTitle(String title) {
    this.title = title;
    return this;
  }

  public Long getAid() {
    return aid;
  }

  public UserDetailInfoDto setAid(Long aid) {
    this.aid = aid;
    return this;
  }

  public Date getLastPositionModifyDate() {
    return lastPositionModifyDate;
  }

  public UserDetailInfoDto setLastPositionModifyDate(Date lastPositionModifyDate) {
    this.lastPositionModifyDate = lastPositionModifyDate;
    return this;
  }

  public Date getEntryDate() {
    return entryDate;
  }

  public UserDetailInfoDto setEntryDate(Date entryDate) {
    this.entryDate = entryDate;
    return this;
  }

  public Date getLeaveDate() {
    return leaveDate;
  }

  public UserDetailInfoDto setLeaveDate(Date leaveDate) {
    this.leaveDate = leaveDate;
    return this;
  }

  public String getRemark() {
    return remark;
  }

  public UserDetailInfoDto setRemark(String remark) {
    this.remark = remark;
    return this;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public UserDetailInfoDto setCreateDate(Date createDate) {
    this.createDate = createDate;
    return this;
  }

  public Date getLastUpdate() {
    return lastUpdate;
  }

  public UserDetailInfoDto setLastUpdate(Date lastUpdate) {
    this.lastUpdate = lastUpdate;
    return this;
  }
}
