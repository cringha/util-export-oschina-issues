package com.siemens.ct.its.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
 

public class HttpUtil {

	static final ConnectionKeepAliveStrategy HTTP_CONNECTION_KEEP_ALIVE_STRATEGY = new ConnectionKeepAliveStrategy() {

		public long getKeepAliveDuration(HttpResponse response,
				HttpContext context) {
			return HTTP_CONNECTION_TIMEOUT * 1000;
		}

	};

	public static int HTTP_CONNECTION_TIMEOUT = 5;

	public static class Proxy {
		String host;
		int port;

		public Proxy(String host, int port) {

			this.host = host;
			this.port = port;
		}

		public String getHost() {
			return host;
		}

		public int getPort() {
			return port;
		}
	}

	public static Proxy parseProxy(String proxy) {
		if (StringUtil.isEmpty(proxy))
			return null;
		String[] pxs = proxy.split(":");
		if (pxs.length == 2) {
			return new Proxy(pxs[0], Integer.parseInt(pxs[1]));
		}

		return null;
	}

	final static Logger LOG = Logger.getLogger(HttpUtil.class);

	public static String getHttpContent(String url)
			throws ClientProtocolException, IOException {
		return getHttpContent(url, null, 0);

	}

	public static String getHttpContent(String url, String proxy)
			throws ClientProtocolException, IOException {
		Proxy _proxy = parseProxy(proxy);
		if (_proxy == null)
			return getHttpContent(url, null, 0);
		else
			return getHttpContent(url, _proxy.getHost(), _proxy.getPort());

	}

	public static String getHttpContent(String url, String proxy, int port)
			throws ClientProtocolException, IOException {
		return getHttpContent(url, proxy, port, null);
	}

	private static CloseableHttpClient _client = null;

	public static CloseableHttpClient getClient(String proxy, int port) {

		if (_client == null) {
			if (proxy != null) {

				HttpHost proxyHost = new HttpHost(proxy, port);
				DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(
						proxyHost);

				RequestConfig cfg = RequestConfig.custom()
						.setConnectTimeout(HTTP_CONNECTION_TIMEOUT * 1000)

						.setSocketTimeout(HTTP_CONNECTION_TIMEOUT * 1000)
						.build();

				_client = HttpClients
						.custom()
						.setRoutePlanner(routePlanner)
						.setDefaultRequestConfig(cfg)

						.setKeepAliveStrategy(
								HTTP_CONNECTION_KEEP_ALIVE_STRATEGY).build();

			} else {
				RequestConfig cfg = RequestConfig.custom()
						.setConnectTimeout(HTTP_CONNECTION_TIMEOUT * 1000)
						.setSocketTimeout(HTTP_CONNECTION_TIMEOUT * 1000)
						.build();

				// httpclient = HttpClients.createDefault();
				_client = HttpClients
						.custom()
						.setKeepAliveStrategy(
								HTTP_CONNECTION_KEEP_ALIVE_STRATEGY)
						.setDefaultRequestConfig(cfg).build();
			}
		}
		return _client;
	}

	public static void closeClient() {
		if (_client != null) {
			try {
				_client.close();
			} catch (Exception e) {
				LOG.warn(e);
			}
			_client = null;
		}
	}

	public static String getHttpContent(String url, String proxy, int port,
			Properties props) throws ClientProtocolException, IOException {

		HttpGet httpget = new HttpGet(url);
		if (props != null) {
			String cookie = props.getProperty("Cookie");
			if (!StringUtil.isEmpty(cookie)) {
				httpget.setHeader("Cookie", cookie);
			}
		}

		LOG.debug("executing request " + httpget.getURI());

		// Create a custom response handler
		ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
			public String handleResponse(final HttpResponse response)
					throws ClientProtocolException, IOException {
				int status = response.getStatusLine().getStatusCode();
				if (status >= 200 && status < 300) {
					HttpEntity entity = response.getEntity();
					return entity != null ? EntityUtils.toString(entity) : null;
				} else {
					throw new ClientProtocolException(
							"Unexpected response status: " + status);
				}
			}

		};
		String responseBody = getClient(proxy, port).execute(httpget,
				responseHandler);
		return responseBody;

	}

	public static byte[] getHttpContentBuff(String url, String proxy, int port,
			Properties props) throws ClientProtocolException, IOException {

		HttpGet httpget = new HttpGet(url);
		if (props != null) {
			String cookie = props.getProperty("Cookie");
			if (!StringUtil.isEmpty(cookie)) {
				httpget.setHeader("Cookie", cookie);
			}
		}

		LOG.debug("executing request " + httpget.getURI());

		// Create a custom response handler
		ResponseHandler<byte[]> responseHandler = new ResponseHandler<byte[]>() {
			public byte[] handleResponse(final HttpResponse response)
					throws ClientProtocolException, IOException {
				int status = response.getStatusLine().getStatusCode();
				if (status >= 200 && status < 300) {
					HttpEntity entity = response.getEntity();
					return entity != null ? EntityUtils.toByteArray(entity)
							: null;
				} else {
					throw new ClientProtocolException(
							"Unexpected response status: " + status);
				}
			}

		};
		byte[] responseBody = getClient(proxy, port).execute(httpget,
				responseHandler);
		return responseBody;

	}

	public static String getLogonAuthority(String proxy, int port, String user,
			String passwd) throws ClientProtocolException, IOException {

		HttpPost httpost = new HttpPost("http://git.oschina.net/login");

		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("authenticity_token", "111"));
		nvps.add(new BasicNameValuePair("utf8", "✓"));
		nvps.add(new BasicNameValuePair("user[login]", user));
		nvps.add(new BasicNameValuePair("user[password]", passwd));
		nvps.add(new BasicNameValuePair("user[remember_me]", "0"));

		httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

		LOG.debug("executing request " + httpost.getURI());

		HttpResponse response = getClient(proxy, port).execute(httpost);
		HttpEntity entity = response.getEntity();

		int status = response.getStatusLine().getStatusCode();

		if (status >= 400)
			throw new ClientProtocolException("Unexpected response status: "
					+ status);

		String cookie = null;
		Header[] headers = response.getAllHeaders();
		for (int i = 0; i < headers.length; i++) {
			Header h = headers[i];
			if (h.getName().equals("Set-Cookie"))
				cookie = h.getValue();
		}

		if (entity != null) {
			String content = EntityUtils.toString(entity);
		}

		return cookie;

	}

}
