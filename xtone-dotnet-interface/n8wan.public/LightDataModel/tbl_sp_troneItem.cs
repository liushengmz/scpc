using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace LightDataModel
{
    /// <summary>
    /// tbl_sp_trone数据模型
    /// </summary>
    public partial class tbl_sp_troneItem : Shotgun.Model.Logical.LightDataModel
    {
        /// <summary>
        /// 数据表字段列表对像
        /// </summary>
        public sealed class Fields
        {
            private Fields() { }
            #region 表字段名称
            /// <summary>
            /// SP总业务表
            /// </summary>
            public const string id = "id";
            ///<summary>
            ///主键
            ///</summary>
            public const string PrimaryKey = "id";


            public const string sp_id = "sp_id";
            /// <summary>
            /// 名称
            /// </summary>
            public const string name = "name";
            /// <summary>
            /// 运营商
            /// </summary>
            public const string @operator = "operator";
            /// <summary>
            /// 产品ID，默认是 短信-短信
            /// </summary>
            public const string product_id = "product_id";
            /// <summary>
            /// 业务类型，0为默认通道，1为包月，2为IVR
            /// </summary>
            public const string trone_type = "trone_type";
            /// <summary>
            /// 0对公周结；1对公双周结；2对公月结；3对私周结；4对私双周结；5对私月结
            /// </summary>
            public const string js_type = "js_type";
            /// <summary>
            /// 结算率
            /// </summary>
            public const string jiesuanlv = "jiesuanlv";
            /// <summary>
            /// 省份
            /// </summary>
            public const string provinces = "provinces";
            /// <summary>
            /// 屏蔽开始时间
            /// </summary>
            public const string shield_start_hour = "shield_start_hour";
            /// <summary>
            /// 屏蔽结束时间，开始和结束都是0就不屏蔽
            /// </summary>
            public const string shield_end_hour = "shield_end_hour";
            /// <summary>
            /// 0是不可透参，1是可透参
            /// </summary>
            public const string extend_flag = "extend_flag";
            /// <summary>
            /// 是否是导量？0为否，1为是
            /// </summary>
            public const string is_unhold_data = "is_unhold_data";
            /// <summary>
            /// 0不走我们的API，1走我们的API
            /// </summary>
            public const string is_on_api = "is_on_api";
            /// <summary>
            /// API必须参数,0=IMEI,1=IMSI,2=PHONE,3=IP,4=PACKAGENAME,5=ANDROIDVERSION,6=NETTYPE,7=CLIENTIP
            /// </summary>
            public const string api_fields = "api_fields";
            /// <summary>
            /// 地区匹配,默认0不匹配，1手机号匹配，2IP地区匹配，3手机和IP地区都必须匹配
            /// </summary>
            public const string locate_match = "locate_match";
            /// <summary>
            /// tbl_up_data_type.id
            /// </summary>
            public const string up_data_type = "up_data_type";
            /// <summary>
            /// 合同编码
            /// </summary>
            public const string contact_code = "contact_code";
            /// <summary>
            /// 限量类型，0元，1条数
            /// </summary>
            public const string limit_type = "limit_type";
            /// <summary>
            /// 是否按省份扣量，0否，1 是
            /// </summary>
            public const string is_province_limit = "is_province_limit";
            /// <summary>
            /// 省份的日月限，,,,=,;,,=,;
            /// </summary>
            public const string province_day_month_limt = "province_day_month_limt";
            /// <summary>
            /// 地区限制不成功的时候是否强制拦截，0否，1是
            /// </summary>
            public const string is_force_hold = "is_force_hold";
            /// <summary>
            /// 日限
            /// </summary>
            public const string day_limit = "day_limit";
            /// <summary>
            /// 月限
            /// </summary>
            public const string month_limit = "month_limit";
            /// <summary>
            /// 用户日限
            /// </summary>
            public const string user_day_limit = "user_day_limit";
            /// <summary>
            /// 用户月限
            /// </summary>
            public const string user_month_limit = "user_month_limit";
            /// <summary>
            /// tbl_sp_trone_api.id
            /// </summary>
            public const string trone_api_id = "trone_api_id";
            /// <summary>
            /// 备注
            /// </summary>
            public const string ramark = "ramark";
            /// <summary>
            /// 0停用1启用
            /// </summary>
            public const string status = "status";

            public const string create_date = "create_date";

            #endregion

        }
        #region 表字段变量定义

        private int _sp_id;
        /// <summary>
        /// 名称
        /// </summary>
        private string _name;
        /// <summary>
        /// 运营商
        /// </summary>
        private int _operator;
        /// <summary>
        /// 产品ID，默认是 短信-短信
        /// </summary>
        private int _product_id;
        /// <summary>
        /// 业务类型，0为默认通道，1为包月，2为IVR
        /// </summary>
        private short _trone_type;
        /// <summary>
        /// 0对公周结；1对公双周结；2对公月结；3对私周结；4对私双周结；5对私月结
        /// </summary>
        private int _js_type;
        /// <summary>
        /// 结算率
        /// </summary>
        private Decimal _jiesuanlv;
        /// <summary>
        /// 省份
        /// </summary>
        private string _provinces;
        /// <summary>
        /// 屏蔽开始时间
        /// </summary>
        private string _shield_start_hour;
        /// <summary>
        /// 屏蔽结束时间，开始和结束都是0就不屏蔽
        /// </summary>
        private string _shield_end_hour;
        /// <summary>
        /// 0是不可透参，1是可透参
        /// </summary>
        private short _extend_flag;
        /// <summary>
        /// 是否是导量？0为否，1为是
        /// </summary>
        private short _is_unhold_data;
        /// <summary>
        /// 0不走我们的API，1走我们的API
        /// </summary>
        private short _is_on_api;
        /// <summary>
        /// API必须参数,0=IMEI,1=IMSI,2=PHONE,3=IP,4=PACKAGENAME,5=ANDROIDVERSION,6=NETTYPE,7=CLIENTIP
        /// </summary>
        private string _api_fields;
        /// <summary>
        /// 地区匹配,默认0不匹配，1手机号匹配，2IP地区匹配，3手机和IP地区都必须匹配
        /// </summary>
        private int _locate_match;
        /// <summary>
        /// tbl_up_data_type.id
        /// </summary>
        private short _up_data_type;
        /// <summary>
        /// 合同编码
        /// </summary>
        private string _contact_code;
        /// <summary>
        /// 限量类型，0元，1条数
        /// </summary>
        private short _limit_type;
        /// <summary>
        /// 是否按省份扣量，0否，1 是
        /// </summary>
        private short _is_province_limit;
        /// <summary>
        /// 省份的日月限，,,,=,;,,=,;
        /// </summary>
        private string _province_day_month_limt;
        /// <summary>
        /// 地区限制不成功的时候是否强制拦截，0否，1是
        /// </summary>
        private short _is_force_hold;
        /// <summary>
        /// 日限
        /// </summary>
        private Decimal _day_limit;
        /// <summary>
        /// 月限
        /// </summary>
        private Decimal _month_limit;
        /// <summary>
        /// 用户日限
        /// </summary>
        private Decimal _user_day_limit;
        /// <summary>
        /// 用户月限
        /// </summary>
        private Decimal _user_month_limit;
        /// <summary>
        /// tbl_sp_trone_api.id
        /// </summary>
        private int _trone_api_id;
        /// <summary>
        /// 备注
        /// </summary>
        private string _ramark;
        /// <summary>
        /// 0停用1启用
        /// </summary>
        private short _status;

        private DateTime _create_date;

        #endregion

        public override string IdentifyField { get { return identifyField; } }
        public override string TableName { get { return tableName; } }

        public static readonly string identifyField = "id";
        public static readonly string tableName = "tbl_sp_trone";

        /// <summary>
        /// SP总业务表
        /// </summary>
        public int id
        {
            get;
            set;
        }

        #region 表字段值存取

        public int sp_id
        {
            get { return this._sp_id; }
            set
            {
#if true && true
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
        /// 名称
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
        /// 运营商
        /// </summary>
        public int @operator
        {
            get { return this._operator; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.@operator);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.@operator);
                else
                    RemoveNullFlag(Fields.@operator);
#endif

                SetFieldHasUpdate(Fields.@operator, this._operator, value);
                this._operator = value;
            }
        }
        /// <summary>
        /// 产品ID，默认是 短信-短信
        /// </summary>
        public int product_id
        {
            get { return this._product_id; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.product_id);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.product_id);
                else
                    RemoveNullFlag(Fields.product_id);
#endif

                SetFieldHasUpdate(Fields.product_id, this._product_id, value);
                this._product_id = value;
            }
        }
        /// <summary>
        /// 业务类型，0为默认通道，1为包月，2为IVR
        /// </summary>
        public short trone_type
        {
            get { return this._trone_type; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.trone_type);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.trone_type);
                else
                    RemoveNullFlag(Fields.trone_type);
