
package com.pay.business.util.kftpay;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.log4j.Logger;

import com.pay.business.util.StringUtil;

public class NetUtil
{
	private static Logger		logger				= Logger
			.getLogger(NetUtil.class);

	private static final int	REQUEST_EXPIRE_MILS	= 5000;

	public static String requetUrl(String url, Map<String, String> headers,
			String postData, String chartSet)
	{
		StringBuffer result = new StringBuffer();

		BufferedReader br = null;
		InputStreamReader isr = null;
		InputStream is = null;

		OutputStream os = null;

		String defaultChartSet = StringUtil.isNullOrEmpty(chartSet) ? "UTF-8"
				: chartSet;

		try
		{
			URL realUrl = new URL(url);

			URLConnection conn = null;

			if (realUrl.getProtocol().toUpperCase().equals("HTTPS"))
			{
				trustAllHosts();
				HttpsURLConnection httpsCont = (HttpsURLConnection) realUrl
						.openConnection();
				httpsCont.setHostnameVerifier(NetUtil.DO_NOT_VERIFY);
				conn = httpsCont;
			}
			else
			{
				conn = realUrl.openConnection();
			}

			realUrl.openConnection();

			conn.setConnectTimeout(REQUEST_EXPIRE_MILS);

			conn.setReadTimeout(REQUEST_EXPIRE_MILS);

			conn.setRequestProperty("accept", "*/*");

			conn.setRequestProperty("connection", "Keep-Alive");

			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");

			if (headers != null)
			{
				for (String key : headers.keySet())
				{
					conn.setRequestProperty(key, headers.get(key));
				}
			}

			if (!StringUtil.isNullOrEmpty(postData))
			{
				conn.setDoOutput(true);
			}

			conn.connect();

			if (!StringUtil.isNullOrEmpty(postData))
			{
				os = conn.getOutputStream();
				os.write(postData.getBytes(defaultChartSet));
				os.flush();
			}

			is = conn.getInputStream();

			isr = new InputStreamReader(is, defaultChartSet);

			br = new BufferedReader(isr);

			String line = null;

			while ((line = br.readLine()) != null)
			{
				result.append(line);
			}
		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
		}
		finally
		{
			try
			{
				if (os != null)
					os.close();
			}
			catch (Exception ex)
			{
			}
			try
			{
				if (br != null)
					br.close();
			}
			catch (Exception ex)
			{
			}
			try
			{
				if (isr != null)
					isr.close();
			}
			catch (Exception ex)
			{
			}
			try
			{
				if (is != null)
					is.close();
			}
			catch (Exception ex)
			{
			}
		}
		return result.toString().trim();
	}


	public static void trustAllHosts()
	{
		TrustManager[] trustAllCerts = new TrustManager[] {
				new X509TrustManager()
				{
					public java.security.cert.X509Certificate[] getAcceptedIssuers()
					{
						return new java.security.cert.X509Certificate[] {};
					}

					public void checkClientTrusted(X509Certificate[] chain,
							String authType) throws CertificateException
					{
					}

					public void checkServerTrusted(X509Certificate[] chain,
							String authType) throws CertificateException
					{
					}
				} };

		try
		{
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection
					.setDefaultSSLSocketFactory(sc.getSocketFactory());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static final HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier()
	{
		@Override
		public boolean verify(String hostname, SSLSession session)
		{
			return true;
		}
	};

}
