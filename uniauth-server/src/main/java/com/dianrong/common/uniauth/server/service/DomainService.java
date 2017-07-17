package com.dianrong.common.uniauth.server.service;

import com.dianrong.common.uniauth.common.bean.InfoName;
import com.dianrong.common.uniauth.common.bean.dto.DomainDto;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.dto.StakeholderDto;
import com.dianrong.common.uniauth.common.bean.request.DomainParam;
import com.dianrong.common.uniauth.common.bean.request.PrimaryKeyParam;
import com.dianrong.common.uniauth.common.bean.request.StakeholderParam;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.util.ObjectUtil;
import com.dianrong.common.uniauth.common.util.StringUtil;
import com.dianrong.common.uniauth.server.data.entity.Domain;
import com.dianrong.common.uniauth.server.data.entity.DomainExample;
import com.dianrong.common.uniauth.server.data.entity.Stakeholder;
import com.dianrong.common.uniauth.server.data.entity.StakeholderExample;
import com.dianrong.common.uniauth.server.data.entity.TagType;
import com.dianrong.common.uniauth.server.data.entity.TagTypeExample;
import com.dianrong.common.uniauth.server.data.mapper.DomainMapper;
import com.dianrong.common.uniauth.server.data.mapper.StakeholderMapper;
import com.dianrong.common.uniauth.server.data.mapper.TagTypeMapper;
import com.dianrong.common.uniauth.server.datafilter.DataFilter;
import com.dianrong.common.uniauth.server.datafilter.FieldType;
import com.dianrong.common.uniauth.server.datafilter.FilterType;
import com.dianrong.common.uniauth.server.exp.AppException;
import com.dianrong.common.uniauth.server.service.common.TenancyBasedService;
import com.dianrong.common.uniauth.server.util.BeanConverter;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.dianrong.common.uniauth.server.util.ParamCheck;
import com.dianrong.common.uniauth.server.util.UniBundle;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
public class DomainService extends TenancyBasedService {

  @Autowired
  private DomainMapper domainMapper;
  @Autowired
  private StakeholderMapper stakeholderMapper;
  @Autowired
  private TagTypeMapper tagTypeMapper;

  @Resource(name = "domainDataFilter")
  private DataFilter dataFilter;

  /**
   * 添加域信息.
   */
  @Transactional
  public DomainDto addNewDomain(DomainParam domainParam) {
    String domainCode = domainParam.getCode();
    CheckEmpty.checkEmpty(domainCode, "域编码");

    // 检查code
    dataFilter.addFieldCheck(FilterType.EXSIT_DATA, FieldType.FIELD_TYPE_CODE, domainCode);

    DomainExample example = new DomainExample();
    example.createCriteria().andCodeEqualTo(domainCode)
        .andTenancyIdEqualTo(AppConstants.TENANCY_UNRELATED_TENANCY_ID);
    List<Domain> domainList = domainMapper.selectByExample(example);
    if (domainList == null || domainList.isEmpty()) {
      Domain param = BeanConverter.convert(domainParam);
      Date now = new Date();
      param.setCreateDate(now);
      param.setLastUpdate(now);
      param.setTenancyId(AppConstants.TENANCY_UNRELATED_TENANCY_ID);
      domainMapper.insert(param);
      return BeanConverter.convert(param);
    } else {
      throw new AppException(InfoName.BAD_REQUEST,
          UniBundle.getMsg("common.entity.code.duplicate", domainCode, "域"));
    }
  }

  /**
   * 根据条件批量获取域信息.
   */
  public PageDto<DomainDto> searchDomain(List<Integer> domainIds, Integer domainId,
      String domainCode, String displayName, Byte status, String description, Integer pageNumber,
      Integer pageSize) {
    CheckEmpty.checkEmpty(pageNumber, "pageNumber");
    CheckEmpty.checkEmpty(pageSize, "pageSize");
    DomainExample domainExample = new DomainExample();
    DomainExample.Criteria criteria = domainExample.createCriteria();
    domainExample.setOrderByClause("status asc");
    domainExample.setPageOffSet(pageNumber * pageSize);
    domainExample.setPageSize(pageSize);
    if (domainIds != null) {
      criteria.andIdIn(domainIds);
    }
    if (domainId != null) {
      criteria.andIdEqualTo(domainId);
    }
    if (!StringUtils.isEmpty(domainCode)) {
      criteria.andCodeEqualTo(domainCode);
    }
    if (!StringUtils.isEmpty(displayName)) {
      criteria.andDisplayNameLike("%" + displayName + "%");
    }
    if (status != null) {
      criteria.andStatusEqualTo(status);
    }
    if (!StringUtils.isEmpty(description)) {
      criteria.andDescriptionLike("%" + description + "%");
    }
    criteria.andTenancyIdEqualTo(AppConstants.TENANCY_UNRELATED_TENANCY_ID);

    int count = domainMapper.countByExample(domainExample);
    ParamCheck.checkPageParams(pageNumber, pageSize, count);
    List<Domain> domains = domainMapper.selectByExample(domainExample);
    if (!CollectionUtils.isEmpty(domains)) {
      List<DomainDto> domainDtos = new ArrayList<>();
      for (Domain domain : domains) {
        domainDtos.add(BeanConverter.convert(domain));
      }
      return new PageDto<>(pageNumber, pageSize, count, domainDtos);
    } else {
      return null;
    }
  }

