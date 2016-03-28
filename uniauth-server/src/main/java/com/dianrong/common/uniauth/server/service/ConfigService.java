package com.dianrong.common.uniauth.server.service;

import com.dianrong.common.uniauth.common.bean.dto.ConfigDto;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.server.data.entity.Cfg;
import com.dianrong.common.uniauth.server.data.entity.CfgExample;
import com.dianrong.common.uniauth.server.data.mapper.CfgMapper;
import com.dianrong.common.uniauth.server.util.BeanConverter;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import org.apache.cxf.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arc on 25/3/2016.
 */
@Service
public class ConfigService {

    @Autowired
    private CfgMapper cfgMapper;

    public ConfigDto addOrUpdateConfig(Integer id, String key, String type, String value, byte[] file) {
        Cfg cfg = new Cfg();
        cfg.setId(id);
        cfg.setKey(key);
        cfg.setFile(file);
        cfg.setValue(value);
        cfg.setType(type);
        // update process.
        if(id != null) {
            if(file != null) {
                cfgMapper.updateByPrimaryKeyWithBLOBs(cfg);
            } else {
                cfgMapper.updateByPrimaryKey(cfg);
            }
        } else {
            // add process.
            cfgMapper.insert(cfg);
        }
        return BeanConverter.convert(cfg);
    }

    public PageDto<ConfigDto> queryConfig(Integer id, String key, String type, String value, Integer pageSize, Integer pageNumber) {
        CheckEmpty.checkEmpty(pageNumber, "pageNumber");
        CheckEmpty.checkEmpty(pageSize, "pageSize");

        CfgExample cfgExample = new CfgExample();
        cfgExample.setPageOffSet(pageNumber * pageSize);
        cfgExample.setPageSize(pageSize);
        CfgExample.Criteria criteria = cfgExample.createCriteria();

        if(id != null) {
            criteria.andIdEqualTo(id);
        }
        if(!StringUtils.isEmpty(key)) {
            criteria.andKeyEqualTo(key);
        }
        if(!StringUtils.isEmpty(type)) {
            criteria.andTypeEqualTo(type);
        }
        if(!StringUtils.isEmpty(value)) {
            criteria.andValueEqualTo(value);
        }
        List<Cfg> cfgs = cfgMapper.selectByExample(cfgExample);
        if(CollectionUtils.isEmpty(cfgs)) {
            return null;
        } else {
            List<ConfigDto> configDtos = new ArrayList<>();
            for(Cfg cfg:cfgs) {
                configDtos.add(BeanConverter.convert(cfg));
            }
            int count = cfgMapper.countByExample(cfgExample);
            return new PageDto<>(pageNumber,pageSize,count,configDtos);
        }
    }

    public void delConfig(Integer cfgId) {
        cfgMapper.deleteByPrimaryKey(cfgId);
    }

}
