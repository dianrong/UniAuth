package com.dianrong.common.uniauth.common.bean.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * IPA权限model.
 *
 * @author wanglin
 */
@ToString
@Getter
@Setter
public class IPAPermissionDto extends TenancyBaseDto {

  private static final long serialVersionUID = 5798097486278500153L;
  /**
   * 包含一系列的角色.
   */
  private List<RoleDto> roleList;

}
