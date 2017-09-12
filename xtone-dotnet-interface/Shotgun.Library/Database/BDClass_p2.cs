using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Data;

namespace Shotgun.Database
{
    [System.Diagnostics.DebuggerNonUserCode]
    partial class BDClass : IBaseDataClass2
    {

        protected abstract System.Data.IDbTransaction Tran { get; set; }
        protected abstract System.Data.IDbConnection Conn { get; }

        #region IBaseDataClass2 成员

        System.Data.IDbConnection IBaseDataClass2.Connection { get { return Conn; } }

        System.Data.IDbTransaction IBaseDataClass2.Transaction { get { return Tran; } }

        public event EventHandler OnConnectionClosed;

        System.Data.IDbCommand IBaseDataClass2.Command() { return Command(); }

        void IBaseDataClass2.TableFill(string sql, System.Data.DataTable table) { TableFill(sql, table); }

        bool IBaseDataClass2.SaveData(IUpatedataInfo DataRow) { return SaveData(DataRow); }

        bool IBaseDataClass2.DeleteData(IUpatedataInfo DataRow) { return DeleteData(DataRow); }

        #endregion


        protected abstract IDbCommand Command();

        protected abstract bool SaveData(IUpatedataInfo DataRow);

        protected abstract bool DeleteData(IUpatedataInfo DataRow);

        protected abstract void TableFill(string sql, System.Data.DataTable table);

        public void BeginTransaction()
        {
            if (IsTransaction)
                throw new BDException("事务已经启用");
            Tran = this.Conn.BeginTransaction();
        }

        public void Rollback()
        {
            if (!IsTransaction)
                throw new BDException("尚未启用事务或已经关闭");
            Tran.Rollback();
        }

        public void Commit()
        {
            if (!IsTransaction)
                throw new BDException("尚未启用事务或已经关闭");
            Tran.Commit();
        }

        public bool IsTransaction
        {
            get
            {
                return !(Tran == null || Tran.Connection == null);
            }
        }

        public virtual IDataReader ExecuteReader(IDbCommand cmd)
        {
            TimerStart();
            try
            {
                return cmd.ExecuteReader();
            }
            catch (System.Data.Common.DbException ex)
            {
                cmd.Cancel();
                Shotgun.Library.ErrLogRecorder.SqlError(ex, cmd);
                throw;
            }
            finally
            {
                TimerEnd(cmd.CommandText);
            }
        }

        public abstract string FieldEncode(string filed);

        protected abstract string GetPageTSql(Shotgun.Model.List.IDBSQLHelper list);

        string IBaseDataClass2.GetPageTSql(Model.List.IDBSQLHelper list)
        {
            return GetPageTSql(list);
        }

        public bool ReleseConnection()
        {
            if (IsTransaction)
                return false;
            OnDisposing();

            OnConnectionClosed?.Invoke(this, new EventArgs());

            return true;
        }

    }
}
