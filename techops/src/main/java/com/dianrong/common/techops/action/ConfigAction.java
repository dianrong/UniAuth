package com.dianrong.common.techops.action;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.ConfigDto;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.request.CfgParam;
import com.dianrong.common.uniauth.common.bean.request.PrimaryKeyParam;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.sharerw.facade.UARWFacade;
import java.io.IOException;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Arc on 28/3/2016.
 */
@RestController
@RequestMapping("cfg")
public class ConfigAction {

  @Resource
  private UARWFacade uarwFacade;

  @RequestMapping(value = "/query", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('ROLE_SUPER_ADMIN') and principal.permMap['DOMAIN'] != null "
      + "and principal.permMap['DOMAIN'].contains('techops')")
  public Response<PageDto<ConfigDto>> queryConfig(@RequestBody CfgParam cfgParam) {
    return uarwFacade.getConfigRWResource().queryConfig(cfgParam);
  }

  /**
   * 添加或更新配置.
   */
  @RequestMapping(value = "/add-or-update", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('ROLE_SUPER_ADMIN') and principal.permMap['DOMAIN'] != null "
      + "and principal.permMap['DOMAIN'].contains('techops')")
  public Response<ConfigDto> addOrUpdateConfig(
      @RequestParam(name = "id", required = false) Integer id,
      @RequestParam("cfgKey") String cfgKey, @RequestParam("cfgTypeId") Integer cfgTypeId,
      @RequestParam(name = "value", required = false) String value,
      @RequestParam(name = "file", required = false) MultipartFile file) throws IOException {
    CfgParam cfgParam = new CfgParam();
    cfgParam.setId(id).setCfgKey(cfgKey).setCfgTypeId(cfgTypeId).setValue(value);
    if (file != null) {
      cfgParam.setFile(file.getBytes());
    }
    return uarwFacade.getConfigRWResource().addOrUpdateConfig(cfgParam);
  }

  /**
   * 获取配置.
   */
  @RequestMapping(value = "/download/{cfgKey}", method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public Response<?> download(@PathVariable("cfgKey") String cfgKey, HttpServletResponse response,
      HttpServletRequest request) throws IOException {
    CfgParam cfgParam = new CfgParam().setCfgKey(cfgKey);
    cfgParam.setNeedBLOBs(Boolean.TRUE);
    cfgParam.setPageNumber(0);
    cfgParam.setPageSize(AppConstants.MAX_PAGE_SIZE);
    Response<PageDto<ConfigDto>> configDtoResponse =
        uarwFacade.getConfigRWResource().queryConfig(cfgParam);
    if (configDtoResponse.getInfo() != null) {
      return Response.success(configDtoResponse);
    } else {
      PageDto<ConfigDto> result = configDtoResponse.getData();
      List<ConfigDto> configDtos = result == null ? null : result.getData();
      if (("TECHOPS_ICON".equals(cfgKey) || "TECHOPS_LOGO".equals(cfgKey))
          && (CollectionUtils.isEmpty(configDtos) || configDtos.get(0).getFile() == null)) {
        // if there are no favicon and logo pictures configured, then use the default one.
        String contextPath = request.getContextPath();
        String staticFaviconPath;
        String staticLogoPath;
        if ("/".equals(contextPath)) {
          staticFaviconPath = "/images/dianrong_favicon.ico";
          staticLogoPath = "/images/dianrong+logo.png";
        } else {
          staticFaviconPath = contextPath + "/images/dianrong_favicon.ico";
          staticLogoPath = contextPath + "/images/dianrong+logo.png";
        }
        if ("TECHOPS_ICON".equals(cfgKey)) {
          response.sendRedirect(staticFaviconPath);
        } else if ("TECHOPS_LOGO".equals(cfgKey)) {
          response.sendRedirect(staticLogoPath);
        }
        return Response.success();
      }

      if (!CollectionUtils.isEmpty(configDtos)) {
        ConfigDto cfgDto = configDtos.get(0);
        byte[] file = cfgDto.getFile();
        if (file != null) {
          String fileName = cfgDto.getValue();
          String mimeType = URLConnection.guessContentTypeFromName(fileName);
          if (mimeType == null) {
            mimeType = javax.ws.rs.core.MediaType.APPLICATION_OCTET_STREAM;
          }
          response.setContentType(mimeType);
          response.setHeader("Content-Disposition",
              String.format("attachment; filename=\"%s\"", fileName));
          response.setContentLength(file.length);
          response.getOutputStream().write(file);
        } else {
          // return the text cfg.
          return Response.success(cfgDto);
        }
      }
    }
    return Response.success();
  }

  @RequestMapping(value = "/cfg-types", method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('ROLE_SUPER_ADMIN') and principal.permMap['DOMAIN'] != null "
      + "and principal.permMap['DOMAIN'].contains('techops')")
  public Response<Map<Integer, String>> getAllCfgTypes() {
    return uarwFacade.getConfigRWResource().getAllCfgTypes();
  }

  @RequestMapping(value = "/del", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('ROLE_SUPER_ADMIN') and principal.permMap['DOMAIN'] != null "
      + "and principal.permMap['DOMAIN'].contains('techops')")
  public Response<Void> delConfig(@RequestBody PrimaryKeyParam primaryKeyParam) {
    return uarwFacade.getConfigRWResource().delConfig(primaryKeyParam);
  }

}
