package com.dianrong.common.techops.service;

import java.util.*;

import javax.annotation.Resource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.dianrong.common.techops.bean.LoginUser;
import com.dianrong.common.techops.helper.CustomizeBeanConverter;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.dianrong.common.uniauth.client.custom.UserExtInfo;
import com.dianrong.common.uniauth.common.bean.dto.DomainDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleDto;
import com.dianrong.common.uniauth.common.bean.request.DomainParam;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.enm.PermTypeEnum;
import com.dianrong.common.uniauth.sharerw.facade.UARWFacade;

@Service
public class TechOpsService {

	private static Logger logger = LoggerFactory.getLogger(TechOpsService.class);

    @Resource
    private UARWFacade uARWFacade;
    
    public UserExtInfo getLoginUserInfo(){
    	UserExtInfo userExtInfo = (UserExtInfo)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	return userExtInfo;
    }
    
	public LoginUser getLoginUser(){
		LoginUser loginUser = new LoginUser();
		UserExtInfo userExtInfo = this.getLoginUserInfo();
		DomainDto domainDto = userExtInfo.getDomainDto();
		List<String> authorizedDomainCodeList = new ArrayList<String>();
		List<DomainDto> domainDtoList = new ArrayList<DomainDto>();
		List<RoleDto> returnRoleList = new ArrayList<RoleDto>();
		// construct returnUser
		UserDto returnUserDto = new UserDto();
		returnUserDto.setId(userExtInfo.getUserDto().getId());
		returnUserDto.setEmail(userExtInfo.getUserDto().getEmail());
		returnUserDto.setName(userExtInfo.getUserDto().getName());
		returnUserDto.setPhone(userExtInfo.getUserDto().getPhone());

		Map<String, Set<String>> returnPermMap = new TreeMap<>();

		if(domainDto != null){
			List<RoleDto> roleDtoList = domainDto.getRoleList();
			if(roleDtoList != null && !roleDtoList.isEmpty()){
				for(RoleDto roleDto: roleDtoList){
					// construct returnRole
					RoleDto returnRoleDto = CustomizeBeanConverter.convert(roleDto);
					returnRoleList .add(returnRoleDto);
					String roleCode = roleDto.getRoleCode();
					// construct returnPermissions
					Map<String, Set<String>> rolePermMap = roleDto.getPermMap();
					if(rolePermMap != null && rolePermMap.size() > 0) {
						Set<String> rolePermMapKeySet = rolePermMap.keySet();
						for(String key : rolePermMapKeySet) {
							Set<String> returnPermValue = returnPermMap.get(key);
							if(returnPermValue != null) {
								returnPermValue.addAll(rolePermMap.get(key));
							} else {
								Set<String> set = new HashSet<>();
								set.addAll(rolePermMap.get(key));
								returnPermMap.put(key, set);
							}
						}
					}

					if(AppConstants.ROLE_ADMIN.equals(roleCode) || AppConstants.ROLE_SUPER_ADMIN.equals(roleCode)){
						Set<String> domainCodeList = rolePermMap.get(PermTypeEnum.DOMAIN.toString());
						if(domainCodeList != null && !domainCodeList.isEmpty()){
							for(String domainCode: domainCodeList){
								if(!authorizedDomainCodeList.contains(domainCode)){
									authorizedDomainCodeList.add(domainCode);
								}
							}
						}
					}
				}
			}
			
			if(!authorizedDomainCodeList.isEmpty()){
				DomainParam domainParam = new DomainParam();
				if(!authorizedDomainCodeList.contains(AppConstants.DOMAIN_CODE_TECHOPS)){
					domainParam.setDomainCodeList(authorizedDomainCodeList);
				}
				// construct switchable domainDtoList.
				domainDtoList = uARWFacade.getDomainRWResource().getAllLoginDomains(domainParam).getData();
			}
		}
		loginUser.setPermMap(returnPermMap);
		loginUser.setRoles(returnRoleList);
		loginUser.setSwitchableDomains(domainDtoList);
		loginUser.setUser(returnUserDto);
		return loginUser;
	}

	public void sendEmail(String email, StringBuffer buffer) {
		logger.debug("Sending email to : " + email);
		logger.debug("Content: \n " + buffer);
		try {
			// 配置发送邮件的环境属性
			final Properties props = new Properties();
        /*
         * 可用的属性： mail.store.protocol / mail.transport.protocol / mail.host /
         * mail.user / mail.from
         */
			// 表示SMTP发送邮件，需要进行身份验证
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.host", "220.181.11.80");
			props.put("mail.smtp.port", "25");
			// 发件人的账号
			props.put("mail.user", "postmaster@dianrong.sendcloud.org");
			// 访问SMTP服务时需要提供的密码
			props.put("mail.password", "mONh8xRTosRPJYC3");

			// 构建授权信息，用于进行SMTP进行身份验证
			Authenticator authenticator = new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					// 用户名、密码
					String userName = props.getProperty("mail.user");
					String password = props.getProperty("mail.password");
					return new PasswordAuthentication(userName, password);
				}
			};
			// 使用环境属性和授权信息，创建邮件会话
			Session mailSession = Session.getInstance(props, authenticator);
			// 创建邮件消息
			MimeMessage message = new MimeMessage(mailSession);
			// 设置发件人
			InternetAddress from = new InternetAddress(props.getProperty("mail.user"));
			message.setFrom(from);

			// 设置收件人
			InternetAddress to = new InternetAddress(email);
			message.setRecipient(Message.RecipientType.TO, to);

			// 设置邮件标题
			message.setSubject("点融后端账号系统 -- 通知");

			// 设置邮件的内容体
			message.setContent(buffer.toString(), "text/html;charset=UTF-8");

			// 发送邮件
			Transport.send(message);
		} catch (Exception e) {
			logger.error("error to send email to the email:" + email, e);
		}
		logger.debug("End of sending email to : " + email);
		logger.debug("Content: \n " + buffer);
	}
}
