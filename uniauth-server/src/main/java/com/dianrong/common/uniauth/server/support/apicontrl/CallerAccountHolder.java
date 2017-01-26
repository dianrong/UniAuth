package com.dianrong.common.uniauth.server.support.apicontrl;

/**
 * help for save request account per request
 * @author wanglin
 */
public final class CallerAccountHolder {
    private static final ThreadLocal<String> holder = new ThreadLocal<String>();
    
    public static void set(String val) {
        if (val == null) {
            holder.remove();
        }
        holder.set(val);
    }
    
    public static String get() {
        return holder.get();
    }
    
    public static void remove() {
        holder.remove();
    }
}
