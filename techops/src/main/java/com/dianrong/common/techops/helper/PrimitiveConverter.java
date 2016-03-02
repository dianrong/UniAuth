package com.dianrong.common.techops.helper;

import com.dianrong.common.uniauth.common.bean.dto.RoleDto;

/**
 * Created by Arc on 2/3/2016.
 */
public class PrimitiveConverter {

    public static RoleDto convert(RoleDto roleDto) {
        if(roleDto == null) {
            return null;
        } else {
            RoleDto returnRoleDto = new RoleDto();
            returnRoleDto.setDescription(roleDto.getDescription()).
                    setId(roleDto.getId()).
                    setStatus(roleDto.getStatus()).
                    setName(roleDto.getName()).
                    setRoleCode(roleDto.getRoleCode());
            return returnRoleDto;
        }
    }

}
