package com.pay.business.util.kftpay;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;

import com.pay.business.util.Base64Util;


public class KeystoreSignProvider
{
	private final X509Certificate	x509Certificate;
	private final PrivateKey		privateKey;

	public KeystoreSignProvider(String kyeStoreType, String keyStorePath,
			char[] keyStorePassword, String alias, char[] keyPassword)
			throws Exception
	{
		this.x509Certificate = (X509Certificate) getCertificate(kyeStoreType,
				keyStorePath, keyStorePassword, alias);
		this.privateKey = getPrivateKeyByKeyStore(kyeStoreType, keyStorePath,
				keyStorePassword, alias, keyPassword);
	}

	public String sign(byte[] plaintext, Charset charset) throws Exception
	{
		byte[] signatureInfo = signByX509Certificate(plaintext,
				this.x509Certificate, this.privateKey);
		return Base64Util.encodeToString(signatureInfo,false);
	}

	public static final String CERTIFICATE_TYPE = "X.509";

	public static byte[] signByX509Certificate(String keystoreType, byte[] data,
			String keyStorePath, char[] keyStorePassword, String alias,
			char[] keyPassword) throws Exception
	{
		KeyStore ks = loadKeyStore(keyStorePath,
				keyStorePassword, keystoreType);
		if (alias == null)
		{
			ArrayList<String> aliases;
			if ((aliases = Collections.list(ks.aliases())).size() != 1)
			{
				throw new IllegalArgumentException(
						"[Assertion failed] - this String argument[alias] must have text; it must not be null, empty, or blank");
			}
			alias = aliases.get(0);
		}
		X509Certificate x509Certificate = (X509Certificate) ks
				.getCertificate(alias);
		Signature signature = Signature
				.getInstance(x509Certificate.getSigAlgName());
		PrivateKey privateKey = (PrivateKey) ks.getKey(alias, keyPassword);
		signature.initSign(privateKey);
		signature.update(data);
		return signature.sign();
	}
	

	public static byte[] signByX509Certificate(byte[] data,
			X509Certificate x509Certificate, PrivateKey privateKey)
			throws Exception
	{
		Signature signature = Signature
				.getInstance(x509Certificate.getSigAlgName());
		signature.initSign(privateKey);
		signature.update(data);
		return signature.sign();
	}

	public static Certificate getCertificate(byte[] certificateContent)
			throws Exception
	{
		ByteArrayInputStream inputStream = new ByteArrayInputStream(
				certificateContent);
		try
		{
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			Certificate certificate =  cf.generateCertificate(inputStream);
			return certificate;
		}
		finally
		{
			if (inputStream != null)
			{
				inputStream.close();
			}
		}
	}

	public static PrivateKey getPrivateKeyByKeyStore(String keystoreType,
			String keyStorePath, char[] keyStorePassword, String alias,
			char[] keyPassword) throws Exception
	{
		KeyStore ks = loadKeyStore(keyStorePath,
				keyStorePassword, keystoreType);
		if (alias == null)
		{
			ArrayList<String> aliases;
			if ((aliases = Collections.list(ks.aliases())).size() != 1)
			{
				throw new IllegalArgumentException(
						"[Assertion failed] - this String argument[alias] must have text; it must not be null, empty, or blank");
			}
			alias = aliases.get(0);
		}
		PrivateKey key = (PrivateKey) ks.getKey(alias, keyPassword);
		return key;
	}

	public static KeyStore loadKeyStore(String keyStorePath, char[] password,
			String keystoreType) throws Exception
	{
		KeyStore ks = KeyStore.getInstance(keystoreType == null
				? KeyStore.getDefaultType() : keystoreType);
		File keystore = new File(keyStorePath);
		if (keystore == null || keystore.exists() && keystore.isDirectory())
		{
			throw new IllegalArgumentException("keystore[" + keyStorePath
					+ "]\u5fc5\u987b\u662f\u4e00\u4e2a\u5df2\u7ecf\u5b58\u5728\u7684\u6587\u4ef6,\u4e0d\u80fd\u4e3a\u7a7a,\u4e14\u4e0d\u80fd\u662f\u4e00\u4e2a\u6587\u4ef6\u5939");
		}
		FileInputStream is = null;
		try
		{
			is = new FileInputStream(keystore);
			ks.load(is, password);
			KeyStore keyStore = ks;
			return keyStore;
		}
		finally
		{
			if (is != null)
			{
				is.close();
			}
		}
	}

