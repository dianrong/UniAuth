package com.dianrong.common.uniauth.server.synchronous.exp;

import java.util.List;

/**
 * 删除FTP服务器的文件失败.
 */
public class DeleteFTPFileFailureException extends FTPServerProcessException{

  private static final long serialVersionUID = -2790987801666164932L;

  /**
   * 删除成功的列表.
   */
  private List<String> deleteSuccessFileNames;

  /**
   * 删除失败的列表.
   */
  private List<String> deleteFailedFileNames;

  /**
   * Define a empty method.
   */
  public DeleteFTPFileFailureException() {
    super();
  }

  /**
   * Define method for parameter msg.
   *
   * @param msg msg
   */
  public DeleteFTPFileFailureException(String msg) {
    super(msg);
  }

  /**
   * Define method for parameter msg.
   *
   * @param msg msg
   */
  public DeleteFTPFileFailureException(String msg, Throwable t) {
    super(msg, t);
  }

  public List<String> getDeleteSuccessFileNames() {
    return deleteSuccessFileNames;
  }

  public void setDeleteSuccessFileNames(List<String> deleteSuccessFileNames) {
    this.deleteSuccessFileNames = deleteSuccessFileNames;
  }

  public List<String> getDeleteFailedFileNames() {
    return deleteFailedFileNames;
  }

  public void setDeleteFailedFileNames(List<String> deleteFailedFileNames) {
    this.deleteFailedFileNames = deleteFailedFileNames;
  }
}
