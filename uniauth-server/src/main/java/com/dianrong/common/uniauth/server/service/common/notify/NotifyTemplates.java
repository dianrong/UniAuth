package com.dianrong.common.uniauth.server.service.common.notify;

import com.dianrong.common.uniauth.common.exp.UniauthCommonException;
import com.dianrong.common.uniauth.common.util.Assert;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * 管理消息通知的各种模板.
 */
public class NotifyTemplates {
  private static final Map<NotifyType, NotifyTemplate> TEMPLATES = Maps.newHashMap();

  // init template
  static {
    // addUser
    StringBuilder addUserEmail = new StringBuilder();
    addUserEmail.append("====================================================<br />");
    addUserEmail.append("            ");
    addUserEmail.append("     系统管理员为您创建了系统账户<br />");
    addUserEmail.append("            ");
    addUserEmail.append(" 您的登录账号为: %s        <br />");
    addUserEmail.append("            ");
    addUserEmail.append(" 您的账户临时密码为: %s        <br />");
    addUserEmail.append("            ");
    addUserEmail.append(" 请到: %s 去登陆       <br />");
    addUserEmail.append("====================================================<br />");
    // sms
    StringBuilder addUserSms = new StringBuilder();
    addUserSms.append("系统管理员为您创建了系统账户!您的登录账号为:%s,临时登陆密码为:%s");
    addUserSms.append(" 请到: %s 去登陆");
    TEMPLATES.put(NotifyType.ADD_USER,
        new SimpleNotifyTemplate(addUserEmail.toString(), addUserSms.toString()));

    // update pwd admin
    StringBuilder updatePwdAdminEmail = new StringBuilder();
    updatePwdAdminEmail.append("====================================================<br />");
    updatePwdAdminEmail.append("            ");
    updatePwdAdminEmail.append("      系统管理员重置了您的系统账户密码<br />");
    updatePwdAdminEmail.append("            ");
    updatePwdAdminEmail.append(" 您的登录账号为: %s        <br />");
    updatePwdAdminEmail.append("            ");
    updatePwdAdminEmail.append(" 您的账户临时密码为: %s        <br />");
    updatePwdAdminEmail.append("            ");
    updatePwdAdminEmail.append("请到: %s 去登陆       <br />");
    updatePwdAdminEmail.append("====================================================<br />");
    // sms
    StringBuilder updatePwdAdminSmsEmail = new StringBuilder();
    updatePwdAdminSmsEmail.append("系统管理员重置了您的系统账户密码!您的登录账号为:%s,临时登陆密码为:%s");
    updatePwdAdminSmsEmail.append(" 请到: %s 去登陆");
    TEMPLATES.put(NotifyType.UPDATE_PSWD_ADMIN,
        new SimpleNotifyTemplate(updatePwdAdminEmail.toString(),
            updatePwdAdminSmsEmail.toString()));

    // update pwd self
    StringBuilder updatePwdSelfEmail = new StringBuilder();
    updatePwdSelfEmail.append("====================================================<br />");
    updatePwdSelfEmail.append("            ");
    updatePwdSelfEmail.append("%s:您好! 您已成功修改密码,原密码将不能使用.如有疑问,请咨询点融it.service团队!<br />");
    updatePwdSelfEmail.append("            ");
    updatePwdSelfEmail.append("系统地址: %s  <br />");
    updatePwdSelfEmail.append("====================================================<br />");
    // sms
    StringBuilder updatePwdSelfSmsEmail = new StringBuilder();
    updatePwdSelfSmsEmail.append("%s:您好! 您已成功修改密码,原密码将不能使用.如有疑问,请咨询点融it.service团队!");
    updatePwdSelfSmsEmail.append("系统地址: %s");
    TEMPLATES.put(NotifyType.UPDATE_PSWD_SELF,
        new SimpleNotifyTemplate(updatePwdSelfEmail.toString(), updatePwdSelfSmsEmail.toString()) {
          @Override
          public String getEmailNotifyMsg(Object... args) {
            return super.getEmailNotifyMsg(getArgs(args));
          }

          @Override
          public String getSmsNotifyMsg(Object... args) {
            return super.getSmsNotifyMsg(getArgs(args));
          }

          private Object[] getArgs(Object... args) {
            if (args == null || args.length < 2) {
              return args;
            }
            Object[] newArgs = new Object[args.length - 1];
            System.arraycopy(args, 0, newArgs, 0, 1);
            System.arraycopy(args, 2, newArgs, 1, newArgs.length - 1);
            return newArgs;
          }
        });
  }

  /**
   * 根据通知类型查找通知的模板.
   * 
   * @param type 类型不能为空.
   * @throws UniauthCommonException 如果传入的type不支持.
   */
  public static NotifyTemplate queryTemplate(NotifyType type) {
    Assert.notNull(type);
    NotifyTemplate template = TEMPLATES.get(type);
    if (template == null) {
      throw new UniauthCommonException("Notify type " + type + " is not supported!");
    }
    return template;
  }

  /**
   * 获取邮件发送标题.
   */
  public static String getEmailTitle(NotifyType type, Object... args) {
    return queryTemplate(type).getEmailTitle(args);
  }

  /**
   * 获取邮件发送信息.
   */
  public static String getEmailMsg(NotifyType type, Object... args) {
    return queryTemplate(type).getEmailNotifyMsg(args);
  }

  /**
   * 获取短信通知信息.
   */
  public static String getSmsMsg(NotifyType type, Object... args) {
    return queryTemplate(type).getSmsNotifyMsg(args);
  }
}
