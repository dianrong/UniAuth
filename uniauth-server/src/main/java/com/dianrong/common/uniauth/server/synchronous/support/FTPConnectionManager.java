package com.dianrong.common.uniauth.server.synchronous.support;

import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.server.synchronous.exp.FTPServerProcessException;
import com.dianrong.common.uniauth.server.synchronous.exp.NoMoreFTPClientException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 处理FTP连接的管理类.
 */

@Slf4j
@Component
public class FTPConnectionManager implements InitializingBean {

  /**
   * 最多同时存在20个连接.
   */
  public static final int MAX_POOL_SIZE = 20;

  @Value("#{uniauthConfig['synchronization.hr.ftp.host']}")
  private String serverHost = "";
  /**
   * 默认端口号为22.
   */
  @Value("#{uniauthConfig['synchronization.hr.ftp.port']}")
  private int serverPort = 22;

  /**
   * 登录FTP服务器的账号和密码
   */
  @Value("#{uniauthConfig['synchronization.hr.ftp.account']}")
  private String ftpAccount = "";
  @Value("#{uniauthConfig['synchronization.hr.ftp.pwd']}")
  private String ftpPassword = "";

  /**
   * 登录FTP的base路径.
   */
  @Value("#{uniauthConfig['synchronization.hr.ftp.base.directory']}")
  private String baseDirectory = "/";

  /**
   * 存放所有创建的连接.
   * Key为一个Boolean变量.
   * <p>true:可分配使用;false:已经被分配使用.</p>
   */
  private ConcurrentHashMap<FTPClient, AtomicBoolean> allFtpClients =
      new ConcurrentHashMap<>(MAX_POOL_SIZE);

  /**
   * 用于创建连接的时候,加锁.
   */
  private Object lock = new Object();


  @Override public void afterPropertiesSet() throws Exception {
    init();
  }

  /**
   * 初始化方法.
   */
  public void init() {
    Runtime.getRuntime().addShutdownHook(new Thread() {
      // 系统退出,退出所有的FTP登录.
      @Override
      public void run() {
        disConnectAll();
      }
    });
  }

  /**
   * 归还FTP连接.
   */
  public void returnFtpClient(FTPClient ftp) throws FTPServerProcessException {
    if (ftp == null) {
      log.warn("Can not return a null FTPClient");
      return;
    }

    for (Map.Entry<FTPClient, AtomicBoolean> entry : allFtpClients.entrySet()) {
      FTPClient tftp = entry.getKey();
      // 同一个对象
      if (tftp == ftp) {
        AtomicBoolean availableBoolean = entry.getValue();
        availableBoolean.compareAndSet(false, true);
        break;
      }
    }
  }

  /**
   * 断掉所有的连接.
   */
  public void disConnectAll() {
    log.info("DisConnect all ftp client, total count:{}", allFtpClients.size());
    for (Map.Entry<FTPClient, AtomicBoolean> entry : allFtpClients.entrySet()) {
      FTPClient ftp = entry.getKey();
      disConnect(ftp);
    }
  }

  /**
   * 获取一个连接.
   */
  public FTPClient getFTPClient() throws FTPServerProcessException {
    FTPClient ftp = null;
    for (Map.Entry<FTPClient, AtomicBoolean> entry : allFtpClients.entrySet()) {
      entry.getValue();
      AtomicBoolean availableBoolean = entry.getValue();
      // 如果可用
      if (availableBoolean.get()) {
        // 抢占到连接.
        if (availableBoolean.compareAndSet(true, false)) {
          ftp = entry.getKey();
          break;
        }
      }
    }
    if (ftp == null) {
      int currentNum = allFtpClients.size();
      if (currentNum < MAX_POOL_SIZE) {
        synchronized (lock) {
          // double check
          currentNum = allFtpClients.size();
          if (currentNum < MAX_POOL_SIZE) {
            try {
              // 创建新的连接
              ftp = new FTPClient();
              connect(ftp);
              // 放入到容器中
              allFtpClients.put(ftp, new AtomicBoolean(false));
            } catch (FTPServerProcessException fpe) {
              log.error("Failed to create a new FTPClient." + getConnection(), fpe);
            }
          }
        }
      }
    }
    if (ftp == null) {
      throw new NoMoreFTPClientException("No more available FTP client.");
    }
    try {
      if (!ftp.isAvailable()) {
        //Check the is available
        reConnect(ftp);
      } else {
        try{
          ftp.changeToParentDirectory();
          if (StringUtils.hasText(baseDirectory)) {
            ftp.changeWorkingDirectory(baseDirectory);
          }
        } catch(IOException ioe) {
          log.error("Failed to change to working directory!", ioe);

          // 有问题的连接,去掉.
          allFtpClients.remove(ftp);
          throw new FTPServerProcessException("Failed to switch to working directory!", ioe);
        }
      }
    } catch (FTPServerProcessException fse) {
      log.error("Failed to init FTP connection", fse);
      throw fse;
    }
    return ftp;
  }

