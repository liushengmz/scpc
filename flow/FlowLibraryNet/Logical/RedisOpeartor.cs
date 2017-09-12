using StackExchange.Redis;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace FlowLibraryNet.Logical
{
    /// <summary>
    /// Redis 操作相关对像
    /// </summary>
    public class RedisOpeartor : IDisposable
    {
        ConnectionMultiplexer _con;
        IDatabase _db;
        bool _disponsed;
        /// <summary>
        /// 默认的缓存生命期
        /// </summary>
        static int _maxage;

        protected static StackExchange.Redis.ConnectionMultiplexer GetConntionInstance()
        {
            var set = System.Configuration.ConfigurationManager.AppSettings["ReidsConStr"];


            if (string.IsNullOrEmpty(set))
                throw new ArgumentNullException("Redis 未配置! config->configuration/appSettings/Reids");


            var maxage = System.Configuration.ConfigurationManager.AppSettings["ReidsMaxage"];

            if (!string.IsNullOrEmpty(maxage))
                int.TryParse(maxage, out _maxage);

#if DEBUG
            var cl = ConnectionMultiplexer.Connect(set, System.Console.Out);
#else
            var cl = ConnectionMultiplexer.Connect(set);
#endif

            return cl;
        }

        public IDatabase DBase
        {
            get
            {
                if (_disponsed)
                    throw new ObjectDisposedException(this.GetType().FullName);
                if (_db != null)
                    return _db;
                if (_con != null)
                    return _db = _con.GetDatabase();

                _con = GetConntionInstance();
                return _db = _con.GetDatabase();
            }
        }


        public void Dispose()
        {
            _disponsed = true;
            if (_db != null && _db is IDisposable)
                ((IDisposable)_db).Dispose();
            _db = null;
            if (_con != null)
                _con.Dispose();
            _con = null;
        }


        public T GetModel<T>(string idStr) where T : Shotgun.Model.Logical.LightDataModel, new()
        {
            var map = DBase.HashGetAll(idStr);
            if (map == null || map.Length == 0)
                return null;

            var m = new T();
            Shotgun.Database.IUpatedataInfo iui = m;
            Type t = m.GetType();

            foreach (var kv in map)
            {
                var pro = t.GetProperty(kv.Name);
                var pType = pro.PropertyType;
                switch (pType.Name)
                {
                    case "Boolean": pro.SetValue(m, (bool)kv.Value); break;
                    case "Int32": pro.SetValue(m, (int)kv.Value); break;
                    case "Long": pro.SetValue(m, (bool)kv.Value); break;
                    case "DateTime": pro.SetValue(m, DateTime.Parse(kv.Value)); break;
                    case "String": pro.SetValue(m, (string)kv.Value); break;
                    default: throw new NotSupportedException("未支持类型：" + pType.FullName);
                }
            }
            //m[m.IdentifyField] = idStr;
            iui.SetUpdated(null);
            return m;
        }

        public void SetModel(Shotgun.Database.IUpatedataInfo m, string idStr = null)
        {
            var fs = m.GetUpateFields();
            if (fs.Count == 0)
                return;

            if (string.IsNullOrEmpty(idStr))
                idStr = m.IdentifyField;


            List<HashEntry> data = new List<HashEntry>();
            foreach (var f in fs)
            {
                var val = m.GetValueByName(f);
                if (val is DateTime)
                    data.Add(new HashEntry(f, val.ToString()));
                else if (val is bool)
                    data.Add(new HashEntry(f, (bool)val));
                else if (val is int)
                    data.Add(new HashEntry(f, (int)val));
                else if (val is long)
                    data.Add(new HashEntry(f, (long)val));
                else if (val is double)
                    data.Add(new HashEntry(f, (double)val));
                else if (val is string)
                    data.Add(new HashEntry(f, (string)val));
                else if (val is byte[])
                    data.Add(new HashEntry(f, (byte[])val));
                else
                    data.Add(new HashEntry(f, val.ToString()));
            }

            Console.WriteLine(string.Join(",", fs));

            DBase.HashSet(idStr, data.ToArray());
            var ts = new TimeSpan(0, 0, _maxage);
            if (_maxage > 0)
                DBase.KeyExpire(idStr, ts);

            //m.SetUpdated(null);


        }

    }
}
