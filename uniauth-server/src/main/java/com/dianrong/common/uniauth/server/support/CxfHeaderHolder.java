package com.dianrong.common.uniauth.server.support;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;

/**
 * . 负责维护cxf header相关holder
 * 
 * @author wanglin
 */
public class CxfHeaderHolder {
	private static Logger logger = Logger.getLogger(CxfHeaderHolder.class);

	// 清空所有holder 信息
	public static void clearAllHolder() {
		managedHolder.managedHoldersClear();
	}

	// tenancyId
	public static class TenancyIdHolder extends managedHolder {
		private static final ThreadLocal<Long> tenancyIds = new ThreadLocal<Long>();

		public static Long get() {
			return tenancyIds.get();
		}

		public static void set(Long tenancyId) {
			tenancyIds.set(tenancyId);
		}

		@Override
		public void clear() {
			tenancyIds.remove();
		}
	}

	// tenancyCode
	public static class TenancyCodeHolder extends managedHolder {
		private static final ThreadLocal<String> tenancyCodes = new ThreadLocal<String>();

		public static String get() {
			return tenancyCodes.get();
		}

		public static void set(String tenancyCode) {
			tenancyCodes.set(tenancyCode);
		}

		@Override
		public void clear() {
			tenancyCodes.remove();
		}
	}

	// 用于清空holder
	public static class HolderClearFilter implements Filter {
		@Override
		public void init(FilterConfig filterConfig) throws ServletException {}

		@Override
		public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
				throws IOException, ServletException {
			try {
				chain.doFilter(request, response);
			} finally {
				// clear 
				clearAllHolder();
			}
		}

		@Override
		public void destroy() {}
	}
	
	// 受管理的holder 抽象类
	private static abstract class managedHolder {
		private static final List<managedHolder> holders = new ArrayList<managedHolder>();

		public static final void managedHoldersClear() {
			for (managedHolder h : holders) {
				try {
					h.clear();
				} catch (Exception ex) {
					logger.warn("failed to clear holder :" + h.getClass().getName());
				}
			}
		}

		public managedHolder() {
			holders.add(this);
		}

		public abstract void clear();
	}
}
