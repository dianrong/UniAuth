package com.dianrong.common.uniauth.server.service;

import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dianrong.common.uniauth.common.util.StringUtil;
import com.dianrong.common.uniauth.server.data.entity.PermType;
import com.dianrong.common.uniauth.server.data.entity.RoleCode;
import com.dianrong.common.uniauth.server.service.cache.CommonCache;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CommonService {

    @Autowired
    private CommonCache commonCache;

    public Map<Integer, PermType> getPermTypeMap() {
        Map<String, PermType> permMap = commonCache.getPermTypeMap();
        Map<Integer, PermType> permTypeMap = Maps.newHashMap();
        for(Entry<String, PermType> entry : permMap.entrySet()) {
            String key = entry.getKey();
            Integer integerKey = StringUtil.tryToTranslateStrToInt(key);
            if (integerKey != null) {
                permTypeMap.put(integerKey, entry.getValue());
            } else {
                log.error("{} is not valid a permission id", key);
            }
        }
        return permTypeMap;
    }

    public Map<Integer, RoleCode> getRoleCodeMap() {
        Map<String, RoleCode> roleMap = commonCache.getRoleCodeMap();
        Map<Integer, RoleCode> roleCodeMap = Maps.newHashMap();
        for(Entry<String, RoleCode> entry : roleMap.entrySet()) {
            String key = entry.getKey();
            Integer integerKey = StringUtil.tryToTranslateStrToInt(key);
            if (integerKey != null) {
                roleCodeMap.put(integerKey, entry.getValue());
            } else {
                log.error("{} is not valid a role id", key);
            }
        }
        return roleCodeMap;
    }
}
