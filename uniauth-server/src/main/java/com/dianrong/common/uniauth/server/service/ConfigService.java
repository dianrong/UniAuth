package com.dianrong.common.uniauth.server.service;

import com.dianrong.common.uniauth.common.bean.dto.ConfigDto;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.util.StringUtil;
import com.dianrong.common.uniauth.server.data.entity.Cfg;
import com.dianrong.common.uniauth.server.data.entity.CfgExample;
import com.dianrong.common.uniauth.server.data.entity.CfgType;
import com.dianrong.common.uniauth.server.data.entity.CfgTypeExample;
import com.dianrong.common.uniauth.server.data.mapper.CfgMapper;
import com.dianrong.common.uniauth.server.data.mapper.CfgTypeMapper;
import com.dianrong.common.uniauth.server.datafilter.DataFilter;
import com.dianrong.common.uniauth.server.datafilter.FieldType;
import com.dianrong.common.uniauth.server.datafilter.FilterType;
import com.dianrong.common.uniauth.server.service.common.TenancyBasedService;
import com.dianrong.common.uniauth.server.util.BeanConverter;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.dianrong.common.uniauth.server.util.ParamCheck;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.cxf.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


/**
 * Created by Arc on 25/3/2016.
 */
@Service
public class ConfigService extends TenancyBasedService {

  @Autowired
  private CfgMapper cfgMapper;

  @Autowired
  private CfgTypeMapper cfgTypeMapper;

  @Resource(name = "cfgDataFilter")
  private DataFilter dataFilter;

  /**
   * 添加或更新配置信息.
   */
  public ConfigDto addOrUpdateConfig(Integer id, String cfgKey, Integer cfgTypeId, String value,
      byte[] file) {
    Cfg cfg = new Cfg();
    cfg.setId(id);
    cfg.setCfgKey(cfgKey);
    cfg.setFile(file);
    cfg.setValue(value);
    cfg.setCfgTypeId(cfgTypeId);
    // update process.
    if (id != null) {
      cfg.setTenancyId(AppConstants.TENANCY_UNRELATED_TENANCY_ID);
      if (!StringUtil.strIsNullOrEmpty(cfgKey)) {
        // 更新判断比较
        dataFilter.updateFieldCheck(id, FieldType.FIELD_TYPE_CFG_KEY, cfgKey);
      }
      Map<String, Integer> cfgTypesMap = this.getAllCfgTypesCodeIdPair();
      if (cfgTypesMap.get(AppConstants.CFG_TYPE_FILE).equals(cfgTypeId) && file != null) {
        // when create new file cfg or update the old file
        cfgMapper.updateByPrimaryKeyWithBlobs(cfg);
      } else if (cfgTypesMap.get(AppConstants.CFG_TYPE_FILE).equals(cfgTypeId) && file == null) {
        // only update the key part for the file type cfg update.
        cfgMapper.updateByPrimaryKey(cfg);
      } else {
        // if cfg is not file, then update them all.
        cfg.setFile(null);
        cfgMapper.updateByPrimaryKeyWithBlobs(cfg);
      }
    } else {
      if (!StringUtil.strIsNullOrEmpty(cfgKey)) {
        // 添加判断比较
        dataFilter.addFieldCheck(FilterType.EXSIT_DATA, FieldType.FIELD_TYPE_CFG_KEY,
            cfgKey);
      }

      cfg.setTenancyId(AppConstants.TENANCY_UNRELATED_TENANCY_ID);
      // add process.
      cfgMapper.insert(cfg);
    }
    // do not need to return file after add or update.
    cfg.setFile(null);
    return BeanConverter.convert(cfg);
  }

  /**
   * 查询配置信息.
   */
  public PageDto<ConfigDto> queryConfig(List<String> cfgKeys, String cfgKeyLike, Integer id,
      String cfgKey, Integer cfgTypeId, String value, Boolean needBlobs, Integer pageSize,
      Integer pageNumber) {

    CheckEmpty.checkEmpty(pageNumber, "pageNumber");
    CheckEmpty.checkEmpty(pageSize, "pageSize");

    CfgExample cfgExample = new CfgExample();
    cfgExample.setPageOffSet(pageNumber * pageSize);
    cfgExample.setPageSize(pageSize);
    cfgExample.setOrderByClause("cfg_type_id asc");
    CfgExample.Criteria criteria = cfgExample.createCriteria();

    if (id != null) {
      criteria.andIdEqualTo(id);
    }
    if (!StringUtils.isEmpty(cfgKey)) {
      criteria.andCfgKeyEqualTo(cfgKey);
    }
    if (!StringUtils.isEmpty(cfgKeyLike)) {
      criteria.andCfgKeyLike(cfgKeyLike + "%");
    }
    if (cfgTypeId != null) {
      criteria.andCfgTypeIdEqualTo(cfgTypeId);
    }
    if (!StringUtils.isEmpty(value)) {
      criteria.andValueLike("%" + value + "%");
    }
    if (!CollectionUtils.isEmpty(cfgKeys)) {
      criteria.andCfgKeyIn(cfgKeys);
    }
    criteria.andTenancyIdEqualTo(AppConstants.TENANCY_UNRELATED_TENANCY_ID);

    int count = cfgMapper.countByExample(cfgExample);
    ParamCheck.checkPageParams(pageNumber, pageSize, count);
    List<Cfg> cfgs;
    if (needBlobs != null && needBlobs) {
      cfgs = cfgMapper.selectByExampleWithBlobs(cfgExample);
    } else {
      cfgs = cfgMapper.selectByExample(cfgExample);
    }

    if (CollectionUtils.isEmpty(cfgs)) {
      return null;
    } else {
      List<ConfigDto> configDtos = new ArrayList<>();
      Map<Integer, String> cfgTypeIndex = this.getAllCfgTypesIdCodePair();
      for (Cfg cfg : cfgs) {
        ConfigDto configDto = BeanConverter.convert(cfg);
        configDto.setCfgType(cfgTypeIndex.get(cfg.getCfgTypeId()));
        configDtos.add(configDto);
      }
      return new PageDto<>(pageNumber, pageSize, count, configDtos);
    }
  }

  public void delConfig(Integer cfgId) {
    cfgMapper.deleteByPrimaryKey(cfgId);
  }

  /**
   * 获取所有的配置的Id和Code的Map.
   */
  public Map<Integer, String> getAllCfgTypesIdCodePair() {
    List<CfgType> cfgTypes = cfgTypeMapper.selectByExample(new CfgTypeExample());
    Map<Integer, String> cfgTypeMap = new HashMap<>();
    for (CfgType cfgType : cfgTypes) {
      cfgTypeMap.put(cfgType.getId(), cfgType.getCode());
    }
    return cfgTypeMap;
  }

  /**
   * 获取所有的配置的Code和Id的Map.
   */
  public Map<String, Integer> getAllCfgTypesCodeIdPair() {
    List<CfgType> cfgTypes = cfgTypeMapper.selectByExample(new CfgTypeExample());
    Map<String, Integer> cfgTypeMap = new HashMap<>();
    for (CfgType cfgType : cfgTypes) {
      cfgTypeMap.put(cfgType.getCode(), cfgType.getId());
    }
    return cfgTypeMap;
  }

}
