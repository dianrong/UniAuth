package com.dianrong.common.uniauth.server.service.inner;

import com.dianrong.common.uniauth.common.bean.InfoName;
import com.dianrong.common.uniauth.common.bean.dto.SimpleProfileDefinitionDto;
import com.dianrong.common.uniauth.common.util.ObjectUtil;
import com.dianrong.common.uniauth.server.data.entity.ProfileDefinitionPath;
import com.dianrong.common.uniauth.server.data.entity.ProfileDefinitionPathExample;
import com.dianrong.common.uniauth.server.data.mapper.ProfileDefinitionPathMapper;
import com.dianrong.common.uniauth.server.datafilter.DataFilter;
import com.dianrong.common.uniauth.server.datafilter.FieldType;
import com.dianrong.common.uniauth.server.datafilter.FilterType;
import com.dianrong.common.uniauth.server.exp.AppException;
import com.dianrong.common.uniauth.server.service.common.TenancyBasedService;
import com.dianrong.common.uniauth.server.service.support.ProcessListQuery;
import com.dianrong.common.uniauth.server.service.support.ProfileSupport;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.dianrong.common.uniauth.server.util.UniBundle;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 内部Service,不对外提供服务的.主要用于服务ProfileService.
 */
@Slf4j
@Service
public class ProfileDefinitionPathInnerService extends TenancyBasedService {

  @Autowired
  private ProfileDefinitionPathMapper profileDefinitionPathMapper;

  /**
   * 进行角色数据过滤的filter.
   */
  @Resource(name = "profileDefinitionDataFilter")
  private DataFilter profileDefinitionDataFilter;

  /**
   * 关联Profile和子Profiles,用于新增Profile的时候调用.
   * 
   * @param ancestorId 新增Profile的id
   * @param descendantIds 子ProfileId的集合,要保证所有子ProfileId都是存在的.
   */
  @Transactional
  public void addNewProfilePath(Long ancestorId, Set<Long> descendantIds) {
    CheckEmpty.checkEmpty(ancestorId, "ancestor definition id");
    if (ObjectUtil.collectionIsEmptyOrNull(descendantIds)) {
      log.debug(
          "relateProfileAndSubProfile, but descendantIds is empty, so just ignore and return!");
      return;
    }
    List<ProfileDefinitionPath> insertList = Lists.newArrayList();
    for (Long descendantId : descendantIds) {
      ProfileDefinitionPath pdp = new ProfileDefinitionPath();
      pdp.setAncestor(ancestorId);
      pdp.setDescendant(descendantId);
      insertList.add(pdp);
    }
    // 批量插入数据.
    profileDefinitionPathMapper.batchInsert(insertList);
  }

  /**
   * 更新Profile与子Profile之间的关联关系.必须确保parentProfileId和subProfileIds没有循环的关联关系.
   * 
   * @param parentProfileId 指定的ProfileId,作为关系中的父Profile,不能为空.
   * @param subProfileIds 关联关系中的子ProfileId集合.
   */
  @Transactional
  public void updateSubProfilePath(Long parentProfileId, Set<Long> subProfileIds) {
    CheckEmpty.checkEmpty(parentProfileId, "profile definition id");
    // 子profile_definition必须存在
    if (!ObjectUtil.collectionIsEmptyOrNull(subProfileIds)) {
      for (Long pdid : subProfileIds) {
        profileDefinitionDataFilter.addFieldCheck(FilterType.NO_DATA, FieldType.FIELD_TYPE_ID,
            pdid);
      }
      // 新设置的子Profile不能与当前Profile形成循环. Profile循环check
      if (isSubProfile(parentProfileId, subProfileIds)) {
        throw new AppException(InfoName.VALIDATE_FAIL, UniBundle
            .getMsg("profile.check.circle.profile.relate", parentProfileId, subProfileIds));
      }
    }

    // 删除已经存在的关联关系.
    ProfileDefinitionPathExample delExample = new ProfileDefinitionPathExample();
    ProfileDefinitionPathExample.Criteria criteria = delExample.createCriteria();
    criteria.andAncestorEqualTo(parentProfileId);
    profileDefinitionPathMapper.deleteByExample(delExample);

    // 批量添加关联关系.
    addNewProfilePath(parentProfileId, subProfileIds);
  }

