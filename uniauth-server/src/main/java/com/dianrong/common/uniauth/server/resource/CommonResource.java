package com.dianrong.common.uniauth.server.resource;

import com.dianrong.common.uniauth.common.bean.Response;

import com.dianrong.common.uniauth.common.bean.dto.RoleCodeDto;
import com.dianrong.common.uniauth.common.interfaces.ICommonResource;
import com.dianrong.common.uniauth.server.service.GroupService;
import com.dianrong.common.uniauth.server.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Arc on 15/1/16.
 */
@RestController
public class CommonResource implements ICommonResource {

    @Autowired
    private RoleService roleService;
    @Autowired
    private GroupService groupService;

    @Override
    public Response<List<RoleCodeDto>> getAllRoleCodes() {
        List<RoleCodeDto> roleCodeDtos = roleService.getAllRoleCodes();
        return Response.success(roleCodeDtos);
    }

}
