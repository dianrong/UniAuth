package com.dianrong.common.uniauth.server.util;

import com.dianrong.common.uniauth.common.bean.dto.DomainDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleCodeDto;
import com.dianrong.common.uniauth.server.data.entity.Domain;
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


    public static DomainDto convert(Domain domain) {
        if(domain == null) {
            return null;
        } else {
            return new DomainDto().setCode(domain.getCode()).setCreateDate(domain.getCreateDate()).setDescription(domain.getDescription()).setDisplayName(domain.getDisplayName()).setId(domain.getId()).setLastUpdate(domain.getLastUpdate()).setStatus(domain.getStatus());
        }
    }
}
