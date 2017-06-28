using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace LightDataModel
{
    /// <summary>
    /// tbl_cp数据模型
    /// </summary>
    public partial class tbl_cpItem : Shotgun.Model.Logical.LightDataModel
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
            /// 用户表ID
            /// </summary>
            public const string user_id = "user_id";
            /// <summary>
            /// CP全称
            /// </summary>
            public const string full_name = "full_name";
            /// <summary>
            /// CP简称
            /// </summary>
            public const string short_name = "short_name";
            /// <summary>
            /// 联系人
            /// </summary>
            public const string contract_person = "contract_person";
            /// <summary>
            /// 商务在用户表里的ID
            /// </summary>
            public const string commerce_user_id = "commerce_user_id";
            /// <summary>
            /// 默认为0，即时同步，1指定时间同步
            /// </summary>
            public const string syn_flag = "syn_flag";
            /// <summary>
            /// 默认的扣量比，默认值为0，表示不扣量
            /// </summary>
            public const string default_hold_percent = "default_hold_percent";
            /// <summary>
            /// address
            /// </summary>
            public const string address = "address";
            /// <summary>
            /// 合同起始日
            /// </summary>
            public const string contract_start_date = "contract_start_date";
            /// <summary>
            /// 合同结束日
            /// </summary>
            public const string contract_end_date = "contract_end_date";
            /// <summary>
            /// 状态，1为正常，0为锁定
            /// </summary>
            public const string status = "status";
            /// <summary>
            /// 创建时间
            /// </summary>
            public const string create_date = "create_date";

            #endregion

        }
        #region 表字段变量定义
        /// <summary>
        /// 用户表ID
        /// </summary>
        private int _user_id;
        /// <summary>
        /// CP全称
        /// </summary>
        private string _full_name;
        /// <summary>
        /// CP简称
        /// </summary>
        private string _short_name;
        /// <summary>
        /// 联系人
        /// </summary>
        private string _contract_person;
        /// <summary>
        /// 商务在用户表里的ID
        /// </summary>
        private int _commerce_user_id;
        /// <summary>
        /// 默认为0，即时同步，1指定时间同步
        /// </summary>
        private int _syn_flag;
        /// <summary>
        /// 默认的扣量比，默认值为0，表示不扣量
        /// </summary>
        private int _default_hold_percent;
        /// <summary>
        /// address
        /// </summary>
        private string _address;
        /// <summary>
        /// 合同起始日
        /// </summary>
        private DateTime _contract_start_date;
        /// <summary>
        /// 合同结束日
        /// </summary>
        private DateTime _contract_end_date;
        /// <summary>
        /// 状态，1为正常，0为锁定
        /// </summary>
        private bool _status;
        /// <summary>
        /// 创建时间
        /// </summary>
        private DateTime _create_date;

        #endregion

        public override string IdentifyField { get { return identifyField; } }
        public override string TableName { get { return tableName; } }

        public static readonly string identifyField = "id";
        public static readonly string tableName = "tbl_cp";


        public int id
        {
            get;
            set;
        }

        #region 表字段值存取
        /// <summary>
        /// 用户表ID
        /// </summary>
        public int user_id
        {
            get { return this._user_id; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.user_id);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.user_id);
                else
                    RemoveNullFlag(Fields.user_id);
#endif

                SetFieldHasUpdate(Fields.user_id, this._user_id, value);
                this._user_id = value;
            }
        }
        /// <summary>
        /// CP全称
        /// </summary>
        public string full_name
        {
            get { return this._full_name; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.full_name);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.full_name);
                else
                    RemoveNullFlag(Fields.full_name);
#endif

                SetFieldHasUpdate(Fields.full_name, this._full_name, value);
                this._full_name = value;
            }
        }
        /// <summary>
        /// CP简称
        /// </summary>
        public string short_name
        {
            get { return this._short_name; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.short_name);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.short_name);
                else
                    RemoveNullFlag(Fields.short_name);
#endif

                SetFieldHasUpdate(Fields.short_name, this._short_name, value);
                this._short_name = value;
            }
        }
        /// <summary>
        /// 联系人
        /// </summary>
        public string contract_person
        {
            get { return this._contract_person; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.contract_person);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.contract_person);
                else
                    RemoveNullFlag(Fields.contract_person);
#endif

                SetFieldHasUpdate(Fields.contract_person, this._contract_person, value);
                this._contract_person = value;
            }
        }
        /// <summary>
        /// 商务在用户表里的ID
        /// </summary>
        public int commerce_user_id
        {
            get { return this._commerce_user_id; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.commerce_user_id);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.commerce_user_id);
                else
                    RemoveNullFlag(Fields.commerce_user_id);
#endif

                SetFieldHasUpdate(Fields.commerce_user_id, this._commerce_user_id, value);
                this._commerce_user_id = value;
            }
        }
        /// <summary>
        /// 默认为0，即时同步，1指定时间同步
        /// </summary>
        public int syn_flag
        {
            get { return this._syn_flag; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.syn_flag);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.syn_flag);
                else
                    RemoveNullFlag(Fields.syn_flag);
