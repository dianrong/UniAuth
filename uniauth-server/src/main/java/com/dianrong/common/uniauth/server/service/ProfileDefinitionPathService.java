package com.dianrong.common.uniauth.server.service;

import com.dianrong.common.uniauth.common.bean.dto.ProfileDefinitionPathDto;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.util.ObjectUtil;
import com.dianrong.common.uniauth.server.data.entity.ProfileDefinitionPath;
import com.dianrong.common.uniauth.server.data.entity.ProfileDefinitionPathExample;
import com.dianrong.common.uniauth.server.data.mapper.ProfileDefinitionPathMapper;
import com.dianrong.common.uniauth.server.util.BeanConverter;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class ProfileDefinitionPathService extends TenancyBasedService {

  @Autowired
  private ProfileDefinitionPathMapper profileDefinitionPathMapper;

  /**
   * 关联Profile和子Profiles.
   * 
   * @param ancestorId Profile的id
   * @param descendantIds 子ProfileId的集合,要保证所有子ProfileId都是存在的.
   */
  @Transactional
  public void relateProfileAndSubProfile(Long ancestorId, Set<Long> descendantIds) {
    CheckEmpty.checkEmpty(ancestorId, "ancestor definition id");
    List<ProfileDefinitionPath> insertList = Lists.newArrayList();

    // deepth = 0 的一行数据.
    ProfileDefinitionPath ownPath = new ProfileDefinitionPath();
    ownPath.setAncestor(ancestorId);
    ownPath.setDescendant(ancestorId);
    ownPath.setDeepth(AppConstants.ZERO_BYTE);
    ownPath.setNum(AppConstants.ONE_BYTE);
    insertList.add(ownPath);
    // 获取所有子Profile的关联关系数据
    ProfileDefinitionPathExample example = new ProfileDefinitionPathExample();
    ProfileDefinitionPathExample.Criteria criteria = example.createCriteria();
    criteria.andAncestorIn(new ArrayList<Long>(descendantIds));
    List<ProfileDefinitionPath> subProfilePaths =
        profileDefinitionPathMapper.selectByExample(example);
    insertList.addAll(getInsertList(ancestorId, subProfilePaths));
    // 批量插入数据.
    profileDefinitionPathMapper.batchInsert(insertList);
  }

  /**
   * 计算新增Profile的时候需要插入的Profile path记录.
   */
  private List<ProfileDefinitionPath> getInsertList(Long ancestorId,
      List<ProfileDefinitionPath> subProfilePaths) {
    if (subProfilePaths == null) {
      return Collections.emptyList();
    }
    Map<String, ProfileDefinitionPath> maps = Maps.newHashMap();
    for (ProfileDefinitionPath pdp : subProfilePaths) {
      String key = getProfileDefinitionPathKey(ancestorId, pdp);
      ProfileDefinitionPath mpdp = maps.get(key);
      if (mpdp == null) {
        mpdp = new ProfileDefinitionPath();
        mpdp.setAncestor(ancestorId);
        mpdp.setDescendant(pdp.getDescendant());
        mpdp.setDeepth((byte) (pdp.getDeepth() + AppConstants.ONE_BYTE_PRIMITIVE));
        mpdp.setNum(pdp.getNum());
        maps.put(key, mpdp);
      } else {
        // 累加num
        mpdp.setNum((byte) (pdp.getNum() + mpdp.getNum()));
      }
    }
    return new ArrayList<ProfileDefinitionPath>(maps.values());
  }

  /**
   * 获取唯一标识ProfileDefinitionPath的key.ancestor-descendant-deepth.<br>
   * 主要是用于实现getInsertList.
   */
  private String getProfileDefinitionPathKey(Long ancestorId, ProfileDefinitionPath pdp) {
    StringBuilder sb = new StringBuilder();
    sb.append(ancestorId).append("_").append(pdp.getDescendant()).append("_")
        .append(pdp.getDeepth());
    return sb.toString();
  }

  /**
   * Check某一Profile是否是一组Profile的某一个的子Profile.
   * 
   * @param profileIds 一组Profile 的Id集合
   * @param profileId 需要被check的ProfileId. 不能为空.
   * @return true. 1. profileIds 为空, 2. Profile Ids的所有子Profile不包括profileId.
   */
  public boolean isSubProfile(Set<Long> profileIds, Long profileId) {
    CheckEmpty.checkEmpty(profileId, "profile definition id");
    if (ObjectUtil.collectionIsEmptyOrNull(profileIds)) {
      return false;
    }
    List<Long> subProfileIds = profileDefinitionPathMapper.querySubProfileId(profileIds);
    if (ObjectUtil.collectionIsEmptyOrNull(subProfileIds)) {
      return false;
    }
    return subProfileIds.contains(profileId);
  }

  /**
   * 重置Profile与子Profile之间的关联关系.必须确保parentProfileId和subProfileIds没有循环的关联关系.
   * 
   * @param parentProfileId 指定的ProfileId,作为关系中的父Profile,不能为空.
   * @param subProfileIds 关联关系中的子ProfileId集合.保证subProfileIds与parentProfileId没有循环的关联关系.
   */
  @Transactional
  public void resetProfileAndSubProfileRelation(Long parentProfileId, Set<Long> subProfileIds) {
    CheckEmpty.checkEmpty(parentProfileId, "profile definition id");
    // 查询目前的关联关系信息.
    // step1. 查询parentProfileId对应的结构关系信息.
    ProfileDefinitionPathExample example = new ProfileDefinitionPathExample();
    ProfileDefinitionPathExample.Criteria criteria = example.createCriteria();
    criteria.andAncestorEqualTo(parentProfileId);
    List<ProfileDefinitionPath> rootProfilePathInfo =
        profileDefinitionPathMapper.selectByExample(example);

    // 如果已经存在的关系为空.
    if (ObjectUtil.collectionIsEmptyOrNull(rootProfilePathInfo)) {
      log.debug(
          "resetProfileAndSubProfileRelation by parentProfileId:{}, but find none relation info!",
          parentProfileId);
      if (ObjectUtil.collectionIsEmptyOrNull(subProfileIds)) {
        log.debug(
            "resetProfileAndSubProfileRelation by parentProfileId:{}, find none exist relation info, and the new relation info is empty, so just return!",
            parentProfileId);
        return;
      }
      log.debug(
          "resetProfileAndSubProfileRelation by parentProfileId:{}, find none exist relation info, and just process it as add a new Profile and create relationship!",
          parentProfileId);
      relateProfileAndSubProfile(parentProfileId, subProfileIds);
      return;
    }

    // 获取需要关注的子ProfileId,用于从数据表中找出需要关注的信息列,然后到内存中分析.
    List<Long> concernedSubProfileId = Lists.newArrayList();
    for (ProfileDefinitionPath pdp : rootProfilePathInfo) {
      concernedSubProfileId.add(pdp.getDescendant());
    }
    // 获取需要关注的profile definition path信息.
    List<ProfileDefinitionPath> concernedProfilePathInfo = profileDefinitionPathMapper
        .queryConcernedProfilePathInfo(parentProfileId, concernedSubProfileId);

    // 删除列表
    Set<Long> delIdList = Sets.newHashSet();
    // 更新关联
    Map<Long, ProfileDefinitionPath> updateMap = Maps.newHashMap();
    // 添加列表
    List<ProfileDefinitionPath> addList = Lists.newArrayList();
    // 删除旧的关联关系
    // a 删除的列表
    // b 更新的列表

    // 添加新的关联关系
    // 添加的列表
    // 更新的列表
  }

  /**
   * 根据rootProfilePathInfo和concernedProfilePathInfo分析出需要删除,更新,添加的列表.
   */
  private void processUpdateList(Set<Long> delIdList, Map<Long, ProfileDefinitionPath> updateMap,
      List<ProfileDefinitionPath> addList, List<ProfileDefinitionPath> concernedProfilePathInfo,
      List<ProfileDefinitionPath> rootProfilePathInfo, Long parentProfileId) {
    // 分析出在rootProfileInfo中Profileid 与 deepth的关联关系.
    Map<Long, Byte> rootPathIdDeepth = Maps.newHashMap();
    for (ProfileDefinitionPath rootPdp : rootProfilePathInfo) {
      rootPathIdDeepth.put(rootPdp.getDescendant(), rootPdp.getDeepth());
    }

    // 将查询出来的关联数据分组. ancestor->descendant->deepth->ProfileDefinitionPath
    Map<Long, Map<Long, Map<Byte, ProfileDefinitionPath>>> concernInfoMap = Maps.newHashMap();
    // 在每一个ancestor, root profiled所在的deepth.
    Map<Long, Set<Byte>> rootDeepth = Maps.newHashMap();
    for (ProfileDefinitionPath concernedPdp : concernedProfilePathInfo) {
      
      // 获取没一个ancestor中关心的Deepth集合.
      if (parentProfileId.equals(concernedPdp.getDescendant())) {
        Set<Byte> concernedDeepth = rootDeepth.get(concernedPdp.getAncestor());
        if (concernedDeepth == null) {
          rootDeepth.put(concernedPdp.getAncestor(), new HashSet<Byte>());
          concernedDeepth = rootDeepth.get(concernedPdp.getAncestor());
        }
        concernedDeepth.add(concernedPdp.getDeepth());
      }
      
      Map<Long, Map<Byte, ProfileDefinitionPath>> ancestorMap =
          concernInfoMap.get(concernedPdp.getAncestor());
      if (ancestorMap == null) {
        concernInfoMap.put(concernedPdp.getAncestor(),
            new HashMap<Long, Map<Byte, ProfileDefinitionPath>>());
        ancestorMap = concernInfoMap.get(concernedPdp.getAncestor());
      }
      Map<Byte, ProfileDefinitionPath> descendantMap =
          ancestorMap.get(concernedPdp.getDescendant());
      if (descendantMap == null) {
        ancestorMap.put(concernedPdp.getDescendant(), new HashMap<Byte, ProfileDefinitionPath>());
        descendantMap = ancestorMap.get(concernedPdp.getDescendant());
      }
      descendantMap.put(concernedPdp.getDeepth(), concernedPdp);
    }

    for(Entry<Long, Set<Byte>> deepthEntry:rootDeepth.entrySet()) {
      Long proccessAncestorId = deepthEntry.getKey();
      Set<Byte> tdeepths = deepthEntry.getValue();
      Map<Long, Map<Byte, ProfileDefinitionPath>> ancestorMap = concernInfoMap.get(proccessAncestorId);
      for (Byte tdeepth: tdeepths) {
        for (Entry<Long, Byte> pathIdDeepthEnty : rootPathIdDeepth.entrySet()) {
          
        }
        
      }
    }
    // 处理每一个ancestor
    for (Entry<Long, Map<Long, Map<Byte, ProfileDefinitionPath>>> entry : concernInfoMap
        .entrySet()) {
      
    }
  }

  /**
   * 根据Profile Id获取Profile的所有子profile的关联关系.
   */
  public ProfileDefinitionPathDto getProfilePathTree(Long profileId) {
    CheckEmpty.checkEmpty(profileId, "profile definition id");
    // 获取指定profileId对应的profile关联树结构
    List<ProfileDefinitionPath> profilePathTree =
        profileDefinitionPathMapper.getProfileTreeLinks(profileId);
    if (ObjectUtil.collectionIsEmptyOrNull(profilePathTree)) {
      return null;
    }
    // 构造profile的父子关系
    ProfileDefinitionPath rootProfileDefinitionPath = null;
    Map<Long, List<ProfileDefinitionPath>> pdpMap = Maps.newHashMap();
    for (ProfileDefinitionPath pdp : profilePathTree) {
      if (AppConstants.ZERO_BYTE.equals(pdp.getDeepth())) {
        rootProfileDefinitionPath = pdp;
      } else {
        List<ProfileDefinitionPath> destPdpList = pdpMap.get(pdp.getAncestor());
        if (destPdpList == null) {
          pdpMap.put(pdp.getAncestor(), new ArrayList<ProfileDefinitionPath>());
          destPdpList = pdpMap.get(pdp.getAncestor());
        }
        destPdpList.add(pdp);
      }
    }
    // check root ProfileDefinitionPath
    if (rootProfileDefinitionPath == null) {
      return null;
    }
    ProfileDefinitionPathDto rootPdp = BeanConverter.convert(rootProfileDefinitionPath);
    return rootPdp.setSubProfileInfo(getSubProfileInfo(profileId, pdpMap));
  }

  private List<ProfileDefinitionPathDto> getSubProfileInfo(Long profileId,
      Map<Long, List<ProfileDefinitionPath>> pdpMap) {
    List<ProfileDefinitionPathDto> result = Lists.newArrayList();
    List<ProfileDefinitionPath> pdpList = pdpMap.get(profileId);
    if (ObjectUtil.collectionIsEmptyOrNull(pdpList)) {
      return result;
    }
    for (ProfileDefinitionPath pdp : pdpList) {
      ProfileDefinitionPathDto pdpDto = BeanConverter.convert(pdp);
      pdpDto.setSubProfileInfo(getSubProfileInfo(pdp.getDescendant(), pdpMap));
    }
    return result;
  }
}
