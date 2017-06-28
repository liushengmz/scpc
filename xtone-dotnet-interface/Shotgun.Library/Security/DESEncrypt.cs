using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Security.Cryptography;
using System.IO;

namespace Shotgun.Security
{
    /// <summary>
    /// 简化的 .DESCryptoServiceProvider(System.Security.Cryptography)
    /// </summary>
    public class DESEncrypt
    {
        private string _iv = "20130816";
        private string _key = "Shotgun.";
        private DES _des = new DESCryptoServiceProvider();
        private Encoding _encoding = Encoding.Default;

        /// <summary>
        /// 
        /// </summary>
        /// <value>The encrypty key.</value>
        public string EncryptyKey
        {
            set { _key = value; }
            get { return _key; }
        }

        /// <summary>
        /// 字符串编码
        /// </summary>
        public Encoding Encoding
        {
            set { _encoding = value; }
            get { return _encoding; }
        }

        /// <summary>
        /// 数据加密
        /// </summary>
        /// <param name="Data"></param>
        /// <returns></returns>
        public string Encrypt(string Data)
        {
            var ivb = Encoding.ASCII.GetBytes(_iv);
            var keyb = Encoding.ASCII.GetBytes(EncryptyKey);
            var tob = Encoding.GetBytes(Data);
            byte[] encrypted;
            var encryptor = _des.CreateEncryptor(keyb, ivb);
            using (var msEncrypt = new MemoryStream())
            {
                using (var csEncrypt = new CryptoStream(msEncrypt, encryptor, CryptoStreamMode.Write))
                {
                    csEncrypt.Write(tob, 0, tob.Length);
                    csEncrypt.FlushFinalBlock();
                    encrypted = msEncrypt.ToArray();
                    StringBuilder sb = new StringBuilder(encrypted.Length * 2);
                    foreach (byte b in encrypted)
                        sb.AppendFormat("{0:x2}", b);
                    return sb.ToString();
                }
            }


        }

        /// <summary>
        /// 还原数据
        /// </summary>
        /// <param name="str"></param>
        /// <returns></returns>
        public string Decrypt(string SrtData)
        {
            char[] chrs = SrtData.ToLower().ToCharArray();
            byte[] tob = new byte[chrs.Length / 2];
            for (int i = 0; i < chrs.Length; i += 2)
            {
                int hi = chrs[i];
                int low = chrs[i + 1];
                if (hi > 'A')
                    hi -= 'a' - 10;
                else
                    hi -= '0';
                if (low > 'A')
                    low -= 'a' - 10;
                else
                    low -= '0';
                tob[i / 2] = (byte)(hi << 4 | low);
            }

            var ivb = Encoding.ASCII.GetBytes(_iv);
            var keyb = Encoding.ASCII.GetBytes(EncryptyKey);

            byte[] encrypted;
            var encryptor = _des.CreateDecryptor(keyb, ivb);
            using (var msEncrypt = new MemoryStream())
            {
                using (var csEncrypt = new CryptoStream(msEncrypt, encryptor, CryptoStreamMode.Write))
                {
                    csEncrypt.Write(tob, 0, tob.Length);
                    csEncrypt.FlushFinalBlock();
                    encrypted = msEncrypt.ToArray();
                    return Encoding.GetString(encrypted);
                }
            }
            
        }

        /// <summary>
        /// 加密数据
        /// </summary>
        /// <param name="Data"></param>
        public static string Encode(string Data)
        {
            DESEncrypt ec = new DESEncrypt();
            return ec.Encrypt(Data);
        }

        /// <summary>
        /// 数据解密
        /// </summary>
        /// <param name="Data"></param>
        /// <returns></returns>
        public static string Decode(string Data)
        {
            DESEncrypt ec = new DESEncrypt();
            return ec.Decrypt(Data);
        }
    }
}
