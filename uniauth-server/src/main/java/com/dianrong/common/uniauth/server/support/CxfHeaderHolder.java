package com.dianrong.common.uniauth.server.support;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.util.Assert;

/**
 * . 负责维护cxf header相关holder
 * @author wanglin
 */
public enum CxfHeaderHolder {
	TENANCYID(Long.class),
	TENANCYCODE(String.class);
	
	private ThreadLocal<Object> holder;
	private Class<?> type;
	private <T> CxfHeaderHolder(Class<?> type) {
		Assert.notNull(type);
		this.type = type;
		holder =new managedHolder<Object>();
	}
	
	public Object get() {
		return holder.get();
	}
	
	public void set(Object val) {
		if (val != null) {
			if (!this.type.isAssignableFrom(val.getClass())){
				throw new IllegalArgumentException("val's type is not right, need " + this.type.getName());
			}
		}
		holder.set(val);
	}
	
	private static Logger logger = Logger.getLogger(CxfHeaderHolder.class);
	// 清空所有holder 信息
	public static void clearAllHolder() {
		managedHolder.managedHoldersClear();
	}
	// 受管理的holder 抽象类
	private static  class managedHolder<T> extends ThreadLocal<T> {
		private static final List<ThreadLocal<?>> holders = new ArrayList<ThreadLocal<?>>();
		public static final void managedHoldersClear() {
			for (ThreadLocal<?> h : holders) {
				try {
					h.remove();
				} catch (Exception ex) {
					logger.warn("failed to clear holder :" + h.getClass().getName());
				}
			}
		}
		public managedHolder() {
			super();
			holders.add(this);
		}
	}
}
