package com.dianrong.common.uniauth.server.resource;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.Role;
import com.dianrong.common.uniauth.common.interfaces.IUserResource;
import com.dianrong.common.uniauth.server.data.entity.RoleCode;
import com.dianrong.common.uniauth.server.data.entity.RoleCodeExample;
import com.dianrong.common.uniauth.server.data.mapper.RoleCodeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arc on 14/1/16.
 */
@Service
public class UserResource implements IUserResource {
    @Autowired
    private RoleCodeMapper roleCodeMapper;

    @Override
    public Response<List<Role>> getAllRoles() {
        List<RoleCode> roleCodes = roleCodeMapper.selectByExample(new RoleCodeExample());
        List<Role> roles = new ArrayList<>();
        for(RoleCode roleCode : roleCodes) {
            Role role = new Role();
            role.setCode(roleCode.getCode()).setId(roleCode.getId());
            roles.add(role);
        }
        return Response.success(roles);
    }
}
