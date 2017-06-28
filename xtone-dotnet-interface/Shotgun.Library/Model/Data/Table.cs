using System;
using System.Collections.Generic;
using System.Text;
using System.Data;
using System.Runtime.Serialization;
using Shotgun.Database;
using Shotgun.Model.List;
using Shotgun.Library;

namespace Shotgun.Model.Data
{
    /// <summary>
    /// 提供基本的数据模型服务
    /// </summary>
    /// <typeparam name="R"></typeparam>
    [System.Diagnostics.DebuggerNonUserCodeAttribute()]
    public abstract class BaseTable<R> : DataTable where R : BaseDataRow
    {
        protected BaseTable(SerializationInfo info, StreamingContext context) :
            base(info, context)
        {
            this.InitVars();
        }
        public BaseTable()
        {
            this.BeginInit();
            this.InitClass();
            this.EndInit();
        }

        bool _iSafeConvert = true;
        /// <summary>
        /// 是否允许将DBNull转换成该类型默认值
        /// 默认设置为允许
        /// </summary>
        public bool IsSafeProperty
        {
            get { return _iSafeConvert; }
            set { _iSafeConvert = value; }
        }


        protected abstract void InitClass();

        protected abstract void InitVars();

        [System.ComponentModel.Browsable(false)]
        public int Count
        {
            get
            {
                return this.Rows.Count;
            }
        }

        public R this[int index]
        {
            get
            {
                return ((R)(this.Rows[index]));
            }
        }

        protected override Type GetRowType()
        {
            return typeof(R);
        }



        public R FindByid(int id)
        {
            return ((R)(this.Rows.Find(new object[] {
                            id})));
        }

        /// <summary>
        /// 创建一个新行
        /// </summary>
        /// <returns></returns>
        public new R NewRow()
        {
            return (R)base.NewRow();
        }

        public override DataTable Clone()
        {
            BaseTable<R> cln = ((BaseTable<R>)(base.Clone()));
            cln.InitVars();
            return cln;
        }
        /// <summary>
        /// 此方法用于静态方法GetRowById()并填充自己，如无数据则销毁自身。
        /// 不能用于其它方式调用
        /// </summary>
        /// <param name="id"></param>
        /// <param name="dBase"></param>
        /// <returns></returns>
        protected R getRowById(int id, IBaseDataClass2 dBase)
        {
            if (dBase == null)
            {
                this.Dispose();
                throw new NullReferenceException("数据库对像不能为空");
            }

            if (this.PrimaryKey.Length != 1)
            {
                this.Dispose();
                throw new Exception("表[" + this.TableName + "] 主键数量不等于1");
            }
            string sql = "select * from [{0}] where {1}={2}";

            if (dBase is IBaseDataSpecial &&
                ((IBaseDataSpecial)dBase).FieldMask == Shotgun.Model.Filter.EM_Safe_Field_MASK.MySQLMode)
            {
                sql = "select * from `{0}` where {1}={2}";
            }

            sql = string.Format(sql, this.TableName, this.PrimaryKey[0].ColumnName, id);

            try
            {
                dBase.TableFill(sql, this);
            }
            catch
            {
                this.Dispose();
                throw;
            }
            if (this.Rows.Count == 0)
            {
                this.Dispose();
                return null;
            }
            return this[0];

        }
        protected R getRowById(int id, IBaseDataClass2 dBase, string[] Fields)
        {

            if (dBase == null)
            {
                this.Dispose();
                throw new NullReferenceException("数据库对像不能为空");
            }

            Shotgun.Model.Filter.EM_Safe_Field_MASK fieldMask;

            if (dBase is IBaseDataSpecial &&
                ((IBaseDataSpecial)dBase).FieldMask == Shotgun.Model.Filter.EM_Safe_Field_MASK.MySQLMode)
                fieldMask = Shotgun.Model.Filter.EM_Safe_Field_MASK.MySQLMode;
            else
                fieldMask = Shotgun.Model.Filter.EM_Safe_Field_MASK.MsSQLMode;


            ClearEmptyColume(this, Fields);
            if (this.PrimaryKey.Length != 1)
            {
                this.Dispose();
                throw new Exception("表[" + this.TableName + "] 主键数量不等于1,或不包括在输出列中");
            }

            string sql = string.Empty;
            if (Fields == null || Fields.Length != 0)
                sql = "select * ";
            else
            {
                if (fieldMask == Shotgun.Model.Filter.EM_Safe_Field_MASK.MySQLMode)
                {
                    foreach (string f in Fields)
                        sql += ",`" + f + "`";
                }
                else
                {
                    foreach (string f in Fields)
                        sql += ",[" + f + "]";
                }
                sql = "select " + sql.Substring(1);
            }

            if (fieldMask == Shotgun.Model.Filter.EM_Safe_Field_MASK.MySQLMode)
                sql += string.Format(" from `{0}` where {1}={2}", this.TableName, this.PrimaryKey[0].ColumnName, id);
            else
                sql += string.Format(" from [{0}] where {1}={2}", this.TableName, this.PrimaryKey[0].ColumnName, id);



            try
            {
                dBase.TableFill(sql, this);
            }
            catch
            {
                this.Dispose();
                throw;
            }
            if (this.Rows.Count == 0)
            {
                this.Dispose();
                return null;
            }
            return this[0];

        }
        private static void ClearEmptyColume(DataTable dt, string[] Fields)
        {
            if (Fields == null || Fields.Length == 0)
                return;
            for (int i = dt.Columns.Count - 1; i >= 0; i--)
            {
                string c = dt.Columns[i].ColumnName.ToLower();
                foreach (string f in Fields)
                {
                    if (f.ToLower() == c)
                    {
                        c = null;
                        break;
                    }
                }
                if (c == null)
                    continue;


                if (dt.PrimaryKey != null && dt.PrimaryKey.Length != 0)
                {
                    for (int x = 0; x < dt.PrimaryKey.Length; x++)
                    {
                        if (dt.PrimaryKey[x].ColumnName.ToLower() == c)
                        {
                            c = null;
                            break;
                        }
                    }
                }
                if (c != null)
                    dt.Columns.Remove(c);

            }
        }
    }

