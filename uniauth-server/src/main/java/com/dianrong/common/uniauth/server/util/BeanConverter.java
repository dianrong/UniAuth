package com.dianrong.common.uniauth.server.util;

import com.dianrong.common.uniauth.common.bean.dto.GroupCodeDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleCodeDto;
import com.dianrong.common.uniauth.server.data.entity.GroupCode;
import com.dianrong.common.uniauth.server.data.entity.RoleCode;

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

    public static GroupCodeDto convert(GroupCode groupCode) {
        if(groupCode == null) {
            return null;
        } else {
            return new GroupCodeDto().setId(groupCode.getId()).setCode(groupCode.getCode()).setDescription(groupCode.getDescription());
        }
    }
}
