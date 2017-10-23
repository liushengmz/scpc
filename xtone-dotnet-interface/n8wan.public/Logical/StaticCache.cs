using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;

namespace n8wan.Public.Logical
{
    public enum Static_Cache_Staus
    {
        Idel,
        Loading,
        AllLoad
    }

    public abstract class StaticCache
    {
        static List<StaticCache> _allCache;

        static Timer timerChecker = null;

        /// <summary>
        /// 用于提供线程同步
        /// </summary>
        public readonly object SyncRoot;

        public StaticCache()
        {
            SyncRoot = this;
        }

        public static void ClearAllCache()
        {
            if (_allCache == null)
                return;
            var all = _allCache.ToArray();

            foreach (var c in all)
            {
                c.ClearCache();
            }
        }

        public abstract void ClearCache();


        protected void Add(StaticCache sc)
        {
            if (sc == null)
                return;
            if (_allCache == null)
                _allCache = new List<StaticCache>();

            lock (_allCache)
            {
                if (timerChecker == null)
                    timerChecker = new Timer(CheckAction, null, 6 * 1000, 120 * 1000);

                if (_allCache.Contains(sc))
                    return;
                _allCache.Add(sc);
            }
        }

        private static void CheckAction(object state)
        {

            var cs = GetAllCache();
            if (cs == null || cs.Length == 0)
                return;
            foreach (var c in cs)
            {
                if (c.IsExpired())
                {
                    c.ClearCache();
                }
            }
            lock (_allCache)
            {
                if (_allCache.Count == 0)
                {
                    timerChecker.Dispose();
                    timerChecker = null;
                }
            }

        }

        protected abstract bool IsExpired();

        protected static StaticCache[] GetAllCache()
        {
            if (_allCache == null)
                return null;
            return _allCache.ToArray();
        }

        internal void Remove(StaticCache staticCache)
        {
            if (_allCache == null)
                return;
            _allCache.Remove(staticCache);
        }
    }

    /// <summary>
    /// 静态变量缓存-带过期时间
    /// </summary>
    /// <typeparam name="T"></typeparam>
    /// <typeparam name="IDX"></typeparam>
    public class StaticCache<T, IDX> : StaticCache where T : Shotgun.Model.Logical.LightDataModel, new()
    {
        protected Dictionary<IDX, T> _data;

        /// <summary>
        /// 数据加载状态
        /// </summary>
        Static_Cache_Staus _satus;
        /// <summary>
        /// 数据实际过期时间
        /// </summary>
        DateTime _expired;
        private string _tabName;
        private string _idField;
        /// <summary>
        /// 缓存内存主索引，默认使用数据库主键
        /// </summary>
        private string _indexField;
        /// <summary>
        /// 数据失效时通知外部代码进行数据处理
        /// </summary>
        private Action<IEnumerable<T>> _onExpired;

        /// <summary>
        /// 正在销毁数据
        /// </summary>
        private bool _clearing;


        public StaticCache()
            : this(null)
        {
        }

        /// <summary>
        /// 创建一个指主键的索引的缓存器
        /// </summary>
        /// <param name="IdxField">缓存内存主索引，默认使用数据库主键，注意数据索引键值的唯一性</param>
        public StaticCache(string IdxField)
        {
            this.Expired = new TimeSpan(24, 24, 24);
            T m = new T();
            this._idField = m.IdentifyField;
            this._tabName = m.TableName;
            if (string.IsNullOrEmpty(IdxField))
                this._indexField = this._idField;
            else
                this._indexField = IdxField;
        }

        /// <summary>
        /// 数据缓存有效期时长
        /// </summary>
        public TimeSpan Expired { get; set; }

        public String TableName { get { return _tabName; } }
        public String IdField { get { return _idField; } }
        public Static_Cache_Staus Status { get { return _satus; } }

        /// <summary>
        /// 加载数据到缓存
        /// </summary>
        /// <param name="isSync">是否在当前线程上加载</param>
        private void LoadFreshData(bool isSync)
        {
            if (_satus != Static_Cache_Staus.Idel || IsManualLoad)
                return;
            lock (this)
            {
                if (_satus != Static_Cache_Staus.Idel)
                    return;
                _satus = Static_Cache_Staus.Loading;
                if (isSync)
                    this.LoadData(null);
                else
                    ThreadPool.QueueUserWorkItem(this.LoadData);
            }
        }

