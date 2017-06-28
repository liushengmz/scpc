using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;

namespace Shotgun.Model.Logical
{
    /// <summary>
    /// 轻质量数据模块，用于替换原旧的BaseDataRow对像
    /// </summary>
    public abstract class LightDataModel : Shotgun.Database.IUpatedataInfo
    {
        /// <summary>
        /// 表示已经更新数据
        /// </summary>
        List<string> updatedFields;

        /// <summary>
        /// 为空的字段,注意转为小写
        /// </summary>
        List<string> NullFields;

        /// <summary>
        /// 用于存储未知的外部字段
        /// </summary>
        Dictionary<string, object> extrFields;

        public LightDataModel()
        {
            updatedFields = new List<string>();
            extrFields = new Dictionary<string, object>();
        }

        #region IUpatedataInfo 成员

        List<string> Database.IUpatedataInfo.GetUpateFields()
        {
            return updatedFields;
        }

        object Database.IUpatedataInfo.GetValueByName(string filds)
        {
            object obj;
            if (GetValue(filds, out obj))
                return obj;
            throw new KeyNotFoundException(string.Format("Key: {0} not Found", filds));
        }

        void Database.IUpatedataInfo.SetUpdated(object IdentifyValue)
        {
            updatedFields.Clear();

            if (IdentifyValue == null)
            {
                OnSaved(false);
                return;
            }
            if (IdentifyValue is decimal)
                this[IdentifyField] = Convert.ToInt32((decimal)IdentifyValue);
            else
                this[IdentifyField] = IdentifyValue;
            OnSaved(true);
        }

        public abstract string IdentifyField { get; }

        public abstract string TableName { get; }

        #endregion

        /// <summary>
        /// 将数据写入数据库
        /// </summary>
        /// <param name="dBase"></param>
        /// <returns></returns>
        public bool SaveToDatabase(Shotgun.Database.IBaseDataClass2 dBase)
        {
            return dBase.SaveData(this);
        }

        protected virtual void OnSaved(bool isInsert) { }

        /// <summary>
        /// 存取字段值,字段值为null时返回default(type);存入null,会调用SetNull(key)
        /// </summary>
        /// <param name="key"></param>
        /// <returns></returns>
        public object this[string key]
        {
            get
            {
                if (extrFields.ContainsKey(key))
                    return extrFields[key];
                object obj;
                if (GetValue(key, out obj))
                    return obj;
                throw new KeyNotFoundException(string.Format("Key: {0} not Found", key));
            }
            set
            {
                if (value is DBNull)
                    value = null;
                if (extrFields.ContainsKey(key))
                {
                    extrFields[key] = value;
                    return;
                }

                if (SetValue(key, value))
                {
                    if (value == null)//SetValue对值类型无效
                        SetNullFlag(key);
                    return;
                }
                extrFields.Add(key, value);
            }
        }

        /// <summary>
        /// 通过反射获取成员成公用属性值
        /// </summary>
        /// <param name="key">成员名</param>
        /// <param name="obj">取得的成员值</param>
        /// <returns></returns>
        private bool GetValue(string key, out object obj)
        {
            obj = null;
            if (IsNull(key))
                return true;
            var t = this.GetType();
            var p = t.GetProperty(key,
                System.Reflection.BindingFlags.Instance |
                System.Reflection.BindingFlags.Public |
                System.Reflection.BindingFlags.IgnoreCase |
                System.Reflection.BindingFlags.GetProperty);
            if (p == null)
                return false;
            obj = p.GetValue(this, null);
            return true;
        }

