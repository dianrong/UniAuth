package com.dianrong.common.uniauth.server.service.inner;

import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.server.data.entity.Grp;
import com.dianrong.common.uniauth.server.data.entity.GrpExample;
import com.dianrong.common.uniauth.server.data.entity.GrpPath;
import com.dianrong.common.uniauth.server.data.entity.GrpPathExample;
import com.dianrong.common.uniauth.server.data.entity.UserGrpExample;
import com.dianrong.common.uniauth.server.data.entity.UserGrpKey;
import com.dianrong.common.uniauth.server.data.mapper.GrpMapper;
import com.dianrong.common.uniauth.server.data.mapper.GrpPathMapper;
import com.dianrong.common.uniauth.server.data.mapper.UserGrpMapper;
import com.dianrong.common.uniauth.server.datafilter.DataFilter;
import com.dianrong.common.uniauth.server.service.common.TenancyBasedService;
import com.dianrong.common.uniauth.server.support.tree.TreeType;
import com.dianrong.common.uniauth.server.support.tree.TreeTypeHolder;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class GroupInnerService extends TenancyBasedService {

  @Autowired
  private GrpMapper grpMapper;
  @Autowired
  private GrpPathMapper grpPathMapper;
  @Autowired
  private UserGrpMapper userGrpMapper;

  /**
   * 进行组数据过滤的filter.
   */
  @Resource(name = "groupDataFilter")
  private DataFilter dataFilter;

  /**
   * 获取用户最新关联的组的结构.(去关联的组中, 组id最大的)
   *
   * @param userId 用户id, 不能为空
   * @return 组信息列表. 以从根组到子组的顺序排序
   */
  public List<Grp> listUserLastGrpPath(Long userId) {
    CheckEmpty.checkEmpty(userId, "userId");
    List<Grp> grps = Lists.newArrayList();
    UserGrpExample ugExample = new UserGrpExample();
    UserGrpExample.Criteria ugCriteria = ugExample.createCriteria();
    ugCriteria.andTypeEqualTo(AppConstants.ZERO_BYTE).andUserIdEqualTo(userId);
    ugExample.setOrderByClause(" grp_id desc");
    List<UserGrpKey> userGrpList = userGrpMapper.selectByExample(ugExample);
    if (CollectionUtils.isEmpty(userGrpList)) {
      return grps;
    }
    Integer grpId = userGrpList.get(0).getGrpId();
    GrpPathExample gpExample = new GrpPathExample();
    GrpPathExample.Criteria gpCriteria = gpExample.createCriteria();
    gpCriteria.andDescendantEqualTo(grpId);
    gpExample.setOrderByClause("deepth desc");
    List<GrpPath> grpPathList = grpPathMapper.selectByExample(gpExample);
    if (CollectionUtils.isEmpty(grpPathList)) {
      return grps;
    }
    List<Integer> grpIds = Lists.newArrayList();
    for (GrpPath gp : grpPathList) {
      grpIds.add(gp.getAncestor());
    }
    GrpExample grpExample = new GrpExample();
    grpExample.setGrpCode(TreeTypeHolder.get(TreeType.NORMAL).getRootCode());
    grpExample.setStatus(AppConstants.STATUS_ENABLED);
    grpExample.setTenancyId(tenancyService.getTenancyIdWithCheck());
    GrpExample.Criteria grpCriteria = grpExample.createCriteria();
    grpCriteria.andIdIn(grpIds).andTenancyIdEqualTo(tenancyService.getTenancyIdWithCheck());
    List<Grp> grpList = grpMapper.selectByExample(grpExample);
    if (CollectionUtils.isEmpty(grpList)) {
      return grps;
    }
    Map<Integer, Grp> grpMap = Maps.newHashMap();
    for (Grp grp : grpList) {
      grpMap.put(grp.getId(), grp);
    }

    // 按照depth从大到小的顺序来放入list
    for (Integer gid : grpIds) {
      Grp grpItem = grpMap.get(gid);
      if (grpItem != null) {
        grps.add(grpItem);
      }
    }
    return grps;
  }

  /**
   * 获取所有的与用户关联的组(直接与用户关联的组,以及其所有的父组)
   *
   * @param userId 用户id, 不能为空
   * @return 组信息列表.
   */
  public List<Grp> listAllGrpRelateUser(Long userId) {
    CheckEmpty.checkEmpty(userId, "userId");
    UserGrpExample ugExample = new UserGrpExample();
    UserGrpExample.Criteria ugCriteria = ugExample.createCriteria();
    ugCriteria.andTypeEqualTo(AppConstants.ZERO_BYTE).andUserIdEqualTo(userId);
    List<UserGrpKey> userGrpList = userGrpMapper.selectByExample(ugExample);
    if (CollectionUtils.isEmpty(userGrpList)) {
      return Collections.emptyList();
    }
    List<Integer> grpIdList = new ArrayList<>();
    for (UserGrpKey userGrpKey : userGrpList) {
      grpIdList.add(userGrpKey.getGrpId());
    }
    GrpPathExample gpExample = new GrpPathExample();
    GrpPathExample.Criteria gpCriteria = gpExample.createCriteria();
    gpCriteria.andDescendantIn(grpIdList);
    List<GrpPath> grpPathList = grpPathMapper.selectByExample(gpExample);
    if (CollectionUtils.isEmpty(grpPathList)) {
      return Collections.emptyList();
    }
    Set<Integer> relateGrpIdSet = new HashSet<>();
    for (GrpPath gp : grpPathList) {
      relateGrpIdSet.add(gp.getAncestor());
    }
    GrpExample grpExample = new GrpExample();
    grpExample.setGrpCode(TreeTypeHolder.get(TreeType.NORMAL).getRootCode());
    grpExample.setStatus(AppConstants.STATUS_ENABLED);
    grpExample.setTenancyId(tenancyService.getTenancyIdWithCheck());
    GrpExample.Criteria grpCriteria = grpExample.createCriteria();
    grpCriteria.andIdIn(new ArrayList<Integer>(relateGrpIdSet))
        .andStatusEqualTo(AppConstants.STATUS_ENABLED)
        .andTenancyIdEqualTo(tenancyService.getTenancyIdWithCheck());
    return grpMapper.selectByExample(grpExample);
  }
}