  /**
   * 断开连接.
   */
  private void disConnect(FTPClient ftp) throws FTPServerProcessException {
    log.debug("Disconnect the ftp client {}", ftp);
    try {
      ftp.noop(); // check that control connection is working OK
      ftp.logout();
    } catch (IOException ex) {
      log.error("Failed to logout." + getConnection(), ex);
    } finally {
      if (ftp.isConnected())
        throw new FTPServerProcessException("Failed to logout." + getConnection());
    }
  }

  /**
   * 连接FTP服务器.
   *
   * @param ftp
   * @throws FTPServerProcessException 连接失败.
   */
  private void connect(FTPClient ftp) throws FTPServerProcessException {
    try {
      ftp.connect(serverHost, serverPort);
      int reply = ftp.getReplyCode();
      if (!FTPReply.isPositiveCompletion(reply)) {
        ftp.disconnect();
        throw new FTPServerProcessException("Failed to connect FTP server." + getConnection());
      } else if (!ftp.login(ftpAccount, ftpPassword)) {
        ftp.logout();
        throw new FTPServerProcessException("Failed to login FTP server." + getConnection());
      }
      ftp.setFileType(FTP.BINARY_FILE_TYPE);//使用二进制
      if (StringUtils.hasText(baseDirectory)) {
        // Switch root directory
        ftp.changeWorkingDirectory(baseDirectory);
      }
    } catch (IOException ex) {
      log.error("Failed connect FTP server." + getConnection(), ex);
      if (ftp.isConnected()) {
        try {
          ftp.disconnect();
        } catch (IOException e) {
          log.error("Failed connect FTP server." + getConnection(), e);
        }
      }
      throw new FTPServerProcessException("Failed to connect FTP server." + getConnection());
    }
  }

  /**
   * 重连服务器.
   */
  private void reConnect(FTPClient ftp) throws FTPServerProcessException {
    try {
      disConnect(ftp);
    } catch (FTPServerProcessException e) {
      throw new FTPServerProcessException("Failed to connect FTP server." + getConnection());
    }
    connect(ftp);
  }

  /**
   * 获取FTP服务器的连接配置信息.
   */
  private String getConnection() {
    StringBuilder sb = new StringBuilder();
    sb.append("Host:").append(serverHost).append(",");
    sb.append("Port:").append(serverPort).append(",");
    sb.append("Account:").append(ftpAccount).append(",");
    sb.append("BaseDirectory:").append(baseDirectory);
    return sb.toString();
  }

  public String getServerHost() {
    return serverHost;
  }

  public void setServerHost(String serverHost) {
    Assert.notNull(serverHost);
    this.serverHost = serverHost;
  }

  public int getServerPort() {
    return serverPort;
  }

  public void setServerPort(int serverPort) {
    if (serverPort < 0 || serverPort >65535) {
      throw new IllegalArgumentException("serverPort need between 0 and 65525, but set "+serverHost);
    }
    this.serverPort = serverPort;
  }

  public String getFtpAccount() {
    return ftpAccount;
  }

  public void setFtpAccount(String ftpAccount) {
    Assert.notNull(ftpAccount);
    this.ftpAccount = ftpAccount;
  }

  public String getFtpPassword() {
    return ftpPassword;
  }

  public void setFtpPassword(String ftpPassword) {
    Assert.notNull(ftpPassword);
    this.ftpPassword = ftpPassword;
  }

  public String getBaseDirectory() {
    return baseDirectory;
  }

  public void setBaseDirectory(String baseDirectory) {
    if (baseDirectory == null) {
      this.baseDirectory = "";
      return;
    }
    this.baseDirectory = baseDirectory.trim();
  }
}
