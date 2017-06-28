using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Web;
using System.Web.UI;
using System.Data;

namespace Shotgun.Database
{
    /// <summary>
    /// 提供基础的数据Helper类
    /// </summary>
    public abstract partial class BDClass : IBaseDataClass, IDisposable
    {
        readonly bool isGobal;
        /// <summary>
        /// 连接串名称
        /// </summary>
        protected readonly string dbConfigName;

        /// <summary>
        /// 
        /// </summary>
        /// <param name="AutoClose">是否通过page_unload事件关行连接串</param>
        /// <param name="ConfigName">指定web.config.connectionStrings名称</param>
        protected BDClass(bool AutoClose, string ConfigName)
        {
            db2 = (IBaseDataClass2)this;
            this.dbConfigName = ConfigName;
            this.isGobal = AutoClose;
            if (!AutoClose)
                return;
            IHttpHandler hd = System.Web.HttpContext.Current.Handler;
            if (hd is Page)
            {
                ((Page)hd).Unload += OnPageUnload;
            }
            else
            {
                throw new Exception("当前模式，无法创建自动关闭的连接");
            }
        }



        /// <summary>
        /// 从web.config取得指定的连串
        /// </summary>
        protected string connectionStrings
        {
            get
            {
                var cs = System.Configuration.ConfigurationManager.ConnectionStrings[this.dbConfigName];
                if (cs == null)
                    throw new Exception("未定义连接串:" + this.dbConfigName);
                return cs.ConnectionString;
            }
        }

        protected virtual void OnPageUnload(object sender, EventArgs e)
        {
            this.Dispose();
        }

        protected IBaseDataClass2 db2;


        #region IBaseDataClass 成员

        public int ExecuteNonQuery(string sql)
        {
            var cmd = db2.Command();
            cmd.CommandText = sql;
            TimerStart();
            try
            {
                return cmd.ExecuteNonQuery();
            }
            catch (System.Data.Common.DbException ex)
            {
                Shotgun.Library.ErrLogRecorder.SqlError(ex, cmd);
                throw;
            }
            finally
            {
                TimerEnd(sql);
                cmd.Dispose();
            }
        }

        public object ExecuteScalar(string sql)
        {
            var cmd = db2.Command();
            cmd.CommandText = sql;
            TimerStart();
            try
            {
                return cmd.ExecuteScalar();
            }
            catch (System.Data.Common.DbException ex)
            {
                Shotgun.Library.ErrLogRecorder.SqlError(ex, cmd);
                throw;
            }
            finally
            {
                TimerEnd(sql);
                cmd.Dispose();
            }
        }

        public System.Data.DataTable GetDataTable(string sql)
        {
            var dt = new DataTable();
            db2.TableFill(sql, dt);
            return dt;
        }

        public string SqlEncode(System.Web.UI.ITextControl txtContrl)
        {
            return SqlEncode(txtContrl.Text, true);
        }

        public string SqlEncode(string vData)
        {
            return SqlEncode(vData, false);
        }

        public virtual string SqlEncode(string vData, bool AddQut)
        {
            if (!string.IsNullOrEmpty(vData))
            {
                vData = vData.Replace("'", "''");
            }
            else if(vData==null)
               vData=string.Empty;
            if(AddQut)
                return "'" + vData + "'";
            return vData;
        }

        #endregion

        #region IDisposable 成员

        public bool Disposed { get; private set; }
        public void Dispose()
        {
            if (Disposed)
                return;
            if (this.isGobal)
            {
                try
                {
                    ((Page)HttpContext.Current.Handler).Unload -= this.OnPageUnload;
                }
                catch
                {
                }
            }
            OnDisposing();
            this.Disposed = true;
            GC.SuppressFinalize(this);
        }

        #endregion

        /// <summary>
        /// 此处进行连接器等销毁
        /// </summary>
        protected abstract void OnDisposing();
    }
}
