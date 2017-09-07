package com.dianrong.common.uniauth.server.synchronous.hr.support;

import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.server.synchronous.exp.FileLoadFailureException;
import com.dianrong.common.uniauth.server.synchronous.support.FileLoader;
import com.dianrong.common.uniauth.server.synchronous.support.SFTPConnectionManager;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * 模糊匹配FTP文件的名称的加载器.
 */

@Slf4j @Component public class FuzzyMatchSFtpFileNameLoader implements FileLoader {
  /**
   * 精确到DATE.
   */
  private static final String DATE_FORMAT = "yyyyMMdd";

  private SFTPConnectionManager sftpConnectionManager;

  /**
   * 前缀名和时间字符串的连接名.
   */
  private String connectionSymbol = "_";

  /**
   * 是否忽略大小写.
   */
  private boolean ignoreCase = true;

  @Autowired public FuzzyMatchSFtpFileNameLoader(SFTPConnectionManager sftpConnectionManager) {
    Assert.notNull(sftpConnectionManager);
    this.sftpConnectionManager = sftpConnectionManager;
  }


  /**
   * 加载获取文件的输入流.
   * @param file 需要再加的文件的模糊名称.
   * @throws FileLoadFailureException 加载异常.
   */
  @Override
  public InputStream loadFile(String file) throws FileLoadFailureException {
    Assert.notNull(file);
    ChannelSftp channelSftp = null;
    try {
      channelSftp = this.sftpConnectionManager.getSftpClient();
      String fileName = computeFileName(channelSftp, file);
      if (fileName == null) {
        throw new FileLoadFailureException(
            "File name start with " + getFileDateStr(file) + " is not exist!");
      }
      return channelSftp.get(fileName);
    } catch (SftpException e) {
      log.error("Failed to load file " + file + " from sftp server", e);
    } finally {
      this.sftpConnectionManager.returnSftpClient(channelSftp);
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
    ChannelSftp channelSftp = null;
    ByteArrayOutputStream baos = null;
    try {
      channelSftp = this.sftpConnectionManager.getSftpClient();
      String fileName = computeFileName(channelSftp, file);
      if (fileName == null) {
        throw new FileLoadFailureException(
            "File name start with " + getFileDateStr(file) + " is not exist!");
      }
      baos = new ByteArrayOutputStream();
      channelSftp.get(fileName, baos);
      return baos.toString("UTF-8");
    } catch (SftpException | UnsupportedEncodingException e) {
      log.error("Failed to load file " + file + " from sftp server", e);
    } finally {
      try {
        this.sftpConnectionManager.returnSftpClient(channelSftp);
        if (baos != null) {
          baos.flush();
          baos.close();
        }
      } catch (IOException ioe){
        log.warn("Failed to disconnect from SFTP server!", ioe);
      }
    }
    throw new FileLoadFailureException(file + " load failed");
  }

  /**
   * 计算需要load的文件名.
   * @param channelSftp 外部传入的sftp服务器连接,不能为空.
   * @param file 文件的模糊名称.
   * @return 一个在FTP服务器上存在的文件的名称. 如果找不到,则返回Null.
   */
  private String computeFileName(ChannelSftp channelSftp, String file) {
    final String fileDateStr = getFileDateStr(file);
    final SingleValueHolder<List<String>> holder = new SingleValueHolder<>(new ArrayList<String>(1));
    try {
      channelSftp.ls(".", new ChannelSftp.LsEntrySelector() {
        @Override public int select(ChannelSftp.LsEntry entry) {
          String fileName = entry.getFilename();
          if (fileName != null) {
            String fileNamePrefix = fileDateStr;
            if (ignoreCase) {
              fileName = fileName.toLowerCase();
              fileNamePrefix = fileNamePrefix.toLowerCase();
            }
            if (fileName.startsWith(fileNamePrefix)) {
              holder.value.add(entry.getFilename());
              return ChannelSftp.LsEntrySelector.BREAK;
            }
          }
          return ChannelSftp.LsEntrySelector.CONTINUE;
        }
      });
    } catch (SftpException e) {
      log.error("Failed ls", e);
    }
    List<String> value = holder.value;
    if (value.isEmpty()) {
      return null;
    }

    if (value.size() > 1) {
      // 选取更近的数据
      Collections.sort(value, new Comparator<String>() {
        @Override public int compare(String o1, String o2) {
          return o2.compareTo(o1);
        }
      });
    }
    return holder.value.get(0);
  }

  /**
   * 辅助类.临时存放一下对象.
   */
  private static final class SingleValueHolder<T> {
    private T value;

    public SingleValueHolder(){}
    public <E extends T>SingleValueHolder(E value){
      this.value = value;
    }
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

  public SFTPConnectionManager getSftpConnectionManager() {
    return sftpConnectionManager;
  }

  public void setSftpConnectionManager(SFTPConnectionManager sftpConnectionManager) {
    Assert.notNull(sftpConnectionManager);
    this.sftpConnectionManager = sftpConnectionManager;
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
