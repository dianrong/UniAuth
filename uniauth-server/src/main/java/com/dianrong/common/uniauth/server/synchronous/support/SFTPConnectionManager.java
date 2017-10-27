package com.dianrong.common.uniauth.server.synchronous.support;

import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.server.synchronous.exp.SFTPServerProcessException;
import com.dianrong.common.uniauth.server.synchronous.hr.support.HrDataSynchronousSwitcher;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * 处理SFTP连接的管理类.
 */

@Slf4j
public class SFTPConnectionManager implements InitializingBean {

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
  @Value("#{uniauthConfig['synchronization.hr.sftp.account']}")
  private String sftpAccount = "";
  @Value("#{uniauthConfig['synchronization.hr.sftp.pwd']}")
  private String sftpPassword = "";

  /**
   * 登录SFTP的base路径.
   */
  @Value("#{uniauthConfig['synchronization.hr.sftp.base.directory']}")
  private String
      baseDirectory = "/";

  /**
   * 开关.
   */
  @Autowired
  private HrDataSynchronousSwitcher switchControl;

  /**
   * 创建SFTP连接的管理类.
   */
  private JSch jsch;

  @Override
  public void afterPropertiesSet() throws Exception {
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
  }

  /**
   * 归还SFTP Channel.
   */
  public void returnSftpClient(ChannelSftp channelSftp) {
    if (channelSftp == null) {
      return;
    }
    // 断开连接
    disConnectChannel(channelSftp);
  }

  /**
   * 获取一个连接.
   */
  public ChannelSftp getSftpClient() throws SFTPServerProcessException {
    ChannelSftp sftp = null;
    Session session = refreshNewSession();
    try {
      // 创建新的sftp Channel
      Channel channel = session.openChannel("sftp");
      sftp = (ChannelSftp) channel;
    } catch (JSchException jsh) {
      log.error("Session failed open a new ChannelSftp." + getConnectionDescription(), jsh);
      throw new SFTPServerProcessException(
          "Session failed open a new ChannelSftp." + getConnectionDescription(), jsh);
    }
    try {
      sftp.connect();
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
   * 每次都创建一个新的session来用,老的session关掉.
   */
  private Session refreshNewSession() {
    Session session;
    try {
      session = this.jsch.getSession(this.sftpAccount, this.serverHost, this.serverPort);
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
      session.connect();
    } catch (JSchException e) {
      log.error("Failed connect sftp session." + getConnectionDescription(), e);
      throw new SFTPServerProcessException(
          "Failed to connect sftp session." + getConnectionDescription(), e);
    }
    return session;
  }

  /**
   * 断开Channel.
   */
  private void disConnectChannel(ChannelSftp channelSftp) throws SFTPServerProcessException {
    log.debug("Disconnect the channelSftp:{}", channelSftp);
    try {
      Session session = channelSftp.getSession();
      if (session != null) {
        session.disconnect();
      }
      channelSftp.disconnect();
    } catch (Exception ex) {
      log.error("Failed to disconnect channel." + getConnectionDescription(), ex);
    } finally {
      if (channelSftp.isConnected()) {
        throw new SFTPServerProcessException(
            "Failed to disconnect channel." + getConnectionDescription());
      }
    }
  }

  /**
   * 获取SFTP服务器的连接配置信息.
   */
  private String getConnectionDescription() {
    StringBuilder sb = new StringBuilder();
    sb.append("\r\n");
    sb.append("Host:").append(serverHost).append(",");
    sb.append("Port:").append(serverPort).append(",");
    sb.append("Account:").append(sftpAccount).append(",");
    sb.append("BaseDirectory:").append(baseDirectory).append(".\r\n");
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
    if (serverPort < 0 || serverPort > 65535) {
      throw new IllegalArgumentException(
          "serverPort need between 0 and 65525, but set " + serverHost);
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
