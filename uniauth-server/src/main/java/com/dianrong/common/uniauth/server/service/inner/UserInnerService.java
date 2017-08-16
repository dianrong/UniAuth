package com.dianrong.common.uniauth.server.service.inner;

import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.util.ObjectUtil;
import com.dianrong.common.uniauth.server.data.entity.User;
import com.dianrong.common.uniauth.server.data.entity.UserExample;
import com.dianrong.common.uniauth.server.data.entity.UserGrpExample;
import com.dianrong.common.uniauth.server.data.entity.UserGrpKey;
import com.dianrong.common.uniauth.server.data.mapper.UserGrpMapper;
import com.dianrong.common.uniauth.server.data.mapper.UserMapper;
import com.dianrong.common.uniauth.server.service.common.TenancyBasedService;
import com.dianrong.common.uniauth.server.util.BeanConverter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户信息操作的内部服务.
 */
@Service
public class UserInnerService extends TenancyBasedService {

  @Autowired
  private UserMapper userMapper;

  @Autowired
  private UserGrpMapper userGrpMapper;

  /**
   * 获取组与用户的映射信息.
   * 
   * @param grpIds 查询组的范围.
   * @param includeDisableUser 是否包含被禁用的用户,默认为false.
   * @param type 组与用户的关联关系. 1: owner关系, 0:普通关联关系.
   */
  public Map<Integer, List<UserDto>> queryGrpUser(List<Integer> grpIds, Boolean includeDisableUser, Byte type) {
    Map<Integer, List<UserDto>> grpUserMap = Maps.newHashMap();
    if (ObjectUtil.collectionIsEmptyOrNull(grpIds)) {
      return grpUserMap;
    }

    UserGrpExample userGrpExample = new UserGrpExample();
    UserGrpExample.Criteria criteria = userGrpExample.createCriteria();
    criteria.andGrpIdIn(grpIds);
    if (type != null) {
      criteria.andTypeEqualTo(type);
    }
    List<UserGrpKey> userGrpList = userGrpMapper.selectByExample(userGrpExample);
    if (ObjectUtil.collectionIsEmptyOrNull(userGrpList)) {
      return grpUserMap;
    }
    List<Long> userIds = Lists.newArrayList();
    for (UserGrpKey ug : userGrpList) {
      userIds.add(ug.getUserId());
    }

    UserExample userExample = new UserExample();
    UserExample.Criteria userCriteria = userExample.createCriteria();
    userCriteria.andIdIn(userIds).andTenancyIdEqualTo(tenancyService.getTenancyIdWithCheck());
    if (includeDisableUser == null || !includeDisableUser) {
      userCriteria.andStatusEqualTo(AppConstants.STATUS_ENABLED);
    }
    List<User> users = userMapper.selectByExample(userExample);
    if (ObjectUtil.collectionIsEmptyOrNull(users)) {
      return grpUserMap;
    }
    Map<Long, UserDto> userMap = Maps.newHashMap();
    for (User u : users) {
      userMap.put(u.getId(), BeanConverter.convert(u));
    }
    for (UserGrpKey ug : userGrpList) {
      List<UserDto> userDtos = grpUserMap.get(ug.getGrpId());
      if (userDtos == null) {
        userDtos = Lists.newArrayList();
        grpUserMap.put(ug.getGrpId(), userDtos);
      }
      UserDto userDto = userMap.get(ug.getUserId());
      if (userDto != null) {
        userDtos.add(userDto);
      }
    }

    return grpUserMap;
  }
}
