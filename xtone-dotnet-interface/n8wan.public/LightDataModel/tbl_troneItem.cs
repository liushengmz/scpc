using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace LightDataModel
{
    /// <summary>
    /// tbl_trone数据模型
    /// </summary>
    public partial class tbl_troneItem : Shotgun.Model.Logical.LightDataModel
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
            /// tbl_sp表主键
            /// </summary>
            public const string sp_id = "sp_id";
            /// <summary>
            /// tbl_sp_api_url主键
            /// </summary>
            public const string sp_api_url_id = "sp_api_url_id";
            /// <summary>
            /// 通道号
            /// </summary>
            public const string trone_num = "trone_num";
            /// <summary>
            /// 指令集
            /// </summary>
            public const string orders = "orders";
            /// <summary>
            /// tbl_sp_trone主键
            /// </summary>
            public const string sp_trone_id = "sp_trone_id";
            /// <summary>
            /// 通道名称
            /// </summary>
            public const string trone_name = "trone_name";

            public const string currency_id = "currency_id";
            /// <summary>
            /// 价格(元)
            /// </summary>
            public const string price = "price";
            /// <summary>
            /// 状态，1为正常，0为停用
            /// </summary>
            public const string status = "status";
            /// <summary>
            /// 创建时间
            /// </summary>
            public const string create_date = "create_date";
            /// <summary>
            /// 是否模糊指令，即是否允许使用通匹符进行配对
            /// </summary>
            public const string is_dynamic = "is_dynamic";
            /// <summary>
            /// 不规则指令，直接匹配价格
            /// </summary>
            public const string match_price = "match_price";

            #endregion

        }
        #region 表字段变量定义
        /// <summary>
        /// tbl_sp表主键
        /// </summary>
        private int _sp_id;
        /// <summary>
        /// tbl_sp_api_url主键
        /// </summary>
        private int _sp_api_url_id;
        /// <summary>
        /// 通道号
        /// </summary>
        private string _trone_num;
        /// <summary>
        /// 指令集
        /// </summary>
        private string _orders;
        /// <summary>
        /// tbl_sp_trone主键
        /// </summary>
        private int _sp_trone_id;
        /// <summary>
        /// 通道名称
        /// </summary>
        private string _trone_name;

        private int _currency_id;
        /// <summary>
        /// 价格(元)
        /// </summary>
        private Decimal _price;
        /// <summary>
        /// 状态，1为正常，0为停用
        /// </summary>
        private int _status;
        /// <summary>
        /// 创建时间
        /// </summary>
        private DateTime _create_date;
        /// <summary>
        /// 是否模糊指令，即是否允许使用通匹符进行配对
        /// </summary>
        private bool _is_dynamic;
        /// <summary>
        /// 不规则指令，直接匹配价格
        /// </summary>
        private bool _match_price;

        #endregion

        public override string IdentifyField { get { return identifyField; } }
        public override string TableName { get { return tableName; } }

        public static readonly string identifyField = "id";
        public static readonly string tableName = "tbl_trone";


        public int id
        {
            get;
            set;
        }

        #region 表字段值存取
        /// <summary>
        /// tbl_sp表主键
        /// </summary>
        public int sp_id
        {
            get { return this._sp_id; }
            set
            {
#if true && false
				RemoveNullFlag(Fields.sp_id);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.sp_id);
                else
                    RemoveNullFlag(Fields.sp_id);
#endif

                SetFieldHasUpdate(Fields.sp_id, this._sp_id, value);
                this._sp_id = value;
            }
        }
        /// <summary>
        /// tbl_sp_api_url主键
        /// </summary>
        public int sp_api_url_id
        {
            get { return this._sp_api_url_id; }
            set
            {
#if true && false
				RemoveNullFlag(Fields.sp_api_url_id);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.sp_api_url_id);
                else
                    RemoveNullFlag(Fields.sp_api_url_id);
#endif

                SetFieldHasUpdate(Fields.sp_api_url_id, this._sp_api_url_id, value);
                this._sp_api_url_id = value;
            }
        }
        /// <summary>
        /// 通道号
        /// </summary>
        public string trone_num
        {
            get { return this._trone_num; }
            set
            {
#if false && false
				RemoveNullFlag(Fields.trone_num);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.trone_num);
                else
                    RemoveNullFlag(Fields.trone_num);
#endif

                SetFieldHasUpdate(Fields.trone_num, this._trone_num, value);
                this._trone_num = value;
            }
        }
        /// <summary>
        /// 指令集
        /// </summary>
        public string orders
        {
            get { return this._orders; }
            set
            {
#if false && false
				RemoveNullFlag(Fields.orders);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.orders);
                else
                    RemoveNullFlag(Fields.orders);
#endif

                SetFieldHasUpdate(Fields.orders, this._orders, value);
                this._orders = value;
            }
        }
        /// <summary>
        /// tbl_sp_trone主键
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
        /// 通道名称
        /// </summary>
        public string trone_name
        {
            get { return this._trone_name; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.trone_name);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.trone_name);
                else
                    RemoveNullFlag(Fields.trone_name);
