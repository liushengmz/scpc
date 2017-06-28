package com.dianfu.logical;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import com.shotgun.Tools.Funcs;

public class HttpSendData implements Runnable {

	private String _url, _linkid;
	private String _queryStr;

	public HttpSendData(String pfxUrl, HashMap<String, String> data) {
		_linkid = data.getOrDefault("linkid", "");
		_url = pfxUrl;
		_queryStr = Funcs.urlEncode(data);

	}

	public String getUrl() {
		return _url;
	}

	public String getLinkId() {
		return _linkid;
	}

	public String getUrlWithData() {

		if (Funcs.isNullOrEmpty(_url))
			return "null?" + _queryStr;
		if (_url.contains("?"))
			return _url + "&" + _queryStr;
		return _url + "?" + _queryStr;
	}

	@Override
	public void run() {
		if (Funcs.isNullOrEmpty(_url) || _url.startsWith("#")) {
			WriteLog("虚拟推送");
			return;
		}
		if (isLocal()) {
			WriteLog("本地测试不发送请求，请删除LocalFlag.txt文件");
			return;
		}

		String url = getUrlWithData();
		URL realUrl;
		HttpURLConnection web = null;
		InputStream stm = null;
		Long st = null, et = null;
		int code = 0;
		String result = null;
		try {
			realUrl = new URL("http://baidu.com?" + url);
			web = (HttpURLConnection) realUrl.openConnection();
			web.setConnectTimeout(3000);
			web.setReadTimeout(3000);
			web.setInstanceFollowRedirects(false);
			st = System.currentTimeMillis();

			web.connect();
			code = web.getResponseCode();
			stm = web.getInputStream();

		} catch (IOException e) {
			try {
				code = web.getResponseCode();
				stm = web.getErrorStream();
			} catch (Exception e1) {
				stm = null;
			}
		}

		try {
			if (stm != null) {
				BufferedReader in = new BufferedReader(new InputStreamReader(stm));
				result = in.readLine();
				et = System.currentTimeMillis();
			}
		} catch (Exception e) {

		} finally {
			if (stm != null) {
				try {
					stm.close();
				} catch (IOException e1) {
				}
			}
			if (web != null)
				web.disconnect();
		}
		et = System.currentTimeMillis();
		if (Funcs.isNullOrEmpty(result))
			result = "<NULL Response>";
		else if (result.length() > 512)
			result = result.substring(0, 509) + "...";

		BasePusher.logger.info(String.format("%s %s http_code:%d %dms %s", _linkid, url, code, et - st, result));

	}

	/** 表示是否处理本地测试环境 */
	private boolean isLocal() {
		File file = new File("LocalFlag.txt");
		return file.exists();
	}

	private void WriteLog(String msg) {
		BasePusher.logger.info(String.format("%s %s http_code:%d %dms %s", _linkid, getUrlWithData(), 0, 0, msg));
	}

}
