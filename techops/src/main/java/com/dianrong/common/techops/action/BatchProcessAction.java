package com.dianrong.common.techops.action;

import com.dianrong.common.techops.bean.BatchProcessResult;
import com.dianrong.common.techops.exp.BatchProcessException;
import com.dianrong.common.techops.service.BatchService;
import com.dianrong.common.uniauth.common.bean.Info;
import com.dianrong.common.uniauth.common.bean.InfoName;
import com.dianrong.common.uniauth.common.bean.Response;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 批量处理的Action.
 * @author wanglin
 */
@RestController
@RequestMapping("batch")
public class BatchProcessAction {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(BatchProcessAction.class);
  
  @Autowired
  private BatchService batchService;
  
  /**
   * 批量添加用户.
   */
  @RequestMapping(value = "add-user", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('ROLE_SUPER_ADMIN') and principal.permMap['DOMAIN'] != null "
      + "and principal.permMap['DOMAIN'].contains('techops')")
  public Response<BatchProcessResult> addUser(@RequestParam(value = "file", required = true) MultipartFile file) throws IOException {
    try {
      return Response.success(batchService.addUser(file.getInputStream()));
    } catch(BatchProcessException bpe) {
      LOGGER.error("failed to process batch add user", bpe);
      return Response.failure(Info.build(InfoName.BAD_REQUEST, bpe.getMessage()));
    }
  }
  
  /**
   * 批量禁用用户.
   */
  @RequestMapping(value = "disable-user", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('ROLE_SUPER_ADMIN') and principal.permMap['DOMAIN'] != null "
      + "and principal.permMap['DOMAIN'].contains('techops')")
  public Response<BatchProcessResult> disableUser(@RequestParam(value = "file", required = true) MultipartFile file) throws IOException {
    try {
      return Response.success(batchService.disableUser(file.getInputStream()));
    } catch(BatchProcessException bpe) {
      LOGGER.error("failed to process batch disable user", bpe);
      return Response.failure(Info.build(InfoName.BAD_REQUEST, bpe.getMessage()));
    }
  }
  
  /**
   * 批量关联用户和组.
   */
  @RequestMapping(value = "relate-user-grp", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('ROLE_SUPER_ADMIN') and principal.permMap['DOMAIN'] != null "
      + "and principal.permMap['DOMAIN'].contains('techops')")
  public Response<BatchProcessResult> relateUserGrp(@RequestParam(value = "file", required = true) MultipartFile file, @RequestParam(value = "groupCode", required = true)String groupCode) throws IOException {
    try {
      return Response.success(batchService.relateUserGrp(groupCode, file.getInputStream()));
    } catch(BatchProcessException bpe) {
      LOGGER.error("failed to process batch relate user and group", bpe);
      return Response.failure(Info.build(InfoName.BAD_REQUEST, bpe.getMessage()));
    }
  }
}