  /**
   * 获取域的开发者信息列表.
   */
  public List<StakeholderDto> getAllStakeholdersInDomain(Integer domainId) {
    if (domainId != null) {
      StakeholderExample stakeholderExample = new StakeholderExample();
      stakeholderExample.createCriteria().andDomainIdEqualTo(domainId)
          .andTenancyIdEqualTo(AppConstants.TENANCY_UNRELATED_TENANCY_ID);
      List<Stakeholder> stakeholders = stakeholderMapper.selectByExample(stakeholderExample);
      List<StakeholderDto> stakeholderDtos = new ArrayList<>();
      if (stakeholders != null) {
        for (Stakeholder stakeholder : stakeholders) {
          stakeholderDtos.add(BeanConverter.convert(stakeholder));
        }
      }
      return stakeholderDtos;
    }
    return null;
  }

  /**
   * 获取域信息.
   */
  public List<DomainDto> getAllLoginDomains(DomainParam domainParam) {
    List<String> domainCodeList = domainParam.getDomainCodeList();
    DomainExample example = new DomainExample();
    DomainExample.Criteria criteria = example.createCriteria();
    criteria.andStatusEqualTo(AppConstants.ZERO_BYTE);
    if (domainCodeList != null) {
      criteria.andCodeIn(domainCodeList);
    }
    criteria.andTenancyIdEqualTo(AppConstants.TENANCY_UNRELATED_TENANCY_ID);

    List<Domain> domainList = domainMapper.selectByExample(example);
    List<DomainDto> domainDtoList = new ArrayList<DomainDto>();
    if (domainList != null) {
      for (Domain domain : domainList) {
        domainDtoList.add(BeanConverter.convert(domain));
      }
    }

    return domainDtoList;
  }

  /**
   * 根据标签类型id集合获取标签类型id与域信息的Map.
   */
  public Map<Integer, DomainDto> getDomainMapByTagTypeIds(List<Integer> tagTypeIds) {
    Map<Integer, DomainDto> map = Maps.newHashMap();
    if (ObjectUtil.collectionIsEmptyOrNull(tagTypeIds)) {
      return map;
    }

    TagTypeExample tagTypeExample = new TagTypeExample();
    TagTypeExample.Criteria tagTypeCriteria = tagTypeExample.createCriteria();
    tagTypeCriteria.andIdIn(tagTypeIds).andTenancyIdEqualTo(tenancyService.getTenancyIdWithCheck());
    List<TagType> tagTypes = tagTypeMapper.selectByExample(tagTypeExample);
    if (ObjectUtil.collectionIsEmptyOrNull(tagTypes)) {
      return map;
    }
    List<Integer> domainIds = Lists.newArrayList();
    for (TagType tt : tagTypes) {
      domainIds.add(tt.getDomainId());
    }

    DomainExample domainExample = new DomainExample();
    DomainExample.Criteria domainCriteria = domainExample.createCriteria();
    domainCriteria.andIdIn(domainIds);
    List<Domain> domains = domainMapper.selectByExample(domainExample);
    if (ObjectUtil.collectionIsEmptyOrNull(domains)) {
      return map;
    }
    Map<Integer, Domain> domainMap = Maps.newHashMap();
    for (Domain domain : domains) {
      domainMap.put(domain.getId(), domain);
    }
    for (TagType tagType : tagTypes) {
      Domain domain = domainMap.get(tagType.getDomainId());
      if (domain != null) {
        map.put(tagType.getId(), BeanConverter.convert(domain));
      }
    }
    return map;
  }

  /**
   * 根据DomainId集合获取域Id与域信息的Map.
   */
  public Map<Integer, DomainDto> getDomainMapByDomainIds(List<Integer> domainIds) {
    Map<Integer, DomainDto> map = Maps.newHashMap();
    if (ObjectUtil.collectionIsEmptyOrNull(domainIds)) {
      return map;
    }
    DomainExample domainExample = new DomainExample();
    DomainExample.Criteria domainCriteria = domainExample.createCriteria();
    domainCriteria.andIdIn(domainIds);
    List<Domain> domains = domainMapper.selectByExample(domainExample);
    if (ObjectUtil.collectionIsEmptyOrNull(domains)) {
      return map;
    }
    for (Domain domain : domains) {
      map.put(domain.getId(), BeanConverter.convert(domain));
    }
    return map;
  }