#endif

                SetFieldHasUpdate(Fields.trone_type, this._trone_type, value);
                this._trone_type = value;
            }
        }
        /// <summary>
        /// 0对公周结；1对公双周结；2对公月结；3对私周结；4对私双周结；5对私月结
        /// </summary>
        public int js_type
        {
            get { return this._js_type; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.js_type);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.js_type);
                else
                    RemoveNullFlag(Fields.js_type);
#endif

                SetFieldHasUpdate(Fields.js_type, this._js_type, value);
                this._js_type = value;
            }
        }
        /// <summary>
        /// 结算率
        /// </summary>
        public Decimal jiesuanlv
        {
            get { return this._jiesuanlv; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.jiesuanlv);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.jiesuanlv);
                else
                    RemoveNullFlag(Fields.jiesuanlv);
#endif

                SetFieldHasUpdate(Fields.jiesuanlv, this._jiesuanlv, value);
                this._jiesuanlv = value;
            }
        }
        /// <summary>
        /// 省份
        /// </summary>
        public string provinces
        {
            get { return this._provinces; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.provinces);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.provinces);
                else
                    RemoveNullFlag(Fields.provinces);
#endif

                SetFieldHasUpdate(Fields.provinces, this._provinces, value);
                this._provinces = value;
            }
        }
        /// <summary>
        /// 屏蔽开始时间
        /// </summary>
        public string shield_start_hour
        {
            get { return this._shield_start_hour; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.shield_start_hour);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.shield_start_hour);
                else
                    RemoveNullFlag(Fields.shield_start_hour);
