package com.dianrong.common.uniauth.server.service;

import com.dianrong.common.uniauth.common.bean.InfoName;
import com.dianrong.common.uniauth.common.bean.dto.RoleCodeDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleDto;
import com.dianrong.common.uniauth.common.enm.RoleActionEnum;
import com.dianrong.common.uniauth.server.data.entity.*;
import com.dianrong.common.uniauth.server.data.mapper.DomainMapper;
import com.dianrong.common.uniauth.server.data.mapper.RoleCodeMapper;
import com.dianrong.common.uniauth.server.data.mapper.RoleMapper;
import com.dianrong.common.uniauth.server.exp.AppException;
import com.dianrong.common.uniauth.server.util.*;
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
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private DomainMapper domainMapper;

    public List<RoleCodeDto> getAllRoleCodes() {
        RoleCodeExample example = new RoleCodeExample();
        List<RoleCode> roleCodeList = roleCodeMapper.selectByExample(example);
        List<RoleCodeDto> roleCodeDtoList = new ArrayList<RoleCodeDto>();
        if(roleCodeList != null) {
            for(RoleCode roleCode : roleCodeList) {
                roleCodeDtoList.add(BeanConverter.convert(roleCode));
            }
        }
        return roleCodeDtoList;
    }

    public RoleDto addNewRole(Integer domainId, Integer roleCodeId, String name, String description) {
        CheckEmpty.checkEmpty(domainId, "domainId");
        CheckEmpty.checkEmpty(roleCodeId, "roleCodeId");
        CheckEmpty.checkEmpty(name, "name");

        if(domainMapper.selectByPrimaryKey(domainId) == null) {
            throw new AppException(InfoName.VALIDATE_FAIL, UniBundle.getMsg("common.entity.notfound", domainId, Domain.class.getSimpleName()));
        }

        Role role = new Role();
        role.setDomainId(domainId);
        role.setName(name);
        role.setRoleCodeId(roleCodeId);
        role.setStatus(AppConstants.ZERO_Byte);
        role.setDescription(description);
        roleMapper.insert(role);
        return BeanConverter.convert(role);
    }

    public void updateRole(RoleActionEnum roleActionEnum, Integer roleId, Integer roleCodeId, String name, String description, Byte status) {
        CheckEmpty.checkEmpty(roleActionEnum, "roleActionEnum");
        Role role = roleMapper.selectByPrimaryKey(roleId);
        if(role == null) {
            throw new AppException(InfoName.VALIDATE_FAIL, UniBundle.getMsg("common.entity.notfound", roleId, Role.class.getSimpleName()));
        } else if(role.getStatus().equals(AppConstants.ONE_Byte) && !RoleActionEnum.STATUS_CHANGE.equals(roleActionEnum)) {
            throw new AppException(InfoName.VALIDATE_FAIL, UniBundle.getMsg("common.entity.status.isone", roleId, Role.class.getSimpleName()));
        }
        switch (roleActionEnum) {
            case STATUS_CHANGE :
                ParamCheck.checkStatus(status);
                role.setStatus(status);
                break;
            case UPDATE_INFO:
                CheckEmpty.checkEmpty(roleCodeId, "roleCodeId");
                if(roleCodeMapper.selectByPrimaryKey(roleCodeId) == null) {
                    throw new AppException(InfoName.VALIDATE_FAIL, UniBundle.getMsg("common.entity.notfound", roleCodeId, RoleCode.class.getSimpleName()));
                }
                role.setDescription(description);
                role.setName(name);
                role.setRoleCodeId(roleCodeId);
                break;
        }
        roleMapper.updateByPrimaryKey(role);
    }
}
