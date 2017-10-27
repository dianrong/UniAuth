package com.dianrong.common.uniauth.server.synchronous.hr.support;

import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.server.service.attributerecord.exp.InvalidParameterTypeException;
import com.dianrong.common.uniauth.server.synchronous.exp.FileLoadFailureException;
import com.dianrong.common.uniauth.server.synchronous.exp.InvalidContentException;
import com.dianrong.common.uniauth.server.synchronous.hr.bean.LoadContent;
import com.dianrong.common.uniauth.server.synchronous.support.FileLoader;
import com.oracle.ucm.Field;
import com.oracle.ucm.Generic;
import com.oracle.ucm.ResultSet;
import com.oracle.ucm.Row;
import com.oracle.ucm.Service;
import genericsoap.GenericSoapPortType;
import genericsoap.GenericSoapService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * 通过WSDL访问UCM,加载同步文件.
 */

@Slf4j
public class FuzzyMatchUCMWsdlFileNameLoader implements FileLoader {

  // Security Group
  private static final String SECURITY_GROUP = "FAFusionImportExport";
  // Account
  private static final String DOC_ACCOUNT = "hcm$/dataloader$/export$";

  /**
   * 请求UCM服务器配置.
   */
  private final String requestUrl;
  private final String account;
  private final String password;

  public FuzzyMatchUCMWsdlFileNameLoader(String requestUrl, String account, String password) {
    Assert.notNull(requestUrl, "UCM request url must not be null");
    Assert.notNull(account, "UCM account must not be null");
    this.requestUrl = requestUrl;
    this.account = account;
    this.password = password;
  }

  /**
   * 加载获取文件的输入流.
   *
   * @param file 需要再加的文件的模糊名称.
   * @throws FileLoadFailureException 加载异常.
   */
  @Override
  public LoadContent<InputStream> loadFile(String file) throws FileLoadFailureException {
    Assert.notNull(file, "FileName can not be null");
    final GenericSoapPortType genericSoapPortType = createGenericSoapPortType();
    try {
      UCMFileBasicInfo basicInfo = computeUCMFileBasicInfo(file, genericSoapPortType);
      if (basicInfo == null) {
        throw new FileLoadFailureException("File name start with " + file + " is not exist!");
      }
      log.info("UCM start to download file {}", basicInfo.originalName);
      // 构造请求参数
      Generic getFileRequest = new Generic();
      getFileRequest.setWebKey("cs");
      Service getFileService = new Service();
      getFileService.setIdcService("GET_FILE");
      Service.Document fileDoc = new Service.Document();
      Field field = new Field();
      field.setName("dID");
      field.setValue(basicInfo.id);
      fileDoc.getField().add(field);
      getFileService.setDocument(fileDoc);
      getFileRequest.setService(getFileService);

      // Request
      Generic genericResponse = genericSoapPortType.genericSoapOperation(getFileRequest);

      List<com.oracle.ucm.File> fileList = genericResponse.getService().getDocument().getFile();
      if (fileList.size() == 1) {
        return new LoadContent<InputStream>(fileList.get(0).getContents().getInputStream(),
            basicInfo.originalName);
      }
    } catch (FileLoadFailureException ffe) {
      log.error("Failed to load file " + file + " from UCM server:" + ffe.getMessage(), ffe);
      throw ffe;
    } catch (Exception e) {
      log.error("Failed to load file " + file + " from UCM server:" + e.getMessage(), e);
    }
    throw new FileLoadFailureException(file + " load failed");
  }

  /**
   * 加载获取文件的输入内容.
   *
   * @param file 需要再加的文件的模糊名称.
   * @throws FileLoadFailureException 加载异常.
   */
  @Override
  public LoadContent<String> loadFileContent(String file)
      throws FileLoadFailureException {
    try {
      LoadContent<InputStream> inputStreamLoad = loadFile(file);
      try {
        String line;
        StringBuilder sb = new StringBuilder();
        try (InputStreamReader reader = new InputStreamReader(inputStreamLoad.getContent(),
            "UTF-8");
            BufferedReader in = new BufferedReader(reader)) {
          while ((line = in.readLine()) != null) {
            sb.append(line).append("\r\n");
          }
        } catch (IOException e) {
          throw new InvalidContentException("Failed read content from InputStream", e);
        }
        return new LoadContent<String>(sb.toString(), inputStreamLoad.getSourceName());
      } finally {
        inputStreamLoad.getContent().close();
      }
    } catch (FileLoadFailureException ffe) {
      log.error("Failed to load file " + file + " from UCM server:" + ffe.getMessage(), ffe);
      throw ffe;
    } catch (IOException e) {
      log.error("Failed to load file " + file + " from UCM server", e);
    }
    throw new FileLoadFailureException(file + " load failed");
  }

