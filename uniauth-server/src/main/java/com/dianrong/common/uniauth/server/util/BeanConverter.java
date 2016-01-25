package com.dianrong.common.uniauth.server.util;

import com.dianrong.common.uniauth.common.bean.dto.GroupDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleCodeDto;
import com.dianrong.common.uniauth.common.bean.request.GroupParam;
import com.dianrong.common.uniauth.server.data.entity.Grp;
import com.dianrong.common.uniauth.server.data.entity.RoleCode;

import java.security.acl.Group;

/**
 * Created by Arc on 15/1/16.
 */
public class BeanConverter {
    public static RoleCodeDto convert(RoleCode roleCode) {
        if(roleCode == null) {
            return null;
        } else {
            return new RoleCodeDto().setCode(roleCode.getCode()).setId(roleCode.getId()).setDescription(roleCode.getDescription());
        }
    }

    public static Grp convert(GroupParam groupParam) {
        if(groupParam == null) {
            return null;
        } else {
            Grp grp = new Grp();
            grp.setDescription(groupParam.getDescription());
            grp.setCode(groupParam.getCode());
            grp.setId(groupParam.getId());
            grp.setName(groupParam.getName());
            grp.setStatus(groupParam.getStatus());
            return grp;
        }
    }

    public static GroupDto convert(Grp grp) {
        if(grp == null) {
            return null;
        } else {
            GroupDto groupDto = new GroupDto();
            groupDto.setCode(grp.getCode());
            groupDto.setId(grp.getId());
            groupDto.setName(grp.getName());
            groupDto.setDescription(grp.getDescription());
            groupDto.setStatus(grp.getStatus());
            groupDto.setCreateDate(grp.getCreateDate());
            groupDto.setLastUpdate(grp.getLastUpdate());
            return groupDto;
        }
    }
}
