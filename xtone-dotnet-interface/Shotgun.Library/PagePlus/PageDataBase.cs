using System;
using System.Collections.Generic;
using System.Text;
using System.Web;

namespace Shotgun.PagePlus
{
    [System.Diagnostics.DebuggerNonUserCode()]
    public partial class ShotgunPage : IPageDataBase
    {
        private Database.BDClass _db;
        private bool _enableTSQLReport;
        private bool _performanceMethod;


        /// <summary>
        /// 是否默认支持页面method=performance，输出TSQL执行报告功能
        /// </summary>
        public bool EnableTSQLReport
        {
            set
            {
                if (!(value ^ _enableTSQLReport))
                    return;//无变化

                if (value && !_performanceMethod)
                    _performanceMethod = System.Web.HttpContext.Current.Request["method"] == "performance";
                if (!_performanceMethod) //用户没有请求输入记录
                    return;
                _enableTSQLReport = value;
                ((Shotgun.Database.IBaseDataPerformance)_db).EnableRecord = value;
            }
            get { return _enableTSQLReport; }
        }


        /// <summary>
        /// 数据支持对像
        /// </summary>
        [Obsolete("改用dBase")]
        public Shotgun.Database.BDClass DataBase { get { return _db; } }

        /// <summary>
        /// 页面级别数据库对像
        /// </summary>
        public Shotgun.Database.BDClass dBase { get { return _db; } }

        public override void ProcessRequest(HttpContext context)
        {
            _db = CreateDBConnector();

            if (EnableTSQLReport)
            {
                if (!_performanceMethod)
                    _performanceMethod = context.Request["method"] == "performance";
                if (_performanceMethod)
                    ((Shotgun.Database.IBaseDataPerformance)_db).EnableRecord = true;
            }
            try
            {
                base.ProcessRequest(context);
                if (_performanceMethod && EnableTSQLReport)
                {
                    context.Response.ContentType = "text/plain";
                    context.Response.Cache.SetCacheability(HttpCacheability.NoCache);

                    context.Response.ClearContent();
                    context.Response.Write(((Shotgun.Database.IBaseDataPerformance)_db).PerformanceReport());
                }
            }
            catch (System.Web.HttpException ex)
            {
                if (ex.GetHttpCode() != 404)
                    throw;
                //避免系统日志记录此错误
                context.Response.StatusCode = 404;
                context.Response.ClearContent();
                context.Response.Write(ex.GetHtmlErrorMessage());
            }
            finally
            {
                _db.Dispose();
                _db = null;
            }
        }

        /// <summary>
        /// 创建数据连接
        /// </summary>
        /// <returns></returns>
        protected virtual Database.BDClass CreateDBConnector()
        {
            const string TypeName = "Shotgun.Database.DBDriver";
            var appset = System.Configuration.ConfigurationManager.AppSettings["DbDriver"];
            if (string.IsNullOrEmpty(appset))
                throw new Exception("使用Shotgun.PagePlus时，请在appseting中指定DbDriver");
            if (appset.EndsWith(".dll", StringComparison.OrdinalIgnoreCase))
                appset = appset.Substring(0, appset.Length - 4);
            var allAss = AppDomain.CurrentDomain.GetAssemblies();
            // var ff= from a in allAss where 
            var ass = Array.Find(allAss, e => e.GetName().Name.Equals(appset, StringComparison.OrdinalIgnoreCase));
            if (ass == null)
            {
                var path = HttpContext.Current.Request.MapPath(string.Format("~/bin/{0}.dll", appset));
                ass = System.Reflection.Assembly.LoadFile(path);
                if (ass == null)
                    throw new System.IO.FileNotFoundException("在appseting中指定DbDriver不存在", appset);
            }

            var drv = (Shotgun.Database.IDBDriver)ass.CreateInstance(TypeName);
            if (drv == null)
                throw new Exception(string.Format("在{0}中找不到{1}", appset, TypeName));
            var idrv = (Shotgun.Database.IDBDriver)drv;
            return idrv.CreateDBase();
        }

    }
}
