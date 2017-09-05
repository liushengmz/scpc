package com.pay.business.util.mail;

import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.sun.mail.util.MailSSLSocketFactory;

public class SslSmtpMailUtil {
	private MimeMessage mimeMsg; //MIME邮件对象
	 
    private Session session; //邮件会话对象
 
    private Properties props; //系统属性
 
    private String username = "support@aijinfu.cn"; //smtp认证用户名和密码
 
    private String password = "U7zSRCxjtGMsr1Rq";
 
    private Multipart mp = new MimeMultipart(); //Multipart对象,邮件内容,标题,附件等内容均添加到其中后再生成MimeMessage对象
 
    public SslSmtpMailUtil() {
        try {
        	MailSSLSocketFactory sf = new MailSSLSocketFactory();  
            sf.setTrustAllHosts(true);  
            //  System.out.println("设置系统属性：mail.smtp.host = "+"mail.tplife.com");
            if (props == null) {
                props = System.getProperties(); //获得系统属性对象
            }
            // 邮件发送协议  
            props.setProperty("mail.transport.protocol", "smtp");  
            // SMTP邮件服务器   
            props.setProperty("mail.smtp.host", "smtp.exmail.qq.com");  
            // SMTP邮件服务器默认端口   
            //props.put("mail.smtp.socketFactory.port", 25);//发信端口  
            props.put("mail.smtp.socketFactory.port", 465);//发信端口  
             // 是否要求身份认证   
            props.setProperty("mail.smtp.auth", "true");  
             // 是否启用调试模式  
            props.setProperty("mail.debug", "true");//设置debug模式  
            props.put("mail.smtp.ssl.enable", "true");//是否开启ssl  
            props.put("mail.smtp.ssl.socketFactory", sf);  
            // 发送邮件协议  
            props.setProperty("mail.smtp.auth", "true");// 需要验证  
            
            session = Session.getDefaultInstance(props,new Authenticator() {  
                protected PasswordAuthentication getPasswordAuthentication() {  
                    return new PasswordAuthentication(username,  
                            password);  
                }  
            }); 
            
            mimeMsg = new MimeMessage(session); //创建MIME邮件对象
        } catch (Exception e) {
            System.err.println("邮件初始化失败！" + e);
        }
    }
 
    public void setSubject(String sub) {
        try {
            mimeMsg.setSubject(sub, "GB2312");
            //   System.out.println("设置邮件标题为：" + sub + ".");
        } catch (Exception e) {
            System.err.println("邮件标题设置失败！" + e);
        }
    }
 
    public void setContent(String text, String type) {
        try {
            BodyPart bp = new MimeBodyPart();
            bp.setContent(text, type);
            mp.addBodyPart(bp);
            //   System.out.println("正在设置邮件内容");
        } catch (Exception e) {
            System.err.println("邮件内容设置失败！" + e);
        }
    }
 
    public void setText(String text) {
        try {
            BodyPart bp = new MimeBodyPart();
            bp.setText(text);
            mp.addBodyPart(bp);
            //  System.out.println("正在设置邮件内容");
        } catch (Exception e) {
            System.err.println("邮件内容设置失败！" + e);
        }
    }
 
 
    public void clearAttach() {
        try {
            int n = mp.getCount();
            System.out.println();
            for (int i = 0; i <= n - 1; i++) {
                mp.removeBodyPart(0);
            }
            mimeMsg.setContent(mp);
            mimeMsg.saveChanges();
        } catch (Exception e) {
            System.err.println("清除附件失败！" + e);
        }
    }
 
    public void setFrom(String address) {
        try {
            mimeMsg.setFrom(new InternetAddress(address)); //设置发信人
            //   System.out.println("正在设置发件人地址");
        } catch (Exception e) {
            System.err.println("邮件发件人地址设置失败！" + e);
        }
    }
 
    public void setRecipients(String address) {
        try {
            mimeMsg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(address)); //收信人
            //  System.out.println("正在设置收件人地址");
        } catch (Exception e) {
            System.err.println("邮件收件人地址设置失败！" + e);
        }
    }
 
    public void setRecipients(String[] address, String type) {
        try {
            Address[] add = new Address[address.length];
            if (address.length - 1 >= 0) {
                for (int i = 0; i <= address.length - 1; i++) {
                    add[i] = new InternetAddress(address[i]);
                }
                if (type == null)
                    type = "TO";
                if (type == "TO")
                    mimeMsg.setRecipients(Message.RecipientType.TO, add);
                else if (type == "CC")
                    mimeMsg.setRecipients(Message.RecipientType.CC, add);
                else if (type == "BCC")
                    mimeMsg.setRecipients(Message.RecipientType.BCC, add);//收信人
                else
                    System.out.println("类型不正确!");
                /// System.out.println("正在设置收件人地址");
            }
        } catch (Exception e) {
            System.err.println("邮件收件人地址设置失败！" + e);
        }
    }
 
    public void setSentDate() {
        try {
            mimeMsg.setSentDate(new Date());
        } catch (Exception e) {
            System.err.println("时间设置失败！" + e);
        }
    }
 
    public boolean sendMail() {
        boolean flag = false;
        try {
            //紧急  
        	mimeMsg.setHeader("X-Priority", "1");  
        	
        	System.out.println("正在发送邮件....");
            // 保存并生成最终的邮件内容   
        	mimeMsg.setContent(mp);
        	mimeMsg.saveChanges();   
            Transport.send(mimeMsg);  
            System.out.println("发送邮件成功！");
            
            flag = true;
        } catch (SendFailedException e1) {
            System.err.println("邮件发送失败SendFailed！" + e1);
            return false;
 
        } catch (MessagingException e2) {
            System.err.println("邮件发送失败Messaging！" + e2);
            return false;
 
        } catch (Exception e3) {
            System.err.println("邮件发送失败！" + e3);
            return false;
        }
 
        return flag;
    }
    
    public static void main(String[] args) {
    	SslSmtpMailUtil ms = new SslSmtpMailUtil();
        ms.setSubject("通道错误【154646546】");
        ms.setText("通道错误【54487666】");
        ms.setFrom("support@aijinfu.cn");
        String []s = new String[3];
		s[0]="shuhongxin@aijinfu.cn";
		s[1]="zhangkui@aijinfu.cn";
		s[2]="qiuguojie@aijinfu.cn";
        ms.setRecipients(s, "TO");
        ms.setSentDate();
        ms.sendMail();
	}
}
