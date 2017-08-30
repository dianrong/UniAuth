package com.dianrong.common.uniauth.server.synchronous.hr.support;

import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.server.synchronous.exp.FileLoadFailureException;
import com.dianrong.common.uniauth.server.synchronous.support.FTPConnectionManager;
import com.dianrong.common.uniauth.server.synchronous.support.FileLoader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.http.client.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

/**
 * 模糊匹配FTP文件的名称的加载器.
 */

@Slf4j
@Component
public class FuzzyMatchFtpFileNameLoader implements FileLoader {
  /**
   * 精确到DATE.
   */
  private static final String DATE_FORMAT = "yyyyMMdd";

  private FTPConnectionManager ftpConnectionManager;

  /**
   * 前缀名和时间字符串的连接名.
   */
  private String connectionSymbol = "_";

  /**
   * 是否忽略大小写.
   */
  private boolean ignoreCase = true;

  @Autowired
  public FuzzyMatchFtpFileNameLoader(FTPConnectionManager ftpConnectionManager) {
    Assert.notNull(ftpConnectionManager);
    this.ftpConnectionManager = ftpConnectionManager;
  }


  /**
   * 加载获取文件的输入流.
   * @param file 需要再加的文件的模糊名称.
   * @throws FileLoadFailureException 加载异常.
   */
  @Override
  public InputStream loadFile(String file) throws FileLoadFailureException {
    Assert.notNull(file);
    ByteArrayInputStream bais = null;
    ByteArrayOutputStream baos = null;
    FTPClient ftpClient = null;
    try {
      ftpClient = this.ftpConnectionManager.getFTPClient();
      String fileName = computeFileName(ftpClient, file);
      baos = new ByteArrayOutputStream();
      ftpClient.retrieveFile(fileName, baos);
      bais = new ByteArrayInputStream(baos.toByteArray());
      return bais;
    } catch (Exception e) {
      log.error("Failed to load FTP server file content", e);
    } finally {
      try {
        this.ftpConnectionManager.returnFtpClient(ftpClient);
        if (bais != null) {
          bais.close();
        }
        if (baos != null) {
          baos.flush();
          baos.close();
        }
      } catch (IOException ioe){
        log.warn("Failed to disconnect from FTP server!", ioe);
      }
    }
    throw new FileLoadFailureException(file + " load failed");
  }

  /**
   * 加载获取文件的输入内容.
   * @param file 需要再加的文件的模糊名称.
   * @throws FileLoadFailureException 加载异常.
   */
  @Override
  public String loadFileContent(String file) throws FileLoadFailureException {
    FTPClient ftpClient = null;
    ByteArrayOutputStream baos = null;
    try {
      ftpClient = this.ftpConnectionManager.getFTPClient();
      String fileName = computeFileName(ftpClient, file);
      ftpClient.retrieveFile(fileName, baos);
      return baos.toString("UTF-8");
    } catch (Exception e) {
      log.error("Failed to loadFile: " + file, e);
    } finally {
      try {
        this.ftpConnectionManager.returnFtpClient(ftpClient);
        if (baos != null) {
          baos.flush();
          baos.close();
        }
      } catch (IOException ioe){
        log.warn("Failed to disconnect from FTP server!", ioe);
      }
    }
    throw new FileLoadFailureException(file + " load failed");
  }

  /**
   * 计算需要load的文件名.
   * @param ftpClient 外部出入的FTPClient,不能为空.
   * @param file 文件的模糊名称.
   * @return 一个在FTP服务器上存在的文件的名称. 如果找不到,则返回Null.
   */
  private String computeFileName(FTPClient ftpClient, String file) throws IOException {
    FTPFile[] ftpFiles = ftpClient.listDirectories();
    String fileDateStr = getFileDateStr(file);
    for(FTPFile ftpFile:ftpFiles) {
      String fileName = ftpFile.getName();
      // 不区分大小写,统一转化为小写处理.
      if (ignoreCase) {
        fileName = fileName.toLowerCase();
        fileDateStr = fileDateStr.toLowerCase();
      }
      if (fileName.startsWith(fileDateStr)) {
        return fileName;
      }
    }
    // 未找到符合条件的文件名称
    return null;
  }

  /**
   * 返回文件的名字前缀.
   * @return 例如字符串:LE_UA_20170824.
   */
  protected String getFileDateStr(String fileName){
    StringBuilder sb = new StringBuilder();
    sb.append(fileName.trim()).append(this.connectionSymbol).append(getDateStr());
    return sb.toString();
  }

  /**
   * 获取时间字符串的日期部分.<br>
   * @return 20170827这种字符串.
   */
  protected String getDateStr() {
    Calendar calendar = Calendar.getInstance();
    // 计算前一天的日期
    calendar.add(Calendar.DAY_OF_YEAR, -1);
    return DateUtils.formatDate(calendar.getTime(), DATE_FORMAT);
  }

  public FTPConnectionManager getFtpConnectionManager() {
    return ftpConnectionManager;
  }

  public void setFtpConnectionManager(FTPConnectionManager ftpConnectionManager) {
    Assert.notNull(ftpConnectionManager);
    this.ftpConnectionManager = ftpConnectionManager;
  }

  public String getConnectionSymbol() {
    return connectionSymbol;
  }

  public void setConnectionSymbol(String connectionSymbol) {
    Assert.notNull(connectionSymbol);
    this.connectionSymbol = connectionSymbol;
  }

  public boolean isIgnoreCase() {
    return ignoreCase;
  }

  public void setIgnoreCase(boolean ignoreCase) {
    this.ignoreCase = ignoreCase;
  }
}
