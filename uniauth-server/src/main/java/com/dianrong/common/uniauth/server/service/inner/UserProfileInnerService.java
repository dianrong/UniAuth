package com.dianrong.common.uniauth.server.service.inner;

import com.dianrong.common.uniauth.common.bean.dto.UserExtendValDto;
import com.dianrong.common.uniauth.common.util.ObjectUtil;
import com.dianrong.common.uniauth.server.data.entity.AttributeExtend;
import com.dianrong.common.uniauth.server.data.entity.UserExtendVal;
import com.dianrong.common.uniauth.server.data.entity.UserExtendValExample;
import com.dianrong.common.uniauth.server.data.mapper.UserExtendValMapper;
import com.dianrong.common.uniauth.server.model.AttributeValModel;
import com.dianrong.common.uniauth.server.service.common.TenancyBasedService;
import com.dianrong.common.uniauth.server.service.support.AtrributeDefine;
import com.dianrong.common.uniauth.server.util.BeanConverter;
import com.dianrong.common.uniauth.server.util.CheckEmpty;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户Profile操作的service实现.
 */
@Slf4j
@Service
public class UserProfileInnerService extends TenancyBasedService {

  @Autowired
  private UserExtendValInnerService userExtendValInnerService;

  @Autowired
  private ExtendValInnerService extendValInnerService;

  @Autowired
  private AttributeExtendInnerService attributeExtendInnerService;

  @Autowired
  private UserExtendValMapper userExtendValMapper;

  /**
   * 更新用户的扩展属性值.
   */
  @Transactional
  public void addOrUpdateUserAttributes(Long userId, Map<String, String> attributes) {
    CheckEmpty.checkEmpty(userId, "userId");
    if (attributes != null && !attributes.isEmpty()) {
      for (Entry<String, String> entry : attributes.entrySet()) {
        String attributeCode = entry.getKey();
        String value = entry.getValue();
        AttributeExtend attributeExtend =
            attributeExtendInnerService.addAttributeExtendIfNonExistent(attributeCode, null);
        addOrUpdate(userId, attributeExtend.getId(), value);
      }
    }
  }

  /**
   * 更新用户的扩展属性.
   */
  @Transactional
  public void addOrUpdateUserProfile(Long uniauthId, Map<String, AttributeValModel> attributes) {
    CheckEmpty.checkEmpty(uniauthId, "uniauthId");
    if (attributes != null && !attributes.isEmpty()) {
      for (Entry<String, AttributeValModel> entry : attributes.entrySet()) {
        String attributeCode = entry.getKey();
        AttributeValModel attributeVal = entry.getValue();
        // attributes 中的属性如果不存在则需要先添加
        AttributeExtend attributeExtend = attributeExtendInnerService
            .addAttributeExtendIfNonExistent(attributeCode, attributeVal);
        String value = attributeVal != null ? attributeVal.getValue() : null;
        // 判断如果是System定义的Code,则需要继续更新系统表中相应的数据
        AtrributeDefine sysUserAtrributeDefine =
            AtrributeDefine.getSystemDefineUserAttribute(attributeCode);
        if (sysUserAtrributeDefine != null) {
          // 系统预定义的扩展属性. 比如User表,UserDetail表中定义好的属性.
          if (sysUserAtrributeDefine.isWritable()) {
            extendValInnerService.addOrUpdateSystemDefineAttribute(uniauthId,
                sysUserAtrributeDefine.getDefineTable().getIdentityFieldName(),
                sysUserAtrributeDefine.getDefineTable().getTableName(),
                sysUserAtrributeDefine.getFieldName(),
                sysUserAtrributeDefine.getTypeTranslater().toRealType(value),
                sysUserAtrributeDefine.getDefineTable().isUpdateAttributeCheck());
            // 更新扩展属性表中的相应字段
            addOrUpdate(uniauthId, attributeExtend.getId(), value);
          } else {
            log.debug("System define attribute {} is not writable, so update just ignore!");
          }
        } else {
          // 普通扩展属性
          addOrUpdate(uniauthId, attributeExtend.getId(), value);
        }
      }
    }
  }

  /**
   * 添加或者更新用户属性.
   */
  private UserExtendValDto addOrUpdate(Long userId, Long extendId, String value) {
    CheckEmpty.checkEmpty(userId, "userId");
    CheckEmpty.checkEmpty(extendId, "extendId");
    UserExtendValExample userExtendValExample = new UserExtendValExample();
    UserExtendValExample.Criteria criteria = userExtendValExample.createCriteria();
    criteria.andUserIdEqualTo(userId).andExtendIdEqualTo(extendId)
        .andTenancyIdEqualTo(tenancyService.getTenancyIdWithCheck());
    List<UserExtendVal> existUserExtendVal =
        userExtendValMapper.selectByExample(userExtendValExample);
    UserExtendVal record;
    if (ObjectUtil.collectionIsEmptyOrNull(existUserExtendVal)) {
      // add
      record = userExtendValInnerService.addNew(userId, extendId, value);
    } else {
      // update
      userExtendValInnerService.update(userId, extendId, value);
      record = userExtendValInnerService.queryByUserIdAndExtendId(userId, extendId);
    }
    return BeanConverter.convert(record, UserExtendValDto.class);
  }
}