        void LoadData(object s)
        {
            //int maxId = 0;
            //var cData = _data;//防止在其它地方被重置为null

            //if (cData == null)
            //    _data = cData = new Dictionary<IDX, T>();
            //else if (cData.Count > 0)
            //    maxId = cData.Values.Max(e => (int)e[_idField]);
            var cData = new Dictionary<IDX, T>();

#if DEBUG
            var stw = new System.Diagnostics.Stopwatch();
            stw.Start();
#endif

            var q = new Shotgun.Model.List.LightDataQueries<T>(_tabName, _idField, null, new T().Schema);
            //if (maxId > 0)
            //    q.Filter.AndFilters.Add(_idField, maxId, Shotgun.Model.Filter.EM_DataFiler_Operator.More);
            q.SortKey.Add(_idField, Shotgun.Model.Filter.EM_SortKeyWord.asc);
            q.PageSize = 1000;
            q.dBase = CreateDBase();
            System.Diagnostics.Stopwatch st = new System.Diagnostics.Stopwatch();
            st.Start();
            base.Add(this);
            try
            {
#if DEBUG
                Console.WriteLine("pointA :{0:#,###}", stw.Elapsed.TotalMilliseconds);
                stw.Restart();
#endif
                var RowCount = q.TotalCount;
#if DEBUG
                Console.WriteLine("pointB :{0:#,###}", stw.Elapsed.TotalMilliseconds);
#endif
                var PageCount = RowCount / q.PageSize + (RowCount % q.PageSize == 0 ? 0 : 1);
                for (var i = 1; i <= PageCount; i++)
                {
                    q.CurrentPage = i;
#if DEBUG
                    stw.Restart();
#endif
                    var items = q.GetDataList();
#if DEBUG
                    Console.WriteLine("pointC :{0:#,###}", stw.Elapsed.TotalMilliseconds);
#endif
                    if (items == null)
                        break;
#if DEBUG
                    stw.Restart();
#endif
                    items.ForEach(e => cData[(IDX)e[_indexField]] = e);
#if DEBUG
                    Console.WriteLine("pointD :{0:#,###}", stw.Elapsed.TotalMilliseconds);
#endif
                }
                lock (SyncRoot)
                {
                    _data = cData;//_data可能在加载数据时，被设置为null，此处进行还原
                    this._expired = DateTime.Now.Add(this.Expired);
                    this._satus = Static_Cache_Staus.AllLoad;
                }


            }
            catch (Exception ex)
            {
                WriteLog(ex.Message);
                _satus = Static_Cache_Staus.Idel;
                this._expired = DateTime.Now.Add(this.Expired);
            }
            finally
            {
                IDisposable dp = (IDisposable)q.dBase;
                if (dp != null)
                    dp.Dispose();
            }
            WriteLog(true, (int)st.ElapsedMilliseconds, _data.Count);

        }

        /// <summary>
        /// 检查数据是否过期，如果过期激发重新加载，并丢弃过期数据
        /// </summary>
        protected virtual Dictionary<IDX, T> CheckExpired()
        {
            var data = _data;
            bool isExp = data == null || DateTime.Now > _expired;
            if (!isExp)
                return data;


            if (_clearing)
                return null;
            lock (base.SyncRoot)
            {
                if (_clearing)
                    return null;
                _clearing = true;//正式进入清除状态
                if (IsManualLoad)
                {
                    ClearCache();
                    _clearing = false;
                    return null;
                }
            }


            ThreadPool.QueueUserWorkItem(e =>
            {
                try
                {
                    ClearCache();
                    LoadFreshData(true);
                    //Shotgun.Library.ErrLogRecorder.
                }
#if !DEBUG
                catch (Exception ex)
                {
                    WriteLog(ex.Message);
                }
#endif
                finally
                {
                    _clearing = false;
                }
            }, null);

            return null;


        }

