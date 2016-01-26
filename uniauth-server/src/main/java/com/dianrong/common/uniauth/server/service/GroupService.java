package com.dianrong.common.uniauth.server.service;

import com.dianrong.common.uniauth.common.bean.InfoName;
import com.dianrong.common.uniauth.common.bean.dto.GroupDto;
import com.dianrong.common.uniauth.common.bean.request.GroupParam;
import com.dianrong.common.uniauth.server.data.entity.*;
import com.dianrong.common.uniauth.server.data.mapper.GrpMapper;
import com.dianrong.common.uniauth.server.data.mapper.GrpPathMapper;
import com.dianrong.common.uniauth.server.data.mapper.GrpRoleMapper;
import com.dianrong.common.uniauth.server.data.mapper.UserGroupMapper;
import com.dianrong.common.uniauth.server.exp.AppException;
import com.dianrong.common.uniauth.server.util.BeanConverter;
import com.dianrong.common.uniauth.server.util.UniBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by Arc on 14/1/16.
 */
@Service
public class GroupService {
    @Autowired
    private GrpMapper grpMapper;
    @Autowired
    private GrpPathMapper grpPathMapper;
    @Autowired
    private UserGroupMapper userGroupMapper;
    @Autowired
    private GrpRoleMapper grpRoleMapper;


    @Transactional
    public GroupDto createDescendantGroup(GroupParam groupParam) {
        Grp grp = BeanConverter.convert(groupParam);
        Date now = new Date();
        grp.setStatus((byte)0);
        grp.setCreateDate(now);
        grp.setLastUpdate(now);
        grpMapper.insert(grp);
        GrpPath grpPath = new GrpPath();
        grpPath.setDeepth((byte)0);
        grpPath.setDescendant(grp.getId());
        grpPath.setAncestor(groupParam.getTargetGroupId());
        grpPathMapper.insertNewNode(grpPath);
        return BeanConverter.convert(grp);
    }

    @Transactional
    public Boolean deleteGroup(Integer groupId) {
        GrpPathExample grpPathExample = new GrpPathExample();
        grpPathExample.createCriteria().andDescendantEqualTo(groupId);
        int desOfDes = grpPathMapper.countByExample(grpPathExample);
        if(desOfDes > 0) {
            throw new AppException(InfoName.VALIDATE_FAIL, UniBundle.getMsg("group.parameter.delgroup"));
        }
        // cascading delete the users in group and the roles on group.
        UserGroupExample userGroupExample = new UserGroupExample();
        userGroupExample.createCriteria().andGrpIdEqualTo(groupId);
        userGroupMapper.deleteByExample(userGroupExample);
        GrpRoleExample grpRoleExample = new GrpRoleExample();
        grpRoleExample.createCriteria().andGrpIdEqualTo(groupId);
        grpRoleMapper.deleteByExample(grpRoleExample);
        grpMapper.deleteByPrimaryKey(groupId);
        grpPathMapper.deleteByExample(grpPathExample);
        return Boolean.TRUE;
    }

    @Transactional
    public GroupDto updateGroup(GroupParam groupParam) {
        Grp grp = BeanConverter.convert(groupParam);
        grp.setLastUpdate(new Date());
        grpMapper.updateByPrimaryKeySelective(grp);
        return BeanConverter.convert(grp);
    }
}
