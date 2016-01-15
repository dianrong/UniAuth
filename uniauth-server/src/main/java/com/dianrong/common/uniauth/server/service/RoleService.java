package com.dianrong.common.uniauth.server.service;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.RoleCodeDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleDto;
import com.dianrong.common.uniauth.server.data.entity.RoleCode;
import com.dianrong.common.uniauth.server.data.entity.RoleCodeExample;
import com.dianrong.common.uniauth.server.data.mapper.RoleCodeMapper;
import com.dianrong.common.uniauth.server.util.BeanConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arc on 15/1/16.
 */
@Service
public class RoleService {

    @Autowired
    private RoleCodeMapper roleCodeMapper;

    public List<RoleCodeDto> getAllRoleCodes() {
        List<RoleCode> roleCodes = roleCodeMapper.selectByExample(new RoleCodeExample());
        if(!CollectionUtils.isEmpty(roleCodes)) {
            List<RoleCodeDto> roleCodeDtos = new ArrayList<>();
            for (RoleCode roleCode : roleCodes) {
                roleCodeDtos.add(BeanConverter.convert(roleCode));
            }
            return roleCodeDtos;
        }
        return null;
    }

}