  /**
   * 扩展Profile的子Profile.
   * 
   * @param parentProfileId 指定的ProfileId,作为关系中的父Profile,不能为空.
   * @param subProfileIds 扩展增加的子ProfileId.
   */
  @Transactional
  public void extendSubProfilePath(Long parentProfileId, Set<Long> subProfileIds) {
    CheckEmpty.checkEmpty(parentProfileId, "profile definition id");
    // 子profile_definition必须存在
    if (ObjectUtil.collectionIsEmptyOrNull(subProfileIds)) {
      log.debug("extendSubProfilePath, but subProfileIds is empty, so just ignore and return!");
      return;
    }

    // 已经存在的子ProfileId需要过滤掉.
    ProfileDefinitionPathExample selectExample = new ProfileDefinitionPathExample();
    ProfileDefinitionPathExample.Criteria criteria = selectExample.createCriteria();
    criteria.andAncestorEqualTo(parentProfileId);
    List<ProfileDefinitionPath> existPdpList =
        profileDefinitionPathMapper.selectByExample(selectExample);
    Set<Long> processProfileIdSet;
    if (ObjectUtil.collectionIsEmptyOrNull(existPdpList)) {
      processProfileIdSet = subProfileIds;
    } else {
      Set<Long> existId = Sets.newHashSet();
      for (ProfileDefinitionPath tpdp : existPdpList) {
        existId.add(tpdp.getDescendant());
      }
      // 添加的扩展属性列表
      processProfileIdSet = new HashSet<>((new ProcessListQuery<Long, Long>() {
        @Override
        public Long getId(Long o) {
          return o;
        }
      }).getProcessList(new ArrayList<Long>(existId), new ArrayList<Long>(subProfileIds), true));
    }

    // 子profile_definition必须存在
    if (!ObjectUtil.collectionIsEmptyOrNull(processProfileIdSet)) {
      for (Long pdid : processProfileIdSet) {
        profileDefinitionDataFilter.addFieldCheck(FilterType.NO_DATA, FieldType.FIELD_TYPE_ID,
            pdid);
      }
      // 新设置的子Profile不能与当前Profile形成循环. Profile循环check
      if (isSubProfile(parentProfileId, processProfileIdSet)) {
        throw new AppException(InfoName.VALIDATE_FAIL, UniBundle
            .getMsg("profile.check.circle.profile.relate", parentProfileId, processProfileIdSet));
      }
    }

    // 批量添加关联关系.
    addNewProfilePath(parentProfileId, processProfileIdSet);
  }

  /**
   * 判断id是否是descendantProfileIds的子Profile.
   */
  public boolean isSubProfile(Long profileId, Set<Long> subProfileIds) {
    CheckEmpty.checkEmpty(profileId, "profile definition id");
    if (ObjectUtil.collectionIsEmptyOrNull(subProfileIds)) {
      return false;
    }
    Set<Long> profileIdSet = Sets.newHashSet();
    for (Long descendantProfileId : subProfileIds) {
      SimpleProfileDefinitionDto spdp = getProfilePathTree(descendantProfileId);
      ProfileSupport.collectSubProfileId(spdp, profileIdSet);
      if (profileIdSet.contains(profileId)) {
        return true;
      }
    }
    return false;
  }

  /**
   * 根据Profile Id获取其所在的树形结构.
   */
  public SimpleProfileDefinitionDto getProfilePathTree(Long profileId) {
    CheckEmpty.checkEmpty(profileId, "profile definition id");
    SimpleProfileDefinitionDto rootPdp = new SimpleProfileDefinitionDto();
    rootPdp.setId(profileId);
    getSubProfileInfo(rootPdp);
    return rootPdp;
  }

  private void getSubProfileInfo(SimpleProfileDefinitionDto parentPdp) {
    // 获取指定profileId对应的profile关联树结构
    ProfileDefinitionPathExample example = new ProfileDefinitionPathExample();
    ProfileDefinitionPathExample.Criteria criteria = example.createCriteria();
    criteria.andAncestorEqualTo(parentPdp.getId());
    List<ProfileDefinitionPath> pdpList = profileDefinitionPathMapper.selectByExample(example);
    if (ObjectUtil.collectionIsEmptyOrNull(pdpList)) {
      return;
    }
    Set<SimpleProfileDefinitionDto> subPdpList = Sets.newHashSet();
    for (ProfileDefinitionPath pdp : pdpList) {
      SimpleProfileDefinitionDto spdp = new SimpleProfileDefinitionDto();
      spdp.setId(pdp.getDescendant());
      getSubProfileInfo(spdp);
      subPdpList.add(spdp);
    }
    parentPdp.setSubProfiles(subPdpList);
  }
}