  /**
   * 根据主键获取域详细信息.
   */
  public DomainDto getDomainInfo(PrimaryKeyParam primaryKeyParam) {
    CheckEmpty.checkParamId(primaryKeyParam, "域ID");
    Integer domainId = primaryKeyParam.getId();
    Domain domain = checkDomain(domainId);

    StakeholderExample stakeholderExample = new StakeholderExample();
    stakeholderExample.createCriteria().andDomainIdEqualTo(domainId)
        .andTenancyIdEqualTo(AppConstants.TENANCY_UNRELATED_TENANCY_ID);
    List<Stakeholder> stakeHolderList = stakeholderMapper.selectByExample(stakeholderExample);
    List<StakeholderDto> stakeholderDtoList = new ArrayList<StakeholderDto>();
    for (Stakeholder stakeholder : stakeHolderList) {
      stakeholderDtoList.add(BeanConverter.convert(stakeholder));
    }

    DomainDto domainDto = BeanConverter.convert(domain);
    domainDto.setStakeholderList(stakeholderDtoList);

    return domainDto;
  }

  /**
   * 更新域信息.
   */
  @Transactional
  public void updateDomain(DomainParam domainParam) {
    if (domainParam == null || domainParam.getId() == null) {
      throw new AppException(InfoName.BAD_REQUEST,
          UniBundle.getMsg("common.parameter.empty", "域ID"));
    }

    Domain domain = domainMapper.selectByPrimaryKey(domainParam.getId());
    if (domain != null && AppConstants.DOMAIN_CODE_TECHOPS.equals(domain.getCode())) {
      if (StringUtils.hasText(domainParam.getCode())
          && !AppConstants.DOMAIN_CODE_TECHOPS.equals(domain.getCode())) {
        throw new AppException(InfoName.BAD_REQUEST,
            UniBundle.getMsg("domain.techops.code.unmodifiable"));
      }
    }

    if ((domainParam.getStatus() != null && domainParam.getStatus() == AppConstants.STATUS_ENABLED)
        || domainParam.getStatus() == null) {
      // 如果需要更新code,则加入判断
      if (!StringUtil.strIsNullOrEmpty(domainParam.getCode())) {
        // 检查code
        dataFilter.updateFieldCheck(domainParam.getId(), FieldType.FIELD_TYPE_CODE,
            domainParam.getCode());
      }
    }

    Domain param = BeanConverter.convert(domainParam);
    param.setLastUpdate(new Date());
    param.setTenancyId(AppConstants.TENANCY_UNRELATED_TENANCY_ID);
    domainMapper.updateByPrimaryKeySelective(param);
  }

  /**
   * 为域添加开发者信息.
   */
  @Transactional
  public StakeholderDto addNewStakeholder(StakeholderParam stakeholderParam) {
    Integer domainId = stakeholderParam.getDomainId();

    if (domainId != null) {
      // 必须要合法的数据才能插入
      dataFilter.addFieldCheck(FilterType.NO_DATA, FieldType.FIELD_TYPE_ID, domainId);
    } else {
      throw new AppException(InfoName.BAD_REQUEST,
          UniBundle.getMsg("common.parameter.empty", "域相关人ID"));
    }
    Stakeholder stakeholder = BeanConverter.convert(stakeholderParam, false);
    stakeholder.setTenancyId(AppConstants.TENANCY_UNRELATED_TENANCY_ID);
    stakeholderMapper.insert(stakeholder);
    StakeholderDto stakeholderDto = BeanConverter.convert(stakeholder);
    return stakeholderDto;
  }

  /**
   * 更新域关联的开发者信息.
   */
  @Transactional
  public void updateStakeholder(StakeholderParam stakeholderParam) {
    if (stakeholderParam == null || stakeholderParam.getId() == null) {
      throw new AppException(InfoName.BAD_REQUEST,
          UniBundle.getMsg("common.parameter.empty", "域相关人ID"));
    }

    Stakeholder param = BeanConverter.convert(stakeholderParam, true);
    stakeholderMapper.updateByPrimaryKeySelective(param);
  }

  @Transactional
  public void deleteStakeholder(PrimaryKeyParam primaryKeyParam) {
    CheckEmpty.checkParamId(primaryKeyParam, "域相关人ID");
    stakeholderMapper.deleteByPrimaryKey(primaryKeyParam.getId());
  }

  private Domain checkDomain(Integer domainId) {
    Domain domain = domainMapper.selectByPrimaryKey(domainId);
    if (domain == null) {
      throw new AppException(InfoName.BAD_REQUEST,
          UniBundle.getMsg("common.entity.notfound", String.valueOf(domainId), "域"));
    }
    if (domain.getStatus() == 1) {
      throw new AppException(InfoName.BAD_REQUEST,
          UniBundle.getMsg("common.entity.status.isone", String.valueOf(domainId), "域"));
    }
    return domain;
  }
}
