using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace LightDataModel
{
    /// <summary>
    /// tbl_sp_trone_api数据模型
    /// </summary>
    public partial class tbl_sp_trone_apiItem : Shotgun.Model.Logical.LightDataModel
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
            /// 业务通道API名字
            /// </summary>
            public const string name = "name";
            /// <summary>
            /// 配匹字段，0:linkid,1:msg,2:cpprams
            /// </summary>
            public const string match_field = "match_field";
            /// <summary>
            /// 匹配关键字，用于去除sp固定的参数，提取有用的匹配关键字
            /// </summary>
            public const string match_keyword = "match_keyword";
            /// <summary>
            /// API必须参数,0=IMEI,1=IMSI,2=PHONE,3=IP,4=PACKAGENAME,5=ANDROIDVERSION,6=NETTYPE,7=CLIENTIP
            /// </summary>
            public const string api_fields = "api_fields";
            /// <summary>
            /// 地区匹配,默认0不匹配，1手机号匹配，2IP地区匹配，3手机和IP地区都必须匹配
            /// </summary>
            public const string locate_match = "locate_match";

            public const string create_date = "create_date";
            /// <summary>
            /// 用户日限
            /// </summary>
            public const string user_day_limit = "user_day_limit";
            /// <summary>
            /// 用户月限
            /// </summary>
            public const string user_month_limit = "user_month_limit";
            /// <summary>
            /// 日限
            /// </summary>
            public const string day_limit = "day_limit";
            /// <summary>
            /// 月限
            /// </summary>
            public const string month_limit = "month_limit";
            /// <summary>
            /// 当前日额
            /// </summary>
            public const string cur_day_limit = "cur_day_limit";
            /// <summary>
            /// 当前月额
            /// </summary>
            public const string cur_month_limit = "cur_month_limit";
            /// <summary>
            /// 可配置参数名，多个参数名以英文逗号分隔
            /// </summary>
            public const string config_fields = "config_fields";

            #endregion

        }
        #region 表字段变量定义
        /// <summary>
        /// 业务通道API名字
        /// </summary>
        private string _name;
        /// <summary>
        /// 配匹字段，0:linkid,1:msg,2:cpprams
        /// </summary>
        private int _match_field;
        /// <summary>
        /// 匹配关键字，用于去除sp固定的参数，提取有用的匹配关键字
        /// </summary>
        private string _match_keyword;
        /// <summary>
        /// API必须参数,0=IMEI,1=IMSI,2=PHONE,3=IP,4=PACKAGENAME,5=ANDROIDVERSION,6=NETTYPE,7=CLIENTIP
        /// </summary>
        private string _api_fields;
        /// <summary>
        /// 地区匹配,默认0不匹配，1手机号匹配，2IP地区匹配，3手机和IP地区都必须匹配
        /// </summary>
        private int _locate_match;

        private DateTime _create_date;
        /// <summary>
        /// 用户日限
        /// </summary>
        private Decimal _user_day_limit;
        /// <summary>
        /// 用户月限
        /// </summary>
        private Decimal _user_month_limit;
        /// <summary>
        /// 日限
        /// </summary>
        private Decimal _day_limit;
        /// <summary>
        /// 月限
        /// </summary>
        private Decimal _month_limit;
        /// <summary>
        /// 当前日额
        /// </summary>
        private Decimal _cur_day_limit;
        /// <summary>
        /// 当前月额
        /// </summary>
        private Decimal _cur_month_limit;
        /// <summary>
        /// 可配置参数名，多个参数名以英文逗号分隔
        /// </summary>
        private string _config_fields;

        #endregion

        public override string IdentifyField { get { return identifyField; } }
        public override string TableName { get { return tableName; } }

        public static readonly string identifyField = "id";
        public static readonly string tableName = "tbl_sp_trone_api";


        public int id
        {
            get;
            set;
        }

        #region 表字段值存取
        /// <summary>
        /// 业务通道API名字
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
        /// 配匹字段，0:linkid,1:msg,2:cpprams
        /// </summary>
        public int match_field
        {
            get { return this._match_field; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.match_field);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.match_field);
                else
                    RemoveNullFlag(Fields.match_field);
#endif

                SetFieldHasUpdate(Fields.match_field, this._match_field, value);
                this._match_field = value;
            }
        }
        /// <summary>
        /// 匹配关键字，用于去除sp固定的参数，提取有用的匹配关键字
        /// </summary>
        public string match_keyword
        {
            get { return this._match_keyword; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.match_keyword);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.match_keyword);
                else
                    RemoveNullFlag(Fields.match_keyword);
