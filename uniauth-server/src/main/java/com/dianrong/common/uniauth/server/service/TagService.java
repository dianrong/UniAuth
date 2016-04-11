package com.dianrong.common.uniauth.server.service;

import com.dianrong.common.uniauth.common.bean.InfoName;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.dto.TagDto;
import com.dianrong.common.uniauth.common.bean.dto.TagTypeDto;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.server.data.entity.*;
import com.dianrong.common.uniauth.server.data.mapper.GrpTagMapper;
import com.dianrong.common.uniauth.server.data.mapper.TagMapper;
import com.dianrong.common.uniauth.server.data.mapper.TagTypeMapper;
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
    private UserTagMapper userTagMapper;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private TagTypeMapper tagTypeMapper;
    @Autowired
    private GrpTagMapper grpTagMapper;

    public PageDto<TagDto> searchTags(Integer tagId, List<Integer> tagIds, String tagCode, Byte tagStatus,
                                      Integer tagTypeId, Long userId, Integer domainId, List<Integer> domainIds, Integer grpId,
                                      Integer pageNumber, Integer pageSize) {
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
        if(tagTypeId != null) {
            criteria.andTagTypeIdEqualTo(tagTypeId);
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
            } else {
                return null;
            }
        }

        if(domainId != null || domainIds != null) {
            List<Integer> unionDomainIds = new ArrayList<Integer>();
            if(domainId != null) {
                unionDomainIds.add(domainId);
            }
            if(domainIds != null) {
                unionDomainIds.addAll(domainIds);
            }
            TagTypeExample tagTypeExample = new TagTypeExample();
            tagTypeExample.createCriteria().andDomainIdIn(unionDomainIds);
            List<TagType> tagTypes = tagTypeMapper.selectByExample(tagTypeExample);
            if(!CollectionUtils.isEmpty(tagTypes)) {
                List<Integer> tagTypeIds = new ArrayList<>();
                for(TagType tagType:tagTypes) {
                    tagTypeIds.add(tagType.getId());
                }
                criteria.andTagTypeIdIn(tagTypeIds);
            } else {
                return null;
            }
        }

        if(grpId != null) {
            GrpTagExample grpTagExample = new GrpTagExample();
            grpTagExample.createCriteria().andGrpIdEqualTo(grpId);
            List<GrpTagKey>  grpTagKeys = grpTagMapper.selectByExample(grpTagExample);
            if(!CollectionUtils.isEmpty(grpTagKeys)) {
                List<Integer> grpTagKeysTagIds = new ArrayList<>();
                for (GrpTagKey grpTagKey : grpTagKeys) {
                    grpTagKeysTagIds.add(grpTagKey.getTagId());
                }
                criteria.andIdIn(grpTagKeysTagIds);
            } else {
                return null;
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

    public TagDto addNewTag(String code, Integer domainId, Integer tagTypeId, String description) {
        CheckEmpty.checkEmpty(domainId, "domainId");
        CheckEmpty.checkEmpty(code, "code");
        Tag tag = new Tag();
        Date now = new Date();
        tag.setCreateDate(now);
        tag.setLastUpdate(now);
        tag.setStatus(AppConstants.ZERO_Byte);
        tag.setTagTypeId(tagTypeId);
        tag.setDescription(description);
        tag.setCode(code);
        tagMapper.insert(tag);
        return BeanConverter.convert(tag);
    }

    public void updateTag(Integer tagId, String code, Byte status, Integer tagTypeId, String description) {
        CheckEmpty.checkEmpty(tagId, "tagId");
        Tag tag = tagMapper.selectByPrimaryKey(tagId);
        if(tag == null) {
            throw new AppException(InfoName.VALIDATE_FAIL, UniBundle.getMsg("common.entity.notfound", tagId, Tag.class.getSimpleName()));
        }

        ParamCheck.checkStatus(status);
        tag.setStatus(status);
        tag.setCode(code);
        tag.setTagTypeId(tagTypeId);
        tag.setDescription(description);
        tagMapper.updateByPrimaryKey(tag);
    }

    public List<TagTypeDto> getTagTypes(Integer domainId) {
        TagTypeExample tagTypeExample = new TagTypeExample();
        if(domainId != null) {
            tagTypeExample.createCriteria().andDomainIdEqualTo(domainId);
        }
        List<TagType> tagTypes = tagTypeMapper.selectByExample(tagTypeExample);
        if(!CollectionUtils.isEmpty(tagTypes)) {
            List<TagTypeDto> tagTypeDtos = new ArrayList<>();
            for(TagType tagType : tagTypes) {
                tagTypeDtos.add(BeanConverter.convert(tagType));
            }
            return tagTypeDtos;
        } else {
            return null;
        }
    }

    public TagTypeDto addNewTagType(String code, Integer domainId) {
        CheckEmpty.checkEmpty(domainId, "domainId");
        CheckEmpty.checkEmpty(code, "code");
        TagType tagType = new TagType();
        tagType.setDomainId(domainId);
        tagType.setCode(code);
        tagTypeMapper.insert(tagType);
        return BeanConverter.convert(tagType);
    }

    public void updateTagType(Integer tagTypeId, String code, Integer domainId) {
        CheckEmpty.checkEmpty(tagTypeId, "tagTypeId");
        CheckEmpty.checkEmpty(code, "code");
        TagType tagType = tagTypeMapper.selectByPrimaryKey(tagTypeId);
        if(tagType == null) {
            throw new AppException(InfoName.VALIDATE_FAIL, UniBundle.getMsg("common.entity.notfound", tagTypeId, TagType.class.getSimpleName()));
        }

        tagType.setCode(code);
        if(domainId != null) {
            tagType.setDomainId(domainId);
        }
        tagTypeMapper.updateByPrimaryKey(tagType);
    }

    public void deleteTagType(Integer tagTypeId) {
        CheckEmpty.checkEmpty(tagTypeId, "tagTypeId");
        TagType tagType = tagTypeMapper.selectByPrimaryKey(tagTypeId);
        if(tagType == null) {
            throw new AppException(InfoName.VALIDATE_FAIL, UniBundle.getMsg("common.entity.notfound", tagTypeId, TagType.class.getSimpleName()));
        }
        TagExample tagExample = new TagExample();
        tagExample.createCriteria().andTagTypeIdEqualTo(tagTypeId);
        int count = tagMapper.countByExample(tagExample);
        if(count > 0) {
            throw new AppException(InfoName.VALIDATE_FAIL, UniBundle.getMsg("tagtype.delete.linked-tag.error"));
        }
        tagTypeMapper.deleteByPrimaryKey(tagTypeId);
    }

}
