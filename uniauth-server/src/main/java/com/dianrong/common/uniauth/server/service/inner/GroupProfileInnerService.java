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
 * 组Profile操作的service实现.
 */
@Slf4j
@Service
public class GroupProfileInnerService extends TenancyBasedService {

  @Autowired
  private GroupExtendValInnerService groupExtendValInnerService;

  @Autowired
  private AttributeExtendInnerService attributeExtendInnerService;

  /**
   * 更新组的扩展属性.
   */
  @Transactional
  public void addOrUpdateGrpProfile(Integer grpId, Map<String, AttributeValModel> attributes) {
    CheckEmpty.checkEmpty(grpId, "groupId");
    if (attributes != null && !attributes.isEmpty()) {
      for (Entry<String, AttributeValModel> entry : attributes.entrySet()) {
        String attributeCode = entry.getKey();
        AttributeValModel attributeVal = entry.getValue();
        // attributes 中的属性如果不存在则需要先添加
        AttributeExtend attributeExtend = attributeExtendInnerService
            .addAttributeExtendIfNonExistent(attributeCode, attributeVal);
        String value = attributeVal != null ? attributeVal.getValue() : null;
        // 判断如果是System定义的Code,则需要通过其他方式去更新
        AtrributeDefine sysGrpAtrributeDefine =
            AtrributeDefine.getSystemDefineGroupAttribute(attributeCode);
        if (sysGrpAtrributeDefine != null) {
          // 系统预定义的扩展属性. 比如Grp表中定义好的属性.
          if (sysGrpAtrributeDefine.isWritable()) {
            groupExtendValInnerService.updateSystemDefineGrpAttribute(grpId,
                sysGrpAtrributeDefine.getDefineTable().getIdentityFieldName(),
                sysGrpAtrributeDefine.getDefineTable().getTableName(),
                sysGrpAtrributeDefine.getFieldName(), value);
          } else {
            log.debug("System define attribute {} is not writable, so update just ignore!");
          }
        } else {
          groupExtendValInnerService.addOrUpdate(grpId, attributeExtend.getId(), value);
        }
      }
    }
  }
}
