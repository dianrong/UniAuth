package com.dianrong.common.uniauth.common.client.cxf;

import java.util.Arrays;
import java.util.List;

import org.apache.cxf.common.util.ProxyHelper;
import org.apache.cxf.jaxrs.client.Client;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;

/**
 * 用于创建接口的代理对象
 * 
 * @author wanglin
 */
public final class UniauthRSClientFactory {

    /**
     * api control filter
     */
    private static final ApiCtlRequestFilter API_CTL_REQ_FILTER = new ApiCtlRequestFilter();

    /**
     * api control filter
     */
    private static final ApiCtlResponseFilter API_CTL_RES_FILTER = new ApiCtlResponseFilter();

    /**
     * create 代理对象
     * 
     * @param baseAddress
     * @param cls
     * @param providers
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T create(String baseAddress, Class<T> cls, List<?> providers) {
        // default
        Object[] providersArray = {};
        if (providers != null) {
            providersArray = providers.toArray();
        }

        // append two provider
        int tempArrayLength = providersArray.length;
        providersArray = Arrays.copyOf(providersArray, tempArrayLength + 2);
        providersArray[tempArrayLength] = API_CTL_REQ_FILTER;
        providersArray[tempArrayLength + 1] = API_CTL_RES_FILTER;
        List<?> cxfProviders = Arrays.asList(providersArray);
        T proxy = JAXRSClientFactory.create(baseAddress, cls, cxfProviders);
        ClassLoader loader = UniauthRSClientFactory.class.getClassLoader();
        ApiControlInvocationHandler handler = new ApiControlInvocationHandler(proxy);
        return (T) ProxyHelper.getProxy(loader, new Class<?>[] {cls, Client.class}, handler);
    }
}