  /**
   * 通过访问服务器获取需要加载文件的基础信息.
   *
   * @param fileNamePrefix 文件名称的开头.
   * @param genericSoapPortType UCM Wsdl连接客户端.
   * @return 找到对应的文件, 并获取其基本信息并返回. 如果找不到,则返回Null.
   */
  private UCMFileBasicInfo computeUCMFileBasicInfo(final String fileNamePrefix,
      final GenericSoapPortType genericSoapPortType) {
    try {
      // 构造请求参数.
      Generic genericRequest = new Generic();
      genericRequest.setWebKey("cs");
      Service service = new Service();
      service.setIdcService("GET_SEARCH_RESULTS");
      Service.Document doc = new Service.Document();
      StringBuilder queryText = new StringBuilder();
      queryText.append("dDocTitle <starts> `").append(fileNamePrefix).append("`");
      queryText.append(" <AND> ");
      queryText.append("dSecurityGroup <matches> `").append(SECURITY_GROUP).append("`");
//      queryText.append(" <AND> ");
//      queryText.append("dDocAccount <matches> `").append(DOC_ACCOUNT).append("`");
      Field queryTextField = new Field();
      queryTextField.setName("QueryText");
      queryTextField.setValue(queryText.toString());
      doc.getField().add(queryTextField);
      service.setDocument(doc);
      genericRequest.setService(service);

      // request start
      Generic genericResponse = genericSoapPortType.genericSoapOperation(genericRequest);

      // 请求结果解析
      Service.Document responseDoc = genericResponse.getService().getDocument();
      // Get response status
      Field status = responseDoc.getField().get(0);
      if ("Error".equals(status.getName())) {
        String msg =
            "Failed to get " + fileNamePrefix + " from server. the server response is Error.";
        log.error(msg);
        throw new FileLoadFailureException(msg);
      }
      List<ResultSet> resultSetList = responseDoc.getResultSet();
      List<UCMFileBasicInfo> basicInfoList = new ArrayList<>();
      if (!CollectionUtils.isEmpty(resultSetList)) {
        for (ResultSet resultSet : resultSetList) {
          if (!resultSet.getName().matches("SearchResults")) {
            continue;
          }
          List<Row> rowList = resultSet.getRow();
          for (Row row : rowList) {
            List<Field> colList = row.getField();
            String dID = null;
            String dOriginalName = null;
            String dDocCreatedDate = null;
            String dDocLastModifiedDate = null;
            for (Field col : colList) {
              if (col.getName().matches("dID")) {
                dID = col.getValue();
                continue;
              }
              if (col.getName().matches("dOriginalName")) {
                dOriginalName = col.getValue();
                continue;
              }
              if (col.getName().matches("dDocCreatedDate")) {
                dDocCreatedDate = col.getValue();
                continue;
              }
              if (col.getName().matches("dDocLastModifiedDate")) {
                dDocLastModifiedDate = col.getValue();
              }
            }
            if (StringUtils.hasText(dID)) {
              log.debug("Search {} from ucm server, find dID:{}", fileNamePrefix, dID);
              basicInfoList
                  .add(new UCMFileBasicInfo(dID, dOriginalName,
                      dDocCreatedDate, dDocLastModifiedDate));
            }
          }
        }
      }
      if (CollectionUtils.isEmpty(basicInfoList)) {
        return null;
      }
      if (basicInfoList.size() > 1) {
        Collections.sort(basicInfoList,
            new Comparator<FuzzyMatchUCMWsdlFileNameLoader.UCMFileBasicInfo>() {
              @Override
              public int compare(FuzzyMatchUCMWsdlFileNameLoader.UCMFileBasicInfo o1,
                  FuzzyMatchUCMWsdlFileNameLoader.UCMFileBasicInfo o2) {
                String updateDate1 = o1.lastUpdate;
                String updateDate2 = o2.lastUpdate;
                if (updateDate1 == null && updateDate2 == null) {
                  return 0;
                }
                if (updateDate1 != null && updateDate2 != null) {
                  return updateDate2.compareTo(updateDate1);
                }
                if (updateDate1 != null) {
                  return 1;
                }
                return -1;
              }
            });
      }
      return basicInfoList.get(0);
    } catch (Exception e) {
      String msg =
          "Failed to get " + fileNamePrefix + " from server. the server response is Error.";
      log.error(msg);
      throw new FileLoadFailureException(msg);
    }
  }

  /**
   * 创建客户端访问对象.
   */
  private GenericSoapPortType createGenericSoapPortType() {
    URL url;
    try {
      url = new URL(this.requestUrl);
    } catch (MalformedURLException e) {
      throw new InvalidParameterTypeException(
          "UCM Wsdl request client init failed. The url : " + requestUrl + " is a invalid url");
    }
    GenericSoapService genericSoapService = new GenericSoapService(url);
    GenericSoapPortType genericSoapPortType = genericSoapService.getGenericSoapPort();

    // Bind credentials
    BindingProvider bindingProvider = (BindingProvider) genericSoapPortType;
    Map<String, Object> requestContext = bindingProvider.getRequestContext();
    requestContext.put(BindingProvider.USERNAME_PROPERTY, this.account);
    requestContext.put(BindingProvider.PASSWORD_PROPERTY, this.password);
    return genericSoapPortType;
  }

  /**
   * 存储UCM文件的基础信息.
   */
  private static final class UCMFileBasicInfo {

    private String id;

    private String originalName;

    private String createDate;

    private String lastUpdate;

    public UCMFileBasicInfo(String id, String originalName, String createDate, String lastUpdate) {
      this.id = id;
      this.originalName = originalName;
      this.createDate = createDate;
      this.lastUpdate = lastUpdate;
    }
  }
}
