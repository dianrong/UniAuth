package com.dianrong.common.uniauth.server.synchronous.hr.support;

import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.server.synchronous.exp.FileLoadFailureException;
import com.dianrong.common.uniauth.server.synchronous.exp.IdcClientCreateFailureException;
import com.dianrong.common.uniauth.server.synchronous.exp.InvalidContentException;
import com.dianrong.common.uniauth.server.synchronous.hr.bean.LoadContent;
import com.dianrong.common.uniauth.server.synchronous.support.FileLoader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import oracle.stellent.ridc.IdcClient;
import oracle.stellent.ridc.IdcClientException;
import oracle.stellent.ridc.IdcClientManager;
import oracle.stellent.ridc.IdcContext;
import oracle.stellent.ridc.model.DataBinder;
import oracle.stellent.ridc.model.DataObject;
import oracle.stellent.ridc.model.DataResultSet;
import oracle.stellent.ridc.protocol.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 * 加载UCM上文件内容.
 */

@Slf4j
public class FuzzyMatchUCMFileNameLoader implements FileLoader {

  // Security Group
  private static final String SECURITY_GROUP = "FAFusionImportExport";
  // Account
  private static final String DOC_ACCOUNT = "hcm$/dataloader$/export$";

  /**
   * Idc客户端.
   */
  private IdcClient idcHttpsClient;
  private IdcClientManager clientManager;

  // UCM 服务器地址.
  private String requestUrl;
  // 账户信息.
  private IdcContext userContext;

  @Autowired
  public FuzzyMatchUCMFileNameLoader(String requestUrl, String userName, String password) {
    Assert.notNull(requestUrl, "UCM request url must not be null");
    Assert.notNull(userName, "UCM userName must not be null");
    this.clientManager = new IdcClientManager();
    this.requestUrl = requestUrl;
    this.userContext = new IdcContext(userName, password);
  }

  private void insureClientReady() {
    if (idcHttpsClient != null) {
      return;
    }
    synchronized (this) {
      if (idcHttpsClient == null) {
        try {
          this.idcHttpsClient = this.clientManager.createClient(this.requestUrl);
        } catch (IdcClientException e) {
          log.error("Failed create UCM idc client", e);
          throw new IdcClientCreateFailureException("Failed create UCM idc client", e);
        }
      }
    }
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
    insureClientReady();
    try {
      UCMFileBasicInfo basicInfo = computeUCMFileBasicInfo(file);
      if (basicInfo == null) {
        throw new FileLoadFailureException("File name start with " + file + " is not exist!");
      }

      log.info("UCM start to download file {}", basicInfo.originalName);
      DataBinder dataBinder = this.idcHttpsClient.createBinder();
      dataBinder.putLocal("IdcService", "GET_FILE");
      dataBinder.putLocal("dID", basicInfo.id);
      dataBinder.putLocal("dSecurityGroup", SECURITY_GROUP);
//      dataBinder.putLocal("dDocAccount", DOC_ACCOUNT);
      dataBinder.putLocal("allowInterrupt", "1");

      // Send the request to Content Server
      ServiceResponse response = this.idcHttpsClient.sendRequest(this.userContext, dataBinder);
      return new LoadContent<InputStream>(response.getResponseStream(), basicInfo.originalName);
    } catch (IdcClientException e) {
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
      String line;
      StringBuilder sb = new StringBuilder();
      try (InputStreamReader reader = new InputStreamReader(inputStreamLoad.getContent(), "UTF-8");
          BufferedReader in = new BufferedReader(reader)) {
        while ((line = in.readLine()) != null) {
          sb.append(line).append("\r\n");
        }
      } catch (IOException e) {
        throw new InvalidContentException("Failed read content from InputStream", e);
      }
      inputStreamLoad.getContent().close();
      return new LoadContent<String>(sb.toString(), inputStreamLoad.getSourceName());
    } catch (IOException e) {
      log.error("Failed to load file " + file + " from UCM server", e);
    }
    throw new FileLoadFailureException(file + " load failed");
  }

  /**
   * 通过访问服务器获取需要加载文件的基础信息.
   *
   * @param fileNamePrefix 文件名称的开头.
   * @return 找到对应的文件, 并获取其基本信息并返回. 如果找不到,则返回Null.
   */
  private UCMFileBasicInfo computeUCMFileBasicInfo(final String fileNamePrefix) {
    try {
      DataBinder dataBinder = this.idcHttpsClient.createBinder();
      dataBinder.putLocal("IdcService", "GET_SEARCH_RESULTS");
      StringBuilder queryText = new StringBuilder();
      queryText.append("dDocTitle <starts> `").append(fileNamePrefix).append("`");
      queryText.append(" <AND> ");
      queryText.append("dSecurityGroup <matches> `").append(SECURITY_GROUP).append("`");
//      queryText.append(" <AND> ");
//      queryText.append("dDocAccount <matches> `").append(DOC_ACCOUNT).append("`");
      dataBinder.putLocal("QueryText", queryText.toString());
      log.info("UCM file loader:searching - data binding completed!");
      final ServiceResponse response = this.idcHttpsClient
          .sendRequest(this.userContext, dataBinder);
      log.info("UCM file loader:searching - start creating response!");
      DataBinder responseData = response.getResponseAsBinder();
      DataResultSet resultSet = responseData.getResultSet("SearchResults");

      List<UCMFileBasicInfo> basicInfoList = null;
      if (resultSet != null && !StringUtils.isEmpty(resultSet.getRows())) {
        basicInfoList = new ArrayList<>(resultSet.getRows().size());
        for (DataObject dataObject : resultSet.getRows()) {
          String dID = dataObject.get("dID");
          if (StringUtils.hasText(dID)) {
            basicInfoList
                .add(new UCMFileBasicInfo(dID,
                    dataObject.get("dOriginalName"),
                    dataObject.get("dDocCreatedDate"),
                    dataObject.get("dDocLastModifiedDate")));
          }
        }
      }
      if (StringUtils.isEmpty(basicInfoList)) {
        return null;
      }
      if (basicInfoList.size() > 1) {
        Collections.sort(basicInfoList, new Comparator<UCMFileBasicInfo>() {
          @Override
          public int compare(UCMFileBasicInfo o1, UCMFileBasicInfo o2) {
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
    } catch (IdcClientException e) {
      log.error("Failed to get UCM file basic info.message:" + e.getMessage(), e);
    }
    return null;
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