#endif

                SetFieldHasUpdate(Fields.shield_start_hour, this._shield_start_hour, value);
                this._shield_start_hour = value;
            }
        }
        /// <summary>
        /// 屏蔽结束时间，开始和结束都是0就不屏蔽
        /// </summary>
        public string shield_end_hour
        {
            get { return this._shield_end_hour; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.shield_end_hour);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.shield_end_hour);
                else
                    RemoveNullFlag(Fields.shield_end_hour);
#endif

                SetFieldHasUpdate(Fields.shield_end_hour, this._shield_end_hour, value);
                this._shield_end_hour = value;
            }
        }
        /// <summary>
        /// 0是不可透参，1是可透参
        /// </summary>
        public short extend_flag
        {
            get { return this._extend_flag; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.extend_flag);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.extend_flag);
                else
                    RemoveNullFlag(Fields.extend_flag);
#endif

                SetFieldHasUpdate(Fields.extend_flag, this._extend_flag, value);
                this._extend_flag = value;
            }
        }
        /// <summary>
        /// 是否是导量？0为否，1为是
        /// </summary>
        public short is_unhold_data
        {
            get { return this._is_unhold_data; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.is_unhold_data);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.is_unhold_data);
                else
                    RemoveNullFlag(Fields.is_unhold_data);
