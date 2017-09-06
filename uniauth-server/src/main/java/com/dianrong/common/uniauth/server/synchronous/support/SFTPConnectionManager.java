package com.dianrong.common.uniauth.server.synchronous.support;

import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.server.synchronous.exp.NoMoreFTPClientException;
import com.dianrong.common.uniauth.server.synchronous.exp.SFTPServerProcessException;
import com.dianrong.common.uniauth.server.synchronous.hr.support.HrDataSynchronousSwitcher;
import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 处理SFTP连接的管理类.
 */

@Slf4j @Component public class SFTPConnectionManager implements InitializingBean {

  /**
   * 最多同时存在5个Channel.
   */
  public static final int MAX_POOL_SIZE = 5;

  @Value("#{uniauthConfig['synchronization.hr.sftp.host']}")
  private String serverHost = "";

  /**
   * 默认端口号为22.
   */
  @Value("#{uniauthConfig['synchronization.hr.sftp.port']?:'22'}")
  private int serverPort = 22;

  /**
   * 登录SFTP服务器的账号和密码
   */
  @Value("#{uniauthConfig['synchronization.hr.sftp.account']}") private String sftpAccount = "";
  @Value("#{uniauthConfig['synchronization.hr.sftp.pwd']}") private String sftpPassword = "";

  /**
   * 登录SFTP的base路径.
   */
  @Value("#{uniauthConfig['synchronization.hr.sftp.base.directory']}")
  private String baseDirectory = "/";

  /**
   * 存放所有创建的Channel.
   * Key为一个Boolean变量.
   * <p>true:可分配使用;false:已经被分配使用.</p>
   */
  private ConcurrentHashMap<ChannelSftp, AtomicBoolean> allSFtpClients =
      new ConcurrentHashMap<>(MAX_POOL_SIZE);

  /**
   * 开关.
   */
  @Autowired private HrDataSynchronousSwitcher switchControl;

