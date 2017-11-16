
package com.andy.system.util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailUtil
{
	/**
	 * 发送文本邮件
	 * 
	 * @param subject
	 *            邮件标题
	 * @param txtBody
	 *            文件格式的邮件件内容
	 * @param address
	 *            收件人，多个收件人私密发送（单独）
	 */
	public static boolean SendTextMail(String subject, String txtBody,
			String[] address)
	{
		return SmtpMail(subject, txtBody, address, false,
				PropertyConfigUtil.getConfigData("MAIL_REPLY"));
	}

	/**
	 * 发送HTML格式邮件
	 * 
	 * @param subject
	 *            邮件标题
	 * @param htmlBody
	 *            Html格式的邮件件内容
	 * @param address
	 *            收件人，多个收件人私密发送（单独）
	 */
	public static boolean SendHtmlMail(String subject, String htmlBody,
			String[] address)
	{
		return SmtpMail(subject, htmlBody, address, true,
				PropertyConfigUtil.getConfigData("MAIL_REPLY"));
	}

	/**
	 * 邮件发送
	 * 
	 * @param subject
	 *            邮件主题
	 * @param body
	 *            邮件内容
	 * @param address
	 *            收件人，多个收件人私密发送（单独）
	 * @param isHtml
	 *            是否以HTML格式发送邮件
	 * @param reply
	 *            接收邮件回复的邮箱
	 * @return
	 */
	private static Boolean SmtpMail(String subject, String body,
			String[] address, boolean isHtml, String reply)
	{
		String host = PropertyConfigUtil.getConfigData("MAIL_HOST");
		String account = PropertyConfigUtil.getConfigData("MAIL_ACCOUNT");
		String password = PropertyConfigUtil.getConfigData("MAIL_PASSWORD");

		Properties prop = new Properties();
		prop.setProperty("mail.host", PropertyConfigUtil.getConfigData("MAIL_HOST"));
		prop.setProperty("mail.transport.protocol", "smtp");
		prop.setProperty("mail.smtp.auth", "true");
		// 使用JavaMail发送邮件的5个步骤
		// 1、创建session
		Session session = Session.getInstance(prop);
		// 开启Session的debug模式，这样就可以查看到程序发送Email的运行状态
		// session.setDebug(true);
		// 2、通过session得到transport对象
		Transport ts = null;

		try
		{
			ts = session.getTransport();
			// 3、使用邮箱的用户名和密码连上邮件服务器，发送邮件时，发件人需要提交邮箱的用户名和密码给smtp服务器，用户名和密码都通过验证之后才能够正常发送邮件给收件人。
			ts.connect(host, account, password);
			// 4、创建邮件
			Message message = new MimeMessage(session);

			// 指明邮件的发件人
			message.setFrom(new InternetAddress(account));
			// 指明邮件的收件人，现在发件人和收件人是一样的，那就是自己给自己发
			for (String addr : address)
			{
				message.addRecipient(Message.RecipientType.BCC,
						new InternetAddress(addr));
			}
			if (!StringUtil.isNullOrEmpty(reply))
				message.setReplyTo(
						new InternetAddress[] { new InternetAddress(reply) });

			// 邮件的标题
			message.setSubject(subject);
			// 邮件的文本内容
			if (isHtml)
				message.setContent(body, "text/html;charset=UTF-8");
			else
				message.setContent(body, "text/plain;charset=UTF-8");

			// 5、发送邮件
			ts.sendMessage(message, message.getAllRecipients());
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return false;
		}
		finally
		{
			if (ts != null)
				try
				{
					ts.close();
				}
				catch (MessagingException e)
				{
				}
		}

		return true;
	}

}