#endif

                SetFieldHasUpdate(Fields.is_unhold_data, this._is_unhold_data, value);
                this._is_unhold_data = value;
            }
        }
        /// <summary>
        /// 0不走我们的API，1走我们的API
        /// </summary>
        public short is_on_api
        {
            get { return this._is_on_api; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.is_on_api);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.is_on_api);
                else
                    RemoveNullFlag(Fields.is_on_api);
#endif

                SetFieldHasUpdate(Fields.is_on_api, this._is_on_api, value);
                this._is_on_api = value;
            }
        }
        /// <summary>
        /// API必须参数,0=IMEI,1=IMSI,2=PHONE,3=IP,4=PACKAGENAME,5=ANDROIDVERSION,6=NETTYPE,7=CLIENTIP
        /// </summary>
        public string api_fields
        {
            get { return this._api_fields; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.api_fields);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.api_fields);
                else
                    RemoveNullFlag(Fields.api_fields);
#endif

                SetFieldHasUpdate(Fields.api_fields, this._api_fields, value);
                this._api_fields = value;
            }
        }
        /// <summary>
        /// 地区匹配,默认0不匹配，1手机号匹配，2IP地区匹配，3手机和IP地区都必须匹配
        /// </summary>
        public int locate_match
        {
            get { return this._locate_match; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.locate_match);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.locate_match);
                else
                    RemoveNullFlag(Fields.locate_match);
#endif

                SetFieldHasUpdate(Fields.locate_match, this._locate_match, value);
                this._locate_match = value;
            }
        }
        /// <summary>
        /// tbl_up_data_type.id
        /// </summary>
        public short up_data_type
        {
            get { return this._up_data_type; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.up_data_type);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.up_data_type);
                else
                    RemoveNullFlag(Fields.up_data_type);
#endif

                SetFieldHasUpdate(Fields.up_data_type, this._up_data_type, value);
                this._up_data_type = value;
            }
        }
        /// <summary>
        /// 合同编码
        /// </summary>
        public string contact_code
        {
            get { return this._contact_code; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.contact_code);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.contact_code);
                else
                    RemoveNullFlag(Fields.contact_code);
#endif

                SetFieldHasUpdate(Fields.contact_code, this._contact_code, value);
                this._contact_code = value;
            }
        }
        /// <summary>
        /// 限量类型，0元，1条数
        /// </summary>
        public short limit_type
        {
            get { return this._limit_type; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.limit_type);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.limit_type);
                else
                    RemoveNullFlag(Fields.limit_type);
#endif

                SetFieldHasUpdate(Fields.limit_type, this._limit_type, value);
                this._limit_type = value;
            }
        }
        /// <summary>
        /// 是否按省份扣量，0否，1 是
        /// </summary>
        public short is_province_limit
        {
            get { return this._is_province_limit; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.is_province_limit);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.is_province_limit);
                else
                    RemoveNullFlag(Fields.is_province_limit);
#endif

                SetFieldHasUpdate(Fields.is_province_limit, this._is_province_limit, value);
                this._is_province_limit = value;
            }
        }
        /// <summary>
        /// 省份的日月限，,,,=,;,,=,;
        /// </summary>
        public string province_day_month_limt
        {
            get { return this._province_day_month_limt; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.province_day_month_limt);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.province_day_month_limt);
                else
                    RemoveNullFlag(Fields.province_day_month_limt);
#endif

                SetFieldHasUpdate(Fields.province_day_month_limt, this._province_day_month_limt, value);
                this._province_day_month_limt = value;
            }
        }
        /// <summary>
        /// 地区限制不成功的时候是否强制拦截，0否，1是
        /// </summary>
        public short is_force_hold
        {
            get { return this._is_force_hold; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.is_force_hold);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.is_force_hold);
                else
                    RemoveNullFlag(Fields.is_force_hold);
#endif

                SetFieldHasUpdate(Fields.is_force_hold, this._is_force_hold, value);
                this._is_force_hold = value;
            }
        }
        /// <summary>
        /// 日限
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
        /// 月限
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
        /// 用户日限
        /// </summary>
        public Decimal user_day_limit
        {
            get { return this._user_day_limit; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.user_day_limit);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.user_day_limit);
                else
                    RemoveNullFlag(Fields.user_day_limit);
