package com.dianrong.loanbusiness.subsystem.help;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.dianrong.common.uniauth.common.bean.request.UserQuery;
import com.dianrong.common.uniauth.common.util.JsonUtil;

/**
 * 写个demo程序, 某些有需求想要自己调用uniauth-server API
 * 
 * @author wanglin
 *
 */
@SuppressWarnings("deprecation")
public class UniauthServerApiInvoke {

    public static final String URI = "http://10.8.12.218:8200/uniauth/ws/rs/user/searchusers";

    public static void main(String[] args) throws Exception {
        for(int i =0; i < 1000000; i++) {
            request("dianrong", 300002193L);
        }
//        System.out.println(request("dianrong", 300002193L));
    }

    public static String request(String tenancyCode, Long userId) throws Exception {
        InputStream is = null;
        BufferedReader br = null;
        StringBuilder sBuilder = null;
        try {
            @SuppressWarnings({"resource"})
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(URI);
            httpPost.setEntity(new StringEntity(JsonUtil.object2Jason(new UserQuery().setUserId(userId).setTenancyCode(tenancyCode)), "UTF-8"));
            httpPost.setHeader("Content-Type", "application/json");
            HttpResponse httpResponse = httpClient.execute(httpPost);
            // 连接成功
            if (200 == httpResponse.getStatusLine().getStatusCode()) {
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
                br = new BufferedReader(new InputStreamReader(is));
                String tempStr;
                sBuilder = new StringBuilder();
                while ((tempStr = br.readLine()) != null) {
                    sBuilder.append(tempStr);
                }
            }
        } finally {
            if (br != null) {
                br.close();
            }
            if (is != null) {
                is.close();
            }
        }
        return sBuilder == null ? "" : sBuilder.toString();
    }
}
