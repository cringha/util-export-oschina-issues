package com.siemens.ct.its.util;

import java.util.Properties;

public class HttpEngine {
	String proxy = null; // "127.0.0.1";
	int proxyPort = 5080;

	String cookie = null;

	static Properties props = null;

	// String user;

	Properties getProperties() {
		if (props == null) {
			props = new Properties();
			props.setProperty("Cookie", cookie);
			HttpUtil.HTTP_CONNECTION_TIMEOUT = 15;
		}
		return props;
	}

	public HttpEngine() {

	}

	public HttpEngine(String proxy, int proxyPort) {
		this.proxy = proxy;
		this.proxyPort = proxyPort;
	}

	/**
	 * login git.oschina.com
	 * 
	 * @param user
	 * @param passwd
	 * @return
	 * @throws Exception
	 */
	public boolean login(String user, String passwd) throws Exception {
		cookie = HttpUtil.getLogonAuthority(proxy, proxyPort, user, passwd);
		if (cookie != null)
			return true;
		return false;
	}

	/**
	 * get image content
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public byte[] loadImage(String url) throws Exception {
		byte[] image = HttpUtil.getHttpContentBuff(url, proxy, proxyPort,
				getProperties());
		return image;
	}

	public String loadPage(String url) throws Exception {

		String msg = HttpUtil.getHttpContent(url, proxy, proxyPort,
				getProperties());

		return msg;
	}

}
