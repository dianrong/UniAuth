package com.dianrong.loanbusiness.subsystem.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class MyService {
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void testService(){
		for(int i = 0;i < 10;i++)
		System.out.println("----------------------------------------------------------------------------------------");
	}
}
