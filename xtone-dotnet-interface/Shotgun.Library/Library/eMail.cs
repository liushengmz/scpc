using System;
using System.Collections.Generic;
using System.Text;
using System.Net.Mail;
using System.Web.Security;
using System.Web.Configuration;
using System.Configuration;
using System.Text.RegularExpressions;

namespace Shotgun.Library
{
    /// <summary>
    /// 静态成公用的函数
    /// </summary>
    public static partial class Static
    {



        /// <summary>
        /// 使用默认的邮件发送者发送邮件
        /// </summary>
        /// <param name="mail"></param>
        /// <returns></returns>
        public static bool SendMail(string MailTo, string title, string body, bool isHtml)
        {
            MailMessage mail = new MailMessage();
            mail.To.Add(new MailAddress(MailTo));
            mail.Subject = title;
            mail.IsBodyHtml = isHtml;
            mail.Body = body;
            mail.Subject = title;
            mail.From = new MailAddress(System.Configuration.ConfigurationManager.AppSettings["FocusSendSender"]);
            try
            {
                return SendSmtpMail(mail);
            }
            finally { mail.Dispose(); }
        }

        /// <summary>
        /// 使用传统的smtp发送邮件
        /// </summary>
        /// <param name="mail"></param>
        /// <returns></returns>
        public static bool SendSmtpMail(MailMessage mail)
        {
            return SendSmtpMail(mail, "SmtpMail");
        }

        /// <summary>
        /// 使用传统的smtp发送邮件
        /// </summary>
        /// <param name="MailTo">多个接收人用逗号分开</param>
        /// <returns></returns>
        public static bool SendSmtpMail(string MailTo, string title, string body, bool isHtml)
        {
            return SendSmtpMail(MailTo, title, body, isHtml, "SmtpMail");
        }

        /// <summary>
        /// 使用传统的smtp发送邮件
        /// </summary>
        /// <param name="MailTo">多个接收人用逗号分开</param>
        /// <param name="title"></param>
        /// <param name="body"></param>
        /// <param name="isHtml"></param>
        /// <param name="SettingKey">采用那一个appSettings->KeyName</param>
        /// <returns></returns>
        public static bool SendSmtpMail(string MailTo, string title, string body, bool isHtml, string SettingKey)
        {
            MailMessage mail = new MailMessage();

            if (!MailTo.Contains(","))
                mail.To.Add(new MailAddress(MailTo));
            else//多个接收者
            {
                string[] Mts = MailTo.Split(new char[] { ',' }, StringSplitOptions.RemoveEmptyEntries);
                foreach (string s in Mts)
                {
                    mail.To.Add(new MailAddress(s.Trim()));
                }
            }

            mail.Subject = title;
            mail.IsBodyHtml = isHtml;
            mail.Body = body;
            mail.Subject = title;
            try
            {
                return SendSmtpMail(mail, SettingKey);
            }
            finally
            {
                mail.Dispose();
            }

        }

        /// <summary>
        /// 使用传统的smtp发送邮件
        /// </summary>
        /// <param name="mail"></param>
        /// <param name="SettingKey">采用那一个appSettings->KeyName</param>
        /// <returns></returns>
        public static bool SendSmtpMail(MailMessage mail, string SettingKey)
        {
            string[] accounts = ConfigurationManager.AppSettings[SettingKey].Split(new char[] { '|' });
            string account;
            if (accounts.Length == 0)
                return false;
            if (accounts.Length > 1)
                account = accounts[((DateTime.Now.Ticks >> 16) & 0xFFFF) % accounts.Length];
            else
                account = accounts[0];

            accounts = account.Split(new char[] { ',' });

            string smtpServer, UserName, Passowrd;
            smtpServer = accounts[0];
            UserName = accounts[1];
            Passowrd = accounts[2];
            if (accounts.Length >= 4)//发送者
                mail.From = new MailAddress(accounts[3]);
            else
                mail.From = new MailAddress(accounts[1]);

            if (accounts.Length >= 5)//回复
            {
                //mail.ReplyTo = 
                mail.ReplyToList.Add(new MailAddress(accounts[4]));
            }
            System.Net.Mail.SmtpClient smtp = new SmtpClient();
            smtp.Host = smtpServer;
            smtp.Credentials = new System.Net.NetworkCredential(UserName, Passowrd);
            smtp.DeliveryMethod = SmtpDeliveryMethod.Network;
#if  !DEBUG
            try
            {
                smtp.Send(mail);
                SimpleLogRecord.WriteLog(SettingKey, string.Format("success,{0},{1},{2}", mail.From.ToString(), mail.To.ToString(), mail.Subject));
            }
            catch(Exception ex) {
                SimpleLogRecord.WriteLog(SettingKey, string.Format("fail,{0},{1},{2},UID:{3},Psw:{4},host:{5},err{6}", mail.From.ToString(), mail.To.ToString(), mail.Subject, UserName, Passowrd, smtpServer, ex.ToString()));
                return false; 
            }
#else
            smtp.Send(mail);
#endif
            return true;
        }


        /// <summary>
        /// 把字符格式日期,转化为Int32的日期
        /// 有简单合法性检查,如2015/07/32
        /// </summary>
        /// <param name="p">日期串,支持格式:yyyy/mm/dd yyyy/mm</param>
        /// <returns></returns>
        public static int ToDigiDate(string p)
        {
            if (string.IsNullOrEmpty(p))
                return 0;
            var ps = p.Split(new char[] { '-', '/' }, StringSplitOptions.RemoveEmptyEntries);
            if (ps.Length < 2 || ps.Length > 3)
                return 0;

            int d, ret;
            if (!int.TryParse(ps[0], out ret))
                return 0;
            if (!int.TryParse(ps[1], out d))
                return 0;
            if (d > 12 || d < 1)
                return 0;
            ret = ret * 100 + d;
            if (ps.Length == 2) //年月格式
                return ret * 100;
            if (!int.TryParse(ps[2], out d))
                return 0;
            if (d > 31 || d < 1)
                return 0;
            return ret * 100 + d;
        }
    }
}