        /// <summary>
        /// 通过反射设置成员成公用属性值
        /// </summary>
        /// <param name="key">成员名</param>
        /// <param name="value">设置的成员值</param>
        /// <returns></returns>
        private bool SetValue(string key, object value)
        {
            var t = this.GetType();
            var p = t.GetProperty(key,
                System.Reflection.BindingFlags.Instance |
                System.Reflection.BindingFlags.Public |
                System.Reflection.BindingFlags.IgnoreCase |
                System.Reflection.BindingFlags.GetProperty);
            if (p == null)
                return false;
            if (p.PropertyType.Name.Equals("Boolean"))
            {
                if (!(value is bool))
                    value = Convert.ToBoolean(value);
                //if (p.MemberType ==typeof( Boolean)
            }
            p.SetValue(this, value, null);
            return true;
        }

        public bool IsNull(string key)
        {
            if (this.NullFields == null)
                return false;
            return this.NullFields.Contains(key.ToLower());
        }

        /// <summary>
        /// 将字段值设置为null
        /// </summary>
        public void SetNull(string key)
        {
            if (!SetValue(key, null)) //引用类型可以此处将被标记为null,值类型无效
                throw new KeyNotFoundException(string.Format("Key: {0} not Found", key));
            SetNullFlag(key);
        }

        /// <summary>
        /// 设置段值空标记
        /// </summary>
        /// <param name="key"></param>
        protected void SetNullFlag(string key)
        {
            if (IsNull(key))
                return;
            var fs = GetNullableFields();
            bool Exist = false;
            foreach (var s in fs)
            {
                if (key.Equals(s, StringComparison.OrdinalIgnoreCase))
                {
                    Exist = true;
                    break;
                }
            }

            if (!Exist && !DataFilling)
                throw new ArgumentException(string.Format("Key:{0} Non-Nullable ", key));
            if (NullFields == null)
                NullFields = new List<string>();
            this.NullFields.Add(key.ToLower());
        }

        /// <summary>
        /// 删除去字段null标记
        /// </summary>
        /// <param name="key"></param>
        protected void RemoveNullFlag(string key)
        {
            if (NullFields == null)
                return;
            var lk = key.ToLower();
            if (NullFields.Contains(lk))
                NullFields.Remove(lk);
        }

        /// <summary>
        /// 取得可以为空的字段
        /// </summary>
        /// <returns></returns>
        protected abstract string[] GetNullableFields();

        protected void SetFieldHasUpdate(string field, object oVal, object nVal)
        {
            if (DataFilling)
                return;
            if (this.IgnoreEquals)
            {
                if (oVal == null)
                {
                    if (nVal == null)
                        return;
                }
                else if (oVal.Equals(nVal))
                    return;
            }
            if (updatedFields.Contains(field))
                return;
            updatedFields.Add(field);
        }

        /// <summary>
        /// 赋值时检查是不是否与原值相同，相同时将忽略该操作(在执行存入数据库时，该字段不会有任何表现)
        /// </summary>
        public bool IgnoreEquals { get; set; }

        /// <summary>
        /// 表示是否正在填充数据
        /// </summary>
        internal bool DataFilling { get; set; }

        /// <summary>
        /// 将数据复制到DataTable中
        /// 主键值已经存在将抛出ArgumentException
        /// </summary>
        /// <param name="dt"></param>
        /// <returns>复制的新行</returns>
        public System.Data.DataRow CopyToDataTable(System.Data.DataTable dt)
        {
            System.Data.DataRow row;
            if (dt.Rows.Count > 0 && dt.PrimaryKey.Length > 0)
            {
                row = dt.Rows.Find(new object[] { this[dt.PrimaryKey[0].ColumnName] });
                if (row != null)
                    throw new ArgumentException("Key值已经存在");
            }
            row = dt.NewRow();
            object obj;
            foreach (System.Data.DataColumn col in dt.Columns)
            {
                obj = this[col.ColumnName];
                if (obj == null)
                    row[col] = DBNull.Value;
                else
                    row[col] = this[col.ColumnName];
            }
            dt.Rows.Add(row);
            return row;
        }

        /// <summary>
        /// 跨库操作时，需要指定
        /// </summary>
        public virtual string Schema
        {
            get;
            protected set;
        }
    }
}