#endif

                SetFieldHasUpdate(Fields.syn_flag, this._syn_flag, value);
                this._syn_flag = value;
            }
        }
        /// <summary>
        /// 默认的扣量比，默认值为0，表示不扣量
        /// </summary>
        public int default_hold_percent
        {
            get { return this._default_hold_percent; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.default_hold_percent);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.default_hold_percent);
                else
                    RemoveNullFlag(Fields.default_hold_percent);
#endif

                SetFieldHasUpdate(Fields.default_hold_percent, this._default_hold_percent, value);
                this._default_hold_percent = value;
            }
        }
        /// <summary>
        /// address
        /// </summary>
        public string address
        {
            get { return this._address; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.address);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.address);
                else
                    RemoveNullFlag(Fields.address);
#endif

                SetFieldHasUpdate(Fields.address, this._address, value);
                this._address = value;
            }
        }
        /// <summary>
        /// 合同起始日
        /// </summary>
        public DateTime contract_start_date
        {
            get { return this._contract_start_date; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.contract_start_date);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.contract_start_date);
                else
                    RemoveNullFlag(Fields.contract_start_date);
#endif

                SetFieldHasUpdate(Fields.contract_start_date, this._contract_start_date, value);
                this._contract_start_date = value;
            }
        }
        /// <summary>
        /// 合同结束日
        /// </summary>
        public DateTime contract_end_date
        {
            get { return this._contract_end_date; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.contract_end_date);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.contract_end_date);
                else
                    RemoveNullFlag(Fields.contract_end_date);
#endif

                SetFieldHasUpdate(Fields.contract_end_date, this._contract_end_date, value);
                this._contract_end_date = value;
            }
        }
        /// <summary>
        /// 状态，1为正常，0为锁定
        /// </summary>
        public bool status
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

        #endregion
        #region 空值相关方法
        protected override string[] GetNullableFields()
        {
            return new string[]{null
			,"user_id"
,"full_name"
,"short_name"
,"contract_person"
,"commerce_user_id"
,"syn_flag"
,"default_hold_percent"
,"address"
,"contract_start_date"
,"contract_end_date"
,"status"
};
        }
        public bool Isuser_idNull() { return IsNull(Fields.user_id); }

        public void Setuser_idNull() { SetNull(Fields.user_id); }
        public bool Isfull_nameNull() { return IsNull(Fields.full_name); }

        public void Setfull_nameNull() { SetNull(Fields.full_name); }
        public bool Isshort_nameNull() { return IsNull(Fields.short_name); }

        public void Setshort_nameNull() { SetNull(Fields.short_name); }
        public bool Iscontract_personNull() { return IsNull(Fields.contract_person); }

        public void Setcontract_personNull() { SetNull(Fields.contract_person); }
        public bool Iscommerce_user_idNull() { return IsNull(Fields.commerce_user_id); }

        public void Setcommerce_user_idNull() { SetNull(Fields.commerce_user_id); }
        public bool Issyn_flagNull() { return IsNull(Fields.syn_flag); }

        public void Setsyn_flagNull() { SetNull(Fields.syn_flag); }
        public bool Isdefault_hold_percentNull() { return IsNull(Fields.default_hold_percent); }

        public void Setdefault_hold_percentNull() { SetNull(Fields.default_hold_percent); }
        public bool IsaddressNull() { return IsNull(Fields.address); }

        public void SetaddressNull() { SetNull(Fields.address); }
        public bool Iscontract_start_dateNull() { return IsNull(Fields.contract_start_date); }

        public void Setcontract_start_dateNull() { SetNull(Fields.contract_start_date); }
        public bool Iscontract_end_dateNull() { return IsNull(Fields.contract_end_date); }

        public void Setcontract_end_dateNull() { SetNull(Fields.contract_end_date); }
        public bool IsstatusNull() { return IsNull(Fields.status); }

        public void SetstatusNull() { SetNull(Fields.status); }

        #endregion
        #region 静态方法
        /// <summary>
        /// 创建一个新的查询器
        /// </summary>
        public static Shotgun.Model.List.LightDataQueries<tbl_cpItem> GetQueries(Shotgun.Database.IBaseDataClass2 dBase)
        {
            return new Shotgun.Model.List.LightDataQueries<tbl_cpItem>(tableName, identifyField, dBase);
        }

        /// <summary>
        /// 创建一个新的查询器
        /// </summary>
        public static Shotgun.Model.List.LightDataQueries<tbl_cpItem> GetQueries()
        {
            return new Shotgun.Model.List.LightDataQueries<tbl_cpItem>(tableName, identifyField);
        }
        /// <summary>
        /// 根据主键查找指定的行,返回指定字段
        /// </summary>
        /// <param name="dBase"></param>
        /// <param name="id">主键值</param>
        /// <param name="fields">返回字段</param>
        /// <returns></returns>
        public static tbl_cpItem GetRowById(Shotgun.Database.IBaseDataClass2 dBase, int id, string[] fields)
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
        public static tbl_cpItem GetRowById(Shotgun.Database.IBaseDataClass2 dBase, int id)
        {
            return GetRowById(dBase, id, null);
        }

        #endregion
    }
}