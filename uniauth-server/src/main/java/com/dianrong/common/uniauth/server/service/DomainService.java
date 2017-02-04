package com.dianrong.common.uniauth.server.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.dianrong.common.uniauth.common.bean.InfoName;
import com.dianrong.common.uniauth.common.bean.dto.DomainDto;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.dto.StakeholderDto;
import com.dianrong.common.uniauth.common.bean.request.DomainParam;
import com.dianrong.common.uniauth.common.bean.request.PrimaryKeyParam;
import com.dianrong.common.uniauth.common.bean.request.StakeholderParam;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.util.StringUtil;
import com.dianrong.common.uniauth.server.data.entity.Domain;
import com.dianrong.common.uniauth.server.data.entity.DomainExample;
import com.dianrong.common.uniauth.server.data.entity.Stakeholder;
import com.dianrong.common.uniauth.server.data.entity.StakeholderExample;
import com.dianrong.common.uniauth.server.data.mapper.DomainMapper;
import com.dianrong.common.uniauth.server.data.mapper.StakeholderMapper;
import com.dianrong.common.uniauth.server.datafilter.DataFilter;
import com.dianrong.common.uniauth.server.datafilter.FieldType;
import com.dianrong.common.uniauth.server.datafilter.FilterType;
import com.dianrong.common.uniauth.server.exp.AppException;
import com.dianrong.common.uniauth.server.util.BeanConverter;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.dianrong.common.uniauth.server.util.ParamCheck;
import com.dianrong.common.uniauth.server.util.UniBundle;

@Service
public class DomainService extends TenancyBasedService{

	@Autowired
	private DomainMapper domainMapper;
	@Autowired
	private StakeholderMapper stakeholderMapper;
	
	/**.
	 * 进行域名数据过滤的filter
	 */
	@Resource(name="domainDataFilter")
	private DataFilter dataFilter;
	
	@Transactional
	public DomainDto addNewDomain(DomainParam domainParam) {
		String domainCode = domainParam.getCode();
		CheckEmpty.checkEmpty(domainCode, "域编码");
		
		//检查code
		dataFilter.addFieldCheck(FilterType.FILTER_TYPE_EXSIT_DATA, FieldType.FIELD_TYPE_CODE, domainCode);
		
		DomainExample example = new DomainExample();
		example.createCriteria().andCodeEqualTo(domainCode).andTenancyIdEqualTo(AppConstants.TENANCY_UNRELATED_TENANCY_ID);
		List<Domain> domainList = domainMapper.selectByExample(example);
		if(domainList == null || domainList.isEmpty()){
			Domain param = BeanConverter.convert(domainParam);
			Date now = new Date();
			param.setCreateDate(now);
			param.setLastUpdate(now);
			param.setTenancyId(AppConstants.TENANCY_UNRELATED_TENANCY_ID);
			domainMapper.insert(param);
			return BeanConverter.convert(param);
		}
		else{
			throw new AppException(InfoName.BAD_REQUEST, UniBundle.getMsg("common.entity.code.duplicate", domainCode, "域"));
		}
	}

	public PageDto<DomainDto> searchDomain(List<Integer> domainIds, Integer domainId, String domainCode, String displayName, Byte status, String description, Integer pageNumber, Integer pageSize) {
		CheckEmpty.checkEmpty(pageNumber, "pageNumber");
		CheckEmpty.checkEmpty(pageSize, "pageSize");
		DomainExample domainExample = new DomainExample();
		DomainExample.Criteria criteria = domainExample.createCriteria();
		domainExample.setOrderByClause("status asc");
		domainExample.setPageOffSet(pageNumber * pageSize);
		domainExample.setPageSize(pageSize);
		if(domainIds != null) {
			criteria.andIdIn(domainIds);
		}
		if(domainId != null) {
			criteria.andIdEqualTo(domainId);
		}
		if(!StringUtils.isEmpty(domainCode)) {
			criteria.andCodeEqualTo(domainCode);
		}
		if(!StringUtils.isEmpty(displayName)) {
			criteria.andDisplayNameLike("%" + displayName + "%");
		}
		if(status != null) {
			criteria.andStatusEqualTo(status);
		}
		if(!StringUtils.isEmpty(description)) {
			criteria.andDescriptionLike("%" + description + "%");
		}
		criteria.andTenancyIdEqualTo(AppConstants.TENANCY_UNRELATED_TENANCY_ID);

		int count = domainMapper.countByExample(domainExample);
		ParamCheck.checkPageParams(pageNumber, pageSize, count);
		List<Domain> domains = domainMapper.selectByExample(domainExample);
		if(!CollectionUtils.isEmpty(domains)) {
			List<DomainDto> domainDtos = new ArrayList<>();
			for(Domain domain : domains) {
				domainDtos.add(BeanConverter.convert(domain));
			}
			return new PageDto<>(pageNumber,pageSize,count,domainDtos);
		} else {
			return null;
		}
	}

	public List<StakeholderDto> getAllStakeholdersInDomain(Integer domainId) {
		if(domainId != null) {
			StakeholderExample stakeholderExample = new StakeholderExample();
			stakeholderExample.createCriteria().andDomainIdEqualTo(domainId).andTenancyIdEqualTo(AppConstants.TENANCY_UNRELATED_TENANCY_ID);
			List<Stakeholder> stakeholders = stakeholderMapper.selectByExample(stakeholderExample);
			List<StakeholderDto> stakeholderDtos = new ArrayList<>();
			if(stakeholders != null) {
				for(Stakeholder stakeholder:stakeholders) {
					stakeholderDtos.add(BeanConverter.convert(stakeholder));
				}
			}
			return stakeholderDtos;
		}
		return null;
	}

