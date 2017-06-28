using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace LightDataModel
{
    /// <summary>
    /// tbl_currency数据模型
    /// </summary>
    public partial class tbl_currencyItem : Shotgun.Model.Logical.LightDataModel
    {
        /// <summary>
        /// 数据表字段列表对像
        /// </summary>
        public sealed class Fields
        {
            private Fields() { }
            #region 表字段名称

            public const string id = "id";
            ///<summary>
            ///主键
            ///</summary>
            public const string PrimaryKey = "id";

            /// <summary>
            /// 货币名称
            /// </summary>
            public const string name = "name";
            /// <summary>
            /// 汇率，1RMB的比
            /// </summary>
            public const string rate = "rate";
            /// <summary>
            /// 国家MCC
            /// </summary>
            public const string mcc = "mcc";
            /// <summary>
            /// 货币符号
            /// </summary>
            public const string symbol = "symbol";

            #endregion

        }
        #region 表字段变量定义
        /// <summary>
        /// 货币名称
        /// </summary>
        private string _name;
        /// <summary>
        /// 汇率，1RMB的比
        /// </summary>
        private Decimal _rate;
        /// <summary>
        /// 国家MCC
        /// </summary>
        private string _mcc;
        /// <summary>
        /// 货币符号
        /// </summary>
        private string _symbol;

        #endregion

        public override string IdentifyField { get { return identifyField; } }
        public override string TableName { get { return tableName; } }

        public static readonly string identifyField = "id";
        public static readonly string tableName = "tbl_currency";


        public int id
        {
            get;
            set;
        }

        #region 表字段值存取
        /// <summary>
        /// 货币名称
        /// </summary>
        public string name
        {
            get { return this._name; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.name);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.name);
                else
                    RemoveNullFlag(Fields.name);
#endif

                SetFieldHasUpdate(Fields.name, this._name, value);
                this._name = value;
            }
        }
        /// <summary>
        /// 汇率，1RMB的比
        /// </summary>
        public Decimal rate
        {
            get { return this._rate; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.rate);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.rate);
                else
                    RemoveNullFlag(Fields.rate);
#endif

                SetFieldHasUpdate(Fields.rate, this._rate, value);
                this._rate = value;
            }
        }
        /// <summary>
        /// 国家MCC
        /// </summary>
        public string mcc
        {
            get { return this._mcc; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.mcc);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.mcc);
                else
                    RemoveNullFlag(Fields.mcc);
#endif

                SetFieldHasUpdate(Fields.mcc, this._mcc, value);
                this._mcc = value;
            }
        }
        /// <summary>
        /// 货币符号
        /// </summary>
        public string symbol
        {
            get { return this._symbol; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.symbol);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.symbol);
                else
                    RemoveNullFlag(Fields.symbol);
#endif

                SetFieldHasUpdate(Fields.symbol, this._symbol, value);
                this._symbol = value;
            }
        }

        #endregion
        #region 空值相关方法
        protected override string[] GetNullableFields()
        {
            return new string[]{null
			,"name"
,"rate"
,"mcc"
,"symbol"
};
        }
        public bool IsnameNull() { return IsNull(Fields.name); }

        public void SetnameNull() { SetNull(Fields.name); }
        public bool IsrateNull() { return IsNull(Fields.rate); }

        public void SetrateNull() { SetNull(Fields.rate); }
        public bool IsmccNull() { return IsNull(Fields.mcc); }

        public void SetmccNull() { SetNull(Fields.mcc); }
        public bool IssymbolNull() { return IsNull(Fields.symbol); }

        public void SetsymbolNull() { SetNull(Fields.symbol); }

        #endregion
        #region 静态方法
        /// <summary>
        /// 创建一个新的查询器
        /// </summary>
        public static Shotgun.Model.List.LightDataQueries<tbl_currencyItem> GetQueries(Shotgun.Database.IBaseDataClass2 dBase)
        {
            return new Shotgun.Model.List.LightDataQueries<tbl_currencyItem>(tableName, identifyField, dBase);
        }

        /// <summary>
        /// 创建一个新的查询器
        /// </summary>
        public static Shotgun.Model.List.LightDataQueries<tbl_currencyItem> GetQueries()
        {
            return new Shotgun.Model.List.LightDataQueries<tbl_currencyItem>(tableName, identifyField);
        }
        /// <summary>
        /// 根据主键查找指定的行,返回指定字段
        /// </summary>
        /// <param name="dBase"></param>
        /// <param name="id">主键值</param>
        /// <param name="fields">返回字段</param>
        /// <returns></returns>
        public static tbl_currencyItem GetRowById(Shotgun.Database.IBaseDataClass2 dBase, int id, string[] fields)
        {
            var q = GetQueries(dBase);
            q.Fields = fields;
            q.Filter.AndFilters.Add(identifyField, id);
            return q.GetRowByFilters();
        }

        /// <summary>
        /// 根据主键查找指定的行,返回所有字段
        /// </summary>
        /// <param name="dBase"></param>
        /// <param name="id">主键值</param>
        /// <returns></returns>
        public static tbl_currencyItem GetRowById(Shotgun.Database.IBaseDataClass2 dBase, int id)
        {
            return GetRowById(dBase, id, null);
        }

        #endregion
    }
}