package com.dianrong.common.uniauth.client;

import org.springframework.security.authentication.encoding.PasswordEncoder;

import com.dianrong.common.uniauth.common.util.UniPasswordEncoder;

@SuppressWarnings("deprecation")
public class SSPasswordEncoder implements PasswordEncoder {

	@Override
	public String encodePassword(String rawPass, Object salt) {
		return UniPasswordEncoder.encodePassword(rawPass, salt);
	}

	@Override
	//encPass from db
	public boolean isPasswordValid(String encPass, String rawPass, Object salt) {
		return UniPasswordEncoder.isPasswordValid(encPass, rawPass, salt);
	}
}