	public List<DomainDto> getAllLoginDomains(DomainParam domainParam) {
		List<String> domainCodeList = domainParam.getDomainCodeList();
		DomainExample example = new DomainExample();
		DomainExample.Criteria criteria=  example.createCriteria();
		criteria.andStatusEqualTo(AppConstants.ZERO_BYTE);
		if(domainCodeList != null){
			criteria.andCodeIn(domainCodeList);
		}
		criteria.andTenancyIdEqualTo(AppConstants.TENANCY_UNRELATED_TENANCY_ID);
		
		List<Domain> domainList = domainMapper.selectByExample(example);
		List<DomainDto> domainDtoList = new ArrayList<DomainDto>();
		if(domainList != null){
			for(Domain domain : domainList){
				domainDtoList.add(BeanConverter.convert(domain));
			}
		}
		
		return domainDtoList;
	}

	public DomainDto getDomainInfo(PrimaryKeyParam primaryKeyParam) {
		CheckEmpty.checkParamId(primaryKeyParam, "域ID");
		Integer domainId = primaryKeyParam.getId();
		Domain domain = checkDomain(domainId);
		
		StakeholderExample stakeholderExample = new StakeholderExample();
		stakeholderExample.createCriteria().andDomainIdEqualTo(domainId).andTenancyIdEqualTo(AppConstants.TENANCY_UNRELATED_TENANCY_ID);
		List<Stakeholder> stakeHolderList = stakeholderMapper.selectByExample(stakeholderExample);
		List<StakeholderDto> stakeholderDtoList = new ArrayList<StakeholderDto>();
		for(Stakeholder stakeholder : stakeHolderList){
			stakeholderDtoList.add(BeanConverter.convert(stakeholder));
		}
		
		DomainDto domainDto = BeanConverter.convert(domain);
		domainDto.setStakeholderList(stakeholderDtoList);
		
		return domainDto;
	}

	@Transactional
	public void updateDomain(DomainParam domainParam) {
		if(domainParam == null || domainParam.getId() == null){
			throw new AppException(InfoName.BAD_REQUEST, UniBundle.getMsg("common.parameter.empty", "域ID"));
		}
		
		if((domainParam.getStatus() != null && domainParam.getStatus() == AppConstants.ZERO_BYTE)|| domainParam.getStatus() == null){
			//如果需要更新code,则加入判断
			if(!StringUtil.strIsNullOrEmpty(domainParam.getCode())){
				//检查code
				dataFilter.updateFieldCheck(domainParam.getId(), FieldType.FIELD_TYPE_CODE, domainParam.getCode());
			}
		}
				
		Domain param = BeanConverter.convert(domainParam);
		param.setLastUpdate(new Date());
		param.setTenancyId(AppConstants.TENANCY_UNRELATED_TENANCY_ID);
		domainMapper.updateByPrimaryKeySelective(param);
//		domainMapper.updateByPrimaryKey(param);
	}

	@Transactional
	public StakeholderDto addNewStakeholder(StakeholderParam stakeholderParam) {
		Integer domainId = stakeholderParam.getDomainId();
		
		if(domainId != null){
			//必须要合法的数据才能插入
			dataFilter.addFieldCheck( FilterType.FILTER_TYPE_NO_DATA, FieldType.FIELD_TYPE_ID, domainId);
		} else {
			throw new AppException(InfoName.BAD_REQUEST, UniBundle.getMsg("common.parameter.empty", "域相关人ID"));
		}
		Stakeholder stakeholder = BeanConverter.convert(stakeholderParam,false);
		stakeholder.setTenancyId(AppConstants.TENANCY_UNRELATED_TENANCY_ID);
		stakeholderMapper.insert(stakeholder);
		StakeholderDto stakeholderDto = BeanConverter.convert(stakeholder);
		return stakeholderDto;
	}

	@Transactional
	public void updateStakeholder(StakeholderParam stakeholderParam) {
		if(stakeholderParam == null || stakeholderParam.getId() == null){
			throw new AppException(InfoName.BAD_REQUEST, UniBundle.getMsg("common.parameter.empty", "域相关人ID"));
		}
		
		Stakeholder param = BeanConverter.convert(stakeholderParam,true);
		stakeholderMapper.updateByPrimaryKeySelective(param);
	}

	@Transactional
	public void deleteStakeholder(PrimaryKeyParam primaryKeyParam) {
		CheckEmpty.checkParamId(primaryKeyParam, "域相关人ID");
		stakeholderMapper.deleteByPrimaryKey(primaryKeyParam.getId());
	}
	
	private Domain checkDomain(Integer domainId){
		Domain domain = domainMapper.selectByPrimaryKey(domainId);
		if(domain == null){
			throw new AppException(InfoName.BAD_REQUEST, UniBundle.getMsg("common.entity.notfound", String.valueOf(domainId), "域"));
		}
		if(domain.getStatus() == 1){
			throw new AppException(InfoName.BAD_REQUEST, UniBundle.getMsg("common.entity.status.isone", String.valueOf(domainId), "域"));
		}
		return domain;
	}
}
