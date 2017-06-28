using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace LightDataModel
{
    /// <summary>
    /// tbl_zyl_detail数据模型
    /// </summary>
    public partial class tbl_zyl_detailItem : Shotgun.Model.Logical.LightDataModel
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
            /// appkey
            /// </summary>
            public const string appkey = "appkey";
            /// <summary>
            /// channel
            /// </summary>
            public const string channel = "channel";
            /// <summary>
            /// 用户ID
            /// </summary>
            public const string userid = "userid";
            /// <summary>
            /// 用户帐号
            /// </summary>
            public const string useracount = "useracount";
            /// <summary>
            /// 昵称
            /// </summary>
            public const string nickname = "nickname";
            /// <summary>
            /// 状态0停用1启用
            /// </summary>
            public const string status = "status";
            /// <summary>
            /// 金额
            /// </summary>
            public const string amount = "amount";
            /// <summary>
            /// 支付次数
            /// </summary>
            public const string amount_times = "amount_times";
            /// <summary>
            /// IMEI
            /// </summary>
            public const string imei = "imei";
            /// <summary>
            /// IMSI
            /// </summary>
            public const string imsi = "imsi";
            /// <summary>
            /// 注册时间
            /// </summary>
            public const string regist_time = "regist_time";
            /// <summary>
            /// 最后登录时间
            /// </summary>
            public const string last_login_time = "last_login_time";
            /// <summary>
            /// 创建时间
            /// </summary>
            public const string create_time = "create_time";

            #endregion

        }
        #region 表字段变量定义
        /// <summary>
        /// appkey
        /// </summary>
        private string _appkey;
        /// <summary>
        /// channel
        /// </summary>
        private string _channel;
        /// <summary>
        /// 用户ID
        /// </summary>
        private string _userid;
        /// <summary>
        /// 用户帐号
        /// </summary>
        private string _useracount;
        /// <summary>
        /// 昵称
        /// </summary>
        private string _nickname;
        /// <summary>
        /// 状态0停用1启用
        /// </summary>
        private short _status;
        /// <summary>
        /// 金额
        /// </summary>
        private Decimal _amount;
        /// <summary>
        /// 支付次数
        /// </summary>
        private int _amount_times;
        /// <summary>
        /// IMEI
        /// </summary>
        private string _imei;
        /// <summary>
        /// IMSI
        /// </summary>
        private string _imsi;
        /// <summary>
        /// 注册时间
        /// </summary>
        private DateTime _regist_time;
        /// <summary>
        /// 最后登录时间
        /// </summary>
        private DateTime _last_login_time;
        /// <summary>
        /// 创建时间
        /// </summary>
        private DateTime _create_time;

        #endregion

        public override string IdentifyField { get { return identifyField; } }
        public override string TableName { get { return tableName; } }

        public static readonly string identifyField = "id";
        public static readonly string tableName = "tbl_zyl_detail";


        public int id
        {
            get;
            set;
        }

        #region 表字段值存取
        /// <summary>
        /// appkey
        /// </summary>
        public string appkey
        {
            get { return this._appkey; }
            set
            {
#if false && false
				RemoveNullFlag(Fields.appkey);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.appkey);
                else
                    RemoveNullFlag(Fields.appkey);
#endif

                SetFieldHasUpdate(Fields.appkey, this._appkey, value);
                this._appkey = value;
            }
        }
        /// <summary>
        /// channel
        /// </summary>
        public string channel
        {
            get { return this._channel; }
            set
            {
#if false && false
				RemoveNullFlag(Fields.channel);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.channel);
                else
                    RemoveNullFlag(Fields.channel);
#endif

                SetFieldHasUpdate(Fields.channel, this._channel, value);
                this._channel = value;
            }
        }
        /// <summary>
        /// 用户ID
        /// </summary>
        public string userid
        {
            get { return this._userid; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.userid);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.userid);
                else
                    RemoveNullFlag(Fields.userid);
#endif

                SetFieldHasUpdate(Fields.userid, this._userid, value);
                this._userid = value;
            }
        }
        /// <summary>
        /// 用户帐号
        /// </summary>
        public string useracount
        {
            get { return this._useracount; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.useracount);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.useracount);
                else
                    RemoveNullFlag(Fields.useracount);
#endif

                SetFieldHasUpdate(Fields.useracount, this._useracount, value);
                this._useracount = value;
            }
        }
        /// <summary>
        /// 昵称
        /// </summary>
        public string nickname
        {
            get { return this._nickname; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.nickname);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.nickname);
                else
                    RemoveNullFlag(Fields.nickname);
#endif

                SetFieldHasUpdate(Fields.nickname, this._nickname, value);
                this._nickname = value;
            }
        }
        /// <summary>
        /// 状态0停用1启用
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
        /// <summary>
        /// 金额
        /// </summary>
        public Decimal amount
        {
            get { return this._amount; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.amount);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.amount);
                else
                    RemoveNullFlag(Fields.amount);
#endif

                SetFieldHasUpdate(Fields.amount, this._amount, value);
                this._amount = value;
            }
        }
        /// <summary>
        /// 支付次数
        /// </summary>
        public int amount_times
        {
            get { return this._amount_times; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.amount_times);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.amount_times);
                else
                    RemoveNullFlag(Fields.amount_times);
