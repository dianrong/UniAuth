package com.dianrong.common.uniauth.server.service.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.dianrong.common.uniauth.server.data.entity.PermType;
import com.dianrong.common.uniauth.server.data.entity.PermTypeExample;
import com.dianrong.common.uniauth.server.data.entity.RoleCode;
import com.dianrong.common.uniauth.server.data.entity.RoleCodeExample;
import com.dianrong.common.uniauth.server.data.mapper.PermTypeMapper;
import com.dianrong.common.uniauth.server.data.mapper.RoleCodeMapper;

@Component
public class CommonCache {
    
    @Autowired
    private PermTypeMapper permTypeMapper;
    @Autowired
    private RoleCodeMapper roleCodeMapper;

    @Cacheable(value = "commonService", key = "'common:permType'")
    public Map<String, PermType> getPermTypeMap() {
        Map<String, PermType> permTypeMap = new HashMap<>();
        PermTypeExample example = new PermTypeExample();
        List<PermType> permTypeList = permTypeMapper.selectByExample(example);
        if (permTypeList != null && !permTypeList.isEmpty()) {
            for (PermType permType : permTypeList) {
                permTypeMap.put(String.valueOf(permType.getId()), permType);
            }
        }
        return permTypeMap;
    }

    @Cacheable(value = "commonService", key = "'common:roleCode'")
    public Map<String, RoleCode> getRoleCodeMap() {
        Map<String, RoleCode> roleCodeMap = new HashMap<>();
        RoleCodeExample example = new RoleCodeExample();
        List<RoleCode> roleCodeList = roleCodeMapper.selectByExample(example);
        if (roleCodeList != null && !roleCodeList.isEmpty()) {
            for (RoleCode rc : roleCodeList) {
                roleCodeMap.put(String.valueOf(rc.getId()), rc);
            }
        }
        return roleCodeMap;
    }
}
