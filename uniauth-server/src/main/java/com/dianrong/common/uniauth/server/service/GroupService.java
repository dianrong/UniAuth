package com.dianrong.common.uniauth.server.service;

import com.dianrong.common.uniauth.common.bean.dto.GroupCodeDto;
import com.dianrong.common.uniauth.server.data.entity.GroupCode;
import com.dianrong.common.uniauth.server.data.entity.GroupCodeExample;
import com.dianrong.common.uniauth.server.data.mapper.GroupCodeMapper;
import com.dianrong.common.uniauth.server.util.BeanConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arc on 14/1/16.
 */
@Service
public class GroupService {
    @Autowired
    private GroupCodeMapper groupCodeMapper;

    public List<GroupCodeDto> getAllGroupCode() {
        List<GroupCode> groupCodes = groupCodeMapper.selectByExample(new GroupCodeExample());
        if(!CollectionUtils.isEmpty(groupCodes)) {
            List<GroupCodeDto> groupCodeDtos = new ArrayList<>();
            for(GroupCode groupCode : groupCodes) {
                groupCodeDtos.add(BeanConverter.convert(groupCode));
            }
            return groupCodeDtos;
        }
        return null;
    }

}
