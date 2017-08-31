package com.dianrong.common.uniauth.server.synchronous.hr.support;

import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.common.util.ObjectUtil;
import com.dianrong.common.uniauth.server.synchronous.exp.DeleteFTPFileFailureException;
import com.dianrong.common.uniauth.server.synchronous.hr.bean.SynchronousFile;
import com.dianrong.common.uniauth.server.synchronous.support.SFTPConnectionManager;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * FTP文件的删除处理.
 */

@Slf4j @Component public class SFTPFileDeleter {

  private SFTPConnectionManager sftpConnectionManager;

  @Autowired public SFTPFileDeleter(SFTPConnectionManager SFTPConnectionManager) {
    Assert.notNull(SFTPConnectionManager);
    this.sftpConnectionManager = SFTPConnectionManager;
  }

  /**
   * 根据指定的时间删除FTP服务器上的过期文件.
   *
   * @return 删除的文件的名称列表.
   * @throws DeleteFTPFileFailureException 删除指定文件失败.
   */
  public List<String> deleteFtpFileByExpiredTime(Date expiredTime)
      throws DeleteFTPFileFailureException {
    if (expiredTime == null) {
      log.warn(
          "Delete FTP server files by expire time, but the expire time is null, so just ignore.");
      return Collections.emptyList();
    }
    final Calendar expiredCalendar = Calendar.getInstance();
    expiredCalendar.setTime(expiredTime);
    ChannelSftp channelSftp = null;
    try {
      channelSftp = this.sftpConnectionManager.getSftpClient();
      final Set<String> deleteFileNames = Sets.newHashSet();

      // 控制只删除Uniauth相关的文件
      final Set<String> deleteFileNamePrefixSet = Sets.newHashSet();
      for (SynchronousFile sf : SynchronousFile.values()) {
        deleteFileNamePrefixSet.add(sf.getName().toLowerCase());
      }
      try {
        channelSftp.ls(".", new ChannelSftp.LsEntrySelector() {
          @Override public int select(ChannelSftp.LsEntry entry) {
            Calendar addTime = Calendar.getInstance();
            addTime.setTime(new Date(entry.getAttrs().getATime() * 1000L));
            String fileName = entry.getFilename();
            if (StringUtils.hasText(fileName) && addTime.before(expiredCalendar)) {
              String fileNameLowercase = fileName.toLowerCase();
              for (String prefix : deleteFileNamePrefixSet) {
                if (fileNameLowercase.startsWith(prefix)) {
                  deleteFileNames.add(fileName);
                  break;
                }
              }
            }
            return ChannelSftp.LsEntrySelector.CONTINUE;
          }
        });
      } catch (SftpException e) {
        log.error("Failed ls", e);
      }
      return deleteFtpFileByFileNames(deleteFileNames, channelSftp);
    } catch (DeleteFTPFileFailureException dfe) {
      log.error("Failed delete FTP server files", dfe);
      throw dfe;
    } catch (Exception e) {
      throw new DeleteFTPFileFailureException("Delete FTP server files occured:" + e.getMessage(),
          e);
    } finally {
      this.sftpConnectionManager.returnSftpClient(channelSftp);
    }
  }

  /**
   * 根据文件名称删除文件.
   *
   * @return 删除的文件的名称列表.
   * @throws DeleteFTPFileFailureException 删除指定文件失败.
   */
  public List<String> deleteFtpFileByFileNames(Set<String> fileNames)
      throws DeleteFTPFileFailureException {
    if (ObjectUtil.collectionIsEmptyOrNull(fileNames)) {
      log.debug("Delete file name list is empty, just ignore.");
      return Collections.emptyList();
    }
    ChannelSftp channelSftp = null;
    try {
      channelSftp = this.sftpConnectionManager.getSftpClient();
      return deleteFtpFileByFileNames(fileNames, channelSftp);
    } finally {
      this.sftpConnectionManager.returnSftpClient(channelSftp);
    }
  }

  /**
   * 根据文件名称删除文件.
   *
   * @param fileNames   待删除的文件名称列表.
   * @param channelSftp 外部提供的ChannelSftp,不能为空.
   * @return 删除的文件的名称列表.
   * @throws DeleteFTPFileFailureException 删除指定文件失败.
   */
  private List<String> deleteFtpFileByFileNames(Set<String> fileNames, ChannelSftp channelSftp)
      throws DeleteFTPFileFailureException {
    List<String> successFileNames = Lists.newArrayList();
    List<String> failedFileNames = Lists.newArrayList();
    for (String fileName : fileNames) {
      try {
        channelSftp.rm(fileName);
        successFileNames.add(fileName);
        log.debug("Success delete file:" + fileName);
      } catch (SftpException e) {
        log.error("Failed to delete file:" + fileName, e);
        failedFileNames.add(fileName);
      }
    }
    if (!failedFileNames.isEmpty()) {
      DeleteFTPFileFailureException deleteException = new DeleteFTPFileFailureException(
          "Failed to delete FTP server file.FileNames: " + failedFileNames);
      deleteException.setDeleteFailedFileNames(failedFileNames);
      deleteException.setDeleteSuccessFileNames(successFileNames);
      throw deleteException;
    }
    return successFileNames;
  }
}