  /**
   * 创建SFTP连接的管理类.
   */
  private JSch jsch;
  private volatile Session session;

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
    if (!switchControl.isOn()) {
      log.debug("Synchronous switch if off, init ignored.");
      return;
    }
    this.jsch = new JSch();
    Runtime.getRuntime().addShutdownHook(new Thread() {
      // 系统退出,退出所有的FTP登录.
      @Override
      public void run() {
        disConnectAll();
      }
    });
  }



  /**
   * 断掉所有的连接.
   */
  public void disConnectAll() {
    log.info("DisConnect all ftp client, total count:{}", allSFtpClients.size());
    for (Map.Entry<ChannelSftp, AtomicBoolean> entry : allSFtpClients.entrySet()) {
      ChannelSftp channelSftp = entry.getKey();
      disConnectChannel(channelSftp);
    }
    // 关闭session
    if (this.session != null) {
      this.session.disconnect();
    }
  }

  /**
   * 归还SFTP Channel.
   */
  public void returnSftpClient(ChannelSftp channelSftp) {
    if (channelSftp == null) {
      return;
    }

    for (Map.Entry<ChannelSftp, AtomicBoolean> entry : allSFtpClients.entrySet()) {
      ChannelSftp sftp = entry.getKey();
      // 同一个对象
      if (channelSftp == sftp) {
        AtomicBoolean availableBoolean = entry.getValue();
        availableBoolean.compareAndSet(false, true);
        break;
      }
    }
  }

  /**
   * 获取一个连接.
   */
  public ChannelSftp getSftpClient() throws SFTPServerProcessException {
    ChannelSftp sftp = null;
    for (Map.Entry<ChannelSftp, AtomicBoolean> entry : allSFtpClients.entrySet()) {
      entry.getValue();
      AtomicBoolean availableBoolean = entry.getValue();
      // 如果可用
      if (availableBoolean.get()) {
        if (availableBoolean.compareAndSet(true, false)) {
          sftp = entry.getKey();
          break;
        }
      }
    }
    if (sftp == null) {
      int currentNum = allSFtpClients.size();
      if (currentNum < MAX_POOL_SIZE) {
        synchronized (lock) {
          // double check
          currentNum = allSFtpClients.size();
          if (currentNum < MAX_POOL_SIZE) {
            try {
              ensureSessionAvailable();
              // 创建新的sftp Channel
              Channel channel = this.session.openChannel("sftp");
              sftp = (ChannelSftp) channel;
              // 放入到容器中
              allSFtpClients.put(sftp, new AtomicBoolean(false));
            } catch (JSchException jsh) {
              log.error("Session failed open a new ChannelSftp." + getConnectionDescription(), jsh);
            }
          }
        }
      }
    }
    if (sftp == null) {
      throw new NoMoreFTPClientException("No more available channelSftp.");
    }
    try {
      if (!sftp.isConnected()) {
        sftp.connect();
      }
      // 切换到目标路径
      sftp.cd(this.baseDirectory);
    } catch (JSchException e) {
      log.error(sftp + " failed to connect server." + getConnectionDescription(), e);
      throw new SFTPServerProcessException(
          sftp + " failed to connect server." + getConnectionDescription(), e);
    } catch (SftpException e) {
      StringBuilder msg = new StringBuilder();
      msg.append(sftp).append(" failed to change to the base directory:").append(this.baseDirectory)
          .append(".").append(getConnectionDescription());
      log.error(msg.toString(), e);
      throw new SFTPServerProcessException(msg.toString(), e);
    }
    return sftp;
  }

  /**
   * 确保当前session可用.
   */
  private void ensureSessionAvailable() {
    // 正常情况,直接返回.
    if (this.session != null) {
      if (this.session.isConnected()) {
        return;
      }
    }
    // 重新创建一个新的session
    synchronized (lock) {
      try {
        this.session = this.jsch.getSession(this.sftpAccount, this.serverHost, this.serverPort);
      } catch (JSchException e) {
        log.error("Failed create sftp session." + getConnectionDescription(), e);
        throw new SFTPServerProcessException(
            "Failed to create sftp session." + getConnectionDescription(), e);
      }
      log.debug("Session created." + getConnectionDescription());
      session.setPassword(this.sftpPassword);
      Properties sshConfig = new Properties();
      sshConfig.put("StrictHostKeyChecking", "no");
      session.setConfig(sshConfig);
      try {
        this.session.connect();
      } catch (JSchException e) {
        log.error("Failed connect sftp session." + getConnectionDescription(), e);
        throw new SFTPServerProcessException(
            "Failed to connect sftp session." + getConnectionDescription(), e);
      }
    }
  }

  /**
   * 断开Channel.
   */
  private void disConnectChannel(ChannelSftp channelSftp) throws SFTPServerProcessException {
    log.debug("Disconnect the channelSftp:{}", channelSftp);
    try {
      if (channelSftp.isConnected()) {
        channelSftp.disconnect();
      }
    } catch (Exception ex) {
      log.error("Failed to disconnect channel." + getConnectionDescription(), ex);
    } finally {
      if (channelSftp.isConnected())
        throw new SFTPServerProcessException(
            "Failed to disconnect channel." + getConnectionDescription());
    }
  }

  /**
   * 获取SFTP服务器的连接配置信息.
   */
  private String getConnectionDescription() {
    StringBuilder sb = new StringBuilder();
    sb.append("Host:").append(serverHost).append(",");
    sb.append("Port:").append(serverPort).append(",");
    sb.append("Account:").append(sftpAccount).append(",");
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

  public String getSftpAccount() {
    return sftpAccount;
  }

  public void setSftpAccount(String sftpAccount) {
    Assert.notNull(sftpAccount);
    this.sftpAccount = sftpAccount;
  }

  public String getSftpPassword() {
    return sftpPassword;
  }

  public void setSftpPassword(String sftpPassword) {
    Assert.notNull(sftpPassword);
    this.sftpPassword = sftpPassword;
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

  public void setSwitchControl(HrDataSynchronousSwitcher switchControl) {
    Assert.notNull(switchControl);
    this.switchControl = switchControl;
  }
}