    [System.Diagnostics.DebuggerNonUserCodeAttribute()]
    public abstract class BaseDataRow : DataRow, Shotgun.Database.IUpatedataInfo
    {

        /// <summary>
        /// 字段变化记录
        /// </summary>
        bool[] _ModifyCellIndexs;


        protected internal BaseDataRow(DataRowBuilder builder)
            : base(builder)
        {
            _ModifyCellIndexs = new bool[base.Table.Columns.Count];
        }
        /// <summary>
        /// 自动转化为默认值
        /// </summary>
        protected bool IsSafe
        {
            get
            {
                if (this.Table == null || !(this.Table is BaseTable<BaseDataRow>))
                    return true;
                return ((BaseTable<BaseDataRow>)this.Table).IsSafeProperty;
            }
        }
        #region 取字段值并转换类型
        protected string GetString(DataColumn col)
        {
            object t = this[col];
            if (t == DBNull.Value)
            {
                if (IsSafe)
                    return string.Empty;
                throw new StrongTypingException("“" + col.ColumnName + "”的值为 DBNull。");
            }
            return (string)t;
        }

        protected int GetInt32(DataColumn col)
        {
            object t = this[col];
            if (t == DBNull.Value)
            {
                if (IsSafe)
                    return 0;
                throw new StrongTypingException("“" + col.ColumnName + "”的值为 DBNull。");
            }
            return (int)t;
        }

        protected short GetInt16(DataColumn col)
        {
            object t = this[col];
            if (t == DBNull.Value)
            {
                if (IsSafe)
                    return 0;
                throw new StrongTypingException("“" + col.ColumnName + "”的值为 DBNull。");
            }
            return (short)t;
        }

        protected long GetInt64(DataColumn col)
        {
            object t = this[col];
            if (t == DBNull.Value)
            {
                if (IsSafe)
                    return 0;
                throw new StrongTypingException("“" + col.ColumnName + "”的值为 DBNull。");
            }
            return (long)t;
        }

        protected DateTime GetDateTime(DataColumn col)
        {
            object t = this[col];
            if (t == DBNull.Value)
            {
                if (IsSafe)
                    return new DateTime(1900, 1, 1);
                throw new StrongTypingException("“" + col.ColumnName + "”的值为 DBNull。");
            }
            return (DateTime)t;
        }

        protected bool GetBoolean(DataColumn col)
        {
            object t = this[col];
            if (t == DBNull.Value)
            {
                if (IsSafe)
                    return false;
                throw new StrongTypingException("“" + col.ColumnName + "”的值为 DBNull。");
            }
            return (bool)t;
        }

