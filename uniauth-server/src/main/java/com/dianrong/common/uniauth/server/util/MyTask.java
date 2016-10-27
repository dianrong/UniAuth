package com.dianrong.common.uniauth.server.util;

public class MyTask {
	public static void main(String[] args) {
		Integer[] bs = new Integer[2];
		bs[0] = 1;
		Integer a = bs[0];
		System.out.println(bs.getClass().getClassLoader());
		System.out.println(a.getClass().getClassLoader());
		System.out.println(MyTask.class.getClass().getClassLoader());
	}
}
