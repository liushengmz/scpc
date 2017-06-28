using Shotgun.Database;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Web;

namespace Shotgun.PagePlus
{
    public abstract class SimpleHttpHandler : IHttpHandler
    {
        IBaseDataClass2 _db;

        /// <summary>
        /// 创建数据库连接实例
        /// </summary>
        /// <returns></returns>
        protected virtual IBaseDataClass2 CreateDBase()
        {
            return Shotgun.Database.ConfigDBClass.CreateDBConnector();
        }

        public Shotgun.Database.IBaseDataClass2 dBase
        {
            get
            {
                if (_db == null)
                    _db = CreateDBase();
                return _db;
            }
        }

        /// <summary>
        /// 开始处理请求
        /// </summary>
        /// <param name="dBase">GetDBase()返回的数据库对像，无需主动关闭</param>
        public abstract void BeginProcess();

        public HttpRequest Request { get; private set; }
        public HttpResponse Response { get; private set; }
        public HttpServerUtility Server { get; private set; }

        /// <summary>
        /// 设置ajax响应处理器，设置实例后将以ajax方式响应
        /// </summary>
        public Library.simpleAjaxResponser Ajax { get; set; }


        #region IHttpHandler 成员

        public virtual bool IsReusable
        {
            get { return false; }
        }

        public void ProcessRequest(HttpContext context)
        {
            Request = context.Request;
            Response = context.Response;
            Server = context.Server;

            try
            {
                BeginProcess();
                if (Ajax != null)
                    Ajax.SendResponse();
            }
            finally
            {
                if (_db != null && (dBase is IDisposable))
                    ((IDisposable)dBase).Dispose();
            }

        }

        #endregion

    }


    public abstract class SimpleHttpHandler<DB> : SimpleHttpHandler where DB : Shotgun.Database.IBaseDataClass2, new()
    {
        protected override IBaseDataClass2 CreateDBase()
        {
            return new DB();
        }
    }


}