#endif

                SetFieldHasUpdate(Fields.user_day_limit, this._user_day_limit, value);
                this._user_day_limit = value;
            }
        }
        /// <summary>
        /// 用户月限
        /// </summary>
        public Decimal user_month_limit
        {
            get { return this._user_month_limit; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.user_month_limit);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.user_month_limit);
                else
                    RemoveNullFlag(Fields.user_month_limit);
#endif

                SetFieldHasUpdate(Fields.user_month_limit, this._user_month_limit, value);
                this._user_month_limit = value;
            }
        }
        /// <summary>
        /// tbl_sp_trone_api.id
        /// </summary>
        public int trone_api_id
        {
            get { return this._trone_api_id; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.trone_api_id);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.trone_api_id);
                else
                    RemoveNullFlag(Fields.trone_api_id);
#endif

                SetFieldHasUpdate(Fields.trone_api_id, this._trone_api_id, value);
                this._trone_api_id = value;
            }
        }
        /// <summary>
        /// 备注
        /// </summary>
        public string ramark
        {
            get { return this._ramark; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.ramark);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.ramark);
                else
                    RemoveNullFlag(Fields.ramark);
#endif

                SetFieldHasUpdate(Fields.ramark, this._ramark, value);
                this._ramark = value;
            }
        }
        /// <summary>
        /// 0停用1启用
        /// </summary>
        public short status
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
			,"sp_id"
