package com.dianrong.common.uniauth.common.bean.dto;

import com.dianrong.common.uniauth.common.util.Assert;
import io.swagger.annotations.ApiModel;
import lombok.ToString;

@ToString @ApiModel("组织关系信息") public class OrganizationDto extends GroupDto {
  private static final long serialVersionUID = 892184382591361179L;

  public OrganizationDto() {
  }

  ;

  /**
   * 根据GroupDto生成一个OrganizationDto.
   *
   * @param groupDto 不能为空.
   */
  public OrganizationDto(GroupDto groupDto) {
    Assert.notNull(groupDto);
    this.setCode(groupDto.getCode()).setCreateDate(groupDto.getCreateDate())
        .setDescription(groupDto.getDescription()).setGroups(groupDto.getGroups())
        .setGrpExtendVals(groupDto.getGrpExtendVals()).setId(groupDto.getId())
        .setIsRootGrp(groupDto.getIsRootGrp()).setLastUpdate(groupDto.getLastUpdate())
        .setName(groupDto.getName()).setOwnerMarkup(groupDto.getOwnerMarkup())
        .setParentId(groupDto.getParentId()).setRoleChecked(groupDto.getRoleChecked())
        .setStatus(groupDto.getStatus()).setRoles(groupDto.getRoles())
        .setTagChecked(groupDto.getTagChecked()).setTags(groupDto.getTags())
        .setUsers(groupDto.getUsers());
  }
}