        public IEnumerable<T> GetCacheData(bool iFull)
        {
            var tData = CheckExpired();
            if (tData == null)
                return null;
            if (iFull && IsManualLoad)
            {
#if DEBUG
                throw new NotSupportedException("手动模式，不支持数据全加载结果集");
#else
                return null;
#endif
            }
            if (iFull && _satus != Static_Cache_Staus.AllLoad)
                return null;
            return tData.Values;
        }

        /// <summary>
        /// 根据主索引，快速查找数据 （非完整数据）
        /// </summary>
        /// <param name="idx"></param>
        /// <returns></returns>
        public T GetDataByIdx(IDX idx)
        {
            if (_data == null)
                return null;
            var tData = CheckExpired();

            if (tData == null || !tData.ContainsKey(idx))
                return null;

            return tData[idx];
        }

        /// <summary>
        /// 找查数据（非完整数据）
        /// </summary>
        /// <param name="func"></param>
        /// <returns></returns>
        public T FindFirstData(Func<T, bool> func)
        {
            var tData = CheckExpired();
            if (tData == null)
                return null;
            lock (base.SyncRoot)
            {
                return tData.Values.FirstOrDefault(func);
            }


        }

        private Shotgun.Database.IBaseDataClass2 CreateDBase()
        {
            return new Shotgun.Database.DBDriver().CreateDBase();
        }

        /// <summary>
        /// 从外部插入单条数据，通常发生在，数据缓存成功之后，新增的数据
        /// 注意Fields要全部读取的，否则在二次取出使用时可能出现问题
        /// </summary>
        /// <param name="data"></param>
        public virtual void InsertItem(T data)
        {
            if (data == null)
                return;
            Dictionary<IDX, T> dt;
            if (_satus == Static_Cache_Staus.Idel)
            {
                dt = this._data;
                if (dt == null)
                {
                    dt = this._data = new Dictionary<IDX, T>();
                    this._expired = DateTime.Now.Add(this.Expired);
                }
            }
            else
            {
                dt = CheckExpired();
                if (dt == null || _satus != Static_Cache_Staus.AllLoad)
                    return;
            }
            base.Add(this);
            try
            {
                lock (base.SyncRoot)
                    dt[(IDX)data[this._indexField]] = data;
            }
            catch (Exception ex)
            {
                WriteLog(ex.ToString());
                WriteLog("缓存记录数据：" + dt.Count);
                return;
            }
            WriteLog(true, 0, 1);
        }

        void WriteLog(bool iAdd, int elapsedMs, int count)
        {
            string msg = string.Format("{0} cache, count {1}, elapsed {2}ms",
                (iAdd ? "add" : "remove"), count, elapsedMs);
            WriteLog(msg);
        }

        void WriteLog(string msg)
        {
            //Shotgun.Library.SimpleLogRecord.WriteLog("static_cache", _tabName + " " + msg);
        }

        /// <summary>
        /// 清除缓存数据
        /// </summary>
        public override void ClearCache()
        {
            Dictionary<IDX, T> _expData;
            lock (this.SyncRoot)
            {
                _expData = _data;
                _satus = Static_Cache_Staus.Idel;
                _data = null;
                base.Remove(this);
            }
            if (_expData != null)
                OnExpired(_expData.Values);
            WriteLog(false, 0, 0);
        }


        protected void OnExpired(IEnumerable<T> data)
        {
            if (_onExpired == null || data == null)
                return;

            try
            {
                _onExpired(data);
            }
#if !DEBUG
            catch (Exception ex)
            {
                WriteLog("过期回调出错：" + ex.ToString());
            }
#endif
            finally { }
        }



        /// <summary>
        /// 缓存销毁通知，可用于保存数据
        /// </summary>
        /// <param name="func"></param>
        public void SetExpriedProc(Action<IEnumerable<T>> func)
        {
            _onExpired = func;
        }

        /// <summary>
        /// 是否以手动方式加载代码（不会自动执行LoadData）
        /// </summary>
        public virtual bool IsManualLoad { get; set; }

        /// <summary>
        /// 检查数据是否过期，不会激发重新加载操作
        /// </summary>
        /// <returns></returns>
        protected override bool IsExpired()
        {
            if (_data == null)
                return true;

            if (DateTime.Now > _expired)
                return true;

            return false;
        }
    }
}
