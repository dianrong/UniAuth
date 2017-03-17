package com.dianrong.common.uniauth.server.util;

/**
 * . object 处理相关的 util
 * 
 * @author wanglin
 */
public final class ObjectUtil {

    /**
     * . 判断 o1 和 o2 是否相等
     * 
     * @param o1
     * @param o2
     * @return true or false
     */
    public static boolean objectEqual(Object o1, Object o2) {
        if (o1 == null && o2 == null) {
            return true;
        }
        // 其中某个字段为空了 都不能认为是相等的
        if (o1 == null || o2 == null) {
            return false;
        }
        return o1.equals(o2);
    }

}
