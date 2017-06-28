using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace n8wan.Public.Logical
{
    public class StaticCacheTimeline<T, IDX> : StaticCache<T, IDX> where T : Shotgun.Model.Logical.LightDataModel, ITimeCacheItem, new()
    {
        int firstExpired = 0;

        protected override Dictionary<IDX, T> CheckExpired()
        {
            if (base._data == null)
                return null;
            var data = base._data;
            int curTime = (int)((DateTime.Now.ToUniversalTime().Ticks - 621355968000000000) / 10000000);
            if (curTime <= firstExpired)
                return data;

            List<IDX> keys = new List<IDX>();
            int minUinx = int.MaxValue;
            T[] items = null;
            lock (base.SyncRoot)
            {
                foreach (var kv in data)
                {
                    if (curTime > kv.Value.ItemExpire)
                        keys.Add(kv.Key);
                    else if (minUinx > kv.Value.ItemExpire)
                        minUinx = kv.Value.ItemExpire;
                }
                firstExpired = minUinx;
                items = new T[keys.Count];

                for (var i = 0; i < keys.Count; i++)
                {
                    var key = keys[i];
                    var item = data[key];
                    data.Remove(key);
                }
            }
            if (items != null)
                OnExpired(items);
            return data;
        }
        protected override bool IsExpired()
        {
            return false;
        }
        public override bool IsManualLoad
        {
            get
            {
                return true;
            }
            set
            {
                if (value == false)
                    throw new NotSupportedException("不支持过期自动加载");
            }
        }

        public override void InsertItem(T data)
        {
            DateTime ex = DateTime.Now.Add(base.Expired);
            data.ItemExpire = (int)((ex.ToUniversalTime().Ticks - 621355968000000000) / 10000000);
            //if (this.firstExpired > data.ItemExpire)
            //    this.firstExpired = data.ItemExpire;
            base.InsertItem(data);
        }

        public override void ClearCache()
        {
            base.ClearCache();
            this.firstExpired = (int)((DateTime.Now.ToUniversalTime().Ticks - 621355968000000000) / 10000000);
        }
    }
}
