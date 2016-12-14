package com.dianrong.common.uniauth.common.server.cxf;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;

import lombok.extern.slf4j.Slf4j;

/**
 * . 负责维护cxf header相关holder
 * @author wanglin
 */
@Slf4j
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
			    holder.set(null);
				throw new IllegalArgumentException("val's type is not right, need " + this.type.getName());
			}
		}
		holder.set(val);
	}
	
	// 清空所有holder 信息
	public static void clearAllHolder() {
		managedHolder.managedHoldersClear();
	}
	// 受管理的holder
	private static  class managedHolder<T> extends ThreadLocal<T> {
		private static final List<ThreadLocal<?>> holders = new ArrayList<ThreadLocal<?>>();
		public static final void managedHoldersClear() {
			for (ThreadLocal<?> h : holders) {
				try {
					h.remove();
				} catch (Exception ex) {
					log.warn("failed to clear holder :" + h.getClass().getName());
				}
			}
		}
		public managedHolder() {
			super();
			holders.add(this);
		}
	}
}