#endif

                SetFieldHasUpdate(Fields.amount_times, this._amount_times, value);
                this._amount_times = value;
            }
        }
        /// <summary>
        /// IMEI
        /// </summary>
        public string imei
        {
            get { return this._imei; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.imei);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.imei);
                else
                    RemoveNullFlag(Fields.imei);
#endif

                SetFieldHasUpdate(Fields.imei, this._imei, value);
                this._imei = value;
            }
        }
        /// <summary>
        /// IMSI
        /// </summary>
        public string imsi
        {
            get { return this._imsi; }
            set
            {
#if false && true
				RemoveNullFlag(Fields.imsi);
#elif !false
                if (value == null)
                    SetNullFlag(Fields.imsi);
                else
                    RemoveNullFlag(Fields.imsi);
#endif

                SetFieldHasUpdate(Fields.imsi, this._imsi, value);
                this._imsi = value;
            }
        }
        /// <summary>
        /// 注册时间
        /// </summary>
        public DateTime regist_time
        {
            get { return this._regist_time; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.regist_time);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.regist_time);
                else
                    RemoveNullFlag(Fields.regist_time);
#endif

                SetFieldHasUpdate(Fields.regist_time, this._regist_time, value);
                this._regist_time = value;
            }
        }
        /// <summary>
        /// 最后登录时间
        /// </summary>
        public DateTime last_login_time
        {
            get { return this._last_login_time; }
            set
            {
#if true && true
                RemoveNullFlag(Fields.last_login_time);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.last_login_time);
                else
                    RemoveNullFlag(Fields.last_login_time);
#endif

                SetFieldHasUpdate(Fields.last_login_time, this._last_login_time, value);
                this._last_login_time = value;
            }
        }
        /// <summary>
        /// 创建时间
        /// </summary>
        public DateTime create_time
        {
            get { return this._create_time; }
            set
            {
#if true && false
				RemoveNullFlag(Fields.create_time);
#elif !true
			    if (value == null)
                    SetNullFlag(Fields.create_time);
                else
                    RemoveNullFlag(Fields.create_time);
#endif

                SetFieldHasUpdate(Fields.create_time, this._create_time, value);
                this._create_time = value;
            }
        }

        #endregion
        #region 空值相关方法
        protected override string[] GetNullableFields()
        {
            return new string[]{null
			,"userid"
,"useracount"
,"nickname"
,"status"
,"amount"
,"amount_times"
,"imei"
,"imsi"
,"regist_time"
,"last_login_time"
};
        }
        public bool IsuseridNull() { return IsNull(Fields.userid); }

        public void SetuseridNull() { SetNull(Fields.userid); }
        public bool IsuseracountNull() { return IsNull(Fields.useracount); }

        public void SetuseracountNull() { SetNull(Fields.useracount); }
        public bool IsnicknameNull() { return IsNull(Fields.nickname); }

        public void SetnicknameNull() { SetNull(Fields.nickname); }
        public bool IsstatusNull() { return IsNull(Fields.status); }

        public void SetstatusNull() { SetNull(Fields.status); }
        public bool IsamountNull() { return IsNull(Fields.amount); }

        public void SetamountNull() { SetNull(Fields.amount); }
        public bool Isamount_timesNull() { return IsNull(Fields.amount_times); }

        public void Setamount_timesNull() { SetNull(Fields.amount_times); }
        public bool IsimeiNull() { return IsNull(Fields.imei); }

        public void SetimeiNull() { SetNull(Fields.imei); }
        public bool IsimsiNull() { return IsNull(Fields.imsi); }

        public void SetimsiNull() { SetNull(Fields.imsi); }
        public bool Isregist_timeNull() { return IsNull(Fields.regist_time); }

        public void Setregist_timeNull() { SetNull(Fields.regist_time); }
        public bool Islast_login_timeNull() { return IsNull(Fields.last_login_time); }

        public void Setlast_login_timeNull() { SetNull(Fields.last_login_time); }

        #endregion
        #region 静态方法
        /// <summary>
        /// 创建一个新的查询器
        /// </summary>
        public static Shotgun.Model.List.LightDataQueries<tbl_zyl_detailItem> GetQueries(Shotgun.Database.IBaseDataClass2 dBase)
        {
            return new Shotgun.Model.List.LightDataQueries<tbl_zyl_detailItem>(tableName, identifyField, dBase);
        }

        /// <summary>
        /// 创建一个新的查询器
        /// </summary>
        public static Shotgun.Model.List.LightDataQueries<tbl_zyl_detailItem> GetQueries()
        {
            return new Shotgun.Model.List.LightDataQueries<tbl_zyl_detailItem>(tableName, identifyField);
        }
        /// <summary>
        /// 根据主键查找指定的行,返回指定字段
        /// </summary>
        /// <param name="dBase"></param>
        /// <param name="id">主键值</param>
        /// <param name="fields">返回字段</param>
        /// <returns></returns>
        public static tbl_zyl_detailItem GetRowById(Shotgun.Database.IBaseDataClass2 dBase, int id, string[] fields)
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
        public static tbl_zyl_detailItem GetRowById(Shotgun.Database.IBaseDataClass2 dBase, int id)
        {
            return GetRowById(dBase, id, null);
        }

        #endregion
    }
}