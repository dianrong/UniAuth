package com.dianrong.common.uniauth.common.client.cxf;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.dianrong.common.uniauth.common.apicontrol.HeaderKey;
import com.dianrong.common.uniauth.common.client.cxf.exp.InvalidHeaderKeyException;
import com.dianrong.common.uniauth.common.util.Assert;
import com.google.common.collect.Maps;

/**
 * . 负责维护cxf header相关holder
 * 
 * @author wanglin
 */
public final class ApiControlHeaderHolder {
    /**
     * private constructor
     */
    private ApiControlHeaderHolder() {
        super();
    }

    // header holder
    public static abstract class HeaderHolder {
        private ThreadLocal<Map<String, String>> holder = new ThreadLocal<Map<String, String>>() {
            public Map<String, String> initialValue() {
                return Maps.newHashMap();
            };
        };

        /**
         * @param key can not be null
         * @return current val
         */
        public String get(String key) {
            Assert.notNull(key);
            if (!getAllKeys().contains(key)) {
                throw new InvalidHeaderKeyException("invalid header key " + key);
            }
            return holder.get().get(key);
        }

        /**
         * Set key and value
         * 
         * @param key can not be null
         * @param val value
         */
        public void set(String key, String val) {
            Assert.notNull(key);
            if (!getAllKeys().contains(key)) {
                throw new InvalidHeaderKeyException("invalid header key " + key);
            }
            if (val == null) {
                holder.get().remove(key);
                return;
            }
            holder.get().put(key, val);
        }

        /**
         * get all header
         * 
         * @return all header
         */
        public Map<String, String> getAllHeader() {
            return holder.get();
        }

        /**
         * Remove holder value
         */
        public void remove() {
            holder.get().clear();
        }

        /**
         * return all header key
         * 
         * @return can not be null, a empty list at least
         */
        public abstract List<String> getAllKeys();

    }

    // Request header holder
    private static HeaderHolder requestHeaderHolder = new HeaderHolder() {
        @Override
        public List<String> getAllKeys() {
            return Arrays.asList(HeaderKey.REQUEST_ACCOUNT, HeaderKey.REQUEST_CONTENT, HeaderKey.REQUEST_PSWD, HeaderKey.REQUEST_TYPE);
        }

    };

    // reponse header holder
    private static HeaderHolder responseHeaderHolder = new HeaderHolder() {
        @Override
        public List<String> getAllKeys() {
            return Arrays.asList(HeaderKey.RESPONSE_EXPIRETIME, HeaderKey.RESPONSE_REULST, HeaderKey.RESPONSE_TOKEN, HeaderKey.RESPONSE_TYPE);
        }
    };

    /**
     * get all request header holders
     * 
     * @return header holder, not null
     */
    public static HeaderHolder getRequestHeaderHolder() {
        return requestHeaderHolder;
    }

    /**
     * get all response header holders
     * 
     * @return header holder, not null
     */
    public static HeaderHolder getResponseHeaderHolder() {
        return responseHeaderHolder;
    }
}