	public static Certificate getCertificate(String certificatePath)
			throws Exception
	{
		File certificate = new File(certificatePath);
		if (certificate == null
				|| certificate.exists() && certificate.isDirectory())
		{
			throw new IllegalArgumentException("certificatePath["
					+ certificatePath
					+ "]\u5fc5\u987b\u662f\u4e00\u4e2a\u5df2\u7ecf\u5b58\u5728\u7684\u6587\u4ef6,\u4e0d\u80fd\u4e3a\u7a7a,\u4e14\u4e0d\u80fd\u662f\u4e00\u4e2a\u6587\u4ef6\u5939");
		}
		FileInputStream inputStream = null;
		try
		{
			inputStream = new FileInputStream(certificate);
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			Certificate certificate2 = cf.generateCertificate(inputStream);
			return certificate2;
		}
		finally
		{
			if (inputStream != null)
			{
				inputStream.close();
			}
		}
	}

	public static Certificate getCertificate(String keystoreType,
			String keyStorePath, char[] keyStorePassword, String alias)
			throws Exception
	{
		ArrayList<String> aliases;
		KeyStore ks = loadKeyStore(keyStorePath,
				keyStorePassword, keystoreType);
		if (alias != null)
			return ks.getCertificate(alias);
		if ((aliases = Collections.list(ks.aliases())).size() != 1)
		{
			throw new IllegalArgumentException(
					"[Assertion failed] - this String argument[alias] must have text; it must not be null, empty, or blank");
		}
		alias = aliases.get(0);
		return ks.getCertificate(alias);
	}

	public static Certificate getSignature(String keystoreType,
			String keyStorePath, char[] keyStorePassword, String alias)
			throws Exception
	{
		ArrayList<String> aliases;
		KeyStore ks = loadKeyStore(keyStorePath,
				keyStorePassword, keystoreType);
		if (alias != null)
			return ks.getCertificate(alias);
		if ((aliases = Collections.list(ks.aliases())).size() != 1)
		{
			throw new IllegalArgumentException(
					"[Assertion failed] - this String argument[alias] must have text; it must not be null, empty, or blank");
		}
		alias = aliases.get(0);
		return ks.getCertificate(alias);
	}

	public static boolean verifySign(byte[] data, byte[] sign,
			String keystoreType, String keyStorePath, char[] keyStorePassword,
			String alias) throws Exception
	{
		X509Certificate certificate = (X509Certificate) getCertificate(keystoreType, keyStorePath, keyStorePassword,
						alias);
		Signature signature = Signature
				.getInstance(certificate.getSigAlgName());
		signature.initVerify(certificate);
		signature.update(data);
		return signature.verify(sign);
	}

	public static boolean verifySign(byte[] data, byte[] sign,
			X509Certificate x509certificate) throws Exception
	{
		Signature signature = Signature
				.getInstance(x509certificate.getSigAlgName());
		signature.initVerify(x509certificate);
		signature.update(data);
		return signature.verify(sign);
	}

	public static boolean verifySign(byte[] data, byte[] sign,
			String certificatePath) throws Exception
	{
		X509Certificate certificate = (X509Certificate)getCertificate(certificatePath);
		Signature signature = Signature
				.getInstance(certificate.getSigAlgName());
		signature.initVerify(certificate);
		signature.update(data);
		return signature.verify(sign);
	}

	public static boolean verifySign(byte[] data, byte[] sign,
			byte[] certificateContent) throws Exception
	{
		X509Certificate certificate = (X509Certificate)getCertificate(certificateContent);
		Signature signature = Signature
				.getInstance(certificate.getSigAlgName());
		signature.initVerify(certificate);
		signature.update(data);
		return signature.verify(sign);
	}

}
