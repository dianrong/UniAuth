package com.dianrong.common.uniauth.server.service;

import com.dianrong.common.uniauth.common.bean.InfoName;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.dto.TagDto;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.server.data.entity.*;
import com.dianrong.common.uniauth.server.data.mapper.GrpTagMapper;
import com.dianrong.common.uniauth.server.data.mapper.TagMapper;
import com.dianrong.common.uniauth.server.data.mapper.UserTagMapper;
import com.dianrong.common.uniauth.server.exp.AppException;
import com.dianrong.common.uniauth.server.util.BeanConverter;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.dianrong.common.uniauth.server.util.ParamCheck;
import com.dianrong.common.uniauth.server.util.UniBundle;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Arc on 7/4/2016.
 */
@Service
public class TagService {
    @Autowired
    private GrpTagMapper grpTagMapper;
    @Autowired
    private UserTagMapper userTagMapper;
    @Autowired
    private TagMapper tagMapper;

    public PageDto<TagDto> searchTags(Integer tagId, List<Integer> tagIds, String tagCode, Byte tagStatus,
                                      Integer domainId, Long userId, Integer grpId, Integer pageNumber,
                                      Integer pageSize) {
        CheckEmpty.checkEmpty(pageNumber, "pageNumber");
        CheckEmpty.checkEmpty(pageSize, "pageSize");
        TagExample tagExample = new TagExample();
        tagExample.setOrderByClause("status asc");
        tagExample.setPageOffSet(pageNumber * pageSize);
        tagExample.setPageSize(pageSize);
        TagExample.Criteria criteria = tagExample.createCriteria();
        if(tagId != null) {
            criteria.andIdEqualTo(tagId);
        }
        if(!CollectionUtils.isEmpty(tagIds)) {
            criteria.andIdIn(tagIds);
        }
        if(!StringUtils.isEmpty(tagCode)) {
            criteria.andCodeEqualTo(tagCode);
        }
        if(tagStatus != null) {
            criteria.andStatusEqualTo(tagStatus);
        }
        if(domainId != null) {
            criteria.andDomainIdEqualTo(domainId);
        }


        if(userId != null) {
            UserTagExample userTagExample = new UserTagExample();
            userTagExample.createCriteria().andUserIdEqualTo(userId);
            List<UserTagKey> userTagKeys = userTagMapper.selectByExample(userTagExample);
            if(!CollectionUtils.isEmpty(userTagKeys)) {
                List<Integer> tagIdsQueryByUserId = new ArrayList<>();
                for(UserTagKey userTagKey : userTagKeys) {
                    tagIdsQueryByUserId.add(userTagKey.getTagId());
                }
                criteria.andIdIn(tagIdsQueryByUserId);
            }
        }

        if(grpId != null) {
            GrpTagExample grpTagExample = new GrpTagExample();
            grpTagExample.createCriteria().andGrpIdEqualTo(grpId);
            List<GrpTagKey> grpTagKeys = grpTagMapper.selectByExample(grpTagExample);
            if(!CollectionUtils.isEmpty(grpTagKeys)) {
                List<Integer> tagIdsQueryByGrpId = new ArrayList<>();
                for(GrpTagKey grpTagKey : grpTagKeys) {
                    tagIdsQueryByGrpId.add(grpTagKey.getTagId());
                }
                criteria.andIdIn(tagIdsQueryByGrpId);
            }
        }

        List<Tag> tags = tagMapper.selectByExample(tagExample);
        if(!CollectionUtils.isEmpty(tags)) {
            int count = tagMapper.countByExample(tagExample);
            List<TagDto> tagDtos = new ArrayList<>();
            for(Tag tag : tags) {
                tagDtos.add(BeanConverter.convert(tag));
            }
            return new PageDto<>(pageNumber,pageSize,count,tagDtos);
        } else {
            return null;
        }
    }

    public TagDto addNewTag(String code, Integer domainId) {
        CheckEmpty.checkEmpty(domainId, "domainId");
        CheckEmpty.checkEmpty(code, "code");
        Tag tag = new Tag();
        Date now = new Date();
        tag.setCreateDate(now);
        tag.setLastUpdate(now);
        tag.setStatus(AppConstants.ZERO_Byte);
        tag.setDomainId(domainId);
        tag.setCode(code);
        tagMapper.insert(tag);
        return BeanConverter.convert(tag);
    }

    public void updateTag(Integer tagId, String code, Byte status) {
        CheckEmpty.checkEmpty(tagId, "tagId");
        Tag tag = tagMapper.selectByPrimaryKey(tagId);
        CheckEmpty.checkEmpty(tag, "tag");
        if(tag == null) {
            throw new AppException(InfoName.VALIDATE_FAIL, UniBundle.getMsg("common.entity.notfound", tagId, Tag.class.getSimpleName()));
        }

        ParamCheck.checkStatus(status);
        tag.setStatus(status);
        tag.setCode(code);

        tagMapper.updateByPrimaryKey(tag);
    }
}
