package com.dianrong.common.uniauth.common.switches;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Switch {
	
	String name() default "";
	
	String desc() default "";
	
	String priority() default "P2";
	
	Class<?> recieiver() default DefaultSwitchReceiver.class;
	
}
