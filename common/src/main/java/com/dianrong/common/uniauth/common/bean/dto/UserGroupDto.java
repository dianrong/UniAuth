package com.dianrong.common.uniauth.common.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import java.util.List;
import lombok.ToString;

@ToString
@ApiModel("用户组信息")
public class UserGroupDto extends TenancyBaseDto {

  private static final long serialVersionUID = 892184382591361189L;

  @ApiModelProperty("用户id")
  private Long userId;

  @ApiModelProperty("组列表信息")
  private List<GroupDto> groupDtoList;

  public Long getUserId() {
    return userId;
  }

  public UserGroupDto setUserId(Long userId) {
    this.userId = userId;
    return this;
  }

  public List<GroupDto> getGroupDtoList() {
    return groupDtoList;
  }

  public UserGroupDto setGroupDtoList(
      List<GroupDto> groupDtoList) {
    this.groupDtoList = groupDtoList;
    return this;
  }
}