,"name"
,"operator"
,"product_id"
,"trone_type"
,"js_type"
,"jiesuanlv"
,"provinces"
,"shield_start_hour"
,"shield_end_hour"
,"extend_flag"
,"is_unhold_data"
,"is_on_api"
,"api_fields"
,"locate_match"
,"up_data_type"
,"contact_code"
,"limit_type"
,"is_province_limit"
,"province_day_month_limt"
,"is_force_hold"
,"day_limit"
,"month_limit"
,"user_day_limit"
,"user_month_limit"
,"trone_api_id"
,"ramark"
,"status"
,"create_date"
};
        }
        public bool Issp_idNull() { return IsNull(Fields.sp_id); }

        public void Setsp_idNull() { SetNull(Fields.sp_id); }
        public bool IsnameNull() { return IsNull(Fields.name); }

        public void SetnameNull() { SetNull(Fields.name); }
        public bool IsoperatorNull() { return IsNull(Fields.@operator); }

        public void SetoperatorNull() { SetNull(Fields.@operator); }
        public bool Isproduct_idNull() { return IsNull(Fields.product_id); }

        public void Setproduct_idNull() { SetNull(Fields.product_id); }
        public bool Istrone_typeNull() { return IsNull(Fields.trone_type); }

        public void Settrone_typeNull() { SetNull(Fields.trone_type); }
        public bool Isjs_typeNull() { return IsNull(Fields.js_type); }

        public void Setjs_typeNull() { SetNull(Fields.js_type); }
        public bool IsjiesuanlvNull() { return IsNull(Fields.jiesuanlv); }

        public void SetjiesuanlvNull() { SetNull(Fields.jiesuanlv); }
        public bool IsprovincesNull() { return IsNull(Fields.provinces); }

        public void SetprovincesNull() { SetNull(Fields.provinces); }
        public bool Isshield_start_hourNull() { return IsNull(Fields.shield_start_hour); }

        public void Setshield_start_hourNull() { SetNull(Fields.shield_start_hour); }
        public bool Isshield_end_hourNull() { return IsNull(Fields.shield_end_hour); }

        public void Setshield_end_hourNull() { SetNull(Fields.shield_end_hour); }
        public bool Isextend_flagNull() { return IsNull(Fields.extend_flag); }

        public void Setextend_flagNull() { SetNull(Fields.extend_flag); }
        public bool Isis_unhold_dataNull() { return IsNull(Fields.is_unhold_data); }

        public void Setis_unhold_dataNull() { SetNull(Fields.is_unhold_data); }
        public bool Isis_on_apiNull() { return IsNull(Fields.is_on_api); }

        public void Setis_on_apiNull() { SetNull(Fields.is_on_api); }
        public bool Isapi_fieldsNull() { return IsNull(Fields.api_fields); }

        public void Setapi_fieldsNull() { SetNull(Fields.api_fields); }
        public bool Islocate_matchNull() { return IsNull(Fields.locate_match); }

        public void Setlocate_matchNull() { SetNull(Fields.locate_match); }
        public bool Isup_data_typeNull() { return IsNull(Fields.up_data_type); }

        public void Setup_data_typeNull() { SetNull(Fields.up_data_type); }
        public bool Iscontact_codeNull() { return IsNull(Fields.contact_code); }

        public void Setcontact_codeNull() { SetNull(Fields.contact_code); }
        public bool Islimit_typeNull() { return IsNull(Fields.limit_type); }

        public void Setlimit_typeNull() { SetNull(Fields.limit_type); }
        public bool Isis_province_limitNull() { return IsNull(Fields.is_province_limit); }

        public void Setis_province_limitNull() { SetNull(Fields.is_province_limit); }
        public bool Isprovince_day_month_limtNull() { return IsNull(Fields.province_day_month_limt); }

        public void Setprovince_day_month_limtNull() { SetNull(Fields.province_day_month_limt); }
        public bool Isis_force_holdNull() { return IsNull(Fields.is_force_hold); }

        public void Setis_force_holdNull() { SetNull(Fields.is_force_hold); }
        public bool Isday_limitNull() { return IsNull(Fields.day_limit); }

        public void Setday_limitNull() { SetNull(Fields.day_limit); }
        public bool Ismonth_limitNull() { return IsNull(Fields.month_limit); }

        public void Setmonth_limitNull() { SetNull(Fields.month_limit); }
        public bool Isuser_day_limitNull() { return IsNull(Fields.user_day_limit); }

        public void Setuser_day_limitNull() { SetNull(Fields.user_day_limit); }
        public bool Isuser_month_limitNull() { return IsNull(Fields.user_month_limit); }

        public void Setuser_month_limitNull() { SetNull(Fields.user_month_limit); }
        public bool Istrone_api_idNull() { return IsNull(Fields.trone_api_id); }

        public void Settrone_api_idNull() { SetNull(Fields.trone_api_id); }
        public bool IsramarkNull() { return IsNull(Fields.ramark); }

        public void SetramarkNull() { SetNull(Fields.ramark); }
        public bool IsstatusNull() { return IsNull(Fields.status); }

        public void SetstatusNull() { SetNull(Fields.status); }
        public bool Iscreate_dateNull() { return IsNull(Fields.create_date); }

        public void Setcreate_dateNull() { SetNull(Fields.create_date); }

        #endregion
        #region 静态方法
        /// <summary>
        /// 创建一个新的查询器
        /// </summary>
        public static Shotgun.Model.List.LightDataQueries<tbl_sp_troneItem> GetQueries(Shotgun.Database.IBaseDataClass2 dBase)
        {
            return new Shotgun.Model.List.LightDataQueries<tbl_sp_troneItem>(tableName, identifyField, dBase);
        }

        /// <summary>
        /// 创建一个新的查询器
        /// </summary>
        public static Shotgun.Model.List.LightDataQueries<tbl_sp_troneItem> GetQueries()
        {
            return new Shotgun.Model.List.LightDataQueries<tbl_sp_troneItem>(tableName, identifyField);
        }
 
        /// <summary>
        /// 根据主键查找指定的行,返回所有字段
        /// </summary>
        /// <param name="dBase"></param>
        /// <param name="id">主键值</param>
        /// <returns></returns>
        public static tbl_sp_troneItem GetRowById(Shotgun.Database.IBaseDataClass2 dBase, int id)
        {
            return GetRowById(dBase, id, null);
        }

        #endregion
    }
}