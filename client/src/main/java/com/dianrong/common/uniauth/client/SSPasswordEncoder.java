package com.dianrong.common.uniauth.client;

import org.springframework.security.authentication.encoding.PasswordEncoder;

import com.dianrong.common.uniauth.common.util.AuthUtils;
import com.dianrong.common.uniauth.common.util.Base64;

@SuppressWarnings("deprecation")
public class SSPasswordEncoder implements PasswordEncoder {

	@Override
	public String encodePassword(String rawPass, Object salt) {
		String passwordSalt = (salt == null ? null : salt.toString());
		byte[] salts = Base64.decode(passwordSalt);
		
		byte[] saltedPass = AuthUtils.digest(rawPass, salts);
		return Base64.encode(saltedPass);
	}

	@Override
	//encPass from db
	public boolean isPasswordValid(String encPass, String rawPass, Object salt) {
		return  encPass.equals(encodePassword(rawPass, salt));
	}
}
