package com.dianrong.common.uniauth.common.client;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dianrong.common.uniauth.common.exp.NotLoginException;
import com.dianrong.common.uniauth.common.exp.LoginFailedException;
import com.dianrong.common.uniauth.common.exp.NetworkException;
import com.dianrong.common.uniauth.common.exp.NotReuseSessionIdException;
import com.dianrong.common.uniauth.common.exp.OperationForbiddenException;

@Component
public class AccessTechOpsApi {

	@Autowired
	private ZooKeeperConfig zooKeeperConfig;
	private volatile HttpClient httpClient;
	private long lastSessionMilliSeconds;

	public String getSessionId(String account, String password) {
		if(lastSessionMilliSeconds != 0L){
			long currentSessionMilliSeconds = new Date().getTime();
			if((currentSessionMilliSeconds - lastSessionMilliSeconds)/1000 < 30L * 60 * 1000){
				throw new NotReuseSessionIdException("Jsession id must be reused, please don't generate sessionid so frequently.");
			}
		}
		
		if (account == null || password == null || "".equals(account.trim()) || "".equals(password.trim())) {
			throw new IllegalArgumentException("Account or password is empty!");
		}
		String techOpsServerUrl = zooKeeperConfig.getTechOpsServerUrl(); //"http://localhost:8090/techops/"; 
		String casServerUrl = zooKeeperConfig.getCasServerUrl(); //"http://localhost:9080/cas";  

		String serviceString = "service=" + techOpsServerUrl + "/login/cas";
		String casRestBaseUrl = casServerUrl + "/v1/tickets";

		HttpContent tgtRequestHc = new HttpContent();
		tgtRequestHc.setBody("username=" + account + "&password=" + password + "&" + serviceString);
		HttpContent tgtResponseHc = requestServer(casRestBaseUrl, "POST", tgtRequestHc);
		String tgtRestUrl = tgtResponseHc.getHeaders().get("Location");
		String tgt = tgtRestUrl.substring(tgtRestUrl.lastIndexOf("/"));

		HttpContent stRequestHc = new HttpContent();
		stRequestHc.setBody(serviceString);
		HttpContent stResponseHc = requestServer(casRestBaseUrl + tgt, "POST", stRequestHc);
		String st = stResponseHc.getBody().trim();

		HttpContent sessionResponsetHc = requestServer(techOpsServerUrl + "/login/cas?ticket=" + st, "GET", null);
		String jsessionId = sessionResponsetHc.getHeaders().get("Set-Cookie").split(";")[0].split("=")[1];
		lastSessionMilliSeconds = new Date().getTime();
		
		return jsessionId;
	}
	public String accessApi(String sessionId, String apiPath, String method, String postBody){
		HttpContent apiRequestHc = new HttpContent(); Map<String,
		String> headers = new HashMap<String, String>();
		headers.put("Cookie", "JSESSIONID=" + sessionId);
		apiRequestHc.setHeaders(headers);
		apiRequestHc.setBody(postBody);
		
		String techOpsServerUrl = zooKeeperConfig.getTechOpsServerUrl(); //"http://localhost:8090/techops/";  
		
		if(method != null && ("GET".equals(method) || "POST".equals(method))){
			HttpContent apiResponseHc = requestServer(techOpsServerUrl + apiPath, method, apiRequestHc);
			return apiResponseHc.getBody();
		}
		else{
			throw new IllegalArgumentException("Only GET or POST supported!");
		}
	}

	private HttpContent requestServer(String url, String method, HttpContent requestHttpContent) {
		initHttpClient();
		HttpMethod httpMethod = null;
		if ("GET".equals(method)) {
			httpMethod = new GetMethod(url);
		} else if ("POST".equals(method)) {
			httpMethod = new PostMethod(url);
			httpMethod.addRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		}
		httpMethod.setFollowRedirects(false);
		if (requestHttpContent != null) {
			Map<String, String> requestHeaderMap = requestHttpContent.getHeaders();
			String requestBody = requestHttpContent.getBody();

			if (requestHeaderMap != null) {
				Iterator<Entry<String, String>> iterator = requestHeaderMap.entrySet().iterator();
				while (iterator.hasNext()) {
					Entry<String, String> entry = iterator.next();
					httpMethod.addRequestHeader(entry.getKey(), entry.getValue());
				}
			}

			if (requestBody != null && "POST".equals(method)) {
				((PostMethod) httpMethod).setRequestBody(requestBody);
			}
		}

		try {
			httpClient.executeMethod(httpMethod);
		} catch (Exception e) {
			throw new NetworkException("Maybe network exception?", e);
		}

		int statusCode = httpMethod.getStatusCode();

		String responseBody = null;
		try {
			responseBody = httpMethod.getResponseBodyAsString();
		} catch (Exception e) {
			throw new NetworkException("Maybe network exception?", e);
		}
		Header[] responseHeaders = httpMethod.getResponseHeaders();
		Map<String, String> responseHeaderMap = new HashMap<String, String>();
		if (responseHeaders != null) {
			for (Header responseHeader : responseHeaders) {
				responseHeaderMap.put(responseHeader.getName(), responseHeader.getValue());
			}
		}
		String location = responseHeaderMap.get("Location");
		location = location == null ? "" : location;
		if (statusCode == HttpStatus.SC_MOVED_TEMPORARILY
				&& (location.indexOf("cas") != -1 || location.indexOf("passport") != -1) 
				&& url.indexOf("cas") == -1 && url.indexOf("passport") == -1) {
			throw new NotLoginException("No valid jsessionid, maybe timeout?");
		} else if (statusCode == HttpStatus.SC_BAD_REQUEST && url.endsWith("/v1/tickets")) {
			throw new LoginFailedException("Wrong account/password!");
		} else if (statusCode == HttpStatus.SC_FORBIDDEN && url.indexOf("techops") != -1) {
			throw new OperationForbiddenException("Operation forbidden, maybe do not have sufficient privileges to perform this operation.");
		}

		HttpContent responseHttpContent = new HttpContent();
		responseHttpContent.setHeaders(responseHeaderMap);
		responseHttpContent.setBody(responseBody);

		httpMethod.releaseConnection();
		return responseHttpContent;
	}

	private void initHttpClient() {
		if (httpClient == null) {
			synchronized (AccessTechOpsApi.class) {
				if (httpClient == null) {
					MultiThreadedHttpConnectionManager manager = new MultiThreadedHttpConnectionManager();
					HttpConnectionManagerParams params = new HttpConnectionManagerParams();
					params.setDefaultMaxConnectionsPerHost(10);
					params.setMaxTotalConnections(50);
					params.setConnectionTimeout(10 * 1000);
					params.setSoTimeout(60 * 1000);
					manager.setParams(params);

					httpClient = new HttpClient(manager);
				}
			}
		}
	}

	public class HttpContent {
		private Map<String, String> headers;
		private String body;

		public Map<String, String> getHeaders() {
			return headers;
		}

		public void setHeaders(Map<String, String> headers) {
			this.headers = headers;
		}

		public String getBody() {
			return body;
		}

		public void setBody(String body) {
			this.body = body;
		}

		public String toString() {
			return "headers: \r\n" + headers + "\r\nbody:\r\n" + body;
		}
	}
}
