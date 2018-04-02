
package com.pay.business.util.kftpay;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pay.business.util.Base64Util;

public class NotifySignUtil
{
	public static boolean verifySign(String cerPath, Map<String, String> param,
			String signatureInfo)
	{
		System.out.println("cerPath:" + cerPath);
		boolean verifySign = false;
		try
		{
			Map<String, String> parameters = paramsFilter(param);

			String createLinkString = createLinkString(parameters);

			byte[] datas = createLinkString.getBytes("UTF-8");
			// 得到的报文中的签
			String signMsg = signatureInfo;

			byte[] sign = Base64Util.decodeFast(URLDecoder.decode(signMsg, "UTF-8"));

			verifySign = verifySign(datas, sign, cerPath);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return verifySign;
	}

	private static boolean verifySign(byte[] data, byte[] sign,
			String certificateContent) throws Exception
	{
		X509Certificate certificate = (X509Certificate) getCertificate(
				certificateContent);

		Signature signature = Signature
				.getInstance(certificate.getSigAlgName());

		// 由证书初始化签名,使用证书中的公钥
		signature.initVerify(certificate);

		signature.update(data);

		return signature.verify(sign);
	}

	private static Map<String, String> paramsFilter(
			final Map<String, String> parameters)
	{
		if (parameters == null || parameters.size() == 0)
		{
			return new HashMap<String, String>();
		}
		final Map<String, String> result = new HashMap<String, String>(
				parameters.size());
		String value = null;
		for (final String key : parameters.keySet())
		{
			value = parameters.get(key);
			if (value == null || "".equals(value)
					|| key.equalsIgnoreCase("signatureAlgorithm")
					|| key.equalsIgnoreCase("signatureInfo"))
			{
				continue;
			}
			result.put(key, value);
		}
		return result;
	}

	private static Certificate getCertificate(String certificatePath)
			throws Exception
	{
		File certificate = new File(certificatePath);
		if (certificate == null
				|| (certificate.exists() && certificate.isDirectory()))
		{
			throw new IllegalArgumentException("certificatePath["
					+ certificatePath + "]必须是一个已经存在的文件,不能为空,且不能是一个文件夹");
		}
		InputStream inputStream = null;
		try
		{
			inputStream = new FileInputStream(certificate);
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			Certificate cert = cf.generateCertificate(inputStream);
			return cert;
		}
		finally
		{
			if (inputStream != null)
			{
				inputStream.close();
			}
		}
	}

	private static String createLinkString(final Map<String, String> params)
	{

		final List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);
		final StringBuilder sb = new StringBuilder();
		String key = null;
		String value = null;
		for (int i = 0; i < keys.size(); i++)
		{
			key = keys.get(i);
			value = params.get(key);
			sb.append(key).append("=").append(value);
			if (i < keys.size() - 1)
			{
				sb.append("&");
			}
		}
		return sb.toString();
	}
	
	public static boolean testVerifySign(String cerFilePath,String postData)
	{
		Map<String, String> params = new HashMap<String, String>();
		
		for(String s : postData.split("&"))
		{
			String[] keyValue = s.split("=");
			
			if(keyValue.length==2)
				params.put(keyValue[0], keyValue[1]);
			else
				params.put(keyValue[0], "");
		}
		
		return verifySign(cerFilePath, params, params.get("signatureInfo"));
	}
	
	public static void main(String[] args)
	{
		//TEST
		String cerFilePath = "D:/20KFT.cer";
		String postData = "orderNo=ON1521430563479&signatureInfo=IF51HDSNj5eYuugVI1wpHGjGGJ0wYTGa6snf0AiFBzEGPUPWyCwjZaIAMu2qYPkX%2B%2FCwBZQVFIhMsSeKHqKMHaLapeb8j8jzguuMtOznPNasMOtJbYeJm%2FhjAjBXQvzCJrbihi7KgX2w0Qt2uJ9IFS%2F4hFelTA09%2BRkafnfTIFIycm6ILnrRt6ymiaqPSrLlCsoKx6a3w6baqOHSGOn8m7gaWRYrFYxPogxMQHEN19gQe5TdNJl2CJ4p27HQeS%2F3O4pJIk1lKmGFlzLwMlR48Mrp7OPQtbT9PZy0xllWUcEphy5tPOfRPJtaPX4D5dph10eFrMo%2FM3ZlQHoc408jBA%3D%3D&status=1&callerIp=10.37.20.46&signatureAlgorithm=RSA&reconStatus=1&settlementAmount=1&checkDate=20180319&language=zh_CN&channelNo=4100000877625774762745372479488";
		
		testVerifySign(cerFilePath,postData);
		
		//PRODUCT
		cerFilePath = "D:/ZS_HD_PRODUCT.cer";
		postData = "orderNo=DD201803221126594720355&signatureInfo=aB8WH9vQGKco74%2FZmdfcWqn608MukFmWgrvV5kOKmXUhuG8uTUn7K%2B8ozoK8QrJDVF%2B4hrsQeCeVZaIK1bIRvIT73i3uS%2BeDdZBYDHuEMSTZGKsz04xRkGB7pKIdEIcDrBVRxzaor7Dln6n0qzEO7SaOwQhV9RQvnN4AMZ3N9O4njLkZuh%2F9%2BJtW%2FAys5eyCKm%2FIRmp88YspA7k6ZRpyDfi3uI6sm9%2Fnh7wMyPm8McDcdlVdReRZFqKoZrkUDhkR2dBY61D98asS0Cd%2F1bhUToicJQWlWQmtGZbl2IqwczMLjHj7QvZUvvnr2OBZaTDo3%2BTFLRRxmcOeTdnWxJLQfA%3D%3D&status=1&callerIp=172.28.6.51&signatureAlgorithm=RSA&reconStatus=1&settlementAmount=2&checkDate=20180322&language=zh_CN&channelNo=91521689220025348908";
		
		testVerifySign(cerFilePath,postData);
		
		cerFilePath = "D:\\ZS_HD_PRODUCT.cer";
		postData = "orderNo=DD201803221126594720355&signatureInfo=aB8WH9vQGKco74%2FZmdfcWqn608MukFmWgrvV5kOKmXUhuG8uTUn7K%2B8ozoK8QrJDVF%2B4hrsQeCeVZaIK1bIRvIT73i3uS%2BeDdZBYDHuEMSTZGKsz04xRkGB7pKIdEIcDrBVRxzaor7Dln6n0qzEO7SaOwQhV9RQvnN4AMZ3N9O4njLkZuh%2F9%2BJtW%2FAys5eyCKm%2FIRmp88YspA7k6ZRpyDfi3uI6sm9%2Fnh7wMyPm8McDcdlVdReRZFqKoZrkUDhkR2dBY61D98asS0Cd%2F1bhUToicJQWlWQmtGZbl2IqwczMLjHj7QvZUvvnr2OBZaTDo3%2BTFLRRxmcOeTdnWxJLQfA%3D%3D&status=1&callerIp=172.28.6.51&signatureAlgorithm=RSA&reconStatus=1&settlementAmount=2&checkDate=20180322&language=zh_CN&channelNo=91521689220025348908";
		
		testVerifySign(cerFilePath,postData);
	}
	
}
