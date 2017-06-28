using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;

namespace n8wan.Public.Logical
{
    /// <summary>
    /// 将城市Id或省份Id，转换为对应的手机段信息
    /// </summary>
    public class CityToPhone
    {
        Dictionary<int, int> city;
        Dictionary<int, int> provice;
        static CityToPhone _instance;
        private CityToPhone()
        {
            city = new Dictionary<int, int>();
            provice = new Dictionary<int, int>();

            var dataFile = AppDomain.CurrentDomain.BaseDirectory + "App_Data/cityPhone.txt";
            if (!File.Exists(dataFile))
                return;

            using (var reader = new StreamReader(dataFile))
            {
                int line = 0;
                do
                {
                    var data = reader.ReadLine();
                    line++;
                    if (string.IsNullOrEmpty(data))
                        continue;

                    if (data.TrimStart().StartsWith("#"))
                        continue;

                    var arr = data.Split(new char[] { ',' }, 4);
                    if (arr.Length < 3)
                        continue;//无效数据
                    int phone, cityId, provinceId;
                    if (!(int.TryParse(arr[0], out cityId)
                            && int.TryParse(arr[1], out provinceId)
                            && int.TryParse(arr[2], out phone)))
                    {//数据信息有问题
                        Shotgun.Library.SimpleLogRecord.WriteLog(string.Format("CityToPhone,{0},行{1} 数据无效", dataFile, line));
                        continue;
                    }
                    city[cityId] = phone;
                    if (!provice.ContainsKey(provinceId))
                        provice[provinceId] = phone;

                } while (!reader.EndOfStream);
            }
        }


        static CityToPhone Instance
        {
            get
            {
                if (_instance == null)
                    _instance = new CityToPhone();
                return _instance;
            }
        }

        /// <summary>
        /// 获取指定城市的虚似手机号
        /// </summary>
        /// <param name="defPhone">默认手机号，SP传入真实手机号</param>
        /// <param name="proviceId">省份</param>
        /// <param name="cityId">城市</param>
        /// <returns></returns>
        public static string GetVirtualPhone(string defPhone, int proviceId, int cityId)
        {
            string vrPhone = defPhone;
            if (string.IsNullOrEmpty(defPhone) || defPhone.Length != 11 || !defPhone.StartsWith("1"))
                vrPhone = "12300000000";

            if (proviceId == 32 || proviceId == 0)
                return vrPhone;//省份未知，城市未知

            if (cityId == 416 || cityId == 0)
            {//城市未知
                if (Instance.provice.ContainsKey(proviceId))
                    return Instance.provice[proviceId].ToString() + "0000";
                return vrPhone;
            }
            if (vrPhone.Equals(defPhone))
                return defPhone;
            if (Instance.city.ContainsKey(cityId))
                return Instance.city[cityId].ToString() + "8888";
            return vrPhone;
        }
    }
}
