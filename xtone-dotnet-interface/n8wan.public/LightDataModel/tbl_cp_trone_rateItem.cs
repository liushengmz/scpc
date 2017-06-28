using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace LightDataModel
{
    /// <summary>
    /// tbl_cp_trone_rate数据模型
    /// </summary>
    public partial class tbl_cp_trone_rateItem : Shotgun.Model.Logical.LightDataModel
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


            public const string cp_id = "cp_id";
            /// <summary>
            /// SP业务id
            /// </summary>
            public const string sp_trone_id = "sp_trone_id";
            /// <summary>
            /// 结算率
            /// </summary>
            public const string rate = "rate";
            /// <summary>
            /// CP业务日限
            /// </summary>
            public const string day_limit = "day_limit";
            /// <summary>
            /// CP业务月限
            /// </summary>
            public const string month_limit = "month_limit";
            /// <summary>
            /// 省份扣量数据
            /// </summary>
            public const string province_hold_rate = "province_hold_rate";

            public const string create_date = "create_date";

            #endregion

        }
        #region 表字段变量定义

        private int _cp_id;
        /// <summary>
        /// SP业务id
        /// </summary>
        private int _sp_trone_id;
        /// <summary>
        /// 结算率
        /// </summary>
        private Decimal _rate;
        /// <summary>
        /// CP业务日限
        /// </summary>
        private Decimal _day_limit;
        /// <summary>
        /// CP业务月限
        /// </summary>
        private Decimal _month_limit;
        /// <summary>
        /// 省份扣量数据
        /// </summary>
        private string _province_hold_rate;

        private DateTime _create_date;

        #endregion

        public override string IdentifyField { get { return identifyField; } }
        public override string TableName { get { return tableName; } }

        public static readonly string identifyField = "id";
        public static readonly string tableName = "tbl_cp_trone_rate";


        public int id
        {
            get;
            set;
        }

        #region 表字段值存取

        public int cp_id
        {
            get { return this._cp_id; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.cp_id);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.cp_id);
                else
                    RemoveNullFlag(Fields.cp_id);
#endif

                SetFieldHasUpdate(Fields.cp_id, this._cp_id, value);
                this._cp_id = value;
            }
        }
        /// <summary>
        /// SP业务id
        /// </summary>
        public int sp_trone_id
        {
            get { return this._sp_trone_id; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.sp_trone_id);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.sp_trone_id);
                else
                    RemoveNullFlag(Fields.sp_trone_id);
#endif

                SetFieldHasUpdate(Fields.sp_trone_id, this._sp_trone_id, value);
                this._sp_trone_id = value;
            }
        }
        /// <summary>
        /// 结算率
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
        /// CP业务日限
        /// </summary>
        public Decimal day_limit
        {
            get { return this._day_limit; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.day_limit);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.day_limit);
                else
                    RemoveNullFlag(Fields.day_limit);
#endif

                SetFieldHasUpdate(Fields.day_limit, this._day_limit, value);
                this._day_limit = value;
            }
        }
        /// <summary>
        /// CP业务月限
        /// </summary>
        public Decimal month_limit
        {
            get { return this._month_limit; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.month_limit);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.month_limit);
                else
                    RemoveNullFlag(Fields.month_limit);
#endif

                SetFieldHasUpdate(Fields.month_limit, this._month_limit, value);
                this._month_limit = value;
            }
        }
        /// <summary>
        /// 省份扣量数据
        /// </summary>
        public string province_hold_rate
        {
            get { return this._province_hold_rate; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.province_hold_rate);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.province_hold_rate);
                else
                    RemoveNullFlag(Fields.province_hold_rate);
#endif

                SetFieldHasUpdate(Fields.province_hold_rate, this._province_hold_rate, value);
                this._province_hold_rate = value;
            }
        }

        public DateTime create_date
        {
            get { return this._create_date; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.create_date);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.create_date);
                else
                    RemoveNullFlag(Fields.create_date);
#endif

                SetFieldHasUpdate(Fields.create_date, this._create_date, value);
                this._create_date = value;
            }
        }

        #endregion
        #region 空值相关方法
        protected override string[] GetNullableFields()
        {
            return new string[]{null
			,"cp_id"
,"sp_trone_id"
,"rate"
,"day_limit"
,"month_limit"
,"province_hold_rate"
,"create_date"
};
        }
        public bool Iscp_idNull() { return IsNull(Fields.cp_id); }

        public void Setcp_idNull() { SetNull(Fields.cp_id); }
        public bool Issp_trone_idNull() { return IsNull(Fields.sp_trone_id); }

        public void Setsp_trone_idNull() { SetNull(Fields.sp_trone_id); }
        public bool IsrateNull() { return IsNull(Fields.rate); }

        public void SetrateNull() { SetNull(Fields.rate); }
        public bool Isday_limitNull() { return IsNull(Fields.day_limit); }

        public void Setday_limitNull() { SetNull(Fields.day_limit); }
        public bool Ismonth_limitNull() { return IsNull(Fields.month_limit); }

        public void Setmonth_limitNull() { SetNull(Fields.month_limit); }
        public bool Isprovince_hold_rateNull() { return IsNull(Fields.province_hold_rate); }

        public void Setprovince_hold_rateNull() { SetNull(Fields.province_hold_rate); }
        public bool Iscreate_dateNull() { return IsNull(Fields.create_date); }

        public void Setcreate_dateNull() { SetNull(Fields.create_date); }

        #endregion
        #region 静态方法
        /// <summary>
        /// 创建一个新的查询器
        /// </summary>
        public static Shotgun.Model.List.LightDataQueries<tbl_cp_trone_rateItem> GetQueries(Shotgun.Database.IBaseDataClass2 dBase)
        {
            return new Shotgun.Model.List.LightDataQueries<tbl_cp_trone_rateItem>(tableName, identifyField, dBase);
        }

        /// <summary>
        /// 创建一个新的查询器
        /// </summary>
        public static Shotgun.Model.List.LightDataQueries<tbl_cp_trone_rateItem> GetQueries()
        {
            return new Shotgun.Model.List.LightDataQueries<tbl_cp_trone_rateItem>(tableName, identifyField);
        }


        /// <summary>
        /// 根据主键查找指定的行,返回所有字段
        /// </summary>
        /// <param name="dBase"></param>
        /// <param name="id">主键值</param>
        /// <returns></returns>
        public static tbl_cp_trone_rateItem GetRowById(Shotgun.Database.IBaseDataClass2 dBase, int id)
        {
            return GetRowById(dBase, id, null);
        }

        #endregion
    }
}