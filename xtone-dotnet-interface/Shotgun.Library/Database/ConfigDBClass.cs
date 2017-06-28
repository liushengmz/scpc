using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Web;

namespace Shotgun.Database
{
    /// <summary>
    /// 一个通过配置实现的数据源。（使用了大量反射方法，建议少用）
    /// </summary>
    public class ConfigDBClass : Shotgun.Database.BDClass
    {
        private static IDBDriver iDrv;
        const string TypeName = "Shotgun.Database.DBDriver";

        Shotgun.Database.BDClass _db;

        public ConfigDBClass() : base(false, null) { }


        private Shotgun.Database.IBaseDataClass2 dBase()
        {
            if (_db != null)
                return _db;
            lock (this)
            {
                if (_db != null)
                    return _db;
                _db = CreateDBConnector();
            }
            return _db;
        }
        /// <summary>
        /// 通过config配置的DbDriver,创建数据库,需要手动关闭连接
        /// </summary>
        /// <returns></returns>
        public static Database.BDClass CreateDBConnector()
        {
            if (iDrv != null)
                return iDrv.CreateDBase();

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
            iDrv = (Shotgun.Database.IDBDriver)drv;
            return iDrv.CreateDBase();
        }


        private MethodInfo getMethond(string name)
        {
            return _db.GetType().GetMethod("OnDisposing", System.Reflection.BindingFlags.Instance | System.Reflection.BindingFlags.NonPublic);
        }


        protected override void OnDisposing()
        {
            _db.Dispose();
        }

        protected override System.Data.IDbTransaction Tran
        {
            get
            {
                return ((IBaseDataClass2)_db).Transaction;
            }
            set
            {
                var property = _db.GetType().GetProperty("Tran", System.Reflection.BindingFlags.Instance | System.Reflection.BindingFlags.NonPublic | BindingFlags.SetProperty);
                property.SetValue(_db, value, null);
            }
        }

        protected override System.Data.IDbConnection Conn
        {
            get { return ((IBaseDataClass2)_db).Connection; }
        }

        protected override System.Data.IDbCommand Command()
        {
            return ((IBaseDataClass2)_db).Command();
        }

        protected override bool SaveData(IUpatedataInfo DataRow)
        {
            return (bool)getMethond("SaveData").Invoke(_db, new object[] { DataRow });
        }

        protected override bool DeleteData(IUpatedataInfo DataRow)
        {
            return (bool)getMethond("DeleteData").Invoke(_db, new object[] { DataRow });
        }

        protected override void TableFill(string sql, System.Data.DataTable table)
        {
            getMethond("TableFill").Invoke(_db, new object[] { sql, table });
        }

        public override string FieldEncode(string filed)
        {
            return (string)getMethond("FieldEncode").Invoke(_db, new object[] { filed });
        }

        protected override string GetPageTSql(Model.List.IDBSQLHelper list)
        {
            return (string)getMethond("GetPageTSql").Invoke(_db, new object[] { list });
        }
    }
}
