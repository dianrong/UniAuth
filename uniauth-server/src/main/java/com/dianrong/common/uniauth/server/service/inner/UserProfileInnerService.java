package com.dianrong.common.uniauth.server.service.inner;

import com.dianrong.common.uniauth.server.data.entity.AttributeExtend;
import com.dianrong.common.uniauth.server.model.AttributeValModel;
import com.dianrong.common.uniauth.server.service.common.TenancyBasedService;
import com.dianrong.common.uniauth.server.service.support.AtrributeDefine;
import com.dianrong.common.uniauth.server.util.CheckEmpty;

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
  private AttributeExtendInnerService attributeExtendInnerService;

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
        // 判断如果是System定义的Code,则需要通过其他方式去更新
        AtrributeDefine sysUserAtrributeDefine =
            AtrributeDefine.getSystemDefineUserAttribute(attributeCode);
        if (sysUserAtrributeDefine != null) {
          // 系统预定义的扩展属性. 比如User表,UserDetail表中定义好的属性.
          if (sysUserAtrributeDefine.isWritable()) {
            userExtendValInnerService.updateSystemDefineUserAttribute(uniauthId,
                sysUserAtrributeDefine.getDefineTable().getIdentityFieldName(),
                sysUserAtrributeDefine.getDefineTable().getTableName(),
                sysUserAtrributeDefine.getFieldName(), value);
          } else {
            log.debug("System define attribute {} is not writable, so update just ignore!");
          }
        } else {
          // 普通扩展属性
          userExtendValInnerService.addOrUpdate(uniauthId, attributeExtend.getId(), value);
        }
      }
    }
  }
}