#endif

                SetFieldHasUpdate(Fields.trone_name, this._trone_name, value);
                this._trone_name = value;
            }
        }

        public int currency_id
        {
            get { return this._currency_id; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.currency_id);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.currency_id);
                else
                    RemoveNullFlag(Fields.currency_id);
#endif

                SetFieldHasUpdate(Fields.currency_id, this._currency_id, value);
                this._currency_id = value;
            }
        }
        /// <summary>
        /// 价格(元)
        /// </summary>
        public Decimal price
        {
            get { return this._price; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.price);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.price);
                else
                    RemoveNullFlag(Fields.price);
#endif

                SetFieldHasUpdate(Fields.price, this._price, value);
                this._price = value;
            }
        }
        /// <summary>
        /// 状态，1为正常，0为停用
        /// </summary>
        public int status
        {
            get { return this._status; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.status);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.status);
                else
                    RemoveNullFlag(Fields.status);
#endif

                SetFieldHasUpdate(Fields.status, this._status, value);
                this._status = value;
            }
        }
        /// <summary>
        /// 创建时间
        /// </summary>
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
        /// <summary>
        /// 是否模糊指令，即是否允许使用通匹符进行配对
        /// </summary>
        public bool is_dynamic
        {
            get { return this._is_dynamic; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.is_dynamic);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.is_dynamic);
                else
                    RemoveNullFlag(Fields.is_dynamic);
#endif

                SetFieldHasUpdate(Fields.is_dynamic, this._is_dynamic, value);
                this._is_dynamic = value;
            }
        }
        /// <summary>
        /// 不规则指令，直接匹配价格
        /// </summary>
        public bool match_price
        {
            get { return this._match_price; }
            set
            {
#if true && false
				RemoveNullFlag(Fields.match_price);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.match_price);
                else
                    RemoveNullFlag(Fields.match_price);
#endif

                SetFieldHasUpdate(Fields.match_price, this._match_price, value);
                this._match_price = value;
            }
        }

        #endregion
        #region 空值相关方法
        protected override string[] GetNullableFields()
        {
            return new string[]{null
			,"sp_trone_id"
,"trone_name"
,"currency_id"
,"price"
,"status"
,"create_date"
,"is_dynamic"
};
        }
        public bool Issp_trone_idNull() { return IsNull(Fields.sp_trone_id); }

        public void Setsp_trone_idNull() { SetNull(Fields.sp_trone_id); }
        public bool Istrone_nameNull() { return IsNull(Fields.trone_name); }

        public void Settrone_nameNull() { SetNull(Fields.trone_name); }
        public bool Iscurrency_idNull() { return IsNull(Fields.currency_id); }

        public void Setcurrency_idNull() { SetNull(Fields.currency_id); }
        public bool IspriceNull() { return IsNull(Fields.price); }

        public void SetpriceNull() { SetNull(Fields.price); }
        public bool IsstatusNull() { return IsNull(Fields.status); }

        public void SetstatusNull() { SetNull(Fields.status); }
        public bool Iscreate_dateNull() { return IsNull(Fields.create_date); }

        public void Setcreate_dateNull() { SetNull(Fields.create_date); }
        public bool Isis_dynamicNull() { return IsNull(Fields.is_dynamic); }

        public void Setis_dynamicNull() { SetNull(Fields.is_dynamic); }

        #endregion
        #region 静态方法
        /// <summary>
        /// 创建一个新的查询器
        /// </summary>
        public static Shotgun.Model.List.LightDataQueries<tbl_troneItem> GetQueries(Shotgun.Database.IBaseDataClass2 dBase)
        {
            return new Shotgun.Model.List.LightDataQueries<tbl_troneItem>(tableName, identifyField, dBase);
        }

        /// <summary>
        /// 创建一个新的查询器
        /// </summary>
        public static Shotgun.Model.List.LightDataQueries<tbl_troneItem> GetQueries()
        {
            return new Shotgun.Model.List.LightDataQueries<tbl_troneItem>(tableName, identifyField);
        }

        /// <summary>
        /// 根据主键查找指定的行,返回所有字段
        /// </summary>
        /// <param name="dBase"></param>
        /// <param name="id">主键值</param>
        /// <returns></returns>
        public static tbl_troneItem GetRowById(Shotgun.Database.IBaseDataClass2 dBase, int id)
        {
            return GetRowById(dBase, id, null);
        }

        #endregion
    }
}