#endif

                SetFieldHasUpdate(Fields.match_keyword, this._match_keyword, value);
                this._match_keyword = value;
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

        public DateTime create_date
        {
            get { return this._create_date; }
            set
            {
#if true && false
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
        /// 当前日额
        /// </summary>
        public Decimal cur_day_limit
        {
            get { return this._cur_day_limit; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.cur_day_limit);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.cur_day_limit);
                else
                    RemoveNullFlag(Fields.cur_day_limit);
#endif

                SetFieldHasUpdate(Fields.cur_day_limit, this._cur_day_limit, value);
                this._cur_day_limit = value;
            }
        }
        /// <summary>
        /// 当前月额
        /// </summary>
        public Decimal cur_month_limit
        {
            get { return this._cur_month_limit; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.cur_month_limit);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.cur_month_limit);
                else
                    RemoveNullFlag(Fields.cur_month_limit);
#endif

                SetFieldHasUpdate(Fields.cur_month_limit, this._cur_month_limit, value);
                this._cur_month_limit = value;
            }
        }
        /// <summary>
        /// 可配置参数名，多个参数名以英文逗号分隔
        /// </summary>
        public string config_fields
        {
            get { return this._config_fields; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.config_fields);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.config_fields);
                else
                    RemoveNullFlag(Fields.config_fields);
#endif

                SetFieldHasUpdate(Fields.config_fields, this._config_fields, value);
                this._config_fields = value;
            }
        }

        #endregion
        #region 空值相关方法
        protected override string[] GetNullableFields()
        {
            return new string[]{null
			,"name"
,"match_field"
,"match_keyword"
,"api_fields"
,"locate_match"
,"user_day_limit"
,"user_month_limit"
,"day_limit"
,"month_limit"
,"cur_day_limit"
,"cur_month_limit"
,"config_fields"
};
        }
        public bool IsnameNull() { return IsNull(Fields.name); }

        public void SetnameNull() { SetNull(Fields.name); }
        public bool Ismatch_fieldNull() { return IsNull(Fields.match_field); }

        public void Setmatch_fieldNull() { SetNull(Fields.match_field); }
        public bool Ismatch_keywordNull() { return IsNull(Fields.match_keyword); }

        public void Setmatch_keywordNull() { SetNull(Fields.match_keyword); }
        public bool Isapi_fieldsNull() { return IsNull(Fields.api_fields); }

        public void Setapi_fieldsNull() { SetNull(Fields.api_fields); }
        public bool Islocate_matchNull() { return IsNull(Fields.locate_match); }

        public void Setlocate_matchNull() { SetNull(Fields.locate_match); }
        public bool Isuser_day_limitNull() { return IsNull(Fields.user_day_limit); }

        public void Setuser_day_limitNull() { SetNull(Fields.user_day_limit); }
        public bool Isuser_month_limitNull() { return IsNull(Fields.user_month_limit); }

        public void Setuser_month_limitNull() { SetNull(Fields.user_month_limit); }
        public bool Isday_limitNull() { return IsNull(Fields.day_limit); }

        public void Setday_limitNull() { SetNull(Fields.day_limit); }
        public bool Ismonth_limitNull() { return IsNull(Fields.month_limit); }

        public void Setmonth_limitNull() { SetNull(Fields.month_limit); }
        public bool Iscur_day_limitNull() { return IsNull(Fields.cur_day_limit); }

        public void Setcur_day_limitNull() { SetNull(Fields.cur_day_limit); }
        public bool Iscur_month_limitNull() { return IsNull(Fields.cur_month_limit); }

        public void Setcur_month_limitNull() { SetNull(Fields.cur_month_limit); }
        public bool Isconfig_fieldsNull() { return IsNull(Fields.config_fields); }

        public void Setconfig_fieldsNull() { SetNull(Fields.config_fields); }

        #endregion
        #region 静态方法
        /// <summary>
        /// 创建一个新的查询器
        /// </summary>
        public static Shotgun.Model.List.LightDataQueries<tbl_sp_trone_apiItem> GetQueries(Shotgun.Database.IBaseDataClass2 dBase)
        {
            return new Shotgun.Model.List.LightDataQueries<tbl_sp_trone_apiItem>(tableName, identifyField, dBase);
        }

        /// <summary>
        /// 创建一个新的查询器
        /// </summary>
        public static Shotgun.Model.List.LightDataQueries<tbl_sp_trone_apiItem> GetQueries()
        {
            return new Shotgun.Model.List.LightDataQueries<tbl_sp_trone_apiItem>(tableName, identifyField);
        }
        /// <summary>
        /// 根据主键查找指定的行,返回指定字段
        /// </summary>
        /// <param name="dBase"></param>
        /// <param name="id">主键值</param>
        /// <param name="fields">返回字段</param>
        /// <returns></returns>
        public static tbl_sp_trone_apiItem GetRowById(Shotgun.Database.IBaseDataClass2 dBase, int id, string[] fields)
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
        public static tbl_sp_trone_apiItem GetRowById(Shotgun.Database.IBaseDataClass2 dBase, int id)
        {
            return GetRowById(dBase, id, null);
        }

        #endregion
    }
}