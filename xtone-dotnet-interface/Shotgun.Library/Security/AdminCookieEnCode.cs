using System;
using System.Collections.Generic;
using System.Text;

namespace Shotgun.Security
{
    public class AdminCookieEnCode
    {
        public static string Encode(string Data, string key)
        {
            byte[] ds = ASCIIEncoding.Default.GetBytes(Data);
            byte[] ks = ASCIIEncoding.Default.GetBytes(key);
            int sum = 0;
            for (int i = 0; i < ds.Length; i++)
            {
                sum += ds[i];
                sum &= 0xFFFF;
                ds[i] ^= ks[i % ks.Length];

            }
            ks = new byte[ds.Length + 2];
            for (int i = 0; i < ds.Length; i++)
            {
                ks[i] = ds[i];
            }
            ks[ds.Length] = (byte)(sum & 0xff);
            ks[ds.Length + 1] = (byte)((sum >> 8) & 0xff);
            return Convert.ToBase64String(ks);
        }

        public static string Decode(string Data, string key)
        {
            byte[] ds = Convert.FromBase64String(Data);
            byte[] ks = ASCIIEncoding.Default.GetBytes(key);
            int sum = 0;
            for (int i = 0; i < ds.Length - 2; i++)
            {
                ds[i] ^= ks[i % ks.Length];
                sum += ds[i];
                sum &= 0xFFFF;
            }
            int v = ds[ds.Length - 2] | (ds[ds.Length - 1] << 8);
            if (sum == v)
                return ASCIIEncoding.Default.GetString(ds, 0, ds.Length - 2);
            return null;
        }
    }
}
