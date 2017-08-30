package com.dianrong.common.uniauth.server.synchronous.hr.support;

import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.common.util.ObjectUtil;
import com.dianrong.common.uniauth.server.synchronous.exp.DeleteFTPFileFailureException;
import com.dianrong.common.uniauth.server.synchronous.support.FTPConnectionManager;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * FTP文件的删除处理.
 */

@Slf4j @Component public class FTPFileDeleter {

  private FTPConnectionManager ftpConnectionManager;

  @Autowired public FTPFileDeleter(FTPConnectionManager ftpConnectionManager) {
    Assert.notNull(ftpConnectionManager);
    this.ftpConnectionManager = ftpConnectionManager;
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
    Calendar expiredCalendar = Calendar.getInstance();
    expiredCalendar.setTime(expiredTime);
    FTPClient ftpClient = null;
    try {
      ftpClient = this.ftpConnectionManager.getFTPClient();
      FTPFile[] ftpFiles = ftpClient.listDirectories();
      Set<String> deleteFileNames = Sets.newHashSet();
      for (FTPFile ftpFile : ftpFiles) {
        if (ftpFile.getTimestamp().before(expiredCalendar)) {
          deleteFileNames.add(ftpFile.getName());
        }
      }
      return deleteFtpFileByFileNames(deleteFileNames, ftpClient);
    } catch (DeleteFTPFileFailureException dfe) {
      log.error("Failed delete FTP server files", dfe);
      throw dfe;
    } catch (Exception e) {
      throw new DeleteFTPFileFailureException("Delete FTP server files occured:" + e.getMessage(),
          e);
    } finally {
      this.ftpConnectionManager.returnFtpClient(ftpClient);
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
    FTPClient ftpClient = null;
    try {
      ftpClient = this.ftpConnectionManager.getFTPClient();
      return deleteFtpFileByFileNames(fileNames, ftpClient);
    } finally {
      this.ftpConnectionManager.returnFtpClient(ftpClient);
    }
  }

  /**
   * 根据文件名称删除文件.
   *
   * @param fileNames 待删除的文件名称列表.
   * @param ftpClient 外部提供的FTPClient.
   * @return 删除的文件的名称列表.
   * @throws DeleteFTPFileFailureException 删除指定文件失败.
   */
  private List<String> deleteFtpFileByFileNames(Set<String> fileNames, FTPClient ftpClient)
      throws DeleteFTPFileFailureException {
    List<String> successFileNames = Lists.newArrayList();
    List<String> failedFileNames = Lists.newArrayList();
    for (String fileName : fileNames) {
      try {
        ftpClient.deleteFile(fileName);
        successFileNames.add(fileName);
        log.debug("Success delete file:" + fileName);
      } catch (Exception e) {
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