        protected byte GetByte(DataColumn col)
        {
            object t = this[col];
            if (t == DBNull.Value)
            {
                if (IsSafe)
                    return 0;
                throw new StrongTypingException("“" + col.ColumnName + "”的值为 DBNull。");
            }
            return (byte)t;
        }

        protected byte[] GetBytes(DataColumn col)
        {
            object t = this[col];
            if (t == DBNull.Value)
            {
                if (IsSafe)
                    return null;
                throw new StrongTypingException("“" + col.ColumnName + "”的值为 DBNull。");
            }
            return (byte[])t;
        }

        protected decimal GetDecimal(DataColumn col)
        {
            object t = this[col];
            if (t == DBNull.Value)
            {
                if (IsSafe)
                    return 0;
                throw new StrongTypingException("“" + col.ColumnName + "”的值为 DBNull。");
            }
            return (decimal)t;
        }

        protected double GetDouble(DataColumn col)
        {
            object t = this[col];
            if (t == DBNull.Value)
            {
                if (IsSafe)
                    return 0;
                throw new StrongTypingException("“" + col.ColumnName + "”的值为 DBNull。");
            }
            return (double)t;
        }
        protected object GetObject(DataColumn col)
        {
            return this[col];
        }

        #endregion


        /// <summary>
        /// 设置字段值
        /// 尽可能使用方法设置字段值
        /// </summary>
        /// <param name="col"></param>
        /// <param name="value"></param>
        protected void SetValue(DataColumn col, object value)
        {
            this[col] = value;
            if (_ModifyCellIndexs != null)
                _ModifyCellIndexs[col.Ordinal] = true;

        }

        #region IUpatedataInfo 成员


        List<string> Shotgun.Database.IUpatedataInfo.GetUpateFields()
        {
            List<string> s = new List<string>();
            for (int i = 0; i < _ModifyCellIndexs.Length; i++)
            {
                if (_ModifyCellIndexs[i])
                    s.Add(this.Table.Columns[i].ColumnName);
            }
            return s;
        }

        object Shotgun.Database.IUpatedataInfo.GetValueByName(string filds)
        {
            return this[filds];
        }

        string Shotgun.Database.IUpatedataInfo.IdentifyField
        {
            get
            {
                if (this.Table.PrimaryKey.Length != 1)
                    throw new TableException("主键不唯一或不存在", this.Table);
                return this.Table.PrimaryKey[0].ColumnName;
            }
        }

        void Shotgun.Database.IUpatedataInfo.SetUpdated(object IdentifyValue)
        {
            for (int i = 0; i < _ModifyCellIndexs.Length; i++)
            {
                _ModifyCellIndexs[i] = false;
            }
            if (IdentifyValue == null)
            {
                OnSaved(false);
                return;
            }

            if (this[Table.PrimaryKey[0]] != IdentifyValue)
                this[Table.PrimaryKey[0]] = IdentifyValue;

            OnSaved(true);
        }

        string Shotgun.Database.IUpatedataInfo.TableName
        {
            get { return Table.TableName; }
        }

        string IUpatedataInfo.Schema
        {
            get { return null; }
        }

        #endregion

        /// <summary>
        /// 保存当前已经修改的数据
        /// 根据标识字段，自动选择insert还update,如是insert会更新标识字段
        /// </summary>
        /// <param name="dBase"></param>
        public virtual void SaveToDatabase(Shotgun.Database.IBaseDataClass2 dBase)
        {
            dBase.SaveData(this);
        }

        /// <summary>
        /// 从指的数据库中删除该行数据。根据主键值执行删除。此操作为永久删除
        /// </summary>
        /// <param name="dBase"></param>
        public virtual void DeleteFrom(Shotgun.Database.IBaseDataClass2 dBase)
        {
            dBase.DeleteData(this);
        }


        /// <summary>
        /// SaveToDatabase保存成功后
        /// </summary>
        /// <param name="row"></param>
        /// <param name="isNews"></param>
        protected virtual void OnSaved(bool isNews) { }

        /// <summary>
        /// 将空值设置为指定值，否则不改变
        /// </summary>
        /// <param name="col"></param>
        /// <param name="defaultValue"></param>
        protected void NullToDefualt(DataColumn col, object defaultValue)
        {
            if (IsNull(col))
                SetValue(col, defaultValue);
        }

    }

    public class TableException : Exception
    {
        public TableException(string message) : base(message) { }
        public TableException(string message, DataTable table)
            : base(message)
        {
        }
        private DataTable _tab;
        public DataTable Table
        {
            internal set { _tab = value; }
            get { return _tab; }
        }
    }
}
