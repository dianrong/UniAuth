package com.dianrong.common.uniauth.server.service;

import com.dianrong.common.uniauth.common.bean.dto.AuditDto;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.server.data.entity.Audit;
import com.dianrong.common.uniauth.server.data.entity.AuditExample;
import com.dianrong.common.uniauth.server.data.mapper.AuditMapper;
import com.dianrong.common.uniauth.server.service.common.TenancyBasedService;
import com.dianrong.common.uniauth.server.util.BeanConverter;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.dianrong.common.uniauth.server.util.ParamCheck;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * Created by Arc on 24/3/2016.
 */
@Service
public class AuditService extends TenancyBasedService {

  @Autowired
  private AuditMapper auditMapper;

  /**
   * 查询日志信息.
   */
  public PageDto<AuditDto> searchAudit(Long userId, Date minRequestDate, Date maxRequestDate,
      Integer domainId, String reqIp, String reqUuid, String reqUrl, Long reqSequence,
      String reqClass, String reqMethod, Byte reqSuccess, String reqException, Long minReqElapse,
      Long maxReqElapse, String reqParam, String reqResult, String orderBy, Boolean ascOrDesc,
      Integer pageNumber, Integer pageSize, String requestDomainCode) {

    AuditExample auditExample = new AuditExample();
    if (orderBy != null) {
      if (ascOrDesc == null || Boolean.FALSE.equals(ascOrDesc)) {
        auditExample.setOrderByClause(orderBy + " desc");
      } else {
        auditExample.setOrderByClause(orderBy + " asc");
      }
    }

    auditExample.setPageOffSet(pageNumber * pageSize);
    auditExample.setPageSize(pageSize);
    CheckEmpty.checkEmpty(pageNumber, "pageNumber");
    CheckEmpty.checkEmpty(pageSize, "pageSize");

    this.constructExample(userId, minRequestDate, maxRequestDate, domainId, reqIp, reqUuid, reqUrl,
        reqSequence, reqClass, reqMethod, reqSuccess, reqException, minReqElapse, maxReqElapse,
        reqParam, reqResult, requestDomainCode, auditExample);
    int count = auditMapper.countByExample(auditExample);
    ParamCheck.checkPageParams(pageNumber, pageSize, count);
    List<Audit> audits = auditMapper.selectByExample(auditExample);
    if (!CollectionUtils.isEmpty(audits)) {
      List<AuditDto> auditDtos = new ArrayList<>();
      for (Audit audit : audits) {
        auditDtos.add(BeanConverter.convert(audit));
      }
      return new PageDto<>(pageNumber, pageSize, count, auditDtos);
    } else {
      return null;
    }
  }

  /**
   * 删除日志信息.
   */
  public Integer deleteAudit(Long userId, Date minRequestDate, Date maxRequestDate,
      Integer domainId, String reqIp, String reqUuid, String reqUrl, Long reqSequence,
      String reqClass, String reqMethod, Byte reqSuccess, String reqException, Long minReqElapse,
      Long maxReqElapse, String reqParam, String reqResult, String requestDomainCode) {
    CheckEmpty.checkEmpty(minRequestDate, "minRequestDate");
    CheckEmpty.checkEmpty(maxRequestDate, "maxRequestDate");
    AuditExample auditExample = new AuditExample();
    this.constructExample(userId, minRequestDate, maxRequestDate, domainId, reqIp, reqUuid, reqUrl,
        reqSequence, reqClass, reqMethod, reqSuccess, reqException, minReqElapse, maxReqElapse,
        reqParam, reqResult, requestDomainCode, auditExample);
    return auditMapper.deleteByExample(auditExample);
  }

  private void constructExample(Long userId, Date minRequestDate, Date maxRequestDate,
      Integer domainId, String reqIp, String reqUuid, String reqUrl, Long reqSequence,
      String reqClass, String reqMethod, Byte reqSuccess, String reqException, Long minReqElapse,
      Long maxReqElapse, String reqParam, String reqResult, String requestDomainCode,
      AuditExample auditExample) {

    AuditExample.Criteria criteria = auditExample.createCriteria();
    if (minReqElapse != null) {
      criteria.andReqElapseGreaterThanOrEqualTo(minReqElapse);
    }
    if (maxReqElapse != null) {
      criteria.andReqElapseLessThanOrEqualTo(maxReqElapse);
    }

    if (!StringUtils.isEmpty(reqParam)) {
      criteria.andReqParamFullTextIndexMatch(reqParam);
    }
    if (!StringUtils.isEmpty(reqResult)) {
      criteria.andReqResultLike("%" + reqResult + "%");
    }
    if (userId != null) {
      criteria.andUserIdEqualTo(userId);
    }
    if (minRequestDate != null) {
      criteria.andReqDateGreaterThanOrEqualTo(minRequestDate);
    }
    if (maxRequestDate != null) {
      criteria.andReqDateLessThanOrEqualTo(maxRequestDate);
    }
    if (domainId != null) {
      criteria.andDomainIdEqualTo(domainId);
    }
    if (!StringUtils.isEmpty(reqIp)) {
      criteria.andReqIpEqualTo(reqIp);
    }
    if (!StringUtils.isEmpty(reqUuid)) {
      criteria.andReqUuidEqualTo(reqUuid);
    }
    if (!StringUtils.isEmpty(reqUrl)) {
      criteria.andReqUrlLike("%" + reqUrl + "%");
    }
    if (reqSequence != null) {
      criteria.andReqSeqEqualTo(reqSequence);
    }
    if (!StringUtils.isEmpty(reqClass)) {
      criteria.andReqClassEqualTo(reqClass);
    }
    if (!StringUtils.isEmpty(reqMethod)) {
      criteria.andReqMethodEqualTo(reqMethod);
    }
    if (reqSuccess != null) {
      criteria.andReqSuccessEqualTo(reqSuccess);
    }
    if (!StringUtils.isEmpty(reqException)) {
      criteria.andReqExpLike("%" + reqException + "%");
    }
    if (!StringUtils.isEmpty(requestDomainCode)) {
      criteria.andRequestDomainCodeEqualTo(requestDomainCode);
    }
    criteria.andTenancyIdEqualTo(tenancyService.getTenancyIdWithCheck());
  }
}
