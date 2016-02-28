package com.dianrong.loanbusiness.subsystem.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.dianrong.loanbusiness.subsystem.model.TestModel;

public class MyService {
	
	@PreAuthorize("hasRole('ROLE_USER')")
	public void testService(){
		for(int i = 0;i < 10;i++)
		System.out.println("----------------------------------------------------------------------------------------");
	}
	
	@PreAuthorize("hasPermission(#tm, 'all')")
	public void testModel(TestModel tm){
		
	}
